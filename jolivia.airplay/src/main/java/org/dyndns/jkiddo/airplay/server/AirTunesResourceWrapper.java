package org.dyndns.jkiddo.airplay.server;

import java.io.IOException;
import java.util.Map;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;

import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AirTunesResourceWrapper extends MDNSResource
{
	public final static String AIRPLAY = "_airplay._tcp.local.";
	
	private static final Logger logger = LoggerFactory.getLogger(AirTunesResourceWrapper.class);

	public static final String AIRPLAY_PORT_NAME = "AIRPLAY_PORT_NAME";

	private final String name;

	@Inject
	public AirTunesResourceWrapper(JmmDNS mDNS, @Named(AIRPLAY_PORT_NAME) Integer port, @Named(Util.APPLICATION_NAME) String applicationName) throws IOException
	{
		super(mDNS, port);
		this.name = applicationName;
		this.signUp();
	}
	
	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		Map<String, String> map = Maps.newHashMap();
		map.put("deviceid", Util.toMacString(Util.getHardwareAddress()));
		map.put("features", "0x100029ff");
		map.put("model", "AppleTV3,1");
		map.put("srcvers", "150.33");
		map.put("rhd", "1.9.0");
		map.put("vv", "1");

		return ServiceInfo.create(AIRPLAY, name, this.port, 0, 0, map);
	}

	@Override
	protected void cleanup()
	{
		super.cleanup();
	}
}
