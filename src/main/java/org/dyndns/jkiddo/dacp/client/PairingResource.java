package org.dyndns.jkiddo.dacp.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.dmap.MDNSResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

@Singleton
public class PairingResource extends MDNSResource implements IPairingResource
{
	/**
	 * 
	 */
	public static final String DACP_CLIENT_PORT_NAME = "DACP_CLIENT_PORT_NAME";

	static Logger logger = LoggerFactory.getLogger(PairingResource.class);

	@Inject
	public PairingResource(JmmDNS mDNS, @Named(DACP_CLIENT_PORT_NAME) Integer port) throws IOException
	{
		super(mDNS, port);
	}

	public final static String REMOTE_TYPE = "_touch-remote._tcp.local.";
	public final static String DEVICE_ID = "0000000000000000000000000000000000000010";

	private static final byte[] CHAR_TABLE = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };
	private static final byte[] PAIRING_RAW = new byte[] { 0x63, 0x6d, 0x70, 0x61, 0x00, 0x00, 0x00, 0x3a, 0x63, 0x6d, 0x70, 0x67, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x63, 0x6d, 0x6e, 0x6d, 0x00, 0x00, 0x00, 0x16, 0x41, 0x64, 0x6d, 0x69, 0x6e, 0x69, 0x73, 0x74, 0x72, 0x61, 0x74, 0x6f, 0x72, (byte) 0xe2, (byte) 0x80, (byte) 0x99, 0x73, 0x20, 0x69, 0x50, 0x6f, 0x64, 0x63, 0x6d, 0x74, 0x79, 0x00, 0x00, 0x00, 0x04, 0x69, 0x50, 0x6f, 0x64 };
	private final Random random = new Random();
	private String guidCode;

	public String getNegotiatedGUID()
	{
		return guidCode;
	}

	private static String toHex(byte[] code)
	{
		// somewhat borrowed from rgagnon.com
		byte[] result = new byte[2 * code.length];
		int index = 0;
		for(byte b : code)
		{
			int v = b & 0xff;
			result[index++] = CHAR_TABLE[v >>> 4];
			result[index++] = CHAR_TABLE[v & 0xf];
		}
		return new String(result);
	}

	@Override
	@GET
	@Path("pair")
	public Response pair(@QueryParam("pairingcode") String pairingcode, @QueryParam("servicename") String servicename)
	{
		logger.debug("pairingcode: " + pairingcode);
		logger.debug("servicename: " + servicename);
		byte[] code = new byte[8];
		random.nextBytes(code);
		System.arraycopy(code, 0, PAIRING_RAW, 16, 8);
		guidCode = toHex(code);

		ResponseBuilder response = new ResponseBuilderImpl();
		response.entity(PAIRING_RAW);
		response.status(Status.OK);
		return response.build();
	}

	@Override
	protected ServiceInfo registerServerRemote()
	{
		final Map<String, String> values = new HashMap<String, String>();
		values.put("DvNm", "Use 5309 as code for " + Jolivia.name);
		values.put("RemV", "10000");
		values.put("DvTy", "JKiddo Inc");
		values.put("RemN", Jolivia.name + " Remote");
		values.put("txtvers", "1");
		values.put("Pair", "0000000000000001");

		return ServiceInfo.create(REMOTE_TYPE, DEVICE_ID, this.port, 0, 0, values);
	}
}
