package org.dyndns.jkiddo.protocol.dmap.chunks.unknown;

import org.dyndns.jkiddo.protocol.dmap.chunks.ContainerChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.RawChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.StringChunk;

public class Unknownml extends ContainerChunk
{
	public Unknownml()
	{
		super("msml", "com.apple.itunes.unknown-ml");
		// this(new byte[] {});
	}

	// public Unknownml(byte[] i)
	// {
	// super("msml", "com.apple.itunes.unknown-ml", i);
	// }
}
