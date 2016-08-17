/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
package org.dyndns.jkiddo.service.dacp.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.jmdns.JmDNS;
import javax.jmdns.NetworkTopologyEvent;
import javax.jmdns.ServiceEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dyndns.jkiddo.IDiscoverer;
import org.dyndns.jkiddo.dmcp.chunks.media.DeviceName;
import org.dyndns.jkiddo.dmcp.chunks.media.DeviceType;
import org.dyndns.jkiddo.dmcp.chunks.media.PairingContainer;
import org.dyndns.jkiddo.dmcp.chunks.media.PairingGuid;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

@Singleton
public class TouchRemoteResource extends MDNSResource implements ITouchRemoteResource, IDiscoverer
{
	public static final String DACP_CLIENT_PORT_NAME = "DACP_CLIENT_PORT_NAME";
	public static final String DACP_CLIENT_PAIRING_CODE = "DACP_CLIENT_PAIRING_CODE";

	private static final Logger LOGGER = LoggerFactory.getLogger(TouchRemoteResource.class);

	private final IPairingDatabase database;
	private final Integer actualCode;
	private final String name;
	private final IZeroconfManager mDNS;

	private static MessageDigest md5;

	static
	{
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch(final NoSuchAlgorithmException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Inject
	public TouchRemoteResource(final IZeroconfManager mDNS, @Named(DACP_CLIENT_PORT_NAME) final Integer port, final IPairingDatabase database, @Named(DACP_CLIENT_PAIRING_CODE) final Integer code, @Named(Util.APPLICATION_NAME) final String applicationName) throws IOException
	{
		super(mDNS, port);
		if(code < 0 || code > 9999)
			throw new RuntimeException("Pairing code is not within required interval");
		this.mDNS = mDNS;
		this.actualCode = code;
		this.database = database;
		this.name = applicationName;
		this.mDNS.addServiceListener(ITouchAbleServerResource.TOUCH_ABLE_SERVER, this);
		this.mDNS.addNetworkTopologyListener(this);
		this.register();
	}

	@Override
	@GET
	@Path("pair")
	public Response pair(@Context final HttpServletRequest httpServletRequest, @Context final HttpServletResponse httpServletResponse, @QueryParam("pairingcode") final String pairingcode, @QueryParam("servicename") final String servicename) throws IOException
	{
		final byte[] code = new byte[] { 0, 0, 0, 0, 0, 0, 0, 1 };

		final String match = expectedPairingCode(actualCode, database.getPairCode());
		if(match.equals(pairingcode))
		{
			database.updateCode(servicename, Util.toHex(code));

			final PairingContainer pc = new PairingContainer();
			pc.add(new PairingGuid(code));
			pc.add(new DeviceName("Jolivia’s iPhone"));
			pc.add(new DeviceType("iPhone"));

			return new ResponseBuilderImpl().entity(DmapUtil.serialize(pc, false)).status(Status.OK).build();
		}

		// TODO Response is not verified to be correct in iTunes regi - it is however better than nothing.
		return new ResponseBuilderImpl().status(Status.UNAUTHORIZED).build();
	}

	@Override
	protected IZeroconfManager.ServiceInfo getServiceInfoToRegister()
	{
		final Map<String, String> values = new HashMap<>();
		values.put("DvNm", "Use " + actualCode + " as code for " + name);
		values.put("RemV", "10000");
		values.put("DvTy", "iPod");
		values.put("RemN", "Remote");
		values.put("txtvers", "1");
		values.put("Pair", database.getPairCode());

		return new IZeroconfManager.ServiceInfo(TOUCH_REMOTE_CLIENT, Util.toHex("JoliviaRemote"), this.port, values);
	}

	public static String expectedPairingCode(final Integer actualCode, final String databaseCode) throws IOException
	{
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(databaseCode.getBytes("UTF-8"));

		final byte[] codeAsBytes = String.format("%04d", actualCode).getBytes("UTF-8");
		for (final byte codeAsByte : codeAsBytes) {
			os.write(codeAsByte);
			os.write(0);
		}

		return Util.toHex(md5.digest(os.toByteArray()));
	}

	@Override
	public void serviceAdded(final ServiceEvent event)
	{}

	@Override
	public void serviceRemoved(final ServiceEvent event)
	{
		LOGGER.info("REMOVE: " + event.getDNS().getServiceInfo(event.getType(), event.getName()));
		try
		{
			final String code = database.findCode(event.getInfo().getName());
			if(code != null)
			{
				database.updateCode(event.getInfo().getName(), null);
				// Seems like someone removed our pairing ... make a re-announcement
				this.register();
			}
		}
		catch(final IOException e)
		{
			LOGGER.debug(e.getMessage(), e);
		}
	}

	@Override
	public void serviceResolved(final ServiceEvent event)
	{}

	@Override
	public void inetAddressAdded(final NetworkTopologyEvent event)
	{
		final JmDNS mdns = event.getDNS();
		final InetAddress address = event.getInetAddress();
		LOGGER.info("Registered PairedRemoteDiscoverer @ " + address.getHostAddress());
		mdns.addServiceListener(ITouchAbleServerResource.TOUCH_ABLE_SERVER, this);
	}

	@Override
	public void inetAddressRemoved(final NetworkTopologyEvent event)
	{
		final JmDNS mdns = event.getDNS();
		mdns.removeServiceListener(ITouchAbleServerResource.TOUCH_ABLE_SERVER, this);
		mdns.unregisterAllServices();
	}
}
