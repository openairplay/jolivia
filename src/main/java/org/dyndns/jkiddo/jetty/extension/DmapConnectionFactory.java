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
package org.dyndns.jkiddo.jetty.extension;

import org.eclipse.jetty.http.HttpParser;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpChannelConfig;
import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.HttpConnectionFactory;

public class DmapConnectionFactory extends HttpConnectionFactory
{
	public DmapConnectionFactory()
	{
		super();
	}

	@Override
	public Connection newConnection(Connector connector, EndPoint endPoint)
	{
		return configure(new DaapConnection(getHttpChannelConfig(), connector, endPoint), connector, endPoint);
	}

	class DaapConnection extends HttpConnection
	{
		public DaapConnection(HttpChannelConfig config, Connector connector, EndPoint endPoint)
		{
			super(config, connector, endPoint);
		}

		@Override
		protected HttpParser newHttpParser()
		{
			return new DmapParser(newRequestHandler(), getHttpChannelConfig().getRequestHeaderSize());
		}
	}
}
