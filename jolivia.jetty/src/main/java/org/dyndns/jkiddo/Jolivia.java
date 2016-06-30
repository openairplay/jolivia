package org.dyndns.jkiddo;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
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
import org.dyndns.jkiddo.jetty.DmapConnectionFactory;
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
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.servlet.GuiceFilter;

public class Jolivia {

	private static final Logger LOGGER = LoggerFactory.getLogger(Jolivia.class);
	private static final String AboutMessage = "Use it only to be disruptive";

	public static class JoliviaBuilder {
		private Integer port = 3689;
		private Integer airplayPort = 5000;
		private Integer pairingCode = 1337;
		private String name = "Jolivia";
		private ISpeakerListener speakerListener;
		private IClientSessionListener clientSessionListener = new DefaultClientSessionListener();
		private IMusicStoreReader musicStoreReader = new IMusicStoreReader() {
			
			@Override
			public void readTunesMemoryOptimized(final Listing listing, final Map<Long, String> map) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Collection<MediaItem> readTunes() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getTune(final String tuneIdentifier) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
		private IImageStoreReader imageStoreReader = new IImageStoreReader() {
			
			@Override
			public Set<IImageItem> readImages() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public byte[] getImageThumb(final IImageItem image) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getImage(final IImageItem image) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
		private IPlayingInformation iplayingInformation = new DefaultIPlayingInformation();
		private PasswordMethod security = PasswordMethod.NO_PASSWORD;
		private SecurityScheme scheme;
		private String appleUsername;
		private String applePassword;

		public JoliviaBuilder port(final int port) {
			this.port = port;
			return this;
		}

		public JoliviaBuilder homeSharing(final String appleUsername, final String applePassword) {
			this.appleUsername = appleUsername;
			this.applePassword = applePassword;
			return this;
		}

		public JoliviaBuilder pairingCode(final int pairingCode) {
			this.pairingCode = pairingCode;
			return this;
		}

		public JoliviaBuilder airplayPort(final int airplayPort) {
			this.airplayPort = airplayPort;
			return this;
		}

		public JoliviaBuilder name(final String name) {
			this.name = name;
			return this;
		}

		public enum SecurityScheme {
			DIGEST, BASIC
		}

		public JoliviaBuilder security(final PasswordMethod security, final SecurityScheme scheme) {
			this.scheme = scheme;
			this.security = security;
			return this;
		}

		public JoliviaBuilder musicStoreReader(final IMusicStoreReader musicStoreReader) {
			this.musicStoreReader = musicStoreReader;
			return this;
		}

		public JoliviaBuilder imageStoreReader(final IImageStoreReader imageStoreReader) {
			this.imageStoreReader = imageStoreReader;
			return this;
		}

		public JoliviaBuilder playingInformation(final IPlayingInformation iplayingInformation) {
			this.iplayingInformation = iplayingInformation;
			return this;
		}

		public JoliviaBuilder clientSessionListener(final IClientSessionListener clientSessionListener) {
			this.clientSessionListener = clientSessionListener;
			return this;
		}

		public Jolivia build() throws Exception {
			return new Jolivia(this);
		}

		class DefaultClientSessionListener implements IClientSessionListener {
			private Session session;

			public DefaultClientSessionListener() {
			}

			@Override
			public void registerNewSession(final Session session) throws Exception {
				this.session = session;
			}

			@Override
			public void tearDownSession(final String server, final int port) {
				try {
					session.logout();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}

		class DefaultIPlayingInformation implements IPlayingInformation {
			private final JFrame frame;
			private final JLabel label;

			public DefaultIPlayingInformation() {
				frame = new JFrame("Cover");
				label = new JLabel();
				frame.getContentPane().add(label, BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(false);
			}

			@Override
			public void notify(final BufferedImage image) {
				try {
					final ImageIcon icon = new ImageIcon(image);
					label.setIcon(icon);
					frame.pack();
					frame.setSize(icon.getIconWidth(), icon.getIconHeight());
					frame.setVisible(true);
				} catch (final Exception e) {
					LOGGER.debug(e.getMessage(), e);
				}
			}

			@Override
			public void notify(final ListingItem listingItem) {
				final String title = listingItem.getSpecificChunk(ItemName.class).getValue();
				final String artist = listingItem.getSpecificChunk(SongArtist.class).getValue();
				final String album = listingItem.getSpecificChunk(SongAlbum.class).getValue();
				frame.setTitle("Playing: " + title + " - " + album + " - " + artist);
			}
		}
	}

	private final JoliviaServer joliviaServer;
	private final Server server;

	private Jolivia(final JoliviaBuilder builder) throws Exception {
		buildUI();

		Preconditions.checkArgument(!(builder.pairingCode > 9999 || builder.pairingCode < 0),
				"Pairingcode must be expressed within 4 ciphers");
		LOGGER.info("Starting " + builder.name + " on port " + builder.port);
		server = new Server(builder.port);

		final ServerConnector dmapConnector = new ServerConnector(server, new DmapConnectionFactory());
		dmapConnector.setPort(builder.port);
		server.setConnectors(new Connector[] { dmapConnector });

		// Guice
		final ServletContextHandler sch = new ServletContextHandler(server, "/");
		joliviaServer = new JoliviaServer(builder.port, builder.airplayPort, builder.pairingCode, builder.name,
				builder.clientSessionListener, builder.speakerListener, builder.imageStoreReader,
				builder.musicStoreReader, builder.iplayingInformation, builder.security, builder.appleUsername,
				builder.applePassword);
		sch.addEventListener(joliviaServer);
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

		final String username;
		if (builder.security == PasswordMethod.PASSWORD)
			username = DmapUtil.DEFAULT_USER;
		else
			username = "user";
		final String password = "password";

		if (builder.scheme == SecurityScheme.BASIC)
			sch.setSecurityHandler(
					getSecurityHandler(username, password, DmapUtil.DAAP_REALM, new BasicAuthenticator()));
		if (builder.scheme == SecurityScheme.DIGEST)
			sch.setSecurityHandler(
					getSecurityHandler(username, password, DmapUtil.DAAP_REALM, new DigestAuthenticator()));
		sch.addServlet(DefaultServlet.class, "/");

		LOGGER.info(builder.name + " started");
	}

	public void start() throws Exception {
		server.start();
	}

	public void stop() throws Exception {
		joliviaServer.contextDestroyed(null);
		server.stop();
	}

	public void reRegister() {
		this.joliviaServer.reRegister();
	}

	private SecurityHandler getSecurityHandler(final String username, final String password, final String realm,
			final Authenticator authenticator) {

		final HashLoginService loginService = new HashLoginService();
		loginService.putUser(username, Credential.getCredential(password), new String[] { "user" });
		loginService.setName(realm);

		final Constraint globalConstraint = new Constraint();
		if (BasicAuthenticator.class.equals(authenticator.getClass()))
			globalConstraint.setName(Constraint.__BASIC_AUTH);
		if (DigestAuthenticator.class.equals(authenticator.getClass()))
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
		// Following is a hack! It should state /databases/*/items/* instead -
		// however, that cannot be used.
		csh.addConstraintMapping(createRelaxation("/databases/*"));
		csh.setLoginService(loginService);

		return csh;
	}

	private static ConstraintMapping createRelaxation(final String pathSpec) {
		final Constraint relaxation = new Constraint();
		relaxation.setName(Constraint.ANY_ROLE);
		relaxation.setAuthenticate(false);
		final ConstraintMapping constraintMapping = new ConstraintMapping();
		constraintMapping.setConstraint(relaxation);
		constraintMapping.setPathSpec(pathSpec);
		return constraintMapping;
	}

	private void buildUI() throws AWTException {

		/* Create about dialog */
		final Dialog aboutDialog = new Dialog((Dialog) null);
		final GridBagLayout aboutLayout = new GridBagLayout();
		aboutDialog.setLayout(aboutLayout);
		aboutDialog.setVisible(false);
		aboutDialog.setTitle("About Jolivia");
		aboutDialog.setResizable(false);
		{
			/* Message */
			final TextArea title = new TextArea(AboutMessage.split("\n").length + 1, 64);
			title.setText(AboutMessage);
			title.setEditable(false);
			final GridBagConstraints titleConstraints = new GridBagConstraints();
			titleConstraints.gridx = 1;
			titleConstraints.gridy = 1;
			titleConstraints.fill = GridBagConstraints.HORIZONTAL;
			titleConstraints.insets = new Insets(0, 0, 0, 0);
			aboutLayout.setConstraints(title, titleConstraints);
			aboutDialog.add(title);
		}
		{
			/* Done button */
			final Button aboutDoneButton = new Button("Done");
			aboutDoneButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt) {
					aboutDialog.setVisible(false);
				}
			});
			final GridBagConstraints aboutDoneConstraints = new GridBagConstraints();
			aboutDoneConstraints.gridx = 1;
			aboutDoneConstraints.gridy = 2;
			aboutDoneConstraints.anchor = GridBagConstraints.PAGE_END;
			aboutDoneConstraints.fill = GridBagConstraints.NONE;
			aboutDoneConstraints.insets = new Insets(0, 0, 0, 0);
			aboutLayout.setConstraints(aboutDoneButton, aboutDoneConstraints);
			aboutDialog.add(aboutDoneButton);
		}
		aboutDialog.setVisible(false);
		aboutDialog.setLocationByPlatform(true);
		aboutDialog.pack();

		/* Create tray icon */
		final URL trayIconUrl = Thread.currentThread().getContextClassLoader().getResource("Ceres.png");
		final TrayIcon trayIcon1 = new TrayIcon(new ImageIcon(trayIconUrl, "AirReceiver").getImage());
		trayIcon1.setToolTip("Jolivia");
		trayIcon1.setImageAutoSize(true);
		final PopupMenu popupMenu = new PopupMenu();
		final MenuItem aboutMenuItem = new MenuItem("About");
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				aboutDialog.setLocationByPlatform(true);
				aboutDialog.setVisible(true);
			}
		});
		popupMenu.add(aboutMenuItem);
		final MenuItem exitMenuItem = new MenuItem("Quit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				try {
					onShutdown();
				} catch (final Exception e) {
					LOGGER.info(e.getMessage(), e);
				}
				System.exit(0);
			}
		});
		popupMenu.add(exitMenuItem);
		trayIcon1.setPopupMenu(popupMenu);
		SystemTray.getSystemTray().add(trayIcon1);

		// trayIcon1.displayMessage("sldkjfsldkfj", "æslkfædslkf",
		// MessageType.INFO);

		if (!SystemTray.isSupported()) {
			// Go directory to the task;
			return;
		}
	}

	protected void onShutdown() throws Exception {
		stop();
	}
}
