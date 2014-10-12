package org.dyndns.jkiddo.dmap.chunks.audio.extension;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.aeGE)
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
