package org.ardverk.daap.chunks;

public class MediaKind extends UByteChunk
{

	public MediaKind()
	{
		this(1);
	}

	public MediaKind(int mode)
	{
		super("aeMK", "com.apple.itunes.mediakind", mode);
	}
}
