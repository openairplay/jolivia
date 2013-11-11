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

import java.io.IOException;
import java.util.Enumeration;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ProxyFilter implements Filter
{
	static Logger logger = LoggerFactory.getLogger(ProxyFilter.class);

	//http://simplapi.wordpress.com/2013/01/24/jersey-jax-rs-implements-a-http-basic-auth-decoder/
		
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String queryString = httpServletRequest.getQueryString();
		if(queryString == null)
			queryString = "";
		else
			queryString = "?" + queryString;

		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		while(headerNames.hasMoreElements())
		{
			String headerName = headerNames.nextElement();
			logger.info((headerName + ": " + httpServletRequest.getHeader(headerName)));
		}

		logger.info(httpServletRequest.getRequestURI() + queryString);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

}
