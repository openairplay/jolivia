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

import javax.jmdns.JmmDNS;

import org.dyndns.jkiddo.ClientSessionListener;
import org.dyndns.jkiddo.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.daap.client.PairingDaemon;
import org.dyndns.jkiddo.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.daap.server.MusicLibraryManager;
import org.dyndns.jkiddo.daap.server.MusicLibraryResource;
import org.dyndns.jkiddo.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.dacp.client.IPairingResource;
import org.dyndns.jkiddo.dacp.client.PairingDatabase;
import org.dyndns.jkiddo.dacp.client.PairingResource;
import org.dyndns.jkiddo.dacp.server.IRemoteControlResource;
import org.dyndns.jkiddo.dacp.server.RemoteControlResource;
import org.dyndns.jkiddo.dmap.service.DMAPInterface;
import org.dyndns.jkiddo.dpap.server.IImageLibrary;
import org.dyndns.jkiddo.dpap.server.ImageResource;
import org.dyndns.jkiddo.jetty.JoliviaExceptionMapper;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaListener extends GuiceServletContextListener
{
	private int hostingPort;

	public JoliviaListener(int port)
	{
		super();
		hostingPort = port;
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
			}

		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(ImageResource.DPAP_SERVER_PORT_NAME)).toInstance(8770);
				bind(IImageLibrary.class).to(ImageResource.class).asEagerSingleton();
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(MusicLibraryResource.DAAP_PORT_NAME)).toInstance(hostingPort);
				bind(MusicLibraryManager.class);
				bind(IMusicLibrary.class).to(MusicLibraryResource.class).asEagerSingleton();

				Multibinder<IMusicStoreReader> multibinder = Multibinder.newSetBinder(binder(), IMusicStoreReader.class);
				multibinder.addBinding().to(DeskMusicStoreReader.class).asEagerSingleton();

			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(PairingResource.DACP_CLIENT_PAIRING_CODE)).toInstance(1337);
				bind(Integer.class).annotatedWith(Names.named(PairingResource.DACP_CLIENT_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(RemoteControlResource.DACP_SERVER_PORT_NAME)).toInstance(hostingPort);

				bind(IPairingDatabase.class).toInstance(new PairingDatabase());
				bind(PairingDaemon.class).asEagerSingleton();
				bind(IPairingResource.class).to(PairingResource.class);
				bind(IRemoteControlResource.class).to(RemoteControlResource.class);
				bind(IClientSessionListener.class).to(ClientSessionListener.class);
			}
		}, new JerseyServletModule() {

			@Override
			protected void configureServlets()
			{
				filter("*").through(ProxyFilter.class);
				serve("/*").with(GuiceContainer.class);
			}
		});
	}
}
