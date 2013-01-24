package org.dyndns.jkiddo.raop.server.service.audio;

public final class Functions
{
	public static double sinc(double x)
	{
		return Math.sin(x) / x;
	}

	public static float sinc(float x)
	{
		return (float) Math.sin(x) / x;
	}
}
