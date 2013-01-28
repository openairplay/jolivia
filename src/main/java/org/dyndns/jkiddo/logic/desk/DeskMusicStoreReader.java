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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dyndns.jkiddo.dmap.Song;
import org.dyndns.jkiddo.dmap.Transaction;
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
	private Map<Song, File> mapOfSongToFile;
	private String path;

	public DeskMusicStoreReader(String path)
	{
		this.mapOfSongToFile = new HashMap<Song, File>();
		this.path = path;
	}

	public DeskMusicStoreReader()
	{
		this(System.getProperty("user.dir") + System.getProperty("file.separator") + "etc");
		this.mapOfSongToFile = new HashMap<Song, File>();
	}

	@Override
	public Set<Song> readTunes()
	{
		try
		{
			traverseRootPathRecursively(new File(path));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return mapOfSongToFile.keySet();
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

	private Song populateSong(File file) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
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

		Transaction t = null;
		Song song = new Song();
		song.setArtist(t, tag.getFirst(FieldKey.ARTIST));
		song.setAlbum(t, tag.getFirst(FieldKey.ALBUM));
		song.setName(t, tag.getFirst(FieldKey.TITLE));
		song.setComposer(t, tag.getFirst(FieldKey.COMPOSER));
		song.setGenre(t, tag.getFirst(FieldKey.GENRE));
		if(!Strings.isNullOrEmpty(tag.getFirst(FieldKey.TRACK)))
			song.setTrackNumber(t, Integer.parseInt(tag.getFirst(FieldKey.TRACK)));
		if(!Strings.isNullOrEmpty(tag.getFirst(FieldKey.YEAR)))
			song.setYear(t, Integer.parseInt(tag.getFirst(FieldKey.YEAR)));
		// f.setTag(tag);
		// AudioFileIO.write(f);
		song.setSize(t, file.length());
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
	public File getTune(Song tune) throws Exception
	{
		if(tune != null)
		{
			logger.debug("Serving " + tune.getArtist() + " " + tune.getAlbum());
		}
		return mapOfSongToFile.get(tune);
	}

	@Override
	public String getLibraryName()
	{
		return getClass().getName();
	}
}
