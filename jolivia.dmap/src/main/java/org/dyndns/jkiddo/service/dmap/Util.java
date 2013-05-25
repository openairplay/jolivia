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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.chunks.Chunk;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

public class Util
{
	public static final String APPLICATION_NAME = "APPLICATION_NAME";
	public static final String APPLICATION_X_DMAP_TAGGED = "application/x-dmap-tagged";
	
	private static final int PARTIAL_CONTENT = 206;

	public static Response buildResponse(Chunk chunk, String dmapKey, String dmapServiceName) throws IOException
	{
		return buildResponse(dmapKey, dmapServiceName).entity(DmapUtil.serialize(chunk, false)).build();// .header("Content-Encoding", "gzip").build();
	}

	public static Response buildAudioResponse(byte[] buffer, long position, long size, String dmapKey, String dmapServiceName)
	{
		ResponseBuilder response = new ResponseBuilderImpl().header("Date", DmapUtil.now()).header(dmapKey, dmapServiceName).header("Content-Type", APPLICATION_X_DMAP_TAGGED).header("Connection", "close");

		if(position == 0)
		{
			response.status(Response.Status.OK);
			response.header("Content-Length", Long.toString(size));
		}
		else
		{
			response.status(PARTIAL_CONTENT);
			response.header("Content-Length", Long.toString(size - position));
			response.header("Content-Range", "bytes " + position + "-" + (size - 1) + "/" + size);
		}
		response.header("Accept-Ranges", "bytes");
		response.entity(buffer);
		return response.build();
	}

	public static ResponseBuilder buildResponse(String dmapKey, String dmapServiceName)
	{
		return new ResponseBuilderImpl().header("Date", DmapUtil.now()).header(dmapKey, dmapServiceName).header("Content-Type", APPLICATION_X_DMAP_TAGGED).header("Connection", "Keep-Alive").status(Response.Status.OK);
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
}
