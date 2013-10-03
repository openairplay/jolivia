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

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.guice.JoliviaServer;
import org.dyndns.jkiddo.jetty.extension.DmapConnector;
import org.dyndns.jkiddo.logic.desk.DeskImageStoreReader;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.desk.GoogleStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.raop.server.IPlayingInformation;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.inject.servlet.GuiceFilter;

public class Jolivia
{
	static Logger logger = LoggerFactory.getLogger(Jolivia.class);

	public static void main(String[] args)
	{
		// return ServiceInfo.create("_mobileiphoto._udp.local.", "00pYaGq1A..SPACE", port, "");
		try
		{
			IMusicStoreReader reader = null;
			if(args.length == 2)
			{
				reader = new GoogleStoreReader(args[0], args[1]);
				new GReporter(args[0]);
			}
			else
			{
				reader = new DeskMusicStoreReader();
				new GReporter("local version");
			}
			//new Jolivia.JoliviaBuilder().port(4000).pairingCode(1337).musicStoreReader(reader).imageStoreReader(new DeskImageStoreReader()).build();
			new Jolivia.JoliviaBuilder().port(8770).pairingCode(1337).musicStoreReader(reader).imageStoreReader(new DeskImageStoreReader("C:\\Users\\JensKristian\\Desktop\\test")).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static class JoliviaBuilder
	{
		private Integer port = 4000;
		private Integer airplayPort = 5000;
		private Integer pairingCode = 1337;
		private String name = "Jolivia";
		private ISpeakerListener speakerListener;
		private IClientSessionListener clientSessionListener = new DefaultClientSessionListener();
		private IMusicStoreReader musicStoreReader = new DefaultMusicStoreReader();
		private IImageStoreReader imageStoreReader = new DefaultImageStoreReader();
		private IPlayingInformation iplayingInformation = new DefaultIPlayingInformation();

		public JoliviaBuilder port(int port)
		{
			this.port = port;
			return this;
		}

		public JoliviaBuilder pairingCode(int pairingCode)
		{
			this.pairingCode = pairingCode;
			return this;
		}

		public JoliviaBuilder airplayPort(int airplayPort)
		{
			this.airplayPort = airplayPort;
			return this;
		}

		public JoliviaBuilder name(String name)
		{
			this.name = name;
			return this;
		}

		public JoliviaBuilder musicStoreReader(IMusicStoreReader musicStoreReader)
		{
			this.musicStoreReader = musicStoreReader;
			return this;
		}

		public JoliviaBuilder imageStoreReader(IImageStoreReader imageStoreReader)
		{
			this.imageStoreReader = imageStoreReader;
			return this;
		}

		public JoliviaBuilder playingInformation(IPlayingInformation iplayingInformation)
		{
			this.iplayingInformation = iplayingInformation;
			return this;
		}

		public JoliviaBuilder clientSessionListener(IClientSessionListener clientSessionListener)
		{
			this.clientSessionListener = clientSessionListener;
			return this;
		}

		public Jolivia build() throws Exception
		{
			return new Jolivia(this);
		}

		class DefaultClientSessionListener implements IClientSessionListener
		{
			private Session session;

			@Override
			public void registerNewSession(Session session) throws Exception
			{
				this.session = session;
			}

			@Override
			public void tearDownSession(String server, int port)
			{
				try
				{
					session.logout();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		class DefaultIPlayingInformation implements IPlayingInformation
		{
			private JFrame frame;
			private JLabel label;

			public DefaultIPlayingInformation()
			{
				frame = new JFrame("Cover");
				label = new JLabel();
				frame.getContentPane().add(label, BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(false);
			}

			@Override
			public void notify(BufferedImage image)
			{
				try
				{
					ImageIcon icon = new ImageIcon(image);
					label.setIcon(icon);
					frame.pack();
					frame.setSize(icon.getIconWidth(), icon.getIconHeight());
					frame.setVisible(true);
				}
				catch(Exception e)
				{
					logger.debug(e.getMessage(), e);
				}
			}

			@Override
			public void notify(ListingItem listingItem)
			{
				String title = listingItem.getSpecificChunk(ItemName.class).getValue();
				String artist = listingItem.getSpecificChunk(SongArtist.class).getValue();
				String album = listingItem.getSpecificChunk(SongAlbum.class).getValue();
				frame.setTitle("Playing: " + title + " - " + album + " - " + artist);
			}
		}

		class DefaultImageStoreReader implements IImageStoreReader
		{
			@Override
			public Set<IImageItem> readImages() throws Exception
			{
				return Sets.newHashSet();
			}

			@Override
			public URI getImage(IImageItem image) throws Exception
			{
				return null;
			}

			@Override
			public byte[] getImageThumb(IImageItem image) throws Exception
			{
				return null;
			}
		}

		class DefaultMusicStoreReader implements IMusicStoreReader
		{
			@Override
			public Set<IMusicItem> readTunes() throws Exception
			{
				return Sets.newHashSet();
			}

			@Override
			public URI getTune(IMusicItem tune) throws Exception
			{
				return null;
			}
		}
	}

	private Jolivia(JoliviaBuilder builder) throws Exception
	{
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		Preconditions.checkArgument(!(builder.pairingCode > 9999 || builder.pairingCode < 0), "Pairingcode must be expressed within 4 ciphers");
		logger.info("Starting " + builder.name + " on port " + builder.port);
		Server server = new Server(builder.port);
		// Server server = new
		// Server(InetSocketAddress.createUnresolved("0.0.0.0", port));
		Connector dmapConnector = new DmapConnector();
		dmapConnector.setPort(builder.port);
		// ServerConnector dmapConnector = new ServerConnector(server, new
		// DmapConnectionFactory());
		// dmapConnector.setPort(port);
		server.setConnectors(new Connector[] { dmapConnector });

		// Guice
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new JoliviaServer(builder.port, builder.airplayPort, builder.pairingCode, builder.name, builder.clientSessionListener, builder.speakerListener, builder.imageStoreReader, builder.musicStoreReader, builder.iplayingInformation));
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		sch.addServlet(DefaultServlet.class, "/");

		server.start();
		logger.info(builder.name + " started");
		server.join();
	}

	
	/*
	 * @Override public void update(Observable arg0, Object arg1) { if(trayIcon != null) { trayIcon.displayMessage(null, arg1.toString(), MessageType.INFO); } }
	 */
}