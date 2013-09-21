package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

public class EpisodeNumberString extends StringChunk
{
	public EpisodeNumberString()
	{
		this("");
	}

	public EpisodeNumberString(String v)
	{
		super("aeEN", "com.apple.itunes.episode-num-str", v);
	}
}
