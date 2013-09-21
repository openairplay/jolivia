package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

public class GaplessEncodingDel extends UIntChunk
{
	public GaplessEncodingDel()
	{
		this(0);
	}

	public GaplessEncodingDel(int value)
	{
		super("aeGE", "com.apple.itunes.gapless-enc-del", value);
	}
}
