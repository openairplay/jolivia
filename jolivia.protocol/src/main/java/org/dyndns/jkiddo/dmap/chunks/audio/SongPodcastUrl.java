package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.StringChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aspu)
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
