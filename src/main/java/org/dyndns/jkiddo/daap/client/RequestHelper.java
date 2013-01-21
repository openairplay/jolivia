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

package org.dyndns.jkiddo.daap.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.dyndns.jkiddo.protocol.dmap.DmapInputStream;
import org.dyndns.jkiddo.protocol.dmap.chunks.Chunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Closeables;

public class RequestHelper
{

	public final static Logger logger = LoggerFactory.getLogger(RequestHelper.class);

	// // Legacy code from TunesRemote+ project
	// private static PlaylistSongs requestSearch(Session session, String search, int start, int end) throws Exception
	// {
	// // http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1535976870&revision-number=61&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum&type=music&sort=name&include-sort-headers=1&query='dmap.itemname:*sea*'&index=0-7
	// // doesnt seem to listen to &sort=name
	// String encodedSearch = Library.escapeUrlString(search);
	// return requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum&type=music&include-sort-headers=1&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+('dmap.itemname:*%s*','daap.songartist:*%s*','daap.songalbum:*%s*'))&sort=name&index=%d-%d", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), encodedSearch, encodedSearch, encodedSearch, start, end));
	// }
	//
	// private static PlaylistSongs requestTracks(Session session, String albumid) throws Exception
	// {
	// // http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
	// return requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), albumid));
	// }
	//
	// private static PlaylistSongs requestAlbums(Session session, int start, int end) throws Exception
	// {
	// // http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1
	// return requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&index=%d-%d", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId(), start, end));
	// }
	//
	// private static PlaylistSongs requestPlaylists(Session session) throws Exception
	// {
	// // http://192.168.254.128:3689/databases/36/containers?session-id=1686799903&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported
	// return requestParsed(String.format("%s/databases/%d/containers?session-id=%s&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported", session.getRequestBase(), session.getDatabase().getItemId(), session.getDatabase().getMasterPlaylist().getItemId(), session.getSessionId()));
	// }
	//
	// private static byte[] requestThumbnail(Session session, int itemid) throws Exception
	// {
	// // http://192.168.254.128:3689/databases/38/items/2854/extra_data/artwork?session-id=788509571&revision-number=196&mw=55&mh=55
	// return request(String.format("%s/databases/%d/items/%d/extra_data/artwork?session-id=%s&mw=55&mh=55", session.getRequestBase(), session.getDatabase().getItemId(), itemid, session.getSessionId()), false);
	// }

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
		Closeables.closeQuietly(inputStream);
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

		byte[] buffer = new byte[1024];

		URL url = new URL(remoteUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setAllowUserInteraction(false);
		connection.setRequestProperty("Viewer-Only-Client", "1");
		connection.setRequestProperty("Client-Daap-Version", "3.10");
		// allow both GZip and Deflate (ZLib) encodings
		connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

		if(!keepalive)
		{
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
		}
		else
		{
			connection.setReadTimeout(0);
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
}
