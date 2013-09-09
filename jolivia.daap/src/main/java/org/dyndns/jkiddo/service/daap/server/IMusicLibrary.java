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
package org.dyndns.jkiddo.service.daap.server;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.dyndns.jkiddo.service.dmap.ILibraryResource;

public interface IMusicLibrary extends ILibraryResource
{
	public static final String DAAP_SERVICE_TYPE = "_daap._tcp.local.";

	@Path("databases/{databaseId}/items/{itemId}.{format}")
	@GET
	public Response item(@PathParam("databaseId") long databaseId, @PathParam("itemId") long itemId, @PathParam("format") String format, @HeaderParam("Range") String rangeHeader) throws IOException;

	@Path("databases/{databaseId}/groups")
	@GET
	public Response groups(@PathParam("databaseId") long databaseId, @QueryParam("meta") String meta, @QueryParam("type") String type, @QueryParam("group-type") String groupType, @QueryParam("sort") String sort, @QueryParam("include-sort-headers") long includeSortHeaders, @QueryParam("query") String query, @QueryParam("session-id") long sessionId, @QueryParam("hsgid") String hsgid) throws IOException;

	// @Path("databases/{databaseId}/groups/{groupdId}/extra_data/artwork")
	@Path("databases/{databaseId}/items/{groupdId}/extra_data/artwork")
	@GET
	public Response artwork(@PathParam("databaseId") long databaseId, @PathParam("groupId") long groupId, @QueryParam("session-id") long sessionId, @QueryParam("mw") String mw, @QueryParam("mh") String mh, @QueryParam("group-type") String group_type, @QueryParam("daapSecInfo") String daapSecInfo) throws IOException;
}
