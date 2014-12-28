package org.dyndns.jkiddo.zeroconf;

import java.io.IOException;

import javax.jmdns.JmmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.NetworkTopologyListener;
import javax.jmdns.ServiceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeroconfManagerImpl implements IZeroconfManager, NetworkTopologyListener
{
	static final Logger logger = LoggerFactory.getLogger(ZeroconfManagerImpl.class);

	private final JmmDNS mdns;
	private javax.jmdns.ServiceInfo info;

	public ZeroconfManagerImpl()
	{
		mdns = JmmDNS.Factory.getInstance();
	}

	@Override
	public void registerService(final ServiceInfo serviceInfo) throws IOException
	{
		info = IZeroconfManager.ServiceInfo.toServiceInfo(serviceInfo);
		mdns.registerService(info);
		mdns.addNetworkTopologyListener(this);
	}

	@Override
	public void unregisterService(final ServiceInfo serviceInfo)
	{
		mdns.unregisterService(IZeroconfManager.ServiceInfo.toServiceInfo(serviceInfo));
	}

	@Override
	public void unregisterAllServices()
	{
		mdns.unregisterAllServices();
	}

	@Override
	public synchronized void inetAddressAdded(final NetworkTopologyEvent event)
	{
		try
		{
			event.getDNS().registerService(info);
		}
		catch(final IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public synchronized void inetAddressRemoved(final NetworkTopologyEvent event)
	{
		event.getDNS().unregisterService(info);
	}

	@Override
	public void addServiceListener(final String type, final ServiceListener listener)
	{
		this.mdns.addServiceListener(type, listener);
	}

	@Override
	public void addNetworkTopologyListener(final NetworkTopologyListener listener)
	{
		this.mdns.addNetworkTopologyListener(listener);
	}

	@Override
	public void close() throws IOException
	{
		this.mdns.close();
	}
}
