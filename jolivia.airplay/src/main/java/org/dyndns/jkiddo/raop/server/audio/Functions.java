package org.dyndns.jkiddo.raop.server.audio;

public final class Functions
{
	private Functions() throws InstantiationException{
		throw new InstantiationException("This class is not created for instantiation");
	}

	public static double sinc(double x)
	{
		return Math.sin(x) / x;
	}

	public static float sinc(float x)
	{
		return (float) Math.sin(x) / x;
	}
}
