package org.dyndns.jkiddo.protocol.dmap.chunks.dmap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class EditCommandSupported extends UIntChunk
{
	public EditCommandSupported()
	{
		this(0);
	}

	public EditCommandSupported(int value)
	{
		super("meds", "dmap.editcommandssupported", value);
	}
}
