package org.dyndns.jkiddo.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class DmapConnector extends ServerConnector {

	public DmapConnector(final Server server) {
		super(server, null, null, null, -1, -1, new DmapConnectionFactory());
	}
}