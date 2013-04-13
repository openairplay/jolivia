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
import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.jmdns.JmmDNS;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.jetty.JoliviaExceptionMapper;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.client.ISpeakerListener;
import org.dyndns.jkiddo.raop.client.RemoteSpeakerDiscoverer;
import org.dyndns.jkiddo.raop.client.model.Device;
import org.dyndns.jkiddo.raop.server.AirPlayResourceWrapper;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.PairedRemoteDiscoverer;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.dyndns.jkiddo.service.daap.client.UnpairedRemoteCrawler;
import org.dyndns.jkiddo.service.daap.server.DAAPResource;
import org.dyndns.jkiddo.service.daap.server.IMusicLibrary;
import org.dyndns.jkiddo.service.daap.server.MusicItemManager;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.client.PairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.TouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.service.dacp.server.TouchAbleServerResource;
import org.dyndns.jkiddo.service.dmap.DMAPInterface;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dpap.server.DPAPResource;
import org.dyndns.jkiddo.service.dpap.server.IImageLibrary;
import org.dyndns.jkiddo.service.dpap.server.ImageItemManager;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaListener extends GuiceServletContextListener
{
	final private String DB_URL = "jdbc:sqlite:db";
	final private Integer hostingPort;
	final private Integer pairingCode;
	final private Integer airplayPort;
	final private String name;
	final private IClientSessionListener clientSessionListener;
	final private ISpeakerListener speakerListener;
	final private IImageStoreReader imageStoreReader;
	final private IMusicStoreReader musicStoreReader;

	public static final String APPLICATION_NAME = "APPLICATION_NAME";
	
	public static String toServiceGuid(String name)
	{
		try {
			return Jolivia.toHex((name+"1111111111111111").getBytes("UTF-8")).substring(0, 16);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public JoliviaListener(Integer port, Integer airplayPort, Integer pairingCode, String name, IClientSessionListener clientSessionListener, ISpeakerListener speakerListener, IImageStoreReader imageStoreReader, IMusicStoreReader musicStoreReader) throws UnsupportedEncodingException
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
		if(musicStoreReader == null)
		{
			this.musicStoreReader = new IMusicStoreReader() {
				
				@Override
				public Set<IMusicItem> readTunes() throws Exception {
					
					return Sets.newHashSet();
				}
				
				@Override
				public File getTune(IMusicItem tune) throws Exception {
					// TODO Auto-generated method stub
					return null;
				}
			};
		}
		else
		{
			this.musicStoreReader = musicStoreReader;
		}
		if(imageStoreReader == null)
		{
			this.imageStoreReader = new IImageStoreReader() {
				
				@Override
				public Set<IImageItem> readImages() throws Exception {
					
					return Sets.newHashSet();
				}
				
				@Override
				public File getImage(IImageItem image) throws Exception {
					// TODO Auto-generated method stub
					return null;
				}
			};
		}
		else
		{
			this.imageStoreReader = imageStoreReader;
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
				bind(IItemManager.class).annotatedWith(Names.named(DPAPResource.DPAP_RESOURCE)).to(ImageItemManager.class);
				bind(IImageStoreReader.class).toInstance(imageStoreReader);
			}
		}, new AbstractModule() {

			@Override
			protected void configure()
			{
				bind(Integer.class).annotatedWith(Names.named(DAAPResource.DAAP_PORT_NAME)).toInstance(hostingPort);
				bind(IMusicLibrary.class).to(DAAPResource.class).asEagerSingleton();
				bind(IItemManager.class).annotatedWith(Names.named(DAAPResource.DAAP_RESOURCE)).to(MusicItemManager.class);
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

				bind(String.class).annotatedWith(Names.named(PairingDatabase.DB_URL)).toInstance(DB_URL);
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
