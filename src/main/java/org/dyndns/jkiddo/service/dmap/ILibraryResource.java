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
package org.dyndns.jkiddo.service.dmap;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface ILibraryResource
{

	@Path("server-info")
	@GET
	public Response serverInfo(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info) throws IOException;

	@Path("login")
	@GET
	public Response login(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException;

	@Path("update")
	@GET
	public Response update(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @Context UriInfo info, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("daap-no-disconnect") int daapNoDisconnect) throws IOException;

	@Path("databases")
	@GET
	public Response databases(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta) throws IOException;

	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers) throws Exception;

	@Path("databases/{databaseId}/items")
	@GET
	public Response items(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("type") String type, @QueryParam("meta") String meta, @QueryParam("query") String query) throws Exception;

	@Path("databases/{databaseId}/containers")
	@GET
	public Response containers(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta) throws IOException;

	@Path("databases/{databaseId}/containers/{containerId}/items")
	@GET
	public Response containerItems(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @PathParam("containerId") long containerId, @PathParam("databaseId") long databaseId, @QueryParam("session-id") long sessionId, @QueryParam("revision-number") long revisionNumber, @QueryParam("delta") long delta, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String group_type, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") String include_sort_headers, @QueryParam("query") String query, @QueryParam("index") String index) throws IOException;

	@Path("content-codes")
	@GET
	public Response contentCodes(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) throws IOException;

	@Path("logout")
	@GET
	public Response logout(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("session-id") long sessionId);
}
