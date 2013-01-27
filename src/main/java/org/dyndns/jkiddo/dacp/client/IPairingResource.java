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
package org.dyndns.jkiddo.dacp.client;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public interface IPairingResource
{
	public final static String REMOTE_TYPE = "_touch-remote._tcp.local.";

	@GET
	@Path("pair")
	Response pair(@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse, @QueryParam("pairingcode") String pairingcode, @QueryParam("servicename") String servicename) throws IOException;

}
