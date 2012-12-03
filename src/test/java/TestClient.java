import java.io.IOException;
import java.util.Hashtable;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.dyndns.jkiddo.dacp.client.PairingResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

public class TestClient
{

	public final static Integer PAIRING_SERVER_PORT = 8080;
	public final static String REMOTE_TYPE = "_touch-remote._tcp.local.";
	public final static String DEVICE_ID = "0000000000000000000000000000000000000010";

	// @Test
	// public void setupTestClient() throws Exception
	// {
	// Server server = new Server(PAIRING_SERVER_PORT);
	// ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
	// handler.setContextPath("/");
	// handler.addServlet(new ServletHolder(new PairingResource(null, null)), "/");
	// server.setHandler(handler);
	// server.start();
	//
	// registerClientRemote();
	// server.join();
	// }

	private void registerClientRemote() throws IOException
	{
		final Hashtable<String, String> values = new Hashtable<String, String>();
		values.put("DvNm", "BrickTunes");
		values.put("RemV", "10000");
		values.put("DvTy", "JKiddo Inc");
		values.put("RemN", "BrickTunes Remote");
		values.put("txtvers", "1");
		values.put("Pair", "0000000000000001");

		ServiceInfo pairservice = ServiceInfo.create(REMOTE_TYPE, DEVICE_ID, PAIRING_SERVER_PORT, 0, 0, values);
		JmDNS.create().registerService(pairservice);
	}
}
