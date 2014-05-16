package test.db;

import java.sql.SQLException;
import java.util.UUID;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.ChunkFactory;
import org.dyndns.jkiddo.dmp.model.Container;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.dmp.model.Library;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Table;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class TestDB
{
	public static final String someST = "ldksjfldskjfl";

	// http://cleancodedevelopment-qualityseal.blogspot.dk/2013/06/how-to-use-ormlite-save-complex-objects.html
	private Dao<Library, Integer> libraryDao;
	private Dao<Database, Integer> databaseDao;
	private Dao<Container, Integer> containerDao;
	private Dao<MediaItem, Integer> mediaItemDao;
	private JdbcConnectionSource connectionSource;

	@Before
	public void before() throws SQLException
	{
		String databaseUrl = "jdbc:h2:mem:test";
		connectionSource = new JdbcConnectionSource(databaseUrl);

		databaseDao = DaoManager.createDao(connectionSource, Database.class);
		containerDao = DaoManager.createDao(connectionSource, Container.class);
		mediaItemDao = DaoManager.createDao(connectionSource, MediaItem.class);
		libraryDao = DaoManager.createDao(connectionSource, Library.class);

		TableUtils.createTable(connectionSource, Library.class);
		TableUtils.createTable(connectionSource, Database.class);
		TableUtils.createTable(connectionSource, Container.class);
		TableUtils.createTable(connectionSource, MediaItem.class);
	}

	@After
	public void after() throws SQLException
	{
		connectionSource.close();
	}

	//@Test
	public void libraryTest() throws SQLException
	{
		Server h2server = Server.createWebServer(new String[] { "-webPort", "9123", "-webAllowOthers" });
		h2server.start();

		String applicationName = "Tommy";
		Library library = new Library(applicationName);
		Database database = new Database("Yadee", "music", library);
		Container container = new Container("MasterPlaylist", -1, -1, database, 1);
		
		libraryDao.createIfNotExists(library);
		databaseDao.createIfNotExists(database);
		containerDao.createIfNotExists(container);
		
		libraryDao.refresh(library);
		databaseDao.refresh(database);
		
		MediaItem mi = new MediaItem(database);
		container.addMediaItem(mi);
		mediaItemDao.createOrUpdate(mi);
		//Container csc = libraryDao.queryForAll().iterator().next().getDatabases().iterator().next().getMasterContainer();
		System.out.println();
	}
	
	//@Test
	public void testMap()
	{
		Table<Integer, String, Class<? extends AbstractChunk>> table = ChunkFactory.getCalculatedmap();
		DmapProtocolDefinition def = IDmapProtocolDefinition.DmapProtocolDefinition.abcp;
		Class<? extends AbstractChunk> clazz = table.get(DmapUtil.toContentCodeNumber(def.getShortname()), def.getLongname());
		System.out.println(clazz);
	}

	protected String randomString()
	{
		return UUID.randomUUID().toString();
	}

	@Test
	public void testAddingMediaItems() throws SQLException
	{/*
	 * ChunkFactory fac = new ChunkFactory(); Set<Class<?>> dskjfhkjsh = new Reflections("test").getTypesAnnotatedWith(DMAPAnnotation.class); new ItemName(); String name = randomString(); Library library = new Library(name); Database database = new Database(library); MediaItem mediaItem = new MediaItem(); database.setMasterPlaylist(new Container()); // persist the account object to the database libraryDao.create(library); databaseDao.create(database); mediaItemDao.create(mediaItem); libraryDao.refresh(library); databaseDao.refresh(database); Database db = databaseDao.queryForAll().iterator().next(); db.addMediaItem(mediaItem); mediaItemDao.queryBuilder().selectColumns("").query(); Collection<MediaItem> items = library.getDatabase(1).getMasterPlaylist().getMediaItems(); String s
	 * ="dmap.itemid,dmap.itemname,dmap.itemkind,dmap.persistentid,daap.songalbum";
	 */
	}
}
