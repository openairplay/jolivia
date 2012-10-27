package org.dyndns.jkiddo;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.inject.Singleton;

@Provider
@Singleton
public class JoliviaExceptionMapper implements ExceptionMapper<Exception>
{

	@Override
	public Response toResponse(Exception exception)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
