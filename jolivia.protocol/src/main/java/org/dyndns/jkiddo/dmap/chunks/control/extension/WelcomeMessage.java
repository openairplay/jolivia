package org.dyndns.jkiddo.dmap.chunks.control.extension;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

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
