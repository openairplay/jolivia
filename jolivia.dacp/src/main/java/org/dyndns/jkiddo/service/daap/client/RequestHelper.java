/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
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

package org.dyndns.jkiddo.service.daap.client;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.dyndns.jkiddo.dmp.DmapInputStream;
import org.dyndns.jkiddo.dmp.chunks.Chunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.google.common.io.Closeables;

public class RequestHelper
{
	private static final int CONNECT_TIMEOUT = 10000;
	private static final int READ_TIMEOUT = 0; // Infinite

	public final static Logger logger = LoggerFactory.getLogger(RequestHelper.class);

	public static byte[] requestBitmap(String remote) throws Exception
	{
		return request(remote, false);
	}

	public static void dispatch(String remoteUrl) throws Exception
	{
		request(remoteUrl, false);
	}

	public static <T extends Chunk> T requestParsed(String url, boolean keepalive) throws Exception
	{
		return requestParsed(url, keepalive, false);
	}

	public static <T extends Chunk> T requestParsed(String url) throws Exception
	{
		return requestParsed(url, false);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Chunk> T requestParsed(String url, boolean keepalive, boolean specialCaseProtocolViolation) throws Exception
	{
		logger.debug(url);
		DmapInputStream inputStream = new DmapInputStream(new ByteArrayInputStream(request(url, keepalive)), specialCaseProtocolViolation);
		Chunk chunk = inputStream.getChunk();
		Closeables.close(inputStream, true);
		return (T) chunk;
	}
	/**
	 * Performs the HTTP request and gathers the response from the server. The gzip and deflate headers are sent in case the server can respond with compressed answers saving network bandwidth and speeding up responses.
	 * <p>
	 * 
	 * @param remoteUrl
	 *            the HTTP URL to connect to
	 * @param keepalive
	 *            true if keepalive false if not
	 * @return a byte array containing the HTTPResponse
	 * @throws Exception
	 *             if any error occurs
	 */
	private static byte[] request(String remoteUrl, boolean keepalive) throws Exception
	{
		logger.debug(String.format("started request(remote=%s)", remoteUrl));

		final byte[] buffer = new byte[1024];

		final HttpURLConnection connection = (HttpURLConnection) new URL(remoteUrl).openConnection();
		connection.setAllowUserInteraction(false);

		// Carefull either Client-DAAP or Client-DPAP
		// iTunes
		{
			connection.setRequestProperty("Viewer-Only-Client", "1");
			connection.setRequestProperty("Client-iTunes-Sharing-Version", "3.10");
			connection.setRequestProperty("Client-DAAP-Version", "3.12");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
		}

		// connection.setRequestProperty("Host", "192.168.1.75");

		// iPhoto
		{
			// connection.setRequestProperty("Client-DPAP-Version", "1.1");
			// connection.setRequestProperty("User-Agent", "iPhoto/9.4.3  (Macintosh; N; PPC)");
		}

		connection.setReadTimeout(READ_TIMEOUT);
		if(!keepalive)
		{
			connection.setConnectTimeout(CONNECT_TIMEOUT);
		}
		connection.connect();

		if(connection.getResponseCode() >= HttpURLConnection.HTTP_UNAUTHORIZED)
			throw new Exception("HTTP Error Response Code: " + connection.getResponseCode());

		// obtain the encoding returned by the server
		String encoding = connection.getContentEncoding();

		InputStream inputStream = null;

		// create the appropriate stream wrapper based on the encoding type
		if(encoding != null && encoding.equalsIgnoreCase("gzip"))
		{
			inputStream = new GZIPInputStream(connection.getInputStream());
		}
		else if(encoding != null && encoding.equalsIgnoreCase("deflate"))
		{
			inputStream = new InflaterInputStream(connection.getInputStream(), new Inflater(true));
		}
		else
		{
			inputStream = connection.getInputStream();
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{
			int bytesRead;
			while((bytesRead = inputStream.read(buffer)) != -1)
			{
				os.write(buffer, 0, bytesRead);
			}
		}
		finally
		{
			os.flush();
			os.close();
			if(inputStream != null)
			{
				inputStream.close();
			}
		}

		return os.toByteArray();
	}

	/**
	 * URL encode a string escaping single quotes first.
	 * <p>
	 * 
	 * @param input
	 *            the string to encode
	 * @return the URL encoded string value
	 */
	public static String escapeUrlString(final String input)
	{
		String encoded = "";
		try
		{
			encoded = URLEncoder.encode(input, "UTF-8");
			encoded = encoded.replaceAll("\\+", "%20");
			encoded = encoded.replaceAll("%27", "%5C'");
		}
		catch(UnsupportedEncodingException e)
		{
			logger.warn("escapeUrlString Exception:" + e.getMessage());
		}
		return encoded;
	}

	public static String requestPList(String username, String password) throws Exception
	{
		final HttpURLConnection connection = (HttpURLConnection) new URL("https://homesharing.itunes.apple.com" + "/WebObjects/MZHomeSharing.woa/wa/getShareIdentifiers").openConnection();
		connection.setAllowUserInteraction(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		connection.setRequestProperty("Viewer-Only-Client", "1");
		connection.setRequestProperty("User-Agent", "Remote/2.0");
		connection.setRequestProperty("Accept-Encoding", "gzip");
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Content-Type", "text/xml");
		connection.setReadTimeout(READ_TIMEOUT);
		
		NSDictionary root = new NSDictionary();
		root.put("appleId", username);
		root.put("guid", "empty");
		root.put("password", password);
		String xml = root.toXMLPropertyList();
		connection.connect();
		
		OutputStream os = connection.getOutputStream();
		BufferedWriter writer = new BufferedWriter(
		        new OutputStreamWriter(os, "UTF-8"));
		writer.write(xml);
		writer.flush();
		writer.close();
		os.close();


		if(connection.getResponseCode() >= HttpURLConnection.HTTP_UNAUTHORIZED)
			throw new Exception("HTTP Error Response Code: " + connection.getResponseCode());

		// obtain the encoding returned by the server
		String encoding = connection.getContentEncoding();

		final InputStream inputStream;

		// create the appropriate stream wrapper based on the encoding type
		if(encoding != null && encoding.equalsIgnoreCase("gzip"))
		{
			inputStream = new GZIPInputStream(connection.getInputStream());
		}
		else if(encoding != null && encoding.equalsIgnoreCase("deflate"))
		{
			inputStream = new InflaterInputStream(connection.getInputStream(), new Inflater(true));
		}
		else
		{
			inputStream = connection.getInputStream();
		}
		NSDictionary dictionary = (NSDictionary) PropertyListParser.parse(inputStream);
		NSString o1 = (NSString) dictionary.get("spid");
		NSNumber o2 = (NSNumber) dictionary.get("status");
		NSNumber o3 = (NSNumber) dictionary.get("dsid");
		NSString o4 = (NSString) dictionary.get("sgid");
		if(o1 == null && o3 == null && o4 == null && o2.intValue() == 5505)
			throw new Exception("bad password");
		return o4.getContent();
	}
}
