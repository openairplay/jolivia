package org.dyndns.jkiddo.service.daap.client;

import java.net.ConnectException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jmdns.JmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.ServiceEvent;
import javax.jmdns.impl.JmDNSImpl;

import org.dyndns.jkiddo.IDiscoverer;
import org.dyndns.jkiddo.dmcp.chunks.media.PairingContainer;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.client.TouchRemoteResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnpairedRemoteCrawler implements IDiscoverer {
	public static final Logger logger = LoggerFactory
			.getLogger(UnpairedRemoteCrawler.class);

	public static final String SERVICE_PORT_NAME = "SERVICE_PORT_NAME";

	private final IZeroconfManager mDNS;
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();

	private final Integer port;

	private final String serviceGuid;

	@Inject
	public UnpairedRemoteCrawler(final IZeroconfManager mDNS,
			final IClientSessionListener clientSessionListener,
			@Named(SERVICE_PORT_NAME) final Integer port, @Named(Util.APPLICATION_NAME) final String name) {
		this.mDNS = mDNS;
		this.port = port;
		this.serviceGuid = Util.toServiceGuid(name);
		this.mDNS.addServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT,
				this);
		this.mDNS.addNetworkTopologyListener(this);
		
	}

	@Override
	public void serviceAdded(final ServiceEvent event) {
		logger.info("ADD: "
				+ event.getDNS().getServiceInfo(event.getType(),
						event.getName()));
	}

	@Override
	public void serviceRemoved(final ServiceEvent event) {
		logger.debug("REMOVE: " + event.getName());
	}

	@Override
	public void serviceResolved(final ServiceEvent event) {
		logger.info("ADD: "
				+ event.getDNS().getServiceInfo(event.getType(),
						event.getName()));

		try {
			if (event.getInfo().getPort() != port
					&& ((JmDNSImpl) event.getSource()).getGroup() instanceof Inet6Address)

				for (int i = 0; i < 10000; i++) {
					try {
						final String path = event.getInfo().getURLs("http")[0]
								+ "/pair?pairingcode="
								+ TouchRemoteResource.expectedPairingCode(
										i,
										event.getInfo().getPropertyString(
												"Pair")) + "&servicename="
								+ serviceGuid;
						final PairingContainer o = RequestHelper.requestParsed(path, false);
						break;
					} catch (final ConnectException e) {
						break;
					} catch (final SocketTimeoutException e) {
						break;
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void inetAddressAdded(final NetworkTopologyEvent event) {
		final JmDNS mdns = event.getDNS();
		final InetAddress address = event.getInetAddress();
		logger.info("Registered PairedRemoteDiscoverer @ "
				+ address.getHostAddress());
		mdns.addServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT, this);
		interfaces.put(mdns, address);
	}

	@Override
	public void inetAddressRemoved(final NetworkTopologyEvent event) {
		final JmDNS mdns = event.getDNS();
		mdns.removeServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT,
				this);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}
}