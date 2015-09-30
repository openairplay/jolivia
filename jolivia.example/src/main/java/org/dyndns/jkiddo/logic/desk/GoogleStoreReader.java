package org.dyndns.jkiddo.logic.desk;
/*
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
import java.util.concurrent.atomic.AtomicLong;

import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SongFormat;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class GoogleStoreReader implements IMusicStoreReader
{
	
	private final GoogleMusicAPI gm;

	public GoogleStoreReader(final String username, final String password) throws IOException, URISyntaxException, InvalidCredentialsException
	{
		gm = new GoogleMusicAPI(new ApacheConnector(), new JSON(), new File("."));
		gm.login(username, password);
	}

	@Override
	public Collection<MediaItem> readTunes() throws Exception
	{
		return Collections2.transform(gm.getAllSongs(), new Function<Song, MediaItem>() {

			@Override
			public MediaItem apply(final Song song)
			{
				final MediaItem mi = new MediaItem();
				mi.setSongArtist(song.getArtist());
				mi.setSongAlbum(song.getAlbum());
				mi.setItemName(song.getName());
				mi.setSongTime((int) song.getDurationMillis());
				mi.setExternalIdentifer(song.getId());
				return mi;
			}
		});
	}
	@Override
	public URI getTune(final String tune) throws Exception
	{
		final Song song = new Song();
		song.setId(tune);
		return gm.getSongURL(song);
	}

	@Override
	public void readTunesMemoryOptimized(final Listing listing, final Map<Long, String> map) throws Exception
	{
		final Collection<Song> songs = gm.getAllSongs();
		System.gc();
		final AtomicLong id = new AtomicLong();

		for(final Song song : songs)
		{
			final ListingItem item = new ListingItem();
			item.add(new ItemKind(ItemKind.AUDIO));
			item.add(new ItemId(id.get()));
			item.add(new SongAlbum(song.getAlbum()));
			item.add(new SongArtist(song.getArtist()));
			item.add(new ItemName(song.getName()));
			item.add(new SongFormat(SongFormat.MP3));
			// item.add(new SongSampleRate(SongSampleRate.KHZ_44100));
			// item.add(new SongComment(song.getId()));
			listing.add(item);
			map.put(Long.valueOf(id.getAndIncrement()), song.getId());
		}
		songs.clear();
	}
}*/
