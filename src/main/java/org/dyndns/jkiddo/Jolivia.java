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
package org.dyndns.jkiddo;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.jetty.extension.DmapConnectionFactory;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.client.ISpeakerListener;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.common.base.Preconditions;
import com.google.inject.servlet.GuiceFilter;

public class Jolivia
{
	static Logger logger = LoggerFactory.getLogger(Jolivia.class);

	public static void main(String[] args) throws Exception
	{
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		new Jolivia(new IClientSessionListener() {

			@Override
			public void tearDownSession(String server, int port)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void registerNewSession(Session session) throws Exception
			{
				session.getRemoteControl().play();
				// TODO Auto-generated method stub

			}
		}, new DeskMusicStoreReader());
	}

	public Jolivia(IClientSessionListener clientSessionListener) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, null, null, null);
	}

	public Jolivia(IClientSessionListener clientSessionListener, IMusicStoreReader musicStoreReader, IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, null, musicStoreReader, imageStoreReader);
	}

	public Jolivia(IClientSessionListener clientSessionListener, IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, null, null, imageStoreReader);
	}

	public Jolivia(IClientSessionListener clientSessionListener, IMusicStoreReader musicStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, null, musicStoreReader, null);
	}

	public Jolivia(ISpeakerListener speakerListener) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, speakerListener, null, null);
	}

	public Jolivia(ISpeakerListener speakerListener, IMusicStoreReader musicStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, speakerListener, musicStoreReader, null);
	}

	public Jolivia(ISpeakerListener speakerListener, IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, speakerListener, null, imageStoreReader);
	}

	public Jolivia(ISpeakerListener speakerListener, IMusicStoreReader musicStoreReader, IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, speakerListener, musicStoreReader, imageStoreReader);
	}

	public Jolivia(IClientSessionListener clientSessionListener, ISpeakerListener speakerListener, IMusicStoreReader musicStoreReader, IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, speakerListener, musicStoreReader, imageStoreReader);
	}

	public Jolivia(IClientSessionListener clientSessionListener, ISpeakerListener speakerListener, IMusicStoreReader musicStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, speakerListener, musicStoreReader, null);
	}

	public Jolivia(IClientSessionListener clientSessionListener, ISpeakerListener speakerListener, IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, speakerListener, null, imageStoreReader);
	}

	public Jolivia(IClientSessionListener clientSessionListener, ISpeakerListener speakerListener) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, speakerListener, null, null);
	}

	public Jolivia(IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, null, null, imageStoreReader);
	}

	public Jolivia(IMusicStoreReader musicStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, null, musicStoreReader, null);
	}

	public Jolivia(Integer port, Integer airplayPort, Integer pairingCode, String name, IClientSessionListener clientSessionListener, ISpeakerListener speakerListener, IMusicStoreReader musicStoreReader, IImageStoreReader imageStoreReader) throws Exception
	{
		Preconditions.checkArgument(!(pairingCode > 9999), "Pairingcode must be expressed within 4 ciphers");
		logger.info("Starting " + name + " on port " + port);
		Server server = new Server(port);
		ServerConnector sc = new ServerConnector(server, new DmapConnectionFactory());
		sc.setPort(port);
		server.setConnectors(new Connector[] { sc });
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new JoliviaListener(port, airplayPort, pairingCode, name, clientSessionListener, speakerListener, imageStoreReader, musicStoreReader));
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		sch.addServlet(DefaultServlet.class, "/");
		server.start();
		logger.info(name + " started");
		server.join();
	}
}