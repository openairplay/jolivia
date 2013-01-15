package org.dyndns.jkiddo.dpap.server;

import java.io.IOException;
import java.util.HashMap;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.dmap.service.MDNSResource;
import org.dyndns.jkiddo.protocol.dmap.DmapUtil;

import com.google.inject.name.Named;

public class ImageServer extends MDNSResource implements IImageServer
{
	public static final String DPAP_SERVER_PORT_NAME = "DPAP_SERVER_PORT_NAME";

	private static final String TXT_VERSION = "1";
	private static final String TXT_VERSION_KEY = "txtvers";
	private static final String DPAP_VERSION_KEY = "Version";
	private static final String MACHINE_ID_KEY = "Machine ID";
	private static final String IPSH_VERSION_KEY = "iPSh Version";
	private static final String PASSWORD_KEY = "Password";
	public static final String DAAP_PORT_NAME = "DAAP_PORT_NAME";

	public ImageServer(JmmDNS mDNS, @Named(DPAP_SERVER_PORT_NAME) Integer port) throws IOException
	{
		super(mDNS, port);
	}

	@Override
	protected ServiceInfo getServiceInfoToRegister()
	{
		String hash = Integer.toHexString(hostname.hashCode()).toUpperCase();
		hash = (hash + hash).substring(0, 13);
		HashMap<String, String> records = new HashMap<String, String>();
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(DPAP_VERSION_KEY, DmapUtil.DPAP_VERSION_1 + "");
		records.put(IPSH_VERSION_KEY, 0x20000 + "");
		records.put(MACHINE_ID_KEY, hash);
		records.put(PASSWORD_KEY, "0");
		return ServiceInfo.create(DPAP_SERVICE_TYPE, Jolivia.name, port, 0, 0, records);
	}

}
