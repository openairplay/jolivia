package org.dyndns.jkiddo.protocol.dmap.chunks.dpap;

import org.dyndns.jkiddo.protocol.dmap.chunks.VersionChunk;

/**
 * DPAP.ProtocolVersion Represents the protocol version of the current implemented DPAP 'standard'.
 * 
 * @author Charles Ikeson
 */
public class ProtocolVersion extends VersionChunk
{

	public ProtocolVersion(int version)
	{
		super("ppro", "dpap.protocolversion", version);
	}

	public ProtocolVersion(int major, int minor, int patch)
	{
		super("ppro", "dpap.protocolversion", major, minor, patch);
	}

}
