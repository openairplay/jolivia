package org.dyndns.jkiddo.service.dmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.google.inject.Singleton;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

@Provider
@Singleton
@Produces({ "application/octet-stream", "*/*", Util.APPLICATION_X_DMAP_TAGGED })
@Consumes({ "application/octet-stream", "*/*", Util.APPLICATION_X_DMAP_TAGGED })
public class CustomByteArrayProvider extends AbstractMessageReaderWriterProvider<byte[]>
{
	public boolean supports(Class type)
	{
		return type == byte[].class;
	}

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type == byte[].class;
	}

	public byte[] readFrom(Class<byte[]> type, Type genericType, Annotation annotations[], MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeTo(entityStream, out);
		return out.toByteArray();
	}

	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type == byte[].class;
	}

	public void writeTo(byte[] t, Class<?> type, Type genericType, Annotation annotations[], MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException
	{
		entityStream.write(t);
	}

	@Override
	public long getSize(byte[] t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return t.length;
	}
}
