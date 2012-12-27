package org.tunesremote;

public interface IDatabase
{

	String findCode(String serviceName);

	void updateCode(String serviceName, String guid);

	String getPairCode();

	String getServiceGuid();

	String getLastSession();

	void setLastSession(String serviceName);

}