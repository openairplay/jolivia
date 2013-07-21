package org.dyndns.jkiddo.service.daap.server;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.Library;
import org.dyndns.jkiddo.dmap.MediaItem;
import org.dyndns.jkiddo.dmap.chunks.VersionChunk;
import org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SongFormat;
import org.dyndns.jkiddo.dmap.chunks.audio.SongSampleRate;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.MusicSharingVersion;
import org.dyndns.jkiddo.dmap.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmap.chunks.media.ItemName;
import org.dyndns.jkiddo.dmap.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader.IMusicItem;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class MusicItemManager implements IItemManager
{
	private static final Logger logger = LoggerFactory.getLogger(MusicItemManager.class);

	private static final VersionChunk pictureProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_200);
	private static final VersionChunk audioProtocolVersion = new AudioProtocolVersion(DmapUtil.APRO_VERSION_3011);
	private static final VersionChunk mediaProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_209);
	private static final MusicSharingVersion musicSharingVersion = new MusicSharingVersion(DmapUtil.MUSIC_SHARING_VERSION_309);

	private final Library library;
	private final IMusicStoreReader reader;
	private final Map<MediaItem, IMusicItem> itemToIMusicItem;

	@Inject
	public MusicItemManager(@Named(Util.APPLICATION_NAME) String applicationName, IMusicStoreReader reader) throws Exception
	{
		this.reader = reader;
		this.itemToIMusicItem = Maps.uniqueIndex(reader.readTunes(), new Function<IMusicItem, MediaItem>() {
			@Override
			public MediaItem apply(IMusicItem iMusicItem)
			{
				MediaItem item = new MediaItem(new ItemKind(ItemKind.AUDIO));
				item.addChunk(new SongAlbum(iMusicItem.getAlbum()));
				item.addChunk(new SongArtist(iMusicItem.getArtist()));
				if(Strings.isNullOrEmpty(iMusicItem.getTitle()))
				{
					logger.warn("Name of " + iMusicItem + " was null. Song/Item may not be displayed");
				}
				item.addChunk(new ItemName(iMusicItem.getTitle()));
				item.addChunk(new SongFormat(SongFormat.MP3));
				item.addChunk(new SongSampleRate(SongSampleRate.KHZ_44100));
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

	public MusicSharingVersion getMusicSharingVersion()
	{
		return musicSharingVersion;
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
	public byte[] getItemAsByteArray(long databaseId, long itemId)
	{
		MediaItem song = library.getDatabase(databaseId).getMasterContainer().getItem(itemId);
		try
		{
			return DmapUtil.uriTobuffer(reader.getTune(itemToIMusicItem.get(song)));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
