/*******************************************************************************
 * Copyright (c) 2012 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - initial API and implementation
 ******************************************************************************/
package org.dyndns.jkiddo.dacp.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.dmap.MDNSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tunesremote.IDatabase;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

@Singleton
public class PairingResource extends MDNSResource implements IPairingResource
{
	public static final String DACP_CLIENT_PORT_NAME = "DACP_CLIENT_PORT_NAME";

	static Logger logger = LoggerFactory.getLogger(PairingResource.class);

	private IDatabase database;

	@Inject
	public PairingResource(JmmDNS mDNS, @Named(DACP_CLIENT_PORT_NAME) Integer port, IDatabase database) throws IOException
	{
		super(mDNS, port);
		this.database = database;
	}

	public final static String DEVICE_ID = "0000000000000000000000000000000000000010";

	private static final byte[] PAIRING_RAW = new byte[] { 0x63, 0x6d, 0x70, 0x61, 0x00, 0x00, 0x00, 0x3a, 0x63, 0x6d, 0x70, 0x67, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x63, 0x6d, 0x6e, 0x6d, 0x00, 0x00, 0x00, 0x16, 0x41, 0x64, 0x6d, 0x69, 0x6e, 0x69, 0x73, 0x74, 0x72, 0x61, 0x74, 0x6f, 0x72, (byte) 0xe2, (byte) 0x80, (byte) 0x99, 0x73, 0x20, 0x69, 0x50, 0x6f, 0x64, 0x63, 0x6d, 0x74, 0x79, 0x00, 0x00, 0x00, 0x04, 0x69, 0x50, 0x6f, 0x64 };

	public static String byteArrayToHex(byte[] a)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString().toUpperCase();
	}

	@Override
	@GET
	@Path("pair")
	public Response pair(@Context UriInfo uri, @Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("pairingcode") String pairingcode, @QueryParam("servicename") String servicename) throws IOException
	{
		logger.debug("pairingcode: " + pairingcode);
		byte[] code = new byte[8];
		new Random().nextBytes(code);
		System.arraycopy(code, 0, PAIRING_RAW, 16, 8);

		database.updateCode(servicename, byteArrayToHex(code));

		return new ResponseBuilderImpl().entity(PAIRING_RAW).status(Status.OK).build();
	}

	@Override
	protected ServiceInfo getServiceToRegister()
	{
		final Map<String, String> values = new HashMap<String, String>();
		values.put("DvNm", "Use 5309 as code for " + Jolivia.name);
		values.put("RemV", "10000");
		values.put("DvTy", "iPod");
		values.put("RemN", "Remote");
		values.put("txtvers", "1");
		values.put("Pair", "0000000000000001");

		return ServiceInfo.create(REMOTE_TYPE, DEVICE_ID, this.port, 0, 0, values);
	}
}
