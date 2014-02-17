package test.db;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class TestDB
{
	private Dao<Library, Integer> libraryDao;
	private Dao<Database, Integer> databaseDao;
	private JdbcConnectionSource connectionSource;

	@Before
	public void before() throws SQLException
	{
		String databaseUrl = "jdbc:h2:mem:account";
		connectionSource = new JdbcConnectionSource(databaseUrl);

		libraryDao = DaoManager.createDao(connectionSource, Library.class);
		databaseDao = DaoManager.createDao(connectionSource, Database.class);

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
		String name = randomString();
		Library library = new Library(name);
		Database database = new Database(library);
		
		// persist the account object to the database
		libraryDao.create(library);
		databaseDao.create(database);
		
	}
}
