package org.dyndns.jkiddo.dmap.chunks.control;
import java.util.Collection;

import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.ContainerChunk;

public class PairingContainer extends ContainerChunk {

	public PairingContainer() {
		super("cmpa", "unknown.pa");
	}

	public Collection<? extends Chunk> getChuncks() {
		return collection;
	}
	
	public PairingGuid getParingGuid()
	{
		return getSingleChunk(PairingGuid.class);
	}
	
	public DeviceName getDeviceName()
	{
		return getSingleChunk(DeviceName.class);
	}
	
	public DeviceType getDeviceType()
	{
		return getSingleChunk(DeviceType.class);
	}
}
