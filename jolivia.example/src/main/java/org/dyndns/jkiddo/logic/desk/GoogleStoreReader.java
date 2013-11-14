package org.dyndns.jkiddo.logic.desk;

import gmusic.api.comm.ApacheConnector;
import gmusic.api.comm.JSON;
import gmusic.api.impl.GoogleMusicAPI;
import gmusic.api.impl.InvalidCredentialsException;
import gmusic.api.model.Song;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

public class GoogleStoreReader implements IMusicStoreReader
{
	/**
	 * 
	 */
	private GoogleMusicAPI gm;
	private Map<IMusicItem, Song> songs;

	public GoogleStoreReader(String username, String password) throws IOException, URISyntaxException, InvalidCredentialsException
	{
		gm = new GoogleMusicAPI(new ApacheConnector(), new JSON(), new File("."));
		gm.login(username, password);
		songs = songsToMusicItem();
	}

	private Map<IMusicItem, Song> songsToMusicItem() throws IOException, URISyntaxException
	{
		Collection<Song> set = gm.getAllSongs();
		Map<IMusicItem, Song> items = new HashMap<IMusicItem, Song>();
		for(Song song : set)
		{
			MusicItem mi = new MusicItem();
			mi.setArtist(song.getArtist());
			mi.setAlbum(song.getAlbum());
			mi.setTitle(song.getName());
			mi.setDuration(song.getDurationMillis());
			items.put(mi, song);
		}
		return items;
	}

	public Set<IMusicItem> readTunes() throws Exception
	{
		return songs.keySet();
	}
	public URI getTune(IMusicItem tune) throws Exception
	{
		return gm.getSongURL(songs.get(tune));
	}

	class MusicItem implements IMusicItem
	{
		private String artist;
		private String album;
		private String title;
		private long duration;

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
		public String getTitle()
		{
			return title;
		}

		@Override
		public long getSize()
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setTitle(String title)
		{
			this.title = title;
		}

		@Override
		public long getDuration() {
			return duration;
		}

		@Override
		public void setDuration(long value) {
			duration = value;			
		}
	}
}
