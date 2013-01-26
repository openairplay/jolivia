package org.dyndns.jkiddo.raop.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.dmap.service.MDNSResource;
import org.dyndns.jkiddo.raop.server.AirReceiver.RaopRtspPipelineFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AirPlayResourceWrapper extends MDNSResource
{
	private static final Logger logger = LoggerFactory.getLogger(AirPlayResourceWrapper.class);

	public static final String RAOP_SERVICE_TYPE = "_raop._tcp.local.";
	public static final String RAOP_PORT_NAME = "RAOP_PORT_NAME";

	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final ChannelGroup allChannels = new DefaultChannelGroup();
	private final ExecutionHandler channelExecutionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(4, 0, 0));

	private final byte[] hardwareAddressBytes = getHardwareAddress();
	private final String hardwareAddressString = toHexString(hardwareAddressBytes);

	@Inject
	public AirPlayResourceWrapper(JmmDNS mDNS, @Named(RAOP_PORT_NAME) Integer port) throws IOException
	{
		super(mDNS, port);
		this.signUp();
		SimpleChannelUpstreamHandler channel = new SimpleChannelUpstreamHandler() {
			@Override
			public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception
			{
				allChannels.add(e.getChannel());
				super.channelOpen(ctx, e);
			}
		};

		final ServerBootstrap airTunesRtspBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(executorService, executorService));
		airTunesRtspBootstrap.setPipelineFactory(new RaopRtspPipelineFactory(hardwareAddressBytes, channelExecutionHandler, executorService, channel));
		airTunesRtspBootstrap.setOption("reuseAddress", true);
		airTunesRtspBootstrap.setOption("child.tcpNoDelay", true);
		airTunesRtspBootstrap.setOption("child.keepAlive", true);
		allChannels.add(airTunesRtspBootstrap.bind(new InetSocketAddress(Inet4Address.getByName("0.0.0.0"), this.port)));
	}
	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		Map<String, String> map = Maps.newHashMap();
		map.put("txtvers", "1");
		map.put("tp", "UDP");
		map.put("ch", "2");
		map.put("ss", "16");
		map.put("sr", "44100");
		map.put("pw", "false");
		map.put("sm", "false");
		map.put("sv", "false");
		map.put("ek", "1");
		map.put("et", "0,1");
		map.put("cn", "0,1");
		map.put("vn", "3");

		return ServiceInfo.create(RAOP_SERVICE_TYPE, hardwareAddressString + "@" + Jolivia.name + "@" + hostname, this.port, 0 /* weight */, 0 /* priority */, map);
	}

	@Override
	protected void cleanup()
	{
		super.cleanup();
		allChannels.close().awaitUninterruptibly();
		executorService.shutdown();
		channelExecutionHandler.releaseExternalResources();
	}

	/**
	 * Converts an array of bytes to a hexadecimal string
	 * 
	 * @param bytes
	 *            array of bytes
	 * @return hexadecimal representation
	 */
	private static String toHexString(final byte[] bytes)
	{
		final StringBuilder s = new StringBuilder();
		for(final byte b : bytes)
		{
			final String h = Integer.toHexString(0x100 | b);
			s.append(h.substring(h.length() - 2, h.length()).toUpperCase());
		}
		return s.toString();
	}

	/**
	 * Returns a suitable hardware address.
	 * 
	 * @return a MAC address
	 */
	private static byte[] getHardwareAddress()
	{
		try
		{
			/* Search network interfaces for an interface with a valid, non-blocked hardware address */
			for(final NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces()))
			{
				if(iface.isLoopback())
					continue;
				if(iface.isPointToPoint())
					continue;

				try
				{
					final byte[] ifaceMacAddress = iface.getHardwareAddress();
					if((ifaceMacAddress != null) && (ifaceMacAddress.length == 6) && !isBlockedHardwareAddress(ifaceMacAddress))
					{
						logger.info("Hardware address is " + toHexString(ifaceMacAddress) + " (" + iface.getDisplayName() + ")");
						return Arrays.copyOfRange(ifaceMacAddress, 0, 6);
					}
				}
				catch(final Throwable e)
				{
					/* Ignore */
				}
			}
		}
		catch(final Throwable e)
		{
			/* Ignore */
		}

		/* Fallback to the IP address padded to 6 bytes */
		try
		{
			final byte[] hostAddress = Arrays.copyOfRange(InetAddress.getLocalHost().getAddress(), 0, 6);
			logger.info("Hardware address is " + toHexString(hostAddress) + " (IP address)");
			return hostAddress;
		}
		catch(final Throwable e)
		{
			/* Ignore */
		}

		/* Fallback to a constant */
		logger.info("Hardware address is 00DEADBEEF00 (last resort)");
		return new byte[] { (byte) 0x00, (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF, (byte) 0x00 };
	}

	/**
	 * Decides whether or nor a given MAC address is the address of some virtual interface, like e.g. VMware's host-only interface (server-side).
	 * 
	 * @param addr
	 *            a MAC address
	 * @return true if the MAC address is unsuitable as the device's hardware address
	 */
	public static boolean isBlockedHardwareAddress(final byte[] addr)
	{
		if((addr[0] & 0x02) != 0)
			/* Locally administered */
			return true;
		else if((addr[0] == 0x00) && (addr[1] == 0x50) && (addr[2] == 0x56))
			/* VMware */
			return true;
		else if((addr[0] == 0x00) && (addr[1] == 0x1C) && (addr[2] == 0x42))
			/* Parallels */
			return true;
		else if((addr[0] == 0x00) && (addr[1] == 0x25) && (addr[2] == (byte) 0xAE))
			/* Microsoft */
			return true;
		else
			return false;
	}

}
