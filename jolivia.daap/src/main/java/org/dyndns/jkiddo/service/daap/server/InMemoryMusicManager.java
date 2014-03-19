package org.dyndns.jkiddo.service.daap.server;

import java.net.URI;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.audio.SongComment;
import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.ContainerCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.common.collect.ImmutableMap;

public class InMemoryMusicManager implements IItemManager
{

	private static final VersionChunk pictureProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_200);
	private static final VersionChunk audioProtocolVersion = new AudioProtocolVersion(DmapUtil.APRO_VERSION_3011);
	private static final VersionChunk mediaProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_209);

	private final Listing databasesResponse;
	private final Listing containersResponse;
	private final Listing mediaItemsResponse;
	private final IMusicStoreReader storeReader;
	private final Map<Long, String> map;

	@Override
	public PasswordMethod getAuthenticationMethod()
	{
		return PasswordMethod.NO_PASSWORD;
	}

	@Override
	public VersionChunk getMediaProtocolVersion()
	{
		return mediaProtocolVersion;
	}

	@Override
	public VersionChunk getAudioProtocolVersion()
	{
		return audioProtocolVersion;
	}

	@Override
	public VersionChunk getPictureProtocolVersion()
	{
		return pictureProtocolVersion;
	}

	@Override
	public String getDMAPKey()
	{
		return "DAAP-Server";
	}

	@Override
	public long getSessionId(String remoteHost)
	{
		return 42;
	}

	@Override
	public void waitForUpdate()
	{
		try
		{
			Thread.sleep(100000000);
		}
		catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}

	@Override
	public long getRevision(String remoteHost, long sessionId)
	{
		return 42;
	}

	@Override
	public Listing getContainers(long databaseId, Iterable<String> parameters) throws SQLException
	{
		return containersResponse;
	}

	@Override
	public Listing getMediaItems(long databaseId, long containerId, Iterable<String> parameters) throws SQLException
	{
		return mediaItemsResponse;
	}

	@Override
	public Listing getMediaItems(long databaseId, Iterable<String> parameters) throws SQLException
	{
		return mediaItemsResponse;
	}

	@Override
	public Listing getDatabases() throws SQLException
	{
		return databasesResponse;
	}

	@Override
	public byte[] getItemAsByteArray(long databaseId, long itemId)
	{
		try
		{
			String identifier = map.get(itemId);
			URI uri = storeReader.getTune(identifier);
			return DmapUtil.uriTobuffer(uri);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Inject
	public InMemoryMusicManager(@Named(Util.APPLICATION_NAME) String applicationName, IMusicStoreReader reader, PasswordMethod pm) throws Exception
	{
		final AtomicLong id = new AtomicLong();
		storeReader = reader;

		/*map = FluentIterable.from(reader.readTunes()).transform(new Function<MediaItem, ListingItem>() {

			@Override
			public ListingItem apply(MediaItem mediaItem)
			{
				ListingItem item = new ListingItem();
				item.add(new ItemKind(ItemKind.AUDIO));
				item.add(new ItemId(id.getAndIncrement()));
				item.add(new SongAlbum(mediaItem.getSongAlbum()));
				item.add(new SongArtist(mediaItem.getSongArtist()));
				item.add(new ItemName(mediaItem.getItemName()));
				item.add(new SongFormat(SongFormat.MP3));
				item.add(new SongSampleRate(SongSampleRate.KHZ_44100));
				item.add(new SongTime(mediaItem.getSongTime()));
				item.add(new SongComment(mediaItem.getExternalIdentifer()));
				return item;
			}
		}).uniqueIndex(new Function<ListingItem, Long>() {

			@Override
			public Long apply(ListingItem input)
			{
				return Long.valueOf(input.getSpecificChunk(ItemId.class).getValue());
			}
		});

		Listing mediaItemslisting = new Listing();*/
		/*
		 * for(ListingItem v : map.values()) { mediaItemslisting.add(v); }
		 */
		/*UnmodifiableIterator<ListingItem> coll = map.values().iterator();
		for(int i = 0; i < 100; i++)
		{
			if(coll.hasNext())
			{
				mediaItemslisting.add(coll.next());				
			}
		}*/

		mediaItemsResponse = new Listing();
		map = new HashMap<Long,String>();
		
		reader.readTunes(mediaItemsResponse, map);
		
		

		Listing databaselisting = new Listing();
		ListingItem databaselistingItem = new ListingItem();
		databaselistingItem.add(new ItemId(1));
		databaselistingItem.add(new ItemName("Jolivia"));
		databaselistingItem.add(new ItemCount(1));
		databaselistingItem.add(new ContainerCount(1));
		databaselisting.add(databaselistingItem);
		databasesResponse = databaselisting;

		Listing containerlisting = new Listing();
		ListingItem containerlistingItem = new ListingItem();
		containerlistingItem.add(new ItemId(1));
		containerlistingItem.add(new ItemName("MasterPlayList"));
		containerlisting.add(containerlistingItem);
		containersResponse = containerlisting;
	}
}