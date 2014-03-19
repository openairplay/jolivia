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
import java.util.Collection;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class DeskMusicStoreReader implements IMusicStoreReader
{
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(DeskMusicStoreReader.class);
	private Collection<MediaItem> mapOfSongToFile;
	private File path;
	private Parser parser;

	public DeskMusicStoreReader(String path)
	{
		parser = new AutoDetectParser();
		this.mapOfSongToFile = Lists.newArrayList();
		this.path = new File(path);
	}

	public DeskMusicStoreReader(File path)
	{
		parser = new AutoDetectParser();
		this.mapOfSongToFile = Lists.newArrayList();
		this.path = path;
	}

	public DeskMusicStoreReader()
	{
		this(System.getProperty("user.dir") + System.getProperty("file.separator") + "etc");
		this.mapOfSongToFile = Lists.newArrayList();
	}

	@Override
	public Collection<MediaItem> readTunes()
	{
		try
		{
			traverseRootPathRecursively(path);
		}
		catch(Exception e)
		{
			logger.info(e.getMessage(), e);
		}
		return mapOfSongToFile;
	}

	protected void traverseRootPathRecursively(File f) throws FileNotFoundException, IOException, InterruptedException, SAXException, TikaException
	{
		if(f.isDirectory())
		{
			File[] contents = f.listFiles();
			if(contents != null)
			{
				for(int i = 0; i < contents.length; i++)
				{
					traverseRootPathRecursively(contents[i]);
				}
			}
			else
				logger.debug("Symlink'ish ... " + f.getAbsolutePath());
		}
		else if(isMusic(f))
		{
			mapOfSongToFile.add(populateSong(f));
		}
	}

	private MediaItem populateSong(File file) throws IOException, SAXException, TikaException
	{
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		InputStream content = new FileInputStream(file);
		parser.parse(content, handler, metadata, new ParseContext());

		MediaItem song = new MediaItem();
		song.setItemName(metadata.get(TikaCoreProperties.TITLE));
		if(Strings.isNullOrEmpty(metadata.get(TikaCoreProperties.TITLE)))
		{
			song.setItemName(file.getName());
		}
		// song.setGenre(metadata.get(XMPDM.GENRE));
		try
		{
			// song.setTrackNumber(metadata.getInt(XMPDM.TRACK_NUMBER));
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage(), e);
		}
		song.setSongAlbum(metadata.get(XMPDM.ALBUM));
		song.setSongArtist(metadata.get(XMPDM.ARTIST));
		// song.setComposer(metadata.get(XMPDM.COMPOSER));
		// song.setSize(file.length());
		try
		{
			song.setSongTime((int) Double.parseDouble(metadata.get(XMPDM.DURATION)));
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage(), e);
		}
		try
		{
			Calendar c = Calendar.getInstance();
			c.setTime(metadata.getDate(XMPDM.SHOT_DATE));
			// song.setYear(c.get(Calendar.YEAR));
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage(), e);
		}
		song.setExternalIdentifer(file.getAbsolutePath());
		return song;
	}
	private static boolean isMusic(File f)
	{
		if(f.getPath().endsWith(".mp3") || f.getPath().endsWith(".wav"))
			return true;
		return false;
	}

	@Override
	public URI getTune(String tune) throws Exception
	{
		return new File(tune).toURI();
	}
	/*
	 * class MusicItem implements IMusicItem { private String artist; private String album; private String title; private long duration; public String getTitle() { return title; }
	 * @Override public String getArtist() { return artist; }
	 * @Override public String getAlbum() { return album; }
	 * @Override public void setArtist(String artist) { this.artist = artist; }
	 * @Override public void setAlbum(String album) { this.album = album; }
	 * @Override public void setTitle(String title) { this.title = title; }
	 * @Override public void setComposer(String composer) { // TODO Auto-generated method stub }
	 * @Override public void setGenre(String genre) { // TODO Auto-generated method stub }
	 * @Override public void setTrackNumber(int trackNumber) { // TODO Auto-generated method stub }
	 * @Override public void setYear(int Year) { // TODO Auto-generated method stub }
	 * @Override public void setSize(long size) { // TODO Auto-generated method stub }
	 * @Override public long getSize() { // TODO Auto-generated method stub return 0; }
	 * @Override public long getDuration() { return duration; }
	 * @Override public void setDuration(long value) { duration = value; } }
	 */

	@Override
	public void readTunes(Listing listing, Map<Long, String> map) throws Exception
	{
		throw new RuntimeException("Not implemented");
		
	}
}
