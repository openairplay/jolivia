package org.dyndns.jkiddo.daap.client;

public interface IAutomatedClientSessionCreator
{
	void createNewSession(String server, int port, String code) throws Exception;
}
