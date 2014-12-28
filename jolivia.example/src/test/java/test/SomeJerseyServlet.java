package test;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;

@Path("/")
@Singleton
public class SomeJerseyServlet implements ITouchRemoteResource {

	private final ITouchRemoteResource touchRemoteResource;

	@Inject
	public SomeJerseyServlet(final ITouchRemoteResource touchRemoteResource) {
		this.touchRemoteResource = touchRemoteResource;
	}

	@Override
	@GET
	@Path("pair")
	public Response pair(@Context final HttpServletRequest httpServletRequest, @Context final HttpServletResponse httpServletResponse, @QueryParam("pairingcode") final String pairingcode, @QueryParam("servicename") final String servicename) throws IOException {
		return this.touchRemoteResource.pair(httpServletRequest, httpServletResponse, pairingcode, servicename);
	}
	
	
}
