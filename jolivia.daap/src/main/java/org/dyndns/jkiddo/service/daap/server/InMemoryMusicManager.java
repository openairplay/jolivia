package org.dyndns.jkiddo.service.daap.server;

import java.net.URI;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ExtendedMediaKind;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.SmartPlaylist;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.SpecialPlaylist;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.ContainerCount;
import org.dyndns.jkiddo.dmp.chunks.media.DatabaseShareType;
import org.dyndns.jkiddo.dmp.chunks.media.EditCommandSupported;
import org.dyndns.jkiddo.dmp.chunks.media.ItemCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.ParentContainerId;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;

public class InMemoryMusicManager implements IItemManager
{

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
	public long getSessionId(final String remoteHost)
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
		catch(final InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}

	@Override
	public long getRevision(final String remoteHost, final long sessionId)
	{
		return 42;
	}

	@Override
	public Listing getContainers(final long databaseId, final Iterable<String> parameters) throws SQLException
	{
		return containersResponse;
	}

	@Override
	public Listing getMediaItems(final long databaseId, final long containerId, final Iterable<String> parameters) throws SQLException
	{
		if(containerId == 1 || containerId == 2)
			return mediaItemsResponse;
		else
		{
			return new Listing();	
		}
	}

	@Override
	public Listing getMediaItems(final long databaseId, final Iterable<String> parameters) throws SQLException
	{
		return mediaItemsResponse;
	}

	@Override
	public Listing getDatabases() throws SQLException
	{
		return databasesResponse;
	}

	@Override
	public byte[] getItemAsByteArray(final long databaseId, final long itemId)
	{
		try
		{
			final String identifier = map.get(Long.valueOf(itemId));
			final URI uri = storeReader.getTune(identifier);
			return DmapUtil.uriTobuffer(uri);
		}
		catch(final Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Inject
	public InMemoryMusicManager(@Named(Util.APPLICATION_NAME) final String applicationName, final IMusicStoreReader reader, final PasswordMethod pm) throws Exception
	{
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
		
		reader.readTunesMemoryOptimized(mediaItemsResponse, map);
		
		

		final Listing databaselisting = new Listing();
		final ListingItem databaselistingItem = new ListingItem();
		databaselistingItem.add(new ItemId(1));
		
		databaselistingItem.add(new DatabaseShareType(DatabaseShareType.LOCAL));
		databaselistingItem.add(new ExtendedMediaKind(ExtendedMediaKind.UNKNOWN_ONE));
		
		databaselistingItem.add(new ItemName(applicationName));
		databaselistingItem.add(new ItemCount(1));
		databaselistingItem.add(new ContainerCount(1));
		databaselisting.add(databaselistingItem);
		databasesResponse = databaselisting;

		final Listing containerlisting = new Listing();
		//Base container
		{
			final ListingItem containerlistingItem = new ListingItem();
			containerlistingItem.add(new ItemId(1));
			containerlistingItem.add(new PersistentId(149483767));
			containerlistingItem.add(new ItemName(applicationName + "'s library"));
			containerlistingItem.add(new BaseContainer(1));
			containerlistingItem.add(new ParentContainerId(0));
			containerlistingItem.add(new ItemCount(map.size()));
			containerlisting.add(containerlistingItem);
		}
		
		{
			final ListingItem containerlistingItem = new ListingItem();
			containerlistingItem.add(new ItemId(2));
			containerlistingItem.add(new PersistentId(149483766));
			containerlistingItem.add(new ItemName("Music"));
			containerlistingItem.add(new SmartPlaylist(true));
			containerlistingItem.add(new ParentContainerId(0));
			containerlistingItem.add(new SpecialPlaylist(SpecialPlaylist.MUSIC_LIBRARY));
			containerlistingItem.add(new ItemCount(map.size()));
			containerlisting.add(containerlistingItem);
		}

		{
			final ListingItem containerlistingItem = new ListingItem();
			containerlistingItem.add(new ItemId(3));
			containerlistingItem.add(new PersistentId(149483765));
			containerlistingItem.add(new ItemName("Movies"));
			containerlistingItem.add(new SmartPlaylist(true));
			containerlistingItem.add(new ParentContainerId(0));
			containerlistingItem.add(new SpecialPlaylist(SpecialPlaylist.MOVIES_LIBRARY));
			containerlistingItem.add(new EditCommandSupported(0));
			containerlistingItem.add(new ItemCount(0));
			containerlisting.add(containerlistingItem);
		}
		containersResponse = containerlisting;
	}
}
