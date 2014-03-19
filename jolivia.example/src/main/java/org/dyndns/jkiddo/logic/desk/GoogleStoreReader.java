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
import java.util.Map;

import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class GoogleStoreReader implements IMusicStoreReader
{
	/**
	 * 
	 */
	private GoogleMusicAPI gm;
	private Collection<MediaItem> songs;

	public GoogleStoreReader(String username, String password) throws IOException, URISyntaxException, InvalidCredentialsException
	{
		gm = new GoogleMusicAPI(new ApacheConnector(), new JSON(), new File("."));
		gm.login(username, password);

		songs = Collections2.transform(gm.getAllSongs(), new Function<Song, MediaItem>() {

			@Override
			public MediaItem apply(Song song)
			{
				MediaItem mi = new MediaItem();
				mi.setSongArtist(song.getArtist());
				mi.setSongAlbum(song.getAlbum());
				mi.setItemName(song.getName());
				mi.setSongTime((int) song.getDurationMillis());
				mi.setExternalIdentifer(song.getId());
				return mi;
			}
		});
	}

	public Collection<MediaItem> readTunes() throws Exception
	{
		return songs;
	}
	public URI getTune(String tune) throws Exception
	{
		Song song = new Song();
		song.setId(tune);
		return gm.getSongURL(song);
	}
	/*
	 * class MusicItem implements IMusicItem { private String artist; private String album; private String title; private long duration;
	 * @Override public String getArtist() { return artist; }
	 * @Override public String getAlbum() { return album; }
	 * @Override public void setArtist(String artist) { this.artist = artist; }
	 * @Override public void setAlbum(String album) { this.album = album; }
	 * @Override public void setComposer(String composer) { // TODO Auto-generated method stub }
	 * @Override public void setGenre(String genre) { // TODO Auto-generated method stub }
	 * @Override public void setTrackNumber(int trackNumber) { // TODO Auto-generated method stub }
	 * @Override public void setYear(int Year) { // TODO Auto-generated method stub }
	 * @Override public void setSize(long size) { // TODO Auto-generated method stub }
	 * @Override public String getTitle() { return title; }
	 * @Override public long getSize() { // TODO Auto-generated method stub return 0; }
	 * @Override public void setTitle(String title) { this.title = title; }
	 * @Override public long getDuration() { return duration; }
	 * @Override public void setDuration(long value) { duration = value; } }
	 */

	@Override
	public void readTunes(Listing listing, Map<Long, String> map) throws Exception
	{
		throw new RuntimeException("Not implemented");
		
	}
}
