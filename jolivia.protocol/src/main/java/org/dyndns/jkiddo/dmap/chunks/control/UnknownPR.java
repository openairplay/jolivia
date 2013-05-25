package org.dyndns.jkiddo.dmap.chunks.control;

import org.dyndns.jkiddo.dmap.chunks.UIntChunk;

public class UnknownPR extends UIntChunk{

	public UnknownPR()
	{
		this(0);
	}
	public UnknownPR(int value) {
		super("cmpr", "unknown-pr", value);
	}
}
