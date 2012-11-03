package org.dyndns.jkiddo.dacp.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public interface IPairingResource
{

	@GET
	@Path("pair")
	Response pair(@QueryParam("pairingcode") String pairingcode, @QueryParam("servicename") String servicename);

}