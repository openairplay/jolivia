package org.dyndns.jkiddo.jetty.extension;

import java.io.IOException;

import org.eclipse.jetty.http.HttpException;
import org.eclipse.jetty.http.HttpGenerator;
import org.eclipse.jetty.http.HttpHeaderValues;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.HttpParser;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.HttpVersions;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.BufferCache.CachedBuffer;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.BlockingHttpConnection;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class DmapConnector extends SocketConnector
{
	public DmapConnector()
	{
		super();
	}

	@Override
	protected Connection newConnection(EndPoint endpoint)
	{
		return new DmapHttpConnection(this, endpoint, getServer());
	}

	class DmapHttpConnection extends BlockingHttpConnection
	{
		public DmapHttpConnection(Connector connector, EndPoint endpoint, Server server)
		{
			super(connector, endpoint, server);
		}

		private final Logger LOG = Log.getLogger(AbstractHttpConnection.class);

		private static final int UNKNOWN = -2;

		private int _version = UNKNOWN;

		private String _charset;
		private boolean _expect = false;
		private boolean _expect100Continue = false;
		private boolean _head = false;
		private boolean _host = false;
		/* ------------------------------------------------------------ */
		@Override
		protected void startRequest(Buffer method, Buffer uri, Buffer version) throws IOException
		{
			uri = uri.asImmutableBuffer();

			_host = false;
			_expect = false;
			_expect100Continue = false;
			_charset = null;

			if(_request.getTimeStamp() == 0)
				_request.setTimeStamp(System.currentTimeMillis());
			_request.setMethod(method.toString());

			try
			{
				_head = false;
				switch(HttpMethods.CACHE.getOrdinal(method))
				{
					case HttpMethods.CONNECT_ORDINAL:
						_uri.parseConnect(uri.array(), uri.getIndex(), uri.length());
						break;

					case HttpMethods.HEAD_ORDINAL:
						_head = true;
						_uri.parse(uri.array(), uri.getIndex(), uri.length());
						break;

					default:
						_uri.parse(uri.array(), uri.getIndex(), uri.length());
				}

				_request.setUri(_uri);

				if(version == null)
				{
					_request.setProtocol(HttpVersions.HTTP_0_9);
					_version = HttpVersions.HTTP_0_9_ORDINAL;
				}
				else
				{
					version = HttpVersions.CACHE.get(version);
					if(version == null)
						throw new HttpException(HttpStatus.BAD_REQUEST_400, null);
					_version = HttpVersions.CACHE.getOrdinal(version);
					if(_version <= 0)
						_version = HttpVersions.HTTP_1_0_ORDINAL;
					_request.setProtocol(version.toString());
				}
			}
			catch(Exception e)
			{
				LOG.debug(e);
				if(e instanceof HttpException)
					throw (HttpException) e;
				throw new HttpException(HttpStatus.BAD_REQUEST_400, null, e);
			}
		}

		/* ------------------------------------------------------------ */
		@Override
		protected void parsedHeader(Buffer name, Buffer value) throws IOException
		{
			int ho = HttpHeaders.CACHE.getOrdinal(name);
			switch(ho)
			{
				case HttpHeaders.HOST_ORDINAL:
					// TODO check if host matched a host in the URI.
					_host = true;
					break;

				case HttpHeaders.EXPECT_ORDINAL:
					if(_version >= HttpVersions.HTTP_1_1_ORDINAL)
					{
						value = HttpHeaderValues.CACHE.lookup(value);
						switch(HttpHeaderValues.CACHE.getOrdinal(value))
						{
							case HttpHeaderValues.CONTINUE_ORDINAL:
								_expect100Continue = _generator instanceof HttpGenerator;
								break;

							case HttpHeaderValues.PROCESSING_ORDINAL:
								break;

							default:
								String[] values = value.toString().split(",");
								for(int i = 0; values != null && i < values.length; i++)
								{
									CachedBuffer cb = HttpHeaderValues.CACHE.get(values[i].trim());
									if(cb == null)
										_expect = true;
									else
									{
										switch(cb.getOrdinal())
										{
											case HttpHeaderValues.CONTINUE_ORDINAL:
												_expect100Continue = _generator instanceof HttpGenerator;
												break;
											case HttpHeaderValues.PROCESSING_ORDINAL:
												break;
											default:
												_expect = true;
										}
									}
								}
						}
					}
					break;

				case HttpHeaders.ACCEPT_ENCODING_ORDINAL:
				case HttpHeaders.USER_AGENT_ORDINAL:
					value = HttpHeaderValues.CACHE.lookup(value);
					break;

				case HttpHeaders.CONTENT_TYPE_ORDINAL:
					value = MimeTypes.CACHE.lookup(value);
					_charset = MimeTypes.getCharsetFromContentType(value);
					break;
			}

			_requestFields.add(name, value);
		}

		/* ------------------------------------------------------------ */
		@Override
		protected void headerComplete() throws IOException
		{
			_generator.setVersion(_version);
			switch(_version)
			{
				case HttpVersions.HTTP_0_9_ORDINAL:
					break;
				case HttpVersions.HTTP_1_0_ORDINAL:
					_generator.setHead(_head);
					if(_parser.isPersistent())
					{
						_responseFields.add(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.KEEP_ALIVE_BUFFER);
						_generator.setPersistent(true);
					}
					else if(HttpMethods.CONNECT.equals(_request.getMethod()))
					{
						_generator.setPersistent(true);
						_parser.setPersistent(true);
					}

					if(_server.getSendDateHeader())
						_generator.setDate(_request.getTimeStampBuffer());
					break;

				case HttpVersions.HTTP_1_1_ORDINAL:
					_generator.setHead(_head);

					if(!_parser.isPersistent())
					{
						_responseFields.add(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
						_generator.setPersistent(false);
					}
					if(_server.getSendDateHeader())
						_generator.setDate(_request.getTimeStampBuffer());

					if(!_host)
					{
						LOG.debug("Nice try, Apple");
						// LOG.debug("!host {}", this);
						// _generator.setResponse(HttpStatus.BAD_REQUEST_400, null);
						// _responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
						// _generator.completeHeader(_responseFields, true);
						// _generator.complete();
						// return;
					}

					if(_expect)
					{
						LOG.debug("!expectation {}", this);
						_generator.setResponse(HttpStatus.EXPECTATION_FAILED_417, null);
						_responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
						_generator.completeHeader(_responseFields, true);
						_generator.complete();
						return;
					}

					break;
				default:
			}

			if(_charset != null)
				_request.setCharacterEncodingUnchecked(_charset);

			// Either handle now or wait for first content
			if((((HttpParser) _parser).getContentLength() <= 0 && !((HttpParser) _parser).isChunking()) || _expect100Continue)
				handleRequest();
		}
	}
}
