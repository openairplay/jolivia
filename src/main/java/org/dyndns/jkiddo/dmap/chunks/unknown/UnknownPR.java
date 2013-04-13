package org.dyndns.jkiddo.dmap.chunks.unknown;

import org.dyndns.jkiddo.dmap.chunks.StringChunk;

public class UnknownPR extends StringChunk{

	public UnknownPR()
	{
		this("");
	}
	public UnknownPR(String value) {
		super("cmpr", "unknown-pr", value);
	}
}
