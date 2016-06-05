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
package org.dyndns.jkiddo.jetty;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

@Provider
@Singleton
public class JoliviaExceptionMapper implements ExceptionMapper<Throwable>
{
	private final static Logger logger = LoggerFactory.getLogger(JoliviaExceptionMapper.class);

	@Override
	public Response toResponse(Throwable exception)
	{
		logger.warn(exception.getMessage(), exception);
		return new ResponseBuilderImpl().status(Status.INTERNAL_SERVER_ERROR).build();
	}
}
