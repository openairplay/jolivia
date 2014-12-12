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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;

import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.ChunkFactory;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.jetty.JoliviaExceptionMapper;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.raop.server.IPlayingInformation;
import org.dyndns.jkiddo.raop.server.RAOPResourceWrapper;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.PairedRemoteDiscoverer;
import org.dyndns.jkiddo.service.daap.client.UnpairedRemoteCrawler;
import org.dyndns.jkiddo.service.daap.server.DAAPResource;
import org.dyndns.jkiddo.service.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.service.daap.server.InMemoryMusicManager;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.client.PairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.TouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.service.dacp.server.TouchAbleServerResource;
import org.dyndns.jkiddo.service.dmap.CustomByteArrayProvider;
import org.dyndns.jkiddo.service.dmap.DMAPInterface;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.service.dpap.server.DPAPResource;
import org.dyndns.jkiddo.service.dpap.server.IImageLibrary;
import org.dyndns.jkiddo.service.dpap.server.ImageItemManager;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.dyndns.jkiddo.zeroconf.ZeroconfManagerImpl;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaServer extends GuiceServletContextListener
{
	static final Logger logger = LoggerFactory.getLogger(JoliviaServer.class);

	final private String DB_NAME = "db";
	final private Integer hostingPort;
	final private Integer pairingCode;
	final private Integer airplayPort;
	final private String name;
	final private IClientSessionListener clientSessionListener;
	// final private ISpeakerListener speakerListener;
	final private IImageStoreReader imageStoreReader;
	final private IMusicStoreReader musicStoreReader;
	final private IPlayingInformation iplayingInformation;
	final private PasswordMethod passwordMethod;

	private Injector injector;

	private final Server h2server;

	public JoliviaServer(final Integer port, final Integer airplayPort, final Integer pairingCode, final String name, final IClientSessionListener clientSessionListener, final ISpeakerListener speakerListener, final IImageStoreReader imageStoreReader, final IMusicStoreReader musicStoreReader, final IPlayingInformation iplayingInformation, final PasswordMethod security) throws SQLException, UnknownHostException
	{
		super();
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.net.preferIPv6Addresses", "false");
		h2server = Server.createWebServer(new String[] { "-webPort", "9123", "-webAllowOthers" });
		h2server.start();
		logger.info("h2 web server started on port http://" + InetAddress.getLocalHost().getHostName() + ":9123/");
		logger.info("log in with empty username and password");
		logger.info("jdbc:h2:mem:test");

		this.hostingPort = port;
		this.pairingCode = pairingCode;
		this.airplayPort = airplayPort;
		this.name = name;
		this.passwordMethod = PasswordMethod.valueOf(security.toString());

		this.clientSessionListener = clientSessionListener;
		this.musicStoreReader = musicStoreReader;
		this.imageStoreReader = imageStoreReader;
		this.iplayingInformation = iplayingInformation;
	}

	private ImmutableSet<MDNSResource> getMDNSResources()
	{
		return ImmutableSet.<MDNSResource> builder().add((MDNSResource) injector.getInstance(IImageLibrary.class)).add((MDNSResource) injector.getInstance(IMusicLibrary.class)).add((MDNSResource) injector.getInstance(ITouchRemoteResource.class)).add((MDNSResource) injector.getInstance(ITouchAbleServerResource.class)).add((injector.getInstance(RAOPResourceWrapper.class))).build();
	}
	public void reRegister()
	{
		try
		{
			for(final MDNSResource r : getMDNSResources())
			{
				r.register();
			}
		}
		catch(final IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected Injector getInjector()
	{
		this.injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(IZeroconfManager.class).toInstance(new ZeroconfManagerImpl());
				bind(JoliviaExceptionMapper.class);
				bind(DMAPInterface.class).asEagerSingleton();
				bind(String.class).annotatedWith(Names.named(Util.APPLICATION_NAME)).toInstance(name);
			}

		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(DPAPResource.DPAP_SERVER_PORT_NAME)).toInstance(hostingPort);
				bind(IImageLibrary.class).to(DPAPResource.class).asEagerSingleton();
				bind(ImageItemManager.class).annotatedWith(Names.named(DPAPResource.DPAP_RESOURCE)).to(ImageItemManager.class);
				bind(IImageStoreReader.class).toInstance(imageStoreReader);
			}
		}, new AbstractModule() {

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

				bind(new TypeLiteral<Table<Integer, String, Class<? extends AbstractChunk>>>() {}).toInstance(ChunkFactory.getCalculatedMap());
				// bind(IItemManager.class).annotatedWith(Names.named(DAAPResource.DAAP_RESOURCE)).to(MusicItemManager.class);
				bind(IItemManager.class).annotatedWith(Names.named(DAAPResource.DAAP_RESOURCE)).to(InMemoryMusicManager.class);
				bind(IMusicStoreReader.class).toInstance(musicStoreReader);
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(TouchRemoteResource.DACP_CLIENT_PAIRING_CODE)).toInstance(pairingCode);
				bind(Integer.class).annotatedWith(Names.named(TouchRemoteResource.DACP_CLIENT_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(TouchAbleServerResource.DACP_SERVER_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(UnpairedRemoteCrawler.SERVICE_PORT_NAME)).toInstance(hostingPort);

				bind(String.class).annotatedWith(Names.named(PairingDatabase.NAME_OF_DB)).toInstance(DB_NAME);
				bind(IPairingDatabase.class).to(PairingDatabase.class).asEagerSingleton();
				bind(PairedRemoteDiscoverer.class).asEagerSingleton();
				bind(UnpairedRemoteCrawler.class).asEagerSingleton();
				bind(ITouchRemoteResource.class).to(TouchRemoteResource.class);
				bind(ITouchAbleServerResource.class).to(TouchAbleServerResource.class);
				bind(IClientSessionListener.class).toInstance(clientSessionListener);
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				// bind(RemoteSpeakerDiscoverer.class).asEagerSingleton();
				// bind(ISpeakerListener.class).toInstance(speakerListener);
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(IPlayingInformation.class).toInstance(iplayingInformation);
				bind(Integer.class).annotatedWith(Names.named(RAOPResourceWrapper.RAOP_PORT_NAME)).toInstance(airplayPort);
				bind(RAOPResourceWrapper.class).asEagerSingleton();
			}
		}

		, new JerseyServletModule() {

			@Override
			protected void configureServlets()
			{
				bind(CustomByteArrayProvider.class);
				filter("*").through(ProxyFilter.class);
				serve("/*").with(GuiceContainer.class);
			}
		});
		return injector;
	}
	@Override
	public void contextDestroyed(final ServletContextEvent servletContextEvent)
	{
		h2server.stop();
		for(final MDNSResource r : getMDNSResources())
		{
			r.deRegister();
		}
		injector.getInstance(IZeroconfManager.class).unregisterAllServices();
		try
		{
			injector.getInstance(IZeroconfManager.class).close();
		}
		catch(final IOException e)
		{
			logger.error(e.getMessage(), e);
		}
		super.contextDestroyed(servletContextEvent);
	}
}
