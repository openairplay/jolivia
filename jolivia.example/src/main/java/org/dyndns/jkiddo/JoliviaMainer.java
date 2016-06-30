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

import java.net.UnknownHostException;

import org.dyndns.jkiddo.Jolivia.JoliviaBuilder.SecurityScheme;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public final class JoliviaMainer {
	private static final Logger LOGGER = LoggerFactory.getLogger(JoliviaMainer.class);

	private JoliviaMainer() throws InstantiationException {
		throw new InstantiationException("This class is not created for instantiation");
	}

	public static void main(final String[] args) throws UnknownHostException {

		System.setProperty("java.net.preferIPv4Stack", "true");

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		try {
			final IMusicStoreReader reader = new DeskMusicStoreReader();
			new Jolivia.JoliviaBuilder().name("Kiddo's Library")
					.homeSharing("someuser", "somepass")
					.security(PasswordMethod.PASSWORD, SecurityScheme.BASIC).pairingCode(1337).musicStoreReader(reader)
					.build().start();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}