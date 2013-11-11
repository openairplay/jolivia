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
package org.dyndns.jkiddo.service.dmap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.dyndns.jkiddo.NotImplementedException;
import org.dyndns.jkiddo.dmp.DmapUtil;
import org.dyndns.jkiddo.dmp.chunks.Chunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

public class Util
{
	public static final String APPLICATION_NAME = "APPLICATION_NAME";
	public static final String APPLICATION_X_DMAP_TAGGED = "application/x-dmap-tagged";
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	private static final int PARTIAL_CONTENT = 206;

	public static Response buildResponse(Chunk chunk, String dmapKey, String dmapServiceName) throws IOException
	{
		return buildResponse(dmapKey, dmapServiceName).entity(DmapUtil.serialize(chunk, false)).build();// .header("Content-Encoding", "gzip").build();
	}

	public static Response buildAudioResponse(byte[] buffer, long position, long size, String dmapKey, String dmapServiceName)
	{
		ResponseBuilder response = new ResponseBuilderImpl().header("Accept-Ranges", "bytes").header(HttpHeaders.DATE, DmapUtil.now()).header(dmapKey, dmapServiceName).header(HttpHeaders.CONTENT_TYPE, APPLICATION_X_DMAP_TAGGED).header("Connection", "close");

		if(position == 0)
		{
			response.status(Response.Status.OK);
			response.header(HttpHeaders.CONTENT_LENGTH, Long.toString(size));
		}
		else
		{
			response.status(PARTIAL_CONTENT);
			response.header(HttpHeaders.CONTENT_LENGTH, Long.toString(size - position));
			response.header("Content-Range", "bytes " + position + "-" + (size - 1) + "/" + size);
		}
		response.entity(buffer);
		return response.build();
	}

	private static ResponseBuilder buildResponse(String dmapKey, String dmapServiceName)
	{
		return new ResponseBuilderImpl().header(HttpHeaders.DATE, DmapUtil.now())
				.header(dmapKey, dmapServiceName)
				.header(HttpHeaders.CONTENT_TYPE, APPLICATION_X_DMAP_TAGGED)
				.header("Connection", "Keep-Alive")
				.status(Response.Status.OK);
	}
	
	enum SecurityType
	{
		BASIC,DIGEST
	}
	
	public static Response buildAuthenticationResponse(String dmapKey, String dmapServiceName, SecurityType sm) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		ResponseBuilder builder = new ResponseBuilderImpl().header(HttpHeaders.DATE, DmapUtil.now())
		.header(dmapKey, dmapServiceName)
		.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML)
		.header(HttpHeaders.CONTENT_LENGTH, "0")
		.header("Connection", "Keep-Alive")
		.status(Response.Status.UNAUTHORIZED);
		
		switch(sm)
		{
			case BASIC:
				builder.header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\""
                        + DmapUtil.DAAP_REALM + "\"");			
				break;
			case DIGEST:
				builder.header(HttpHeaders.WWW_AUTHENTICATE, "Digest realm=\""
                        + DmapUtil.DAAP_REALM + "\", nonce=\"" + DmapUtil.nonce()
                        + "\"");
				break;
				default:
					throw new NotImplementedException();
		}
		return builder.build();
		
	} 

	public static Response buildEmptyResponse(String dmapKey, String dmapServiceName)
	{
		return buildResponse(dmapKey, dmapServiceName).status(Response.Status.NO_CONTENT).build();
	}
	
	public static String toHex(String value)
	{
		try
		{
			return toHex(value.getBytes("UTF-8"));
		}
		catch(UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static String toHex(byte[] code)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b : code)
		{
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString().toUpperCase();
	}
	
	public static String toServiceGuid(String name)
	{
		try {
			return toHex((name+"1111111111111111").getBytes("UTF-8")).substring(0, 16);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String fromHex(String hex)
	{
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < hex.length(); i += 2)
		{
			str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
		}
		return str.toString();
	}
	
	/**
	 * Converts an array of bytes to a hexadecimal string
	 * 
	 * @param bytes
	 *            array of bytes
	 * @return hexadecimal representation
	 */
	public static String toHexString(final byte[] bytes)
	{
		final StringBuilder s = new StringBuilder();
		for(final byte b : bytes)
		{
			final String h = Integer.toHexString(0x100 | b);
			s.append(h.substring(h.length() - 2, h.length()).toUpperCase());
		}
		return s.toString();
	}
	
	public static String toMacString(final byte[] bytes)
	{
		String hex = toHexString(bytes);
		return hex.substring(0,2) +  ":" + hex.substring(2,4) + ":" + hex.substring(4,6) + ":" + hex.substring(6,8) + ":" + hex.substring(8,10) + ":" + hex.substring(10,12);
	}

	/**
	 * Returns a suitable hardware address.
	 * 
	 * @return a MAC address
	 */
	public static byte[] getHardwareAddress()
	{
		try
		{
			/* Search network interfaces for an interface with a valid, non-blocked hardware address */
			for(final NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces()))
			{
				if(iface.isLoopback())
					continue;
				if(iface.isPointToPoint())
					continue;
				if(!iface.isUp())
					continue;
				try
				{
					final byte[] ifaceMacAddress = iface.getHardwareAddress();
					if((ifaceMacAddress != null) && (ifaceMacAddress.length == 6) && !isBlockedHardwareAddress(ifaceMacAddress))
					{
						logger.info("Hardware address is " + toHexString(ifaceMacAddress) + " (" + iface.getDisplayName() + ")");
						return Arrays.copyOfRange(ifaceMacAddress, 0, 6);
					}
				}
				catch(final Throwable e)
				{
					/* Ignore */
				}
			}
		}
		catch(final Throwable e)
		{
			/* Ignore */
		}

		/* Fallback to the IP address padded to 6 bytes */
		try
		{
			final byte[] hostAddress = Arrays.copyOfRange(InetAddress.getLocalHost().getAddress(), 0, 6);
			logger.info("Hardware address is " + toHexString(hostAddress) + " (IP address)");
			return hostAddress;
		}
		catch(final Throwable e)
		{
			/* Ignore */
		}

		/* Fallback to a constant */
		logger.info("Hardware address is 00DEADBEEF00 (last resort)");
		return new byte[] { (byte) 0x00, (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF, (byte) 0x00 };
	}

	/**
	 * Decides whether or nor a given MAC address is the address of some virtual interface, like e.g. VMware's host-only interface (server-side).
	 * 
	 * @param addr
	 *            a MAC address
	 * @return true if the MAC address is unsuitable as the device's hardware address
	 */
	public static boolean isBlockedHardwareAddress(final byte[] addr)
	{
		if((addr[0] & 0x02) != 0)
			/* Locally administered */
			return true;
		else if((addr[0] == 0x00) && (addr[1] == 0x50) && (addr[2] == 0x56))
			/* VMware */
			return true;
		else if((addr[0] == 0x00) && (addr[1] == 0x1C) && (addr[2] == 0x42))
			/* Parallels */
			return true;
		else if((addr[0] == 0x00) && (addr[1] == 0x25) && (addr[2] == (byte) 0xAE))
			/* Microsoft */
			return true;
		else
			return false;
	}

}
