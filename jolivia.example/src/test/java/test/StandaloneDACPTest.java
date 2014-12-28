package test;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.jetty.extension.DmapConnector;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.PairedRemoteDiscoverer;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.dyndns.jkiddo.service.daap.client.UnpairedRemoteCrawler;
import org.dyndns.jkiddo.service.dacp.client.IPairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.ITouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.client.PairingDatabase;
import org.dyndns.jkiddo.service.dacp.client.TouchRemoteResource;
import org.dyndns.jkiddo.service.dacp.server.ITouchAbleServerResource;
import org.dyndns.jkiddo.service.dacp.server.TouchAbleServerResource;
import org.dyndns.jkiddo.service.dmap.CustomByteArrayProvider;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;
import org.dyndns.jkiddo.zeroconf.ZeroconfManagerImpl;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class StandaloneDACPTest {

	public static void main(final String[] args) throws Exception {
		
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.net.preferIPv6Addresses", "false");
		
		final Integer pairingCode = 7666;
		final Integer hostingPort = 4321;
		final String DB_NAME = "someDBName";
		final String name = "myName";
		final IClientSessionListener clientSessionListener = new IClientSessionListener() {
			
			@Override
			public void tearDownSession(final String server, final int port) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void registerNewSession(final Session session) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("New session started: " + session);
			}
		};
		
		final Injector injector = Guice.createInjector(new JerseyServletModule(){
			@Override
			protected void configureServlets()
			{
				bind(String.class).annotatedWith(Names.named(Util.APPLICATION_NAME)).toInstance(name);
				bind(Integer.class).annotatedWith(Names.named(TouchRemoteResource.DACP_CLIENT_PAIRING_CODE)).toInstance(pairingCode);
				bind(Integer.class).annotatedWith(Names.named(TouchRemoteResource.DACP_CLIENT_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(TouchAbleServerResource.DACP_SERVER_PORT_NAME)).toInstance(hostingPort);
				bind(Integer.class).annotatedWith(Names.named(UnpairedRemoteCrawler.SERVICE_PORT_NAME)).toInstance(hostingPort);

				bind(IZeroconfManager.class).toInstance(new ZeroconfManagerImpl());
				bind(String.class).annotatedWith(Names.named(PairingDatabase.NAME_OF_DB)).toInstance(DB_NAME);
				bind(IPairingDatabase.class).to(PairingDatabase.class).asEagerSingleton();
				bind(PairedRemoteDiscoverer.class).asEagerSingleton();
//				bind(UnpairedRemoteCrawler.class).asEagerSingleton();
				bind(ITouchRemoteResource.class).to(TouchRemoteResource.class);
				bind(ITouchAbleServerResource.class).to(TouchAbleServerResource.class);
				bind(IClientSessionListener.class).toInstance(clientSessionListener);
				
				bind(SomeJerseyServlet.class).asEagerSingleton();
				
				bind(CustomByteArrayProvider.class);
				filter("*").through(ProxyFilter.class);
				serve("/*").with(GuiceContainer.class);
			}
		});
		
		final Server server = new Server(hostingPort);
		final Connector dmapConnector = new DmapConnector();
		dmapConnector.setPort(hostingPort);
		server.setConnectors(new Connector[] { dmapConnector });
		final ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new GuiceServletContextListener() {
			
			@Override
			protected Injector getInjector() {
				return injector;
			}
		});
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		sch.addServlet(DefaultServlet.class, "/");
		server.start();
		System.out.println("");
	}
}
