package org.dyndns.jkiddo.raop.server.AirReceiver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.dyndns.jkiddo.raop.server.airreceiver.Base64;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.primitives.Bytes;

//@RunWith(Suite.class)
public class TestAll {

	@BeforeClass
	public static void setUpClass() {
		System.out.println("Master setup");

	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("Master tearDown");

	}

	@Test
	public void doStuff() throws UnknownHostException, IOException {
		final byte[] bytes = new byte[] { 70, 80, 76, 89, 2, 1, 1, 0, 0, 0, 0,
				4, 2, 0, 0, -69 };
		
//		 final byte[] bytes = new
//		 byte[]{(byte)0x46 ,(byte)0x50 ,(byte)0x4c ,(byte)0x59 ,(byte)0x02 ,(byte)0x01 ,(byte)0x03 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x98 ,(byte)0x00 ,(byte)0x8f ,(byte)0x1a ,(byte)0x9c ,(byte)0xe4 ,(byte)0x47 ,(byte)0x0b ,(byte)0xe2 ,(byte)0xcb ,(byte)0x30 ,(byte)0xf2 ,(byte)0x59 ,(byte)0x5d ,(byte)0xb4 ,(byte)0xe0 ,(byte)0xd8 ,(byte)0xac ,(byte)0x2f ,(byte)0x22 ,(byte)0x7a ,(byte)0x75 ,(byte)0x47 ,(byte)0xf8 ,(byte)0x91 ,(byte)0x26 ,(byte)0x5f ,(byte)0x75 ,(byte)0x42 ,(byte)0x2b ,(byte)0x62 ,(byte)0x8d ,(byte)0x90 ,(byte)0x13 ,(byte)0x91 ,(byte)0x4a ,(byte)0x80 ,(byte)0x08 ,(byte)0x55 ,(byte)0x12 ,(byte)0xdb ,(byte)0x34 ,(byte)0xed ,(byte)0x98 ,(byte)0xe4 ,(byte)0x19 ,(byte)0xf9 ,(byte)0xb5 ,(byte)0x3e ,(byte)0x7c ,(byte)0xb7 ,(byte)0x52 ,(byte)0xb2 ,(byte)0x28 ,(byte)0x58 ,(byte)0x18 ,(byte)0xdb ,(byte)0x3d ,(byte)0xb0 ,(byte)0x09 ,(byte)0x7e ,(byte)0x51 ,(byte)0xcb ,(byte)0x91 ,(byte)0x5b ,(byte)0x82 ,(byte)0x67 ,(byte)0x4c ,(byte)0xb1 ,(byte)0x68 ,(byte)0x4b ,(byte)0x14 ,(byte)0x2a ,(byte)0xcc ,(byte)0xc3 ,(byte)0x4d ,(byte)0x9b ,(byte)0x7d ,(byte)0xc0 ,(byte)0x09 ,(byte)0xb3 ,(byte)0xed ,(byte)0xdd ,(byte)0x47 ,(byte)0xfc ,(byte)0xe0 ,(byte)0x36 ,(byte)0x65 ,(byte)0x11 ,(byte)0xe6 ,(byte)0x07 ,(byte)0xb9 ,(byte)0x4a ,(byte)0x84 ,(byte)0xaa ,(byte)0xaa ,(byte)0xa9 ,(byte)0xce ,(byte)0x3e ,(byte)0xe2 ,(byte)0xb3 ,(byte)0x25 ,(byte)0x6e ,(byte)0xc4 ,(byte)0x24 ,(byte)0x4a ,(byte)0xfc ,(byte)0x18 ,(byte)0x08 ,(byte)0x7f ,(byte)0x42 ,(byte)0x16 ,(byte)0x36 ,(byte)0x8d ,(byte)0x13 ,(byte)0x32 ,(byte)0x6d ,(byte)0x5b ,(byte)0x00 ,(byte)0x67 ,(byte)0xbf ,(byte)0x75 ,(byte)0x73 ,(byte)0xcb ,(byte)0x25 ,(byte)0x41 ,(byte)0x25 ,(byte)0x01 ,(byte)0xdc ,(byte)0x38 ,(byte)0x6a ,(byte)0x8d ,(byte)0x93 ,(byte)0x50 ,(byte)0xdc ,(byte)0x0e ,(byte)0xa7 ,(byte)0xae ,(byte)0xb5 ,(byte)0x3e ,(byte)0x43 ,(byte)0x67 ,(byte)0x45 ,(byte)0x11 ,(byte)0xcd ,(byte)0x97 ,(byte)0x61 ,(byte)0x15 ,(byte)0x30 ,(byte)0xde ,(byte)0xf1 ,(byte)0x85 ,(byte)0x1d};
		 System.out.println(Base64.encodePadded(bytes));
		final Socket clientSocket = new Socket("192.168.1.78", 19999);
		final DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());

		byte challenge;
		if (bytes.length == 16)
			challenge = 1;
		else
			challenge = 2;

		final byte[] fullChallenge = Bytes.concat(new byte[] { challenge,
				(byte) (bytes.length + 2) }, bytes);

		System.out.println(Base64.encodePadded(fullChallenge));
		System.out.println(new String(com.sun.jersey.core.util.Base64.encode(fullChallenge)));
		final byte[] fff = com.sun.jersey.core.util.Base64.decode("ARJGUExZAgEBAAAAAAQCAAC7");
		outToServer.write(fullChallenge);
		outToServer.flush();
System.out.println(fff);
		byte[] resultBuff = new byte[0];
		final byte[] buff = new byte[1024];
		int k = -1;
		final int doo = clientSocket.getInputStream().read();
		System.out.println(doo);
		while ((k = clientSocket.getInputStream().read(buff, 0, buff.length)) > -1) {
			final byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer
																	// size =
																	// bytes
																	// already
																	// read +
																	// bytes
																	// last read
			System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy
																			// previous
																			// bytes
			System.arraycopy(buff, 0, tbuff, resultBuff.length, k); // copy
																	// current
																	// lot
			resultBuff = tbuff; // call the temp buffer as your result buff
		}

		clientSocket.close();

		System.out.println(Base64.encodePadded(resultBuff));
	}
}