package org.dyndns.jkiddo.dmap.chunks.unknown;
import org.dyndns.jkiddo.dmap.chunks.RawChunk;

public class UnknownPG extends RawChunk {

	public UnknownPG()
	{
		this(new byte[] {});
	}

	public UnknownPG(byte[] value)
	{
		super("cmpg", "com.apple.itunes.unknown-pg", value);
	}
}