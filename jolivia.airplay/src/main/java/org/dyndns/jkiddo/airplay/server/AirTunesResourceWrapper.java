package org.dyndns.jkiddo.airplay.server;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;

import com.google.common.collect.Maps;

public class AirTunesResourceWrapper extends MDNSResource
{
	public final static String AIRPLAY = "_airplay._tcp.local.";

	public static final String AIRPLAY_PORT_NAME = "AIRPLAY_PORT_NAME";

	private final String name;

	@Inject
	public AirTunesResourceWrapper(final IZeroconfManager mDNS, @Named(AIRPLAY_PORT_NAME) final Integer port, @Named(Util.APPLICATION_NAME) final String applicationName) throws IOException
	{
		super(mDNS, port);
		this.name = applicationName;
		this.register();
	}

	// https://code.google.com/p/open-airplay/wiki/Protocol
	// http://www.tuaw.com/2010/12/08/dear-aunt-tuaw-can-i-airplay-to-my-mac/
	// http://nto.github.io/AirPlay.html
	// http://redragger.wordpress.com/2011/02/09/using-java-to-mimic-an-airplay-server/
	@Override
	protected IZeroconfManager.ServiceInfo getServiceInfoToRegister()
	{
		final Map<String, String> map = Maps.newHashMap();
		map.put("deviceid", Util.toMacString(Util.getHardwareAddress()));
		map.put("features", "0x77");
		map.put("model", "Jolivia,1");
		map.put("srcvers", "150.33");
		map.put("rhd", "1.9.0");
		map.put("vv", "1");

		return new IZeroconfManager.ServiceInfo(AIRPLAY, name, this.port, map);
	}
}
