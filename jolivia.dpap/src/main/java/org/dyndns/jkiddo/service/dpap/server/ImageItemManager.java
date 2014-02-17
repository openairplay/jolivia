package org.dyndns.jkiddo.service.dpap.server;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.dmp.IDatabase;
import org.dyndns.jkiddo.dmp.ILibrary;
import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
import org.dyndns.jkiddo.dmp.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.dmp.model.Library;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.dpap.chunks.picture.AspectRatio;
import org.dyndns.jkiddo.dpap.chunks.picture.CreationDate;
import org.dyndns.jkiddo.dpap.chunks.picture.ImageComment;
import org.dyndns.jkiddo.dpap.chunks.picture.ImageFileSize;
import org.dyndns.jkiddo.dpap.chunks.picture.ImageFilename;
import org.dyndns.jkiddo.dpap.chunks.picture.ImageFormat;
import org.dyndns.jkiddo.dpap.chunks.picture.ImageLargeFileSize;
import org.dyndns.jkiddo.dpap.chunks.picture.ImagePixelHeight;
import org.dyndns.jkiddo.dpap.chunks.picture.ImagePixelWidth;
import org.dyndns.jkiddo.dpap.chunks.picture.ImageRating;
import org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader.IImageItem;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class ImageItemManager implements IItemManager
{
	private static final VersionChunk dpapProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_101);
	private static final VersionChunk dmapProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_200);

	private final ILibrary library;
	private final IImageStoreReader reader;
	private final Map<MediaItem, IImageItem> itemToIImageItem;
	private PasswordMethod authentication;

	@Inject
	public ImageItemManager(@Named(Util.APPLICATION_NAME) String applicationName, final IImageStoreReader reader, final PasswordMethod authentication) throws Exception
	{
		this.authentication = authentication;
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
		Database database = new Database(applicationName);
		database.setMediaItems(itemToIImageItem.keySet());
		this.library.addDatabase(database);
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

	@Override
	public Collection<IDatabase> getDatabases()
	{
		return library.getDatabases();
	}

	@Override
	public IDatabase getDatabase(long databaseId)
	{
		return library.getDatabase(databaseId);
	}

	@Override
	public byte[] getItemAsByteArray(long databaseId, long itemId)
	{
		MediaItem image = library.getDatabase(databaseId).getMasterContainer().getItem(itemId);
		try
		{
			return DmapUtil.uriTobuffer(reader.getImage(itemToIImageItem.get(image)));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public byte[] getThumb(long databaseId, long itemId)
	{
		MediaItem image = library.getDatabase(databaseId).getMasterContainer().getItem(itemId);
		try
		{
			return reader.getImageThumb(itemToIImageItem.get(image));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
