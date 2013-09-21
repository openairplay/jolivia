package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

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
