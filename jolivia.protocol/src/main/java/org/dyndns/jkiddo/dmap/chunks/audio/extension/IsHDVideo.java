package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.aeHD)
public class IsHDVideo extends BooleanChunk
{
	public IsHDVideo()
	{
		this(false);
	}

	public IsHDVideo(boolean b)
	{
		super("aeHD", "com.apple.itunes.is-hd-video", b);
	}
}
