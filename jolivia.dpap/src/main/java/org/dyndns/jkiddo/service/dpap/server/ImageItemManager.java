package org.dyndns.jkiddo.service.dpap.server;

import java.sql.SQLException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.dmp.model.Library;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader.IImageItem;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;

public class ImageItemManager implements IItemManager
{
	private static final VersionChunk dpapProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_101);
	private static final VersionChunk dmapProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_200);

	private final Library library;
	private final IImageStoreReader reader;
	private final Map<MediaItem, IImageItem> itemToIImageItem;
	private PasswordMethod authentication;

	@Inject
	public ImageItemManager(@Named(Util.APPLICATION_NAME) String applicationName, final IImageStoreReader reader, final PasswordMethod authentication) throws Exception
	{
		library = null;
		itemToIImageItem = null;
		this.reader = null; 
		/*this.authentication = authentication;
		this.reader = reader;
		this.itemToIImageItem = Maps.uniqueIndex(reader.readImages(), new Function<IImageItem, MediaItem>() {
			@Override
			public MediaItem apply(IImageItem iImageItem)
			{
				// dpap:
				// http://192.168.1.2dpap://192.168.1.2:8770/databases/1/containers/5292/items?session-id=1101478641&meta=dpap.aspectratio,dmap.itemid,dmap.itemname,dpap.imagefilename,dpap.imagefilesize,dpap.creationdate,dpap.imagepixelwidth,dpap.imagepixelheight,dpap.imageformat,dpap.imagerating,dpap.imagecomments,dpap.imagelargefilesize&type=photo
				MediaItem item = new MediaItem(new ItemKind(ItemKind.IMAGE));
				item.addChunk(new AspectRatio(((double) iImageItem.getImageWidth() / iImageItem.getImageHeight()) + ""));
				item.addChunk(new CreationDate(iImageItem.getCreationDate().getTime()));
				item.addChunk(new ImageFilename(iImageItem.getImageFilename()));
				item.addChunk(new ItemName(iImageItem.getImageFilename()));
				item.addChunk(new ImageFileSize(iImageItem.getSize()));
				item.addChunk(new ImagePixelWidth(iImageItem.getImageWidth()));
				item.addChunk(new ImagePixelHeight(iImageItem.getImageHeight()));
				item.addChunk(new ImageFormat(iImageItem.getFormat()));
				item.addChunk(new ImageRating(iImageItem.getRating()));
				item.addChunk(new ImageLargeFileSize(iImageItem.getSize()));
				item.addChunk(new ImageComment("This photo is served by Jolivia"));
				// try
				// {
				// item.addChunk(new FileData(iImageItem.getImageThumb()));
				// }
				// catch(Exception e)
				// {
				// e.printStackTrace();
				// }

				return item;
			}
		});

		this.library = new Library(applicationName);
		Database database = new Database(this.library);
		database.addMediaItems(itemToIImageItem.keySet());
		this.library.addDatabase(database);*/
	}

	@Override
	public PasswordMethod getAuthenticationMethod()
	{
		return this.authentication;
	}

	@Override
	public VersionChunk getMediaProtocolVersion()
	{
		return dmapProtocolVersion;
	}

	@Override
	public VersionChunk getAudioProtocolVersion()
	{
		return null;
	}

	@Override
	public VersionChunk getPictureProtocolVersion()
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
/*
	@Override
	public Database getDatabase(long databaseId)
	{
		return library.getDatabase(databaseId);
	}*/

	@Override
	public byte[] getItemAsByteArray(long databaseId, long itemId)
	{
		/*MediaItem image = library.getDatabase(databaseId).getMasterContainer().getItem(itemId);
		try
		{
			return DmapUtil.uriTobuffer(reader.getImage(itemToIImageItem.get(image)));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}*/
		return null;
	}

	public byte[] getThumb(long databaseId, long itemId)
	{
		/*MediaItem image = library.getDatabase(databaseId).getMasterContainer().getItem(itemId);
		try
		{
			return reader.getImageThumb(itemToIImageItem.get(image));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}*/
		return null;
	}

	@Override
	public Listing getContainers(long databaseId, Iterable<String> parameters)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Listing getMediaItems(long databaseId, long containerId, Iterable<String> parameters)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Listing getMediaItems(long databaseId, Iterable<String> parameters)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Listing getDatabases() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
