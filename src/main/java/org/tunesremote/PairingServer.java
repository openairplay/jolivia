package org.tunesremote;

/*
 TunesRemote+ - http://code.google.com/p/tunesremote-plus/

 Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
 Copyright (C) 2010 TunesRemote+, http://code.google.com/p/tunesremote-plus/

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 The Initial Developer of the Original Code is Jeffrey Sharkey.
 Portions created by Jeffrey Sharkey are
 Copyright (C) 2008. Jeffrey Sharkey, http://jsharkey.org/
 All Rights Reserved.
 */

//package org.tunesremote.daap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PairingServer
{

	public final static Logger logger = LoggerFactory.getLogger(PairingServer.class);
	public final static int CLOSED = -1;
	public final static int SUCCESS = 0;
	public final static int ERROR = 1;

	// the pairing service waits for any incoming requests from itunes
	// it always returns a valid pairing code
	public final static String TAG = PairingServer.class.toString();

	protected final static byte[] CHAR_TABLE = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

	protected static byte[] PAIRING_RAW = new byte[] { 0x63, 0x6d, 0x70, 0x61, 0x00, 0x00, 0x00, 0x3a, 0x63, 0x6d, 0x70, 0x67, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x63, 0x6d, 0x6e, 0x6d, 0x00, 0x00, 0x00, 0x16, 0x41, 0x64, 0x6d, 0x69, 0x6e, 0x69, 0x73, 0x74, 0x72, 0x61, 0x74, 0x6f, 0x72, (byte) 0xe2, (byte) 0x80, (byte) 0x99, 0x73, 0x20, 0x69, 0x50, 0x6f, 0x64, 0x63, 0x6d, 0x74, 0x79, 0x00, 0x00, 0x00, 0x04, 0x69, 0x50, 0x6f, 0x64 };

	protected ServerSocket server;
	protected final Random random = new Random();
	protected int portNumber = 0;
	protected String pairCode;
	protected String serviceGuid;
	protected boolean pairing = false;

	protected MessageDigest md5;

	private PairingDatabase pairingDatabase;

	private static PairingServer instance = new PairingServer();
	protected PairingServer()
	{
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch(NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	// PairingServer is a singleton
	public static PairingServer getInstance()
	{
		return instance;
	}

	public synchronized int pair(String code)
	{

		int result = ERROR;
		pairing = true;

		// Open a socket if we aren't using one
		if(this.server == null)
		{
			try
			{
				// open a free port
				this.server = new ServerSocket(0);
				this.portNumber = this.server.getLocalPort();

				// set a timeout so we can cancel the pairing process
				server.setSoTimeout(100);

			}
			catch(IOException e)
			{
				logger.warn(TAG, e);
			}
		}

		if(this.pairingDatabase == null)
		{
			this.pairingDatabase = new PairingDatabase();
			this.pairCode = pairingDatabase.getPairCode();
			this.serviceGuid = pairingDatabase.getServiceGuid();
		}

		logger.info(TAG, "Started Pairing Server on Port " + portNumber);

		String expectedCode = expectedPairingCode(code);

		// Register pairing service
//		TunesService.getInstance().registerPairingService(serviceGuid, pairCode, portNumber);

		while(pairing && result != SUCCESS)
		{
			try
			{
				// start accepting data on incoming socket
				final Socket socket = server.accept();
				final String address = socket.getInetAddress().getHostAddress();

				logger.info(TAG, "accepted connection from " + address + "...");
				OutputStream output = null;

				try
				{
					String serviceName = null;
					String pairingcode = null;
					output = socket.getOutputStream();

					// output the contents for debugging
					final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					while(br.ready())
					{
						String line = br.readLine();
						logger.debug(TAG, line);
						if(line.contains("servicename="))
						{
							// Decode the input URL
							line = URLDecoder.decode(line, "UTF-8");

							String[] tokens = line.split("[ &?=]");

							for(int i = 0; i < tokens.length - 1; i++)
							{
								if(tokens[i].equals("pairingcode"))
								{
									pairingcode = tokens[i + 1];
									i++;
								}
								else if(tokens[i].equals("servicename"))
								{
									serviceName = tokens[i + 1];
									i++;
								}
							}
						}
					}

					if(serviceName != null && pairingcode != null && pairingcode.equals(expectedCode))
					{

						// edit our local PAIRING_RAW to return the correct guid
						byte[] loginGuid = new byte[8];
						random.nextBytes(loginGuid);
						System.arraycopy(loginGuid, 0, PAIRING_RAW, 16, 8);
						final String niceCode = toHex(loginGuid);

						byte[] header = String.format("HTTP/1.1 200 OK\r\nContent-Length: %d\r\n\r\n", PAIRING_RAW.length).getBytes();
						byte[] reply = new byte[header.length + PAIRING_RAW.length];

						System.arraycopy(header, 0, reply, 0, header.length);
						System.arraycopy(PAIRING_RAW, 0, reply, header.length, PAIRING_RAW.length);

						output.write(reply);

						logger.info(TAG, "Received pairing command");

						// add this to the pairing db
						logger.info(TAG, "address = " + address);
						logger.info(TAG, "servicename = \"" + serviceName + "\"");
						logger.info(TAG, "pairingcode = \"" + pairingcode + "\"");
						logger.debug(TAG, "niceCode = \"" + niceCode + "\"");

						pairingDatabase.updateCode(serviceName, niceCode);
						result = SUCCESS;
					}
					else
					{
						logger.info(TAG, "Wrong pairing code");
						output.write("HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n".getBytes());
					}

				}
				finally
				{
					if(output != null)
					{
						output.flush();
						output.close();
					}
					output = null;
				}
			}
			catch(java.net.SocketTimeoutException e)
			{
				if(!pairing)
				{
					result = CLOSED;
					break;
				}
			}
			catch(java.net.SocketException e)
			{
				logger.info(TAG, e.getMessage());
				break;
			}
			catch(IOException e)
			{
				logger.warn(TAG, e);
				break;
			}
		}

		logger.info(TAG, "Unregistering Pairing Service");

//		TunesService.getInstance().unregisterPairingService();

		logger.info(TAG, "Finished Pairing");
		return result;
	}

	private String expectedPairingCode(String code)
	{
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			os.write(pairCode.getBytes("UTF-8"));

			byte passcode[] = code.getBytes("UTF-8");
			for(int c = 0; c < passcode.length; c++)
			{
				os.write(passcode[c]);
				os.write(0);
			}

			return toHex(md5.digest(os.toByteArray()));
		}
		catch(UnsupportedEncodingException e)
		{
			logger.error(TAG, e.getMessage());
			return "";
		}
		catch(IOException e)
		{
			logger.error(TAG, e.getMessage());
			return "";
		}
	}

	public void cancelPairing()
	{
		pairing = false;
	}

	public static String toHex(byte[] code)
	{
		// somewhat borrowed from rgagnon.com
		byte[] result = new byte[2 * code.length];
		int index = 0;
		for(byte b : code)
		{
			int v = b & 0xff;
			result[index++] = CHAR_TABLE[v >>> 4];
			result[index++] = CHAR_TABLE[v & 0xf];
		}
		return new String(result);
	}

}
