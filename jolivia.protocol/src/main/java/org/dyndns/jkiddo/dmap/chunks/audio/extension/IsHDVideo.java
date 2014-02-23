package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.BooleanChunk;

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
