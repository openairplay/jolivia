package org.dyndns.jkiddo.jetty;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Provider
@Singleton
public class JoliviaExceptionMapper implements ExceptionMapper<Throwable>
{
	final static Logger logger = LoggerFactory.getLogger(JoliviaExceptionMapper.class);

	@Override
	public Response toResponse(Throwable exception)
	{
		logger.warn(exception.getMessage(), exception);
		return null;
	}

}
