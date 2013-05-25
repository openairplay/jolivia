package org.dyndns.jkiddo.service.dpap.server;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.Item;
import org.dyndns.jkiddo.dmap.Library;
import org.dyndns.jkiddo.dmap.chunks.VersionChunk;
import org.dyndns.jkiddo.dmap.chunks.media.DmapProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmap.chunks.picture.ProtocolVersion;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader.IImageItem;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ImageItemManager implements IItemManager
{
	private static final VersionChunk dpapProtocolVersion = new ProtocolVersion(DmapUtil.PPRO_VERSION_101);
	private static final VersionChunk dmapProtocolVersion = new DmapProtocolVersion(DmapUtil.MPRO_VERSION_200);
	
	private final Library library;
	private final IImageStoreReader reader;
	private final Map<Item, IImageItem> itemToIImageItem;

	@Inject
	public ImageItemManager(@Named(Util.APPLICATION_NAME) String applicationName, IImageStoreReader reader) throws Exception
	{
		this.reader = reader;
		this.itemToIImageItem = Maps.uniqueIndex(reader.readImages(), new Function<IImageItem, Item>() {
			@Override
			public Item apply(IImageItem iMusicItem)
			{
				Item item = new Item();
				return item;
			}
		});

		this.library = new Library(applicationName);
		Database database = new Database(applicationName);
		database.setSongs(null, itemToIImageItem.keySet());
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
		return null;
	}

	@Override
	public VersionChunk getProtocolVersion()
	{
		return dpapProtocolVersion;
	}

	@Override
	public String getDMAPKey()
	{
		return "DPAP-Server";
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
		Item image = library.getDatabase(databaseId).getMasterContainer().getSong(itemId);
		try
		{
			return reader.getImage(itemToIImageItem.get(image));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
