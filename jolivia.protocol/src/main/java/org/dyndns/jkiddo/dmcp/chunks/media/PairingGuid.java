package org.dyndns.jkiddo.dmcp.chunks.media;
import org.dyndns.jkiddo.dmap.chunks.RawChunk;

public class PairingGuid extends RawChunk {

	public PairingGuid()
	{
		this(new byte[] {});
	}

	public PairingGuid(byte[] value)
	{
		super("cmpg", "com.apple.itunes.unknown-pg", value);
	}
}