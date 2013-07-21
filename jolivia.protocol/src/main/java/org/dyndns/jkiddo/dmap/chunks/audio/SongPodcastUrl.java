package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class SongPodcastUrl extends StringChunk
{
	public SongPodcastUrl()
	{
		this("");
	}

	public SongPodcastUrl(String v)
	{
		super("aspu", "daap.songpodcasturl", v);
	}
}
