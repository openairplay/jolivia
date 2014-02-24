package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceWM)
public class WelcomeMessage extends StringChunk
{
	public WelcomeMessage()
	{
		this("");
	}

	public WelcomeMessage(String string)
	{
		super("ceWM", "com.apple.itunes.welcome-message", string);
	}
}
