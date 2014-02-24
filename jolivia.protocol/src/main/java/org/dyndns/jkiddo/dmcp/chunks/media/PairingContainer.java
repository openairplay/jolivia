package org.dyndns.jkiddo.dmcp.chunks.media;
import java.util.Collection;

import org.dyndns.jkiddo.dmp.chunks.Chunk;
import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.cmpa)
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
