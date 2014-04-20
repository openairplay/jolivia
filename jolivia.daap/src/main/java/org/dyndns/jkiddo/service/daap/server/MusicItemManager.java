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

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.ContainerCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmp.model.Container;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.dmp.model.Library;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class MusicItemManager implements IItemManager
{
	private static final Logger logger = LoggerFactory.getLogger(MusicItemManager.class);

	private final IMusicStoreReader reader;
	// private final Map<MediaItem, IMusicItem> itemToIMusicItem;

	private final PasswordMethod passwordMethod;

	private final Dao<Library, Integer> libraryDao;
	private final Dao<Database, Integer> databaseDao;
	private final Dao<Container, Integer> containerDao;
	private final Dao<MediaItem, Integer> mediaItemDao;
	private final ConnectionSource connectionSource;

	private final Table<Integer, String, Class<? extends AbstractChunk>> annotatedtable;

	@Inject
	public MusicItemManager(@Named(Util.APPLICATION_NAME) String applicationName, IMusicStoreReader reader, PasswordMethod pm, ConnectionSource connectionSource, Table<Integer, String, Class<? extends AbstractChunk>> annotatedtable) throws Exception
	{
		this.connectionSource = connectionSource;

		clearAll();

		databaseDao = DaoManager.createDao(this.connectionSource, Database.class);
		containerDao = DaoManager.createDao(this.connectionSource, Container.class);
		mediaItemDao = DaoManager.createDao(this.connectionSource, MediaItem.class);
		libraryDao = DaoManager.createDao(this.connectionSource, Library.class);

		TableUtils.createTableIfNotExists(this.connectionSource, Library.class);
		TableUtils.createTableIfNotExists(this.connectionSource, Database.class);
		TableUtils.createTableIfNotExists(this.connectionSource, Container.class);
		TableUtils.createTableIfNotExists(this.connectionSource, MediaItem.class);

		final Library library = new Library(applicationName);
		final Database database = new Database(applicationName, "music", library);
		final Container container = new Container("MasterPlaylist", -1, -1, database, 1);

		libraryDao.createIfNotExists(library);
		databaseDao.createIfNotExists(database);
		containerDao.createIfNotExists(container);
		databaseDao.refresh(database);

		this.annotatedtable = annotatedtable;
		this.passwordMethod = pm;
		this.reader = reader;
		/*
		 * this.itemToIMusicItem = Maps.uniqueIndex(reader.readTunes(), new Function<IMusicItem, MediaItem>() {
		 * @Override public MediaItem apply(IMusicItem iMusicItem) { MediaItem item = new MediaItem(database); item.setExternalIdentifer(externalIdentifer); item.setItemKind(new ItemKind(ItemKind.AUDIO).getValue()); item.setSongAlbum(new SongAlbum(iMusicItem.getAlbum()).getValue()); item.setSongArtist(new SongArtist(iMusicItem.getArtist()).getValue()); if(Strings.isNullOrEmpty(iMusicItem.getTitle())) { logger.warn("Name of " + iMusicItem + " was null. Song/Item may not be displayed"); } item.setItemName(new ItemName(iMusicItem.getTitle()).getValue()); item.setMediaFormat(new SongFormat(SongFormat.MP3).getValue()); item.setSongSampleRate(new SongSampleRate(SongSampleRate.KHZ_44100).getValue()); item.setSongTime(new SongTime(iMusicItem.getDuration()).getValue()); // if musicItem has album
		 * art remember the following: // // new SongExtraData(1); // new ArtworkChecksum(); 4 bytes // new SongArtworkCount(1); return item; } });
		 */

		Map<Class<?>, Map<DmapProtocolDefinition, Field>> _definitionToFieldsMap = Maps.newHashMap();

		_definitionToFieldsMap.put(MediaItem.class, definitionToField(MediaItem.class, mediaItemAnnotations));
		_definitionToFieldsMap.put(Container.class, definitionToField(Container.class, containerAnnotations));

		definitionToFieldsMap = new ImmutableMap.Builder<Class<?>, Map<DmapProtocolDefinition, Field>>().putAll(_definitionToFieldsMap).build();

		// for(MediaItem item : itemToIMusicItem.keySet())
		for(MediaItem item : reader.readTunes())
		{
			item.setDatabase(database);
			item.setItemKind(ItemKind.AUDIO);
			mediaItemDao.createIfNotExists(item);
		}

	}
	private void clearAll() throws SQLException
	{
		TableUtils.dropTable(this.connectionSource, Library.class, true);
		TableUtils.dropTable(this.connectionSource, Database.class, true);
		TableUtils.dropTable(this.connectionSource, Container.class, true);
		TableUtils.dropTable(this.connectionSource, MediaItem.class, true);

	}
	@Override
	public PasswordMethod getAuthenticationMethod()
	{
		return passwordMethod;
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
	/*
	 * @Override public Database getDatabase(long databaseId) throws SQLException { return databaseDao.queryForId((int) databaseId); }
	 */

	@Override
	public byte[] getItemAsByteArray(long databaseId, long itemId)
	{
		try
		{
			MediaItem song = mediaItemDao.queryBuilder().where().eq("database_id", databaseId).and().idEq((int) itemId).queryForFirst();
			return DmapUtil.uriTobuffer(reader.getTune(song.getExternalIdentifer()));
			// return DmapUtil.uriTobuffer(reader.getTune(itemToIMusicItem.get(song)));
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
			final Class<? extends AbstractChunk> chunkClass = annotatedtable.get(DmapUtil.toContentCodeNumber(param.getShortname()), param.getLongname());
			try
			{
				final AbstractChunk chunk = chunkClass.newInstance();
				Field field = definitionToFieldsMap.get(input.getClass()).get(param);
				if(field != null)
				{
					field.setAccessible(true);
					Object value = field.get(input);

					// The following 'if-else' statements handles references
					if(Database.class.isInstance(value))
					{
						// chunk.setObjectValue(((Database) value).getItemId());
					}
					if(Container.class.isInstance(value))
					{
						// chunk.setObjectValue(((Container) value).getItemId());
					}
					else
					{
						chunk.setObjectValue(value);
					}
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
		final List<MediaItem> mediaItems = mediaItemDao.queryBuilder().where().eq("database_id", databaseId).and().eq("container_id", containerId).query();
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

	private final ImmutableMap<Class<?>, Map<DmapProtocolDefinition, Field>> definitionToFieldsMap;

	private final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> libraryAnnotations = findDbStructureFields(Library.class);
	private final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> databaseAnnotations = findDbStructureFields(Database.class);
	private final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> containerAnnotations = findDbStructureFields(Container.class);
	private final ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> mediaItemAnnotations = findDbStructureFields(MediaItem.class);

	private final ImmutableMap<Class<?>, ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition>> definitionsTable = new ImmutableMap.Builder<Class<?>, ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition>>().put(Library.class, libraryAnnotations).put(Database.class, databaseAnnotations).put(Container.class, containerAnnotations).put(MediaItem.class, mediaItemAnnotations).build();

	private ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> parameters2Definition(final Iterable<String> parameters, Class<?> clazz)
	{

		if(Iterables.size(parameters) == 1 && "all".equals(parameters.iterator().next()))
		{
			return definitionsTable.get(clazz);
		}
		else
		{
			final ImmutableCollection<DmapProtocolDefinition> collection = definitionsTable.get(clazz);
			return new ImmutableList.Builder<IDmapProtocolDefinition.DmapProtocolDefinition>().addAll(FluentIterable.from(parameters).transform(new Function<String, IDmapProtocolDefinition.DmapProtocolDefinition>() {

				@Override
				public DmapProtocolDefinition apply(final String inputName)
				{
					return Iterables.tryFind(collection, new Predicate<DmapProtocolDefinition>() {

						@Override
						public boolean apply(DmapProtocolDefinition input)
						{
							return input.getLongname().equals(inputName);
						}
					}).orNull();
				}
			}).filter(Predicates.notNull())).build();
		}
	}

	private ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> findDbStructureFields(Class<?> clazz)
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

	private Map<DmapProtocolDefinition, Field> definitionToField(Class<?> clazz, ImmutableCollection<IDmapProtocolDefinition.DmapProtocolDefinition> definitions)
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
