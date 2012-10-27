package org.dyndns.jkiddo;

import org.apache.log4j.BasicConfigurator;
import org.dyndns.jkiddo.jetty.DaapConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;

public class Jolivia
{
	// http://randomizedsort.blogspot.dk/2011/05/using-guice-ified-jersey-in-embedded.html
	// http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
	// https://github.com/teamlazerbeez/simple-web-stack
	public static void main(String[] args) throws Exception
	{
		BasicConfigurator.configure();
		new Jolivia();
	}
	public static int port = 4001;

	public Jolivia() throws Exception
	{
		Server server = new Server(port);
		ServerConnector sc = new ServerConnector(server, new DaapConnectionFactory());
		sc.setPort(port);
		server.setConnectors(new Connector[] { sc });
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new JoliviaListener());
		sch.addFilter(GuiceFilter.class, "/*", null);
		sch.addServlet(DefaultServlet.class, "/");

		server.setSendServerVersion(false);
		server.start();
		server.join();
	}
}
