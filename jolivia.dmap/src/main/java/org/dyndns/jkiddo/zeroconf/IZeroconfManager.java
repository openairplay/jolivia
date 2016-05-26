package org.dyndns.jkiddo.zeroconf;

import java.io.IOException;
import java.util.Map;

import javax.jmdns.NetworkTopologyListener;
import javax.jmdns.ServiceListener;

public interface IZeroconfManager
{
	void registerService(ServiceInfo serviceInfo) throws IOException;
	void unregisterService(ServiceInfo serviceInfo);
	void unregisterAllServices();
	void addServiceListener(String type, ServiceListener listener);
	void addNetworkTopologyListener(NetworkTopologyListener listener);
	void close() throws IOException;

	class ServiceInfo
	{
		private final String type;
		private final String name;
		private final int port;
		private final Map<String, String> props;

		public ServiceInfo(final String type, final String name, final int port, final Map<String, String> props)
		{
			super();
			this.type = type;
			this.name = name;
			this.port = port;
			this.props = props;
		}

		public static javax.jmdns.ServiceInfo toServiceInfo(final ServiceInfo serviceInfo)
		{
			return javax.jmdns.ServiceInfo.create(serviceInfo.getType(), serviceInfo.getName(), serviceInfo.getPort(), 0, 0, serviceInfo.getProps());
		}

		public String getType()
		{
			return type;
		}
		public String getName()
		{
			return name;
		}
		public int getPort()
		{
			return port;
		}
		public Map<String, String> getProps()
		{
			return props;
		}
	}
}
