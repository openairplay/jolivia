package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.UIntChunk;

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
