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
package org.dyndns.jkiddo.logic.desk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.base.Strings;

public class DeskMusicStoreReader implements IMusicStoreReader
{
	private static final Logger logger = LoggerFactory.getLogger(DeskMusicStoreReader.class);
	private Map<IMusicItem, File> mapOfSongToFile;
	private String path;
	private Parser parser;

	public DeskMusicStoreReader(String path)
	{
		parser = new AutoDetectParser();
		this.mapOfSongToFile = new HashMap<IMusicItem, File>();
		this.path = path;
	}

	public DeskMusicStoreReader()
	{
		this(System.getProperty("user.dir") + System.getProperty("file.separator") + "etc");
		this.mapOfSongToFile = new HashMap<IMusicItem, File>();
	}

	@Override
	public Set<IMusicItem> readTunes()
	{
		try
		{
			traverseRootPathRecursively(new File(path));
		}
		catch(Exception e)
		{
			logger.info(e.getMessage(),e);
		}
		return Collections.unmodifiableSet(mapOfSongToFile.keySet());
	}

	protected void traverseRootPathRecursively(File f) throws FileNotFoundException, IOException, InterruptedException, SAXException, TikaException
	{
		if(f.isDirectory())
		{
			File[] contents = f.listFiles();
			for(int i = 0; i < contents.length; i++)
			{
				traverseRootPathRecursively(contents[i]);
			}
		}
		else if(isMusic(f))
		{
			mapOfSongToFile.put(populateSong(f), f);
		}
	}

	@SuppressWarnings("deprecation")
	private IMusicItem populateSong(File file) throws IOException, SAXException, TikaException
	{
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		InputStream content = new FileInputStream(file);
		parser.parse(content, handler, metadata, new ParseContext());

		IMusicItem song = new MusicItem();
		song.setTitle(metadata.get(Metadata.TITLE));
		if(Strings.isNullOrEmpty(song.getTitle()))
		{
			song.setTitle(file.getName());
		}
		song.setGenre(metadata.get(XMPDM.GENRE));
		try
		{
			song.setTrackNumber(metadata.getInt(XMPDM.TRACK_NUMBER));
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage(),e);
		}
		song.setArtist(metadata.get(XMPDM.ARTIST));
		song.setComposer(metadata.get(XMPDM.COMPOSER));
		song.setSize(file.length());
		try
		{
			Calendar c = Calendar.getInstance();
			c.setTime(metadata.getDate(XMPDM.SHOT_DATE));
			song.setYear(c.get(Calendar.YEAR));
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage(),e);
		}
		return song;
	}
	private static boolean isMusic(File f)
	{
		if(f.getPath().endsWith(".mp3") || f.getPath().endsWith(".wav"))
			return true;
		return false;
	}

	@Override
	public URI getTune(IMusicItem tune) throws Exception
	{
		if(tune != null)
		{
			logger.debug("Serving " + tune.getArtist() + " " + tune.getAlbum());
		}
		return mapOfSongToFile.get(tune).toURI();
	}

	class MusicItem implements IMusicItem
	{
		private String artist;
		private String album;
		private String title;

		public String getTitle()
		{
			return title;
		}

		@Override
		public String getArtist()
		{
			return artist;
		}

		@Override
		public String getAlbum()
		{
			return album;
		}

		@Override
		public void setArtist(String artist)
		{
			this.artist = artist;

		}

		@Override
		public void setAlbum(String album)
		{
			this.album = album;

		}

		@Override
		public void setTitle(String title)
		{
			this.title = title;
		}

		@Override
		public void setComposer(String composer)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void setGenre(String genre)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void setTrackNumber(int trackNumber)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void setYear(int Year)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void setSize(long size)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public long getSize()
		{
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
