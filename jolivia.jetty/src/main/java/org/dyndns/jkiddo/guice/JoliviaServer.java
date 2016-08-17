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

import javax.servlet.ServletContextEvent;

import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.jetty.JoliviaExceptionMapper;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.raop.server.IPlayingInformation;
import org.dyndns.jkiddo.raop.server.RAOPResourceWrapper;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.server.HomeSharingResource;
import org.dyndns.jkiddo.service.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.service.dmap.CustomByteArrayProvider;
import org.dyndns.jkiddo.service.dmap.DMAPInterface;
import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.service.dpap.server.IImageLibrary;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.dyndns.jkiddo.zeroconf.ZeroconfManagerImpl;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaServer extends GuiceServletContextListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger(JoliviaServer.class);

	private final String DB_NAME = "db";
	private final Integer hostingPort;
	private final Integer pairingCode;
	private final Integer airplayPort;
	private final String name;
	private final IClientSessionListener clientSessionListener;
	// final private ISpeakerListener speakerListener;
	private final IImageStoreReader imageStoreReader;
	private final IMusicStoreReader musicStoreReader;
	private final IPlayingInformation iplayingInformation;
	private final PasswordMethod passwordMethod;
	private final String databaseUrl = "jdbc:h2:mem:test";

	private Injector injector;

	private final Server h2server;

	private final ZeroconfManagerImpl iZeroconfManager;

	private final HomeSharingResource homeSharing;

	public JoliviaServer(final Integer port, final Integer airplayPort, final Integer pairingCode, final String name, final IClientSessionListener clientSessionListener, final ISpeakerListener speakerListener, final IImageStoreReader imageStoreReader, final IMusicStoreReader musicStoreReader, final IPlayingInformation iplayingInformation, final PasswordMethod security, final String appleUsername, final String applePassword) throws Exception
	{
		super();
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.net.preferIPv6Addresses", "false");
		h2server = Server.createWebServer(new String[] { "-webPort", "9123", "-webAllowOthers" });
		h2server.start();
		LOGGER.info("h2 web server started on port http://" + InetAddress.getLocalHost().getHostName() + ":9123/");
		LOGGER.info("log in with empty username and password");
		LOGGER.info(databaseUrl);

		this.hostingPort = port;
		this.pairingCode = pairingCode;
		this.airplayPort = airplayPort;
		this.name = name;
		this.passwordMethod = PasswordMethod.valueOf(security.toString());

		this.clientSessionListener = clientSessionListener;
		this.musicStoreReader = musicStoreReader;
		this.imageStoreReader = imageStoreReader;
		this.iplayingInformation = iplayingInformation;
		this.iZeroconfManager = new ZeroconfManagerImpl();
		if(!(Strings.isNullOrEmpty(appleUsername) && Strings.isNullOrEmpty(applePassword)))
			this.homeSharing = new HomeSharingResource(iZeroconfManager, this.hostingPort, this.name, appleUsername, applePassword);
		else
			this.homeSharing = null;
		
	}

	private ImmutableSet<MDNSResource> getMDNSResources()
	{
		final Builder<MDNSResource> set = ImmutableSet.<MDNSResource> builder().add((MDNSResource) injector.getInstance(IImageLibrary.class)).add((MDNSResource) injector.getInstance(IMusicLibrary.class)).add((MDNSResource) injector.getInstance(ITouchRemoteResource.class)).add((MDNSResource) injector.getInstance(ITouchAbleServerResource.class)).add(injector.getInstance(RAOPResourceWrapper.class));
		if(homeSharing != null)
			return set.add(homeSharing).build();
		else
			return set.build();
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
				bind(IZeroconfManager.class).toInstance(iZeroconfManager);
				bind(JoliviaExceptionMapper.class);
				bind(DMAPInterface.class).asEagerSingleton();
				bind(String.class).annotatedWith(Names.named(Util.APPLICATION_NAME)).toInstance(name);
			}

		},
		new DPAPModule(hostingPort, imageStoreReader), 
		new DAAPModule(passwordMethod, musicStoreReader, hostingPort),
		new DACPModule(pairingCode, hostingPort, DB_NAME, clientSessionListener), 
		new AbstractModule() {

			@Override
			protected void configure()
			{
				// bind(RemoteSpeakerDiscoverer.class).asEagerSingleton();
				// bind(ISpeakerListener.class).toInstance(speakerListener);
			}
		}, 
		new RAOPModule(iplayingInformation, airplayPort), 
		new JerseyServletModule() {

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
			LOGGER.error(e.getMessage(), e);
		}
		super.contextDestroyed(servletContextEvent);
	}
}
