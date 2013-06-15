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
import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.ServiceEvent;
import javax.jmdns.impl.JmDNSImpl;

import org.dyndns.jkiddo.IDiscoverer;
import org.dyndns.jkiddo.dmap.chunks.control.PairingContainer;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.client.TouchRemoteResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnpairedRemoteCrawler implements IDiscoverer {
	public static final Logger logger = LoggerFactory
			.getLogger(UnpairedRemoteCrawler.class);

	public static final String SERVICE_PORT_NAME = "SERVICE_PORT_NAME";

	private final JmmDNS mDNS;
	private final Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();

	private Integer port;

	private String serviceGuid;

	@Inject
	public UnpairedRemoteCrawler(JmmDNS mDNS,
			IClientSessionListener clientSessionListener,
			@Named(SERVICE_PORT_NAME) Integer port, @Named(Util.APPLICATION_NAME) String name) {
		this.mDNS = mDNS;
		this.port = port;
		this.serviceGuid = Util.toServiceGuid(name);
		this.mDNS.addServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT,
				this);
		this.mDNS.addNetworkTopologyListener(this);
		
	}

	@Override
	public void serviceAdded(ServiceEvent event) {
		logger.info("ADD: "
				+ event.getDNS().getServiceInfo(event.getType(),
						event.getName()));
	}

	@Override
	public void serviceRemoved(ServiceEvent event) {
		logger.debug("REMOVE: " + event.getName());
	}

	@Override
	public void serviceResolved(ServiceEvent event) {
		logger.info("ADD: "
				+ event.getDNS().getServiceInfo(event.getType(),
						event.getName()));

		try {
			if (event.getInfo().getPort() != port
					&& ((JmDNSImpl) event.getSource()).getGroup() instanceof Inet6Address)

				for (int i = 0; i < 10000; i++) {
					try {
						String path = event.getInfo().getURLs("http")[0]
								+ "/pair?pairingcode="
								+ TouchRemoteResource.expectedPairingCode(
										i,
										event.getInfo().getPropertyString(
												"Pair")) + "&servicename="
								+ serviceGuid;
						PairingContainer o = RequestHelper.requestParsed(path, false);
						break;
					} catch (ConnectException e) {
						break;
					} catch (SocketTimeoutException e) {
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void inetAddressAdded(NetworkTopologyEvent event) {
		JmDNS mdns = event.getDNS();
		InetAddress address = event.getInetAddress();
		logger.info("Registered PairedRemoteDiscoverer @ "
				+ address.getHostAddress());
		mdns.addServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT, this);
		interfaces.put(mdns, address);
	}

	@Override
	public void inetAddressRemoved(NetworkTopologyEvent event) {
		JmDNS mdns = event.getDNS();
		mdns.removeServiceListener(ITouchRemoteResource.TOUCH_REMOTE_CLIENT,
				this);
		mdns.unregisterAllServices();
		interfaces.remove(mdns);
	}
}