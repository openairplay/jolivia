package org.dyndns.jkiddo.guice;

import java.sql.SQLException;

import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.service.daap.server.DAAPResource;
import org.dyndns.jkiddo.service.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.service.daap.server.InMemoryMusicManager;
import org.dyndns.jkiddo.service.dmap.IItemManager;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DAAPModule extends AbstractModule
{
	private final PasswordMethod passwordMethod;
	private final IMusicStoreReader musicStoreReader;
	private final int hostingPort;

	public DAAPModule(final PasswordMethod passwordMethod, final IMusicStoreReader musicStoreReader, final int hostingPort)
	{
		this.passwordMethod = passwordMethod;
		this.musicStoreReader = musicStoreReader;
		this.hostingPort = hostingPort;
	}
	
	@Override
	protected void configure()
	{
		bind(Integer.class).annotatedWith(Names.named(DAAPResource.DAAP_PORT_NAME)).toInstance(hostingPort);
		bind(IMusicLibrary.class).to(DAAPResource.class).asEagerSingleton();
		bind(PasswordMethod.class).toInstance(passwordMethod);
		try
		{
			bind(ConnectionSource.class).toInstance(new JdbcConnectionSource("jdbc:h2:mem:test"));
		}
		catch(final SQLException e)
		{
			throw new RuntimeException(e);
		}

		// bind(new TypeLiteral<Table<String, String, Class<? extends AbstractChunk>>>() {}).toInstance(ChunkFactory.getCalculatedMap());
		// bind(IItemManager.class).annotatedWith(Names.named(DAAPResource.DAAP_RESOURCE)).to(MusicItemManager.class);
		bind(IItemManager.class).annotatedWith(Names.named(DAAPResource.DAAP_RESOURCE)).to(InMemoryMusicManager.class);
		bind(IMusicStoreReader.class).toInstance(musicStoreReader);
	}

}
