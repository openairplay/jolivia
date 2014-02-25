package test.db;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.dyndns.jkiddo.dmp.chunks.ChunkFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class TestDB
{
	public static final String someST = "ldksjfldskjfl";
	
	//http://cleancodedevelopment-qualityseal.blogspot.dk/2013/06/how-to-use-ormlite-save-complex-objects.html
	private Dao<Library, Integer> libraryDao;
	private Dao<Database, Integer> databaseDao;
	private Dao<Container, Integer> containerDao;
	private Dao<MediaItem, Integer> mediaItemDao;
	private JdbcConnectionSource connectionSource;

	@Before
	public void before() throws SQLException
	{
		String databaseUrl = "jdbc:h2:mem:account";
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
	
	@Test
	public void libraryTest() throws SQLException
	{
		String name = randomString();
		Library library = new Library(name);
		Database database = new Database(library);
		
		// persist the account object to the database
		libraryDao.create(library);
		databaseDao.create(database);

		Library library2 = libraryDao.queryForEq("name", name).get(0);
		Collection<Database> someDatabase = library2.getDatabases();
		Library library3 = someDatabase.iterator().next().getLibrary();
		assertEquals(library.getName(), library2.getName());
		assertEquals(library2.getName(), library3.getName());
	}

	protected String randomString()
	{
		return UUID.randomUUID().toString();
	}
	
	@Test
	public void testAddingMediaItems() throws SQLException
	{
		ChunkFactory fac = new ChunkFactory();
		
		Set<Class<?>> dskjfhkjsh = new Reflections("test").getTypesAnnotatedWith(DMAPAnnotation.class);
		new ItemName();
		String name = randomString();
		Library library = new Library(name);
		Database database = new Database(library);
		MediaItem mediaItem = new MediaItem();
		database.setMasterPlaylist(new Container());
		// persist the account object to the database
		libraryDao.create(library);
		databaseDao.create(database);
		mediaItemDao.create(mediaItem);
		libraryDao.refresh(library);
		databaseDao.refresh(database);
		Database db = databaseDao.queryForAll().iterator().next();
		db.addMediaItem(mediaItem);
		
		
		mediaItemDao.queryBuilder().selectColumns("").query();
		Collection<MediaItem> items = library.getDatabase(1).getMasterPlaylist().getMediaItems();
		
		String s ="dmap.itemid,dmap.itemname,dmap.itemkind,dmap.persistentid,daap.songalbum";
	}
}
