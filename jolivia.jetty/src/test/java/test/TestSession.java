package test;

import org.dyndns.jkiddo.dmap.Database;
import org.dyndns.jkiddo.service.daap.client.RequestHelper;
import org.dyndns.jkiddo.service.daap.client.Session;

public class TestSession extends Session {

	public TestSession(String host, int port, String pairingGuid)
			throws Exception {
		super(host, port, pairingGuid);
	}

	public Database getTheDatabase() {
		return this.database;
	}

	public <T> T fire(String request) throws Exception {
		return RequestHelper.requestParsed(String.format("%s" + request,
				getRequestBase()));
	}

}
