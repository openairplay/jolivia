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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class DeskMusicStoreReader implements IMusicStoreReader
{
	private static final Logger logger = LoggerFactory.getLogger(DeskMusicStoreReader.class);
	private Map<IMusicItem, File> mapOfSongToFile;
	private String path;

	public DeskMusicStoreReader(String path)
	{
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
			e.printStackTrace();
		}
		return Collections.unmodifiableSet(mapOfSongToFile.keySet());
	}

	protected void traverseRootPathRecursively(File f) throws FileNotFoundException, IOException, InterruptedException
	{
		if(f.isDirectory())
		{
			File[] contents = f.listFiles();
			for(int i = 0; i < contents.length; i++)
			{
				traverseRootPathRecursively(contents[i]);
			}
		}
		else if(isMp3(f))
		{
			addFileToDatabase(f);
		}
	}

	private void addFileToDatabase(File f)
	{

		try
		{
			mapOfSongToFile.put(populateSong(f), f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private IMusicItem populateSong(File file) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		AudioFile f = AudioFileIO.read(file);
		Tag tag = f.getTag();
		if(tag == null)
		{
			tag = new ID3v24Tag();
		}

		for(FieldKey k : FieldKey.values())
		{
			List<TagField> list = tag.getFields(k);
			for(TagField tf : list)
			{
				System.out.println(k + " " + new String(tf.getRawContent()));
			}
		}

		IMusicItem song = new MusicItem();
		song.setArtist(tag.getFirst(FieldKey.ARTIST));
		song.setAlbum(tag.getFirst(FieldKey.ALBUM));
		String title = tag.getFirst(FieldKey.TITLE);
		song.setName(title);
		if(Strings.isNullOrEmpty(title))
		{
			logger.debug("Title in file " + file.getName() + " tags was null - using filename instead");
			song.setName(AudioFile.getBaseFilename(file));	
		}
		song.setComposer(tag.getFirst(FieldKey.COMPOSER));
		song.setGenre(tag.getFirst(FieldKey.GENRE));
		if(!Strings.isNullOrEmpty(tag.getFirst(FieldKey.TRACK)))
			song.setTrackNumber(Integer.parseInt(tag.getFirst(FieldKey.TRACK)));
		if(!Strings.isNullOrEmpty(tag.getFirst(FieldKey.YEAR)))
			song.setYear(Integer.parseInt(tag.getFirst(FieldKey.YEAR)));
		// f.setTag(tag);
		// AudioFileIO.write(f);
		song.setSize(file.length());
		// song.setTime(t, 217778);
		return song;
	}

	private static boolean isMp3(File f)
	{
		if(!f.getPath().endsWith(".mp3"))
			return false;
		return true;
	}

	@Override
	public File getTune(IMusicItem tune) throws Exception
	{
		if(tune != null)
		{
			logger.debug("Serving " + tune.getArtist() + " " + tune.getAlbum());
		}
		return mapOfSongToFile.get(tune);
	}

	class MusicItem implements IMusicItem
	{
		private String artist;
		private String album;
		private String name;

		public String getName() {
			return name;
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
		public void setName(String name)
		{
			this.name = name;
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
