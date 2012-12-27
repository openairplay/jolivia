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

package org.tunesremote.daap;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Response class to help parse DACP Responses from the server.
 * <p>
 */
public class Response extends HashMap<String, Object>
{

	private static final long serialVersionUID = -6250046829864074559L;

	public Response getNested(String key) throws Exception
	{
		return (Response) this.get(key);
	}

	public String getString(String key) throws Exception
	{
		Object obj = this.get(key);
		if(obj instanceof String)
			return (String) obj;
		return "";
	}

	public BigInteger getNumber(String key) throws Exception
	{
		Object obj = this.get(key);
		if(obj instanceof BigInteger)
			return (BigInteger) obj;
		return new BigInteger("-1");
	}

	public long getNumberLong(String key) throws Exception
	{
		return getNumber(key).longValue();
	}

	public String getNumberString(String key) throws Exception
	{
		return getNumber(key).toString();
	}

	public String getNumberHex(String key) throws Exception
	{
		return Long.toHexString(getNumberLong(key));
	}

	public byte[] getRaw(String key) throws Exception
	{
		Object obj = this.get(key);
		return (byte[]) obj;
	}

	public List<Response> findArray(String prefix) throws Exception
	{
		List<Response> found = new LinkedList<Response>();

		// find all values with same key prefix
		// sort keys to make sure we return in original order

		String[] keys = this.keySet().toArray(new String[] {});
		Arrays.sort(keys);

		for(String key : keys)
		{
			if(key.startsWith(prefix))
				found.add((Response) this.get(key));
		}

		return found;
	}

	/**
	 * Convert milliseconds to m:ss string format for track lengths.
	 * <p>
	 * 
	 * @param milliseconds
	 *            the milliseconds value to convert
	 * @return a String formatted in m:ss format
	 */
	public static String convertTime(long milliseconds)
	{
		final int seconds = (int) ((milliseconds / 1000) % 60);
		final int minutes = (int) ((milliseconds / 1000) / 60);
		return String.format("%d:%02d", minutes, seconds);
	}
}
