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

import java.awt.AWTException;
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
import java.net.URL;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.swing.ImageIcon;

import org.dyndns.jkiddo.guice.JoliviaServer;
import org.dyndns.jkiddo.jetty.extension.DmapConnector;
import org.dyndns.jkiddo.logic.desk.DeskImageStoreReader;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
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

	public static void main(String[] args)
	{
		try
		{
			new Jolivia(new DeskMusicStoreReader(), new DeskImageStoreReader());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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

	public Jolivia(IMusicStoreReader musicStoreReader, IImageStoreReader imageStoreReader) throws Exception
	{
		this(4000, 5000, 1337, "Jolivia", null, null, musicStoreReader, imageStoreReader);
	}

	public Jolivia(Integer port, Integer airplayPort, Integer pairingCode, String name, IClientSessionListener clientSessionListener, ISpeakerListener speakerListener, IMusicStoreReader musicStoreReader, IImageStoreReader imageStoreReader) throws Exception
	{
		setupGui();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		Preconditions.checkArgument(!(pairingCode > 9999), "Pairingcode must be expressed within 4 ciphers");
		logger.info("Starting " + name + " on port " + port);
		Server server = new Server(port);
		// Server server = new
		// Server(InetSocketAddress.createUnresolved("0.0.0.0", port));
		Connector dmapConnector = new DmapConnector();
		dmapConnector.setPort(port);
		// ServerConnector dmapConnector = new ServerConnector(server, new
		// DmapConnectionFactory());
		// dmapConnector.setPort(port);
		server.setConnectors(new Connector[] { dmapConnector });

		// Guice
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new JoliviaServer(port, airplayPort, pairingCode, name, clientSessionListener, speakerListener, imageStoreReader, musicStoreReader));
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		sch.addServlet(DefaultServlet.class, "/");

		server.start();
		logger.info(name + " started");
		server.join();
	}

	private void setupGui()
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run()
			{
				onShutdown();
			}

		}));

		try
		{
			/* Create about dialog */
			final Dialog aboutDialog = new Dialog((Dialog) null);
			final GridBagLayout aboutLayout = new GridBagLayout();
			aboutDialog.setLayout(aboutLayout);
			aboutDialog.setVisible(false);
			aboutDialog.setTitle("About Jolivia");
			aboutDialog.setResizable(false);
			
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
			
			
				/* Done button */
				final Button aboutDoneButton = new Button("Done");
				aboutDoneButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent evt)
					{
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
			
			aboutDialog.setVisible(false);
			aboutDialog.setLocationByPlatform(true);
			aboutDialog.pack();

			/* Create tray icon */
			final URL trayIconUrl = Jolivia.class.getClassLoader().getResource("icon_32.png");
			if(trayIconUrl == null)
			{
				throw new Exception("No image found");
			}
			// final URL trayIconUrl = new File("./src/main/resources/icon_32.png").toURI().toURL();
			TrayIcon trayIcon = new TrayIcon((new ImageIcon(trayIconUrl, "Jolivia").getImage()));
			trayIcon.setToolTip("Jolivia");
			trayIcon.setImageAutoSize(true);
			final PopupMenu popupMenu = new PopupMenu();
			final MenuItem aboutMenuItem = new MenuItem("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt)
				{
					aboutDialog.setLocationByPlatform(true);
					aboutDialog.setVisible(true);
				}
			});
			popupMenu.add(aboutMenuItem);
			final MenuItem exitMenuItem = new MenuItem("Quit");
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt)
				{
					onShutdown();
					System.exit(0);
				}
			});
			popupMenu.add(exitMenuItem);
			trayIcon.setPopupMenu(popupMenu);
			SystemTray.getSystemTray().add(trayIcon);

			logger.info("Running with GUI, created system tray icon and menu");
		}
		catch( NullPointerException e)
		{
			logger.info("Running headless", e);
		}
		catch( RuntimeException e)
		{
			logger.info("Running headless", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void onShutdown()
	{
		// TODO Auto-generated method stub

	}

	private final String AboutMessage = "   * Jolivia *\n" + "\n" + "Copyright (c) 2013 Jens Kristian Villadsen\n" + "\n" + "didms is free software: you can redistribute it and/or modify\n" + "it under the terms of the GNU General Public License as published by\n" + "the Free Software Foundation, either version 3 of the License, or\n" + "(at your option) any later version.\n" + "\n" + "didms is distributed in the hope that it will be useful,\n" + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\n" + "GNU General Public License for more details.\n" + "\n" + "You should have received a copy of the GNU General Public License\n" + "along with didms.  If not, see <http://www.gnu.org/licenses/>." + "\n\n";
	/*
	 * @Override public void update(Observable arg0, Object arg1) { if(trayIcon != null) { trayIcon.displayMessage(null, arg1.toString(), MessageType.INFO); } }
	 */
}