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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RequestHelper
{

	public final static String TAG = RequestHelper.class.toString();

	public final static Logger logger = LoggerFactory.getLogger(RequestHelper.class);

	// handle packaging a request off to itunes
	// based on the enum type we might ask for incremental data (searches, full
	// album list)
	// also think about handling keep-alive requests
	// this class also maintains session information

	// also consider handling image requests separately? (for caching)
	// we ask for cover art two ways:

	public static byte[] requestSearch(Session session, String search, int start, int end) throws Exception
	{
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1535976870&revision-number=61&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum&type=music&sort=name&include-sort-headers=1&query='dmap.itemname:*sea*'&index=0-7
		// doesnt seem to listen to &sort=name
		String encodedSearch = Library.escapeUrlString(search);
		return request(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum&type=music&include-sort-headers=1&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+('dmap.itemname:*%s*','daap.songartist:*%s*','daap.songalbum:*%s*'))&sort=name&index=%d-%d", session.getRequestBase(), session.databaseId, session.musicId, session.sessionId, encodedSearch, encodedSearch, encodedSearch, start, end), false);
	}

	public static byte[] requestTracks(Session session, String albumid) throws Exception
	{
		// http://192.168.254.128:3689/databases/36/containers/113/items?session-id=1301749047&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:11624070975347817354'
		return request(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.databaseId, session.musicId, session.sessionId, albumid), false);
	}

	public static byte[] requestAlbums(Session session, int start, int end) throws Exception
	{
		// http://192.168.254.128:3689/databases/36/groups?session-id=1034286700&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1
		return request(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dmap.itemname,dmap.itemid,dmap.persistentid,daap.songartist&type=music&group-type=albums&sort=artist&include-sort-headers=1&index=%d-%d", session.getRequestBase(), session.databaseId, session.musicId, session.sessionId, start, end), false);
	}

	public static byte[] requestPlaylists(Session session) throws Exception
	{
		// http://192.168.254.128:3689/databases/36/containers?session-id=1686799903&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported
		return request(String.format("%s/databases/%d/containers?session-id=%s&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported", session.getRequestBase(), session.databaseId, session.musicId, session.sessionId), false);
	}

	public static Response requestParsed(String url, boolean keepalive) throws Exception
	{
		logger.debug(TAG, url);
		return ResponseParser.performParse(request(url, keepalive));
	}

	public static void attemptRequest(String url)
	{
		try
		{
			request(url, false);
		}
		catch(Exception e)
		{
			logger.warn(TAG, "attemptRequest:" + e.getMessage());
		}
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
	public static byte[] request(String remoteUrl, boolean keepalive) throws Exception
	{
		logger.debug(TAG, String.format("started request(remote=%s)", remoteUrl));

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

	public static Bitmap requestThumbnail(Session session, int itemid) throws Exception
	{
		return requestThumbnail(session, itemid, "");
	}

	public static Bitmap requestThumbnail(Session session, int itemid, String type) throws Exception
	{
		// http://192.168.254.128:3689/databases/38/items/2854/extra_data/artwork?session-id=788509571&revision-number=196&mw=55&mh=55
		try
		{
			byte[] raw = request(String.format("%s/databases/%d/items/%d/extra_data/artwork?session-id=%s&mw=55&mh=55%s", session.getRequestBase(), session.databaseId, itemid, session.sessionId, type), false);
			return BitmapFactory.decodeByteArray(raw, 0, raw.length);
		}
		catch(java.lang.OutOfMemoryError e)
		{
			logger.warn(TAG, "Bitmap OOM:" + e.getMessage());
			return null;
		}
	}

	public static Bitmap requestBitmap(String remote) throws Exception
	{
		try
		{
			byte[] raw = request(remote, false);
			return BitmapFactory.decodeByteArray(raw, 0, raw.length);
		}
		catch(java.lang.OutOfMemoryError e)
		{
			logger.warn(TAG, "Bitmap OOM:" + e.getMessage());
			return null;
		}
	}

}
