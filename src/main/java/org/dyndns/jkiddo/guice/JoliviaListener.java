package org.dyndns.jkiddo.guice;

import javax.jmdns.JmDNS;
import javax.jmdns.impl.JmDNSImpl;

import org.dyndns.jkiddo.daap.server.LibraryManager;
import org.dyndns.jkiddo.daap.server.LibraryResource;
import org.dyndns.jkiddo.jetty.JoliviaExceptionMapper;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaListener extends GuiceServletContextListener
{
	private int hostingPort;

	public JoliviaListener(int port)
	{
		super();
		hostingPort = port;
	}

	@Override
	protected Injector getInjector()
	{
		return Guice.createInjector(new DAAPModule(), new JerseyServletModule() {
			@Override
			protected void configureServlets()
			{
				filter("*").through(ProxyFilter.class);
				// Route all requests through GuiceContainer
				serve("/*").with(GuiceContainer.class);
			}
		});
	}

	class DAAPModule extends AbstractModule
	{
		@Override
		protected void configure()
		{
			try
			{
				Multibinder<IMusicStoreReader> multibinder = Multibinder.newSetBinder(binder(), IMusicStoreReader.class);
				multibinder.addBinding().to(DeskMusicStoreReader.class).asEagerSingleton();

				bind(Integer.class).annotatedWith(Names.named(LibraryResource.DAAP_PORT_NAME)).toInstance(hostingPort);
				bind(LibraryManager.class);
				bind(JmDNS.class).toInstance(JmDNSImpl.create());
				bind(LibraryResource.class).asEagerSingleton();
				bind(JoliviaExceptionMapper.class);
			}
			catch(Exception e)
			{
				addError(e);
			}
		}
	}
}
