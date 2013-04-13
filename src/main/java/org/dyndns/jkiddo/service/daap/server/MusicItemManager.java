package org.dyndns.jkiddo.service.daap.server;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.Item;
import org.dyndns.jkiddo.dmap.Library;
import org.dyndns.jkiddo.dmap.chunks.VersionChunk;
import org.dyndns.jkiddo.dmap.chunks.com.apple.itunes.MusicSharingVersion;
import org.dyndns.jkiddo.dmap.chunks.daap.DaapProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmap.chunks.dmap.DmapProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.dpap.ProtocolVersion;
import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader.IMusicItem;
import org.dyndns.jkiddo.service.dmap.IItemManager;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MusicItemManager implements IItemManager
{

	private static final VersionChunk protocolVersion = new ProtocolVersion(DmapUtil.PPRO_VERSION_200);
	private static final VersionChunk daapProtocolVersion = new DaapProtocolVersion(DmapUtil.APRO_VERSION_3011);
	private static final VersionChunk dmapProtocolVersion = new DmapProtocolVersion(DmapUtil.MPRO_VERSION_209);
	private static final MusicSharingVersion musicSharingVersion = new MusicSharingVersion(DmapUtil.MUSIC_SHARING_VERSION_309);

	private final Library library;
	private final IMusicStoreReader reader;
	private final Map<Item, IMusicItem> itemToIMusicItem;

	@Inject
	public MusicItemManager(@Named(JoliviaListener.APPLICATION_NAME) String applicationName, IMusicStoreReader reader) throws Exception
	{
		this.reader = reader;
		this.itemToIMusicItem = Maps.uniqueIndex(reader.readTunes(), new Function<IMusicItem, Item>() {
			@Override
			public Item apply(IMusicItem iMusicItem)
			{
				Item item = new Item();
				item.setAlbum(null, iMusicItem.getAlbum());
				item.setArtist(null, iMusicItem.getArtist());
				return item;
			}
		});

		this.library = new Library(applicationName);
		Database database = new Database(applicationName);
		database.setSongs(null, itemToIMusicItem.keySet());
		this.library.addDatabase(null, database);
	}
	@Override
	public PasswordMethod getAuthenticationMethod()
	{
		return PasswordMethod.NO_PASSWORD;
	}

	@Override
	public VersionChunk getDmapProtocolVersion()
	{
		return dmapProtocolVersion;
	}

	@Override
	public VersionChunk getDaapProtocolVersion()
	{
		return daapProtocolVersion;
	}

	@Override
	public VersionChunk getProtocolVersion()
	{
		return protocolVersion;
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
		return library.getRevision();
	}

	@Override
	public Collection<Database> getDatabases()
	{
		return library.getDatabases();
	}

	@Override
	public Database getDatabase(long databaseId)
	{
		return library.getDatabase(databaseId);
	}

	@Override
	public File getItemAsFile(long databaseId, long itemId)
	{
		Item song = library.getDatabase(databaseId).getMasterContainer().getSong(itemId);
		try
		{
			return reader.getTune(itemToIMusicItem.get(song));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public MusicSharingVersion getMusicSharingVersion() {
		return musicSharingVersion;
	}
}
