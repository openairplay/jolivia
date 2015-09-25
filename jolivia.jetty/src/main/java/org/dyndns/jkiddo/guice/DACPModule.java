package org.dyndns.jkiddo.guice;

import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.PairedRemoteDiscoverer;
import org.dyndns.jkiddo.service.daap.client.UnpairedRemoteCrawler;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.client.PairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.TouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.service.dacp.server.TouchAbleServerResource;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class DACPModule extends AbstractModule
{

	public DACPModule(final Integer pairingCode, final Integer hostingPort, final String dB_NAME, final IClientSessionListener clientSessionListener)
	{
		super();
		this.pairingCode = pairingCode;
		this.hostingPort = hostingPort;
		DB_NAME = dB_NAME;
		this.clientSessionListener = clientSessionListener;
	}

	private final Integer pairingCode;
	private final Integer hostingPort;
	private final String DB_NAME;
	private final IClientSessionListener clientSessionListener;

	@Override
	protected void configure()
	{
		bind(Integer.class).annotatedWith(Names.named(TouchRemoteResource.DACP_CLIENT_PAIRING_CODE)).toInstance(pairingCode);
		bind(Integer.class).annotatedWith(Names.named(TouchRemoteResource.DACP_CLIENT_PORT_NAME)).toInstance(hostingPort);
		bind(Integer.class).annotatedWith(Names.named(TouchAbleServerResource.DACP_SERVER_PORT_NAME)).toInstance(hostingPort);
		bind(Integer.class).annotatedWith(Names.named(UnpairedRemoteCrawler.SERVICE_PORT_NAME)).toInstance(hostingPort);

		bind(String.class).annotatedWith(Names.named(PairingDatabase.NAME_OF_DB)).toInstance(DB_NAME);
		bind(IPairingDatabase.class).to(PairingDatabase.class).asEagerSingleton();
		bind(PairedRemoteDiscoverer.class).asEagerSingleton();
		bind(UnpairedRemoteCrawler.class).asEagerSingleton();
		bind(ITouchRemoteResource.class).to(TouchRemoteResource.class);
		bind(ITouchAbleServerResource.class).to(TouchAbleServerResource.class);
		bind(IClientSessionListener.class).toInstance(clientSessionListener);
	}

}
