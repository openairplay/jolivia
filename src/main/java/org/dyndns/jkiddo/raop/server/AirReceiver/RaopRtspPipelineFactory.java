/*
 * This file is part of AirReceiver.
 *
 * AirReceiver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * AirReceiver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with AirReceiver.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dyndns.jkiddo.raop.server.AirReceiver;

import java.util.concurrent.ExecutorService;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.rtsp.*;
import org.jboss.netty.handler.execution.ExecutionHandler;

/**
 * Factory for AirTunes/RAOP RTSP channels
 */
public class RaopRtspPipelineFactory implements ChannelPipelineFactory
{
	final private byte[] hardwareAddressBytes;
	final private ExecutionHandler channelExecutionHandler;
	final private ExecutorService executorService;
	private SimpleChannelUpstreamHandler channel;

	public RaopRtspPipelineFactory(byte[] hardwareAddressBytes, ExecutionHandler channelExecutionHandler, ExecutorService executorService, SimpleChannelUpstreamHandler channel)
	{
		this.hardwareAddressBytes = hardwareAddressBytes;
		this.channelExecutionHandler = channelExecutionHandler;
		this.executorService = executorService;
		this.channel = channel;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		final ChannelPipeline pipeline = Channels.pipeline();

		pipeline.addLast("executionHandler", channelExecutionHandler);
		pipeline.addLast("closeOnShutdownHandler", channel);
		pipeline.addLast("exceptionLogger", new ExceptionLoggingHandler());
		pipeline.addLast("decoder", new RtspRequestDecoder());
		pipeline.addLast("encoder", new RtspResponseEncoder());
		pipeline.addLast("logger", new RtspLoggingHandler());
		pipeline.addLast("errorResponse", new RtspErrorResponseHandler());
		pipeline.addLast("challengeResponse", new RaopRtspChallengeResponseHandler(this.hardwareAddressBytes));
		pipeline.addLast("header", new RaopRtspHeaderHandler());
		pipeline.addLast("options", new RaopRtspOptionsHandler());
		pipeline.addLast("audio", new RaopAudioHandler(executorService, channelExecutionHandler));
		pipeline.addLast("unsupportedResponse", new RtspUnsupportedResponseHandler());

		return pipeline;
	}

}
