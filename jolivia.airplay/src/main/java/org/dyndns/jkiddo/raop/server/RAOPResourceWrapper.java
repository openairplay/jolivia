package org.dyndns.jkiddo.raop.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;

import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.raop.server.AirReceiver.RaopRtspPipelineFactory;
import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RAOPResourceWrapper extends MDNSResource
{
	public static final String RAOP_PORT_NAME = "RAOP_PORT_NAME";

	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final ChannelGroup allChannels = new DefaultChannelGroup();
	private final ExecutionHandler channelExecutionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(4, 0, 0));

	private final byte[] hardwareAddressBytes = Util.getHardwareAddress();
	private final String hardwareAddressString = Util.toHexString(Util.getHardwareAddress());

	private final String name;

	@Inject
	public RAOPResourceWrapper(JmmDNS mDNS, @Named(RAOP_PORT_NAME) Integer port, @Named(Util.APPLICATION_NAME) String applicationName) throws IOException
	{
		super(mDNS, port);
		this.name = applicationName;
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

	// http://nto.github.io/AirPlay.html
	// http://cocoadev.com/wiki/AirTunesRendezvous
	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		Map<String, String> map = Maps.newHashMap();

		map.put("am", "Jolivia");
		// map.put("am", "AirPort4,107");
		map.put("ch", "2");
		map.put("cn", "0,1");
		map.put("da", "true");
		map.put("ek", "1");
		map.put("et", "0,1");
		map.put("md", "0,1,2");
		// map.put("fv", "76300.7");
		map.put("pw", "false");
		map.put("sm", "false");
		// map.put("sf", "0x1");
		map.put("sr", "44100");
		map.put("ss", "16");
		// map.put("sv", "false");
		map.put("tp", "UDP");
		// map.put("tp", "TCP,UDP");
		map.put("txtvers", "1");
		map.put("vn", "65537");
		map.put("vs", "105.1");

		// ...

		// map.put("txtvers", "1");
		// map.put("tp", "UDP");
		// map.put("ch", "2");
		// map.put("ss", "16");
		// map.put("sr", "44100");
		// map.put("pw", "false");
		// map.put("sm", "false");
		// map.put("sv", "false");
		// map.put("ek", "1");
		// map.put("et", "0,1");
		// map.put("cn", "0,1");
		// map.put("vn", "3");

		return ServiceInfo.create(ISpeakerListener.RAOP_TYPE, hardwareAddressString + "@" + name, this.port, 0, 0, map);
	}

	@Override
	protected void cleanup()
	{
		super.cleanup();
		allChannels.close().awaitUninterruptibly();
		executorService.shutdown();
		channelExecutionHandler.releaseExternalResources();
	}
}
