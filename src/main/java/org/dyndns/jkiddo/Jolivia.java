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
import org.dyndns.jkiddo.raop.client.ISpeakerListener;
import org.dyndns.jkiddo.raop.client.model.Device;
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
				session.getServerInfo();
				session.getLibrary().getAllAlbums();
				session.getRemoteControl().play();

			}
		}, new ISpeakerListener() {

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
		});
	}

	public Jolivia(IClientSessionListener clientSessionListener, ISpeakerListener speakerListener) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", clientSessionListener, speakerListener);
	}

	public Jolivia() throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, null);
	}

	public Jolivia(Integer port, Integer airplayPort, Integer pairingCode, String name, IClientSessionListener clientSessionListener, ISpeakerListener speakerListener) throws Exception
	{
		Preconditions.checkArgument(!(pairingCode > 9999), "Pairingcode must be expressed within 4 ciphers");
		logger.info("Starting " + name + " on port " + port);
		Server server = new Server(port);
		ServerConnector sc = new ServerConnector(server, new DmapConnectionFactory());
		sc.setPort(port);
		server.setConnectors(new Connector[] { sc });
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new JoliviaListener(port, airplayPort, pairingCode, name, clientSessionListener, speakerListener));
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		sch.addServlet(DefaultServlet.class, "/");
		server.start();
		logger.info(name + " started");
		server.join();
	}
}