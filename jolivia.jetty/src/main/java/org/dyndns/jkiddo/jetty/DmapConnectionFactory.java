package org.dyndns.jkiddo.jetty;

import org.eclipse.jetty.http.DmapParser;
import org.eclipse.jetty.http.HttpCompliance;
import org.eclipse.jetty.http.HttpParser;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.HttpConnectionFactory;

public class DmapConnectionFactory extends HttpConnectionFactory {
	@Override
	public Connection newConnection(final Connector connector, final EndPoint endPoint) {
		final HttpConnection conn = new DmapConnection(getHttpConfiguration(), connector, endPoint, getHttpCompliance(),
				isRecordHttpComplianceViolations());
		return configure(conn, connector, endPoint);
	}
}

class DmapConnection extends HttpConnection {

	public DmapConnection(final HttpConfiguration config, final Connector connector, final EndPoint endPoint,
			final HttpCompliance compliance, final boolean recordComplianceViolations) {
		super(config, connector, endPoint, compliance, recordComplianceViolations);
	}

	@Override
	protected HttpParser newHttpParser(final HttpCompliance compliance) {
		return new DmapParser(newRequestHandler(), getHttpConfiguration().getRequestHeaderSize(), compliance);
	}
}