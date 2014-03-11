package org.dyndns.jkiddo.service.daap.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SongFormat;
import org.dyndns.jkiddo.dmap.chunks.audio.SongSampleRate;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTime;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.MusicSharingVersion;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.ChunkFactory;
import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.ContainerCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmp.model.Container;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.dmp.model.Library;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader.IMusicItem;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class MusicItemManager implements IItemManager
{
	private static final Logger logger = LoggerFactory.getLogger(MusicItemManager.class);

	private static final VersionChunk pictureProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_200);
	private static final VersionChunk audioProtocolVersion = new AudioProtocolVersion(DmapUtil.APRO_VERSION_3011);
	private static final VersionChunk mediaProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_209);
	private static final MusicSharingVersion musicSharingVersion = new MusicSharingVersion(DmapUtil.MUSIC_SHARING_VERSION_309);

	private final IMusicStoreReader reader;
	private final Map<MediaItem, IMusicItem> itemToIMusicItem;

	private final PasswordMethod passwordMethod;

	private Dao<Library, Integer> libraryDao;
	private Dao<Database, Integer> databaseDao;
	private Dao<Container, Integer> containerDao;
	private Dao<MediaItem, Integer> mediaItemDao;

	private JdbcConnectionSource connectionSource;

	@Inject
	public MusicItemManager(@Named(Util.APPLICATION_NAME) String applicationName, IMusicStoreReader reader, PasswordMethod pm) throws Exception
	{
		String databaseUrl = "jdbc:h2:mem:test";
		connectionSource = new JdbcConnectionSource(databaseUrl);

		databaseDao = DaoManager.createDao(connectionSource, Database.class);
		containerDao = DaoManager.createDao(connectionSource, Container.class);
		mediaItemDao = DaoManager.createDao(connectionSource, MediaItem.class);
		libraryDao = DaoManager.createDao(connectionSource, Library.class);

		TableUtils.createTableIfNotExists(connectionSource, Library.class);
		TableUtils.createTableIfNotExists(connectionSource, Database.class);
		TableUtils.createTableIfNotExists(connectionSource, Container.class);
		TableUtils.createTableIfNotExists(connectionSource, MediaItem.class);
		
		Library library = new Library(applicationName);
		Database database = new Database(applicationName, "music", library);
		Container container = new Container("MasterPlaylist", -1, -1, database, 1);

		libraryDao.createIfNotExists(library);
		databaseDao.createIfNotExists(database);
		containerDao.createIfNotExists(container);

		this.passwordMethod = pm;
		this.reader = reader;
		this.itemToIMusicItem = Maps.uniqueIndex(reader.readTunes(), new Function<IMusicItem, MediaItem>() {
			@Override
			public MediaItem apply(IMusicItem iMusicItem)
			{
				MediaItem item = new MediaItem();
				item.setItemKind(new ItemKind(ItemKind.AUDIO).getValue());
				item.setSongAlbum(new SongAlbum(iMusicItem.getAlbum()).getValue());
				item.setSongArtist(new SongArtist(iMusicItem.getArtist()).getValue());
				if(Strings.isNullOrEmpty(iMusicItem.getTitle()))
				{
					logger.warn("Name of " + iMusicItem + " was null. Song/Item may not be displayed");
				}
				item.setItemName(new ItemName(iMusicItem.getTitle()).getValue());
				item.setMediaFormat(new SongFormat(SongFormat.MP3).getValue());
				item.setSongSampleRate(new SongSampleRate(SongSampleRate.KHZ_44100).getValue());
				item.setSongTime(new SongTime(iMusicItem.getDuration()).getValue());
				// if musicItem has album art remember the following:
				//
				// new SongExtraData(1);
				// new ArtworkChecksum(); 4 bytes
				// new SongArtworkCount(1);

				return item;
			}
		});

		for(MediaItem item : itemToIMusicItem.keySet())
		{
			item.setDatabase(database);
			mediaItemDao.createIfNotExists(item);
		}

	}
	@Override
	public PasswordMethod getAuthenticationMethod()
	{
		return passwordMethod;
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
		return 42;
	}

	@Override
	public Listing getDatabases() throws SQLException
	{
		Listing listing = new Listing();

		for(Database database : databaseDao.queryForAll())
		{
			ListingItem listingItem = new ListingItem();
			listingItem.add(new ItemId(database.getItemId()));
			listingItem.add(new PersistentId(database.getPersistentId()));
			listingItem.add(new ItemName(database.getName()));
			listingItem.add(new ItemCount(database.getMediaItems().size()));
			listingItem.add(new ContainerCount(database.getContainers().size()));
			listing.add(listingItem);
		}
		return listing;
	}

	private Library getLibrary() throws SQLException
	{
		final List<Library> libraries = libraryDao.queryForAll();
		if(libraries.size() == 1)
		{
			return libraryDao.queryForAll().iterator().next();
		}
		else
			throw new RuntimeException("Multiple libraries is currently not supported");
	}

	@Override
	public Database getDatabase(long databaseId) throws SQLException
	{
		return databaseDao.queryForId((int) databaseId);
	}

	@Override
	public byte[] getItemAsByteArray(long databaseId, long itemId)
	{
		try
		{
			// Should have included a querybuilder instead, joined with the databaseID
			// mediaItemDao.queryBuilder().where().eq("database_id", databaseId).query();
			MediaItem song = mediaItemDao.queryForId(new Integer((int) itemId));
			return DmapUtil.uriTobuffer(reader.getTune(itemToIMusicItem.get(song)));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Listing getContainers(long databaseId, Iterable<String> parameters) throws SQLException
	{
		final ImmutableCollection<DmapProtocolDefinition> queryParameters = parameters2Definition(parameters, Container.class);
		final List<Container> containers = containerDao.queryBuilder().where().eq("database_id", databaseId).query();
		return generateListing(queryParameters, containers);
	}

	private <T> ListingItem transformEntity(ImmutableCollection<DmapProtocolDefinition> queryParameters, T input)
	{
		final ListingItem item = new ListingItem();
		for(DmapProtocolDefinition param : queryParameters)
		{
			// build query ...
			// and parse it
			final Class<? extends AbstractChunk> chunkClass = ChunkFactory.getCalculatedmap().get(DmapUtil.toContentCodeNumber(param.getShortname()), param.getLongname());
			try
			{
				final AbstractChunk chunk = chunkClass.newInstance();
				Field field = definitionToFieldsMap.get(input.getClass()).get(param);
				if(field != null)
				{
					field.setAccessible(true);
					Object value = field.get(input);
					chunk.setObjectValue(value);
					item.add(chunk);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return item;
	}

	@Override
	public Listing getMediaItems(long databaseId, long containerId, Iterable<String> parameters) throws SQLException
	{
		final ImmutableCollection<DmapProtocolDefinition> queryParameters = parameters2Definition(parameters, MediaItem.class);
		// final List<MediaItem> mediaItems = mediaItemDao.queryBuilder().where().eq("database_id", databaseId).and().eq("container_id", containerId).query();
		final List<MediaItem> mediaItems = mediaItemDao.queryBuilder().where().eq("database_id", databaseId).query();
		return generateListing(queryParameters, mediaItems);
	}

	private <T> Listing generateListing(final ImmutableCollection<DmapProtocolDefinition> queryParameters, final List<T> entities)
	{
		FluentIterable<ListingItem> listings = FluentIterable.from(entities).transform(new Function<T, ListingItem>() {

			@Override
			public ListingItem apply(T input)
			{
				return transformEntity(queryParameters, input);
			}
		});
		Listing listing = new Listing();
		for(ListingItem listingItem : listings)
		{
			listing.add(listingItem);
		}

		return listing;
	}
	@Override
	public Listing getMediaItems(long databaseId, Iterable<String> parameters) throws SQLException
	{
		return getMediaItems(databaseId, getLibrary().getDatabase(databaseId).getMasterContainer().getItemId(), parameters);
	}

	private static final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> libraryAnnotations = findDbStructureFields(Library.class);
	private static final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> databaseAnnotations = findDbStructureFields(Database.class);
	private static final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> containerAnnotations = findDbStructureFields(Container.class);
	private static final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> mediaItemAnnotations = findDbStructureFields(MediaItem.class);

	private static final ImmutableMap<Class<?>, ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition>> definitionsTable = new ImmutableMap.Builder<Class<?>, ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition>>().put(Library.class, libraryAnnotations).put(Database.class, databaseAnnotations).put(Container.class, containerAnnotations).put(MediaItem.class, mediaItemAnnotations).build();

	private static ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> parameters2Definition(final Iterable<String> parameters, Class<?> clazz)
	{

		if(Iterables.size(parameters) == 1 && "all".equals(parameters.iterator().next()))
		{
			return definitionsTable.get(clazz);
		}
		else
		{
			final ArrayList<DmapProtocolDefinition> collection = Lists.newArrayList(DmapProtocolDefinition.values());
			return new ImmutableList.Builder<IDmapProtocolDefinition.DmapProtocolDefinition>().addAll(FluentIterable.from(parameters).transform(new Function<String, IDmapProtocolDefinition.DmapProtocolDefinition>() {

				@Override
				public DmapProtocolDefinition apply(final String inputName)
				{
					return Iterables.find(collection, new Predicate<DmapProtocolDefinition>() {

						@Override
						public boolean apply(DmapProtocolDefinition input)
						{
							return input.getLongname().equals(inputName);
						}
					});
				}
			}).filter(Predicates.notNull())).build();
		}
	}

	private static ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> findDbStructureFields(Class<?> clazz)
	{
		final ArrayList<DmapProtocolDefinition> collection = Lists.newArrayList(DmapProtocolDefinition.values());

		return new ImmutableList.Builder<IDmapProtocolDefinition.DmapProtocolDefinition>().addAll(FluentIterable.from(Lists.newArrayList(clazz.getDeclaredFields())).transform(new Function<Field, Annotation>() {

			@Override
			public Annotation apply(Field input)
			{
				return input.getAnnotation(DatabaseField.class);
			}
		}).filter(Predicates.notNull()).transform(new Function<Annotation, IDmapProtocolDefinition.DmapProtocolDefinition>() {

			@Override
			public DmapProtocolDefinition apply(final Annotation input)
			{
				final DatabaseField correctAnnotation = (DatabaseField) input;
				Optional<DmapProtocolDefinition> result = Iterables.tryFind(collection, new Predicate<DmapProtocolDefinition>() {

					@Override
					public boolean apply(DmapProtocolDefinition input)
					{
						return input.getLongname().equals(correctAnnotation.columnName());
					}
				});
				return result.orNull();
			}
		}).filter(Predicates.notNull())).build();
	}

	private static final ImmutableMap<Class<?>, Map<DmapProtocolDefinition, Field>> definitionToFieldsMap;

	static
	{
		Map<Class<?>, Map<DmapProtocolDefinition, Field>> _definitionToFieldsMap = Maps.newHashMap();

		_definitionToFieldsMap.put(MediaItem.class, definitionToField(MediaItem.class, mediaItemAnnotations));
		_definitionToFieldsMap.put(Container.class, definitionToField(Container.class, containerAnnotations));

		definitionToFieldsMap = new ImmutableMap.Builder<Class<?>, Map<DmapProtocolDefinition, Field>>().putAll(_definitionToFieldsMap).build();
	}

	private static Map<DmapProtocolDefinition, Field> definitionToField(Class<?> clazz, ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> definitions)
	{
		HashMap<DmapProtocolDefinition, Field> map = Maps.newHashMap();
		for(DmapProtocolDefinition def : definitions)
		{
			for(Field f : clazz.getDeclaredFields())
			{
				DatabaseField dbAnnotation = f.getAnnotation(DatabaseField.class);
				if(dbAnnotation != null)
				{
					if(def.getLongname().equals(dbAnnotation.columnName()))
					{
						map.put(def, f);
					}
				}
			}
		}
		return map;
	}
}
