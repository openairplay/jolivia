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
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tunesremote.TagListener;

/**
 * Class responsible for parsing binary HTTP responses from a DACP Server.
 * <p>
 */
public class ResponseParser
{

	public final static Logger logger = LoggerFactory.getLogger(ResponseParser.class);
	public final static String TAG = ResponseParser.class.toString();
	public final static Pattern BRANCHES = Pattern.compile("(casp|cmst|mlog|agal|mccr|mlcl|mdcl|mshl|mlit|abro|abar|agar|apso|caci|avdb|cmgt|aply|adbs)");
	public final static Pattern STRINGS = Pattern.compile("(minm|cann|cana|cang|canl|asaa|asal|asar|ascn|asgn|assa|assu|mcnm|mcna)");
	public final static Pattern RAWS = Pattern.compile("(canp)");

	public static int performSearch(byte[] raw, TagListener listener, Pattern listenFor, boolean haltmlit) throws IOException
	{
		logger.debug(TAG, "ResponseParser performSearch...");
		final DataInputStream stream = new DataInputStream(new ByteArrayInputStream(raw));
		final int hits = ResponseParser.search(stream, listener, listenFor, stream.available(), haltmlit);
		listener.searchDone();
		return hits;
	}

	public static Response performParse(byte[] raw, TagListener listener, Pattern listenFor) throws IOException
	{
		logger.debug(TAG, "ResponseParser performParse...");
		final DataInputStream stream = new DataInputStream(new ByteArrayInputStream(raw));
		final Response resp = ResponseParser.parse(stream, listener, listenFor, stream.available());
		listener.searchDone();
		return resp;
	}

	public static Response performParse(byte[] raw) throws IOException
	{
		logger.debug(TAG, "ResponseParser performParse...");
		final DataInputStream stream = new DataInputStream(new ByteArrayInputStream(raw));
		return ResponseParser.parse(stream, null, null, stream.available());
	}

	private static int search(DataInputStream raw, TagListener listener, Pattern listenFor, int handle, boolean haltmlit) throws IOException
	{
		int hits = 0;

		// loop until done with the section we have been assigned
		while(handle > 0)
		{
			final String key = ResponseParser.readString(raw, 4);
			// Log.d(TAG, key);
			int length = -1;
			try
			{
				length = raw.readInt();
			}
			catch(EOFException eofe)
			{
				return hits;
			}
			handle -= 8 + length;

			// check if we need to handle mlit special-case where it doesnt branch
			if(haltmlit && key.equals("mlit"))
			{
				final Response resp = new Response();
				resp.put(key, ResponseParser.readString(raw, length));
				listener.foundTag(key, resp);
				hits++;

			}
			else if(BRANCHES.matcher(key).matches())
			{
				if(listenFor.matcher(key).matches())
				{
					// parse and report if interesting branches
					listener.foundTag(key, ResponseParser.parse(raw, listener, listenFor, length));
					hits++;
				}
				else
				{
					// recurse searching for other branches
					hits += ResponseParser.search(raw, listener, listenFor, length, haltmlit);
				}

			}
			else
			{
				// otherwise discard data
				ResponseParser.readString(raw, length);
			}
		}

		return hits;
	}

	private static Response parse(DataInputStream raw, TagListener listener, Pattern listenFor, int handle) throws IOException
	{
		final Response resp = new Response();
		int progress = 0;

		// loop until done with the section weve been assigned
		while(handle > 0)
		{
			final String key = ResponseParser.readString(raw, 4);
			final int length = raw.readInt();
			handle -= 8 + length;
			progress += 8 + length;

			// handle key collisions by using index notation
			final String nicekey = resp.containsKey(key) ? String.format("%s[%06d]", key, progress) : key;

			if(BRANCHES.matcher(key).matches())
			{
				// recurse off to handle branches
				final Response branch = ResponseParser.parse(raw, listener, listenFor, length);
				resp.put(nicekey, branch);

				// pass along to listener if needed
				if(listener != null)
					if(listenFor.matcher(key).matches())
						listener.foundTag(key, branch);

			}
			else if(STRINGS.matcher(key).matches())
			{
				// force handling as string
				resp.put(nicekey, ResponseParser.readString(raw, length));
			}
			else if(RAWS.matcher(key).matches())
			{
				// force handling as raw
				resp.put(nicekey, ResponseParser.readRaw(raw, length));
			}
			else if(length == 1 || length == 2 || length == 4 || length == 8)
			{
				// handle parsing unsigned bytes, ints, longs
				resp.put(nicekey, new BigInteger(1, ResponseParser.readRaw(raw, length)));
			}
			else
			{
				// fallback to just parsing as string
				resp.put(nicekey, ResponseParser.readString(raw, length));
			}

		}

		return resp;
	}

	private static byte[] readRaw(DataInputStream raw, int length) throws IOException
	{
		byte[] buf = new byte[length];
		raw.read(buf, 0, length);
		return buf;
	}

	private static String readString(DataInputStream raw, int length) throws IOException
	{
		byte[] key = new byte[length];
		raw.read(key, 0, length);
		return new String(key, "UTF-8");
	}

}
