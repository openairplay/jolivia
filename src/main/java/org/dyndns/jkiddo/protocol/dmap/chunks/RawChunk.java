package org.dyndns.jkiddo.protocol.dmap.chunks;

public abstract class RawChunk extends AbstractChunk
{
	private byte[] value;

	protected RawChunk(String contentCode, String name)
	{
		super(contentCode, name);
	}
	
	protected RawChunk(String contentCode, String name, byte[] array)
	{
		super(contentCode, name);
		setValue(array);
	}

	public void setValue(byte[] array)
	{
		value = new byte[array.length];
		System.arraycopy(array, 0, value, 0, value.length);
	}

	@Override
	public int getType()
	{
		return RAW_TYPE;
	}

}
