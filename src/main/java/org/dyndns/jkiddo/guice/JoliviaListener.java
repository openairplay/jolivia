/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
package org.dyndns.jkiddo.guice;

import java.io.File;
import java.util.Collection;

import javax.jmdns.JmmDNS;

import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.dmap.chunks.VersionChunk;
import org.dyndns.jkiddo.dmap.chunks.dmap.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.jetty.JoliviaExceptionMapper;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.client.ISpeakerListener;
import org.dyndns.jkiddo.raop.client.RemoteSpeakerDiscoverer;
import org.dyndns.jkiddo.raop.client.model.Device;
import org.dyndns.jkiddo.raop.server.AirPlayResourceWrapper;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.PairedRemoteDiscoverer;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.dyndns.jkiddo.service.daap.server.DAAPResource;
import org.dyndns.jkiddo.service.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.service.daap.server.MusicItemManager;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.IPairingResource;
import org.dyndns.jkiddo.service.dacp.client.PairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.PairingResource;
import org.dyndns.jkiddo.service.dacp.server.IRemoteControlResource;
import org.dyndns.jkiddo.service.dacp.server.RemoteControlResource;
import org.dyndns.jkiddo.service.dmap.DMAPInterface;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dpap.server.DPAPResource;
import org.dyndns.jkiddo.service.dpap.server.IImageLibrary;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaListener extends GuiceServletContextListener
{
	final private Integer hostingPort;
	final private Integer pairingCode;
	final private Integer airplayPort;
	final private String name;
	final private IClientSessionListener clientSessionListener;
	final private ISpeakerListener speakerListener;

	public static final String APPLICATION_NAME = "APPLICATION_NAME";

	public JoliviaListener(Integer port, Integer airplayPort, Integer pairingCode, String name, IClientSessionListener clientSessionListener, ISpeakerListener speakerListener)
	{
		super();
		this.hostingPort = port;
		this.pairingCode = pairingCode;
		this.airplayPort = airplayPort;
		this.name = name;
		if(clientSessionListener == null)
		{
			this.clientSessionListener = new IClientSessionListener() {

				@Override
				public void tearDownSession(String server, int port)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void registerNewSession(Session session) throws Exception
				{
					// TODO Auto-generated method stub

				}
			};
		}
		else
		{
			this.clientSessionListener = clientSessionListener;
		}
		if(speakerListener == null)
		{
			this.speakerListener = new ISpeakerListener() {

				@Override
				public void removeAvailableSpeaker(String server, int port)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void registerAvailableSpeaker(Device device)
				{
					// TODO Auto-generated method stub

				}
			};
		}
		else
		{
			this.speakerListener = speakerListener;
		}
	}

	@Override
	protected Injector getInjector()
	{
		return Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(JmmDNS.class).toInstance(JmmDNS.Factory.getInstance());
				bind(JoliviaExceptionMapper.class);
				bind(DMAPInterface.class).asEagerSingleton();
				bind(String.class).annotatedWith(Names.named(APPLICATION_NAME)).toInstance(name);
			}

		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(DPAPResource.DPAP_SERVER_PORT_NAME)).toInstance(hostingPort);
				bind(IImageLibrary.class).to(DPAPResource.class).asEagerSingleton();
				bind(IItemManager.class).annotatedWith(Names.named(DPAPResource.DPAP_RESOURCE)).toInstance(new IItemManager() {

					@Override
					public void waitForUpdate()
					{
						// TODO Auto-generated method stub

					}

					@Override
					public long getSessionId(String remoteHost)
					{
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public long getRevision(String remoteHost, long sessionId)
					{
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public File getItemAsFile(long databaseId, long itemId)
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public VersionChunk getDpapProtocolVersion()
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public VersionChunk getDmapProtocolVersion()
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Collection<Database> getDatabases()
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Database getDatabase(long databaseId)
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public VersionChunk getDaapProtocolVersion()
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public String getDMAPKey()
					{
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public PasswordMethod getAuthenticationMethod()
					{
						// TODO Auto-generated method stub
						return null;
					}
				});
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(DAAPResource.DAAP_PORT_NAME)).toInstance(hostingPort);
				// bind(MusicLibraryManager.class);
				// bind(IMusicLibrary.class).to(MusicLibraryResource.class).asEagerSingleton();
				bind(IMusicLibrary.class).to(DAAPResource.class).asEagerSingleton();
				bind(IItemManager.class).annotatedWith(Names.named(DAAPResource.DAAP_RESOURCE)).to(MusicItemManager.class);
				bind(IMusicStoreReader.class).to(DeskMusicStoreReader.class).asEagerSingleton();
				// Multibinder<IMusicStoreReader> multibinder = Multibinder.newSetBinder(binder(), IMusicStoreReader.class);
				// multibinder.addBinding().to(DeskMusicStoreReader.class).asEagerSingleton();

			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(PairingResource.DACP_CLIENT_PAIRING_CODE)).toInstance(pairingCode);
				bind(Integer.class).annotatedWith(Names.named(PairingResource.DACP_CLIENT_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(RemoteControlResource.DACP_SERVER_PORT_NAME)).toInstance(hostingPort);

				bind(String.class).annotatedWith(Names.named(PairingDatabase.DB_URL)).toInstance(new String("jdbc:sqlite:db"));
				bind(IPairingDatabase.class).to(PairingDatabase.class).asEagerSingleton();
				bind(PairedRemoteDiscoverer.class).asEagerSingleton();
				bind(IPairingResource.class).to(PairingResource.class);
				bind(IRemoteControlResource.class).to(RemoteControlResource.class);
				bind(IClientSessionListener.class).toInstance(clientSessionListener);
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(RemoteSpeakerDiscoverer.class).asEagerSingleton();
				bind(ISpeakerListener.class).toInstance(speakerListener);
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(AirPlayResourceWrapper.RAOP_PORT_NAME)).toInstance(airplayPort);
				bind(AirPlayResourceWrapper.class).asEagerSingleton();
			}
		}

		, new JerseyServletModule() {

			@Override
			protected void configureServlets()
			{
				filter("*").through(ProxyFilter.class);
				serve("/*").with(GuiceContainer.class);
			}
		});
	}
}
