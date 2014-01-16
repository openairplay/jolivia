package org.dyndns.jkiddo.dmp;

public class ProtocolViolationException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8644472754144298352L;

	public ProtocolViolationException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
