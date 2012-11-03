package org.dyndns.jkiddo.daap.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

public interface ILibraryResource
{

	@Path("/server-info")
	@GET
	Response serverInfo() throws IOException;

	@Path("/login")
	@GET
	Response login(@Context HttpServletRequest httpServletRequest) throws IOException;

	@Path("/update")
	@GET
	Response update(@QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @Context HttpServletRequest httpServletRequest) throws IOException;

	@Path("/databases")
	@GET
	Response databases(@QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta) throws IOException;

	@Path("/databases/{databaseId}/items")
	@GET
	Response songs(@PathParam("databaseId") String databaseId, @QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @QueryParam("meta") String meta) throws Exception;

	@Path("/databases/{databaseId}/containers")
	@GET
	Response playlists(@PathParam("databaseId") String databaseId, @QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @QueryParam("meta") String meta) throws IOException;

	@Path("/databases/{databaseId}/containers/{containerId}/items")
	@GET
	Response playlistSongs(@PathParam("containerId") String containerId, @PathParam("databaseId") String databaseId, @QueryParam("session-id") String sessionId, @QueryParam("revision-number") String revisionNumber, @QueryParam("delta") String delta, @QueryParam("meta") String meta) throws IOException;

	@Path("/content-codes")
	@GET
	Response contentCodes() throws IOException;

	@Path("/logout")
	@GET
	Response logout();

	@Path("/resolve")
	@GET
	Response resolve();

	@Path("/databases/{databaseId}/items/{itemId}.{format}")
	@GET
	Response song(@PathParam("databaseId") String databaseId, @PathParam("itemId") String itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws Exception;

}