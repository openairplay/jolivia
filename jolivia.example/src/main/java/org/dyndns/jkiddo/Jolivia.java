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
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.dyndns.jkiddo.Jolivia.JoliviaBuilder.SecurityScheme;
import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.guice.JoliviaServer;
import org.dyndns.jkiddo.jetty.extension.DmapConnector;
import org.dyndns.jkiddo.logic.desk.DeskImageStoreReader;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.raop.server.IPlayingInformation;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.security.authentication.DigestAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.inject.servlet.GuiceFilter;

public class Jolivia
{
	static Logger logger = LoggerFactory.getLogger(Jolivia.class);

	public static void main(final String[] args) throws UnknownHostException
	{
		System.out.println(InetAddress.getLocalHost().getHostName());
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		// return ServiceInfo.create("_mobileiphoto._udp.local.",
		// "00pYaGq1A..SPACE", port, "");
		try
		{
			//IMusicStoreReader reader = new GoogleStoreReader(args[0], args[1]);
			IMusicStoreReader reader = new DeskMusicStoreReader();
			/*
			 * if(args.length == 2) { reader = new GoogleStoreReader(args[0], args[1]); new GReporter(args[0]); } else {
			 */
			reader = new DeskMusicStoreReader();
			new Jolivia.JoliviaBuilder().name("JensKristian’s Library").port(3700)/*.security(PasswordMethod.PASSWORD, SecurityScheme.BASIC)*/.pairingCode(1337).musicStoreReader(reader).imageStoreReader(new DeskImageStoreReader("C:\\Users\\JensKristian\\Desktop\\test")).build();
		}
		catch(final Exception e)
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
		private PasswordMethod security = PasswordMethod.NO_PASSWORD;
		private SecurityScheme scheme = null;

		public JoliviaBuilder port(final int port)
		{
			this.port = port;
			return this;
		}

		public JoliviaBuilder pairingCode(final int pairingCode)
		{
			this.pairingCode = pairingCode;
			return this;
		}

		public JoliviaBuilder airplayPort(final int airplayPort)
		{
			this.airplayPort = airplayPort;
			return this;
		}

		public JoliviaBuilder name(final String name)
		{
			this.name = name;
			return this;
		}

		public enum SecurityScheme
		{
			DIGEST, BASIC
		}

		public JoliviaBuilder security(final PasswordMethod security, final SecurityScheme scheme)
		{
			this.scheme = scheme;
			this.security = security;
			return this;
		}

		public JoliviaBuilder musicStoreReader(final IMusicStoreReader musicStoreReader)
		{
			this.musicStoreReader = musicStoreReader;
			return this;
		}

		public JoliviaBuilder imageStoreReader(final IImageStoreReader imageStoreReader)
		{
			this.imageStoreReader = imageStoreReader;
			return this;
		}

		public JoliviaBuilder playingInformation(final IPlayingInformation iplayingInformation)
		{
			this.iplayingInformation = iplayingInformation;
			return this;
		}

		public JoliviaBuilder clientSessionListener(final IClientSessionListener clientSessionListener)
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
			public DefaultClientSessionListener()  {
			}
			
			private Session session;

			@Override
			public void registerNewSession(final Session session) throws Exception
			{
			 	this.session = session;
			}

			@Override
			public void tearDownSession(final String server, final int port)
			{
				try
				{
					session.logout();
				}
				catch(final Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		class DefaultIPlayingInformation implements IPlayingInformation
		{
			private final JFrame frame;
			private final JLabel label;

			public DefaultIPlayingInformation()
			{
				frame = new JFrame("Cover");
				label = new JLabel();
				frame.getContentPane().add(label, BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(false);
			}

			@Override
			public void notify(final BufferedImage image)
			{
				try
				{
					final ImageIcon icon = new ImageIcon(image);
					label.setIcon(icon);
					frame.pack();
					frame.setSize(icon.getIconWidth(), icon.getIconHeight());
					frame.setVisible(true);
				}
				catch(final Exception e)
				{
					logger.debug(e.getMessage(), e);
				}
			}

			@Override
			public void notify(final ListingItem listingItem)
			{
				final String title = listingItem.getSpecificChunk(ItemName.class).getValue();
				final String artist = listingItem.getSpecificChunk(SongArtist.class).getValue();
				final String album = listingItem.getSpecificChunk(SongAlbum.class).getValue();
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
			public URI getImage(final IImageItem image) throws Exception
			{
				return null;
			}

			@Override
			public byte[] getImageThumb(final IImageItem image) throws Exception
			{
				return null;
			}
		}

		class DefaultMusicStoreReader implements IMusicStoreReader
		{
			@Override
			public Set<MediaItem> readTunes() throws Exception
			{
				return Sets.newHashSet();
			}

			@Override
			public URI getTune(final String tuneIdentifier) throws Exception
			{
				return null;
			}

			@Override
			public void readTunesMemoryOptimized(final Listing listing, final Map<Long, String> map) throws Exception
			{
				// TODO Auto-generated method stub

			}
		}
	}

	private final JoliviaServer joliviaServer;

	private Jolivia(final JoliviaBuilder builder) throws Exception
	{
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		Preconditions.checkArgument(!(builder.pairingCode > 9999 || builder.pairingCode < 0), "Pairingcode must be expressed within 4 ciphers");
		logger.info("Starting " + builder.name + " on port " + builder.port);
		final Server server = new Server(builder.port);
		
		// Server server = new
		// Server(InetSocketAddress.createUnresolved("0.0.0.0", port));
		final Connector dmapConnector = new DmapConnector();
		dmapConnector.setPort(builder.port);
		// ServerConnector dmapConnector = new ServerConnector(server, new
		// DmapConnectionFactory());
		// dmapConnector.setPort(port);
		server.setConnectors(new Connector[] { dmapConnector });

		// Guice
		final ServletContextHandler sch = new ServletContextHandler(server, "/");
		joliviaServer = new JoliviaServer(builder.port, builder.airplayPort, builder.pairingCode, builder.name, builder.clientSessionListener, builder.speakerListener, builder.imageStoreReader, builder.musicStoreReader, builder.iplayingInformation, builder.security);
		sch.addEventListener(joliviaServer);
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

		final String username;
		if(builder.security == PasswordMethod.PASSWORD)
			username = DmapUtil.DEFAULT_USER;
		else
			username = "user";
		final String password = "password";

		if(builder.scheme == SecurityScheme.BASIC)
			sch.setSecurityHandler(getSecurityHandler(username, password, DmapUtil.DAAP_REALM, new BasicAuthenticator()));
		if(builder.scheme == SecurityScheme.DIGEST)
			sch.setSecurityHandler(getSecurityHandler(username, password, DmapUtil.DAAP_REALM, new DigestAuthenticator()));
		sch.addServlet(DefaultServlet.class, "/");

		server.start();
		logger.info(builder.name + " started");
		new Scanner(System.in).nextLine();
		server.stop();
	}

	private SecurityHandler getSecurityHandler(final String username, final String password, final String realm, final Authenticator authenticator)
	{

		final HashLoginService loginService = new HashLoginService();
		loginService.putUser(username, Credential.getCredential(password), new String[] { "user" });
		loginService.setName(realm);

		final Constraint globalConstraint = new Constraint();
		if(BasicAuthenticator.class.equals(authenticator.getClass()))
			globalConstraint.setName(Constraint.__BASIC_AUTH);
		if(DigestAuthenticator.class.equals(authenticator.getClass()))
			globalConstraint.setName(Constraint.__DIGEST_AUTH);
		globalConstraint.setRoles(new String[] { "user" });
		globalConstraint.setAuthenticate(true);

		final ConstraintMapping globalConstraintMapping = new ConstraintMapping();
		globalConstraintMapping.setConstraint(globalConstraint);
		globalConstraintMapping.setPathSpec("/*");

		final ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
		csh.setAuthenticator(authenticator);
		csh.setRealmName(realm);
		csh.addConstraintMapping(globalConstraintMapping);
		csh.addConstraintMapping(createRelaxation("/server-info"));
		csh.addConstraintMapping(createRelaxation("/logout"));
		// Following is a hack! It should state /databases/*/items/* instead - however, that cannot be used.
		csh.addConstraintMapping(createRelaxation("/databases/*"));
		csh.setLoginService(loginService);

		return csh;
	}

	private static ConstraintMapping createRelaxation(final String pathSpec)
	{
		final Constraint relaxation = new Constraint();
		relaxation.setName(Constraint.ANY_ROLE);
		relaxation.setAuthenticate(false);
		final ConstraintMapping constraintMapping = new ConstraintMapping();
		constraintMapping.setConstraint(relaxation);
		constraintMapping.setPathSpec(pathSpec);
		return constraintMapping;
	}

	public void reRegister()
	{
		this.joliviaServer.reRegister();
	}

	/*
	 * @Override public void update(Observable arg0, Object arg1) { if(trayIcon != null) { trayIcon.displayMessage(null, arg1.toString(), MessageType.INFO); } }
	 */
}