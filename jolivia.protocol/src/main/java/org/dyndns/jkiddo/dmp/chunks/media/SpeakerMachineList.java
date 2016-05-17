package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;

@DMAPAnnotation(type=DmapChunkDefinition.msml)
public class SpeakerMachineList extends ContainerChunk
{
	public SpeakerMachineList()
	{
		super("msml", "dmap.speakermachinelist");
	}
}
