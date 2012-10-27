package org.dyndns.jkiddo;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.impl.JmDNSImpl;

import org.dyndns.jkiddo.daap.server.LibraryManager;
import org.dyndns.jkiddo.daap.server.LibraryRessource;
import org.dyndns.jkiddo.jetty.ProxyFilter;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class JoliviaListener extends GuiceServletContextListener
{
	@Override
	protected Injector getInjector()
	{
		return Guice.createInjector(new JerseyServletModule() {
			@Override
			protected void configureServlets()
			{
				try
				{
					Multibinder<IMusicStoreReader> multibinder = Multibinder.newSetBinder(binder(), IMusicStoreReader.class);
					multibinder.addBinding().to(DeskMusicStoreReader.class).asEagerSingleton();
					
					bind(Integer.class).annotatedWith(Names.named(LibraryRessource.DAAP_PORT_NAME)).toInstance(Jolivia.port);
					bind(LibraryManager.class);
					bind(JmDNS.class).toInstance(JmDNSImpl.create());
					bind(LibraryRessource.class).asEagerSingleton();
					bind(JoliviaExceptionMapper.class);
					filter("*").through(ProxyFilter.class);
					// bind(DACPResource.class).toInstance(new DACPResource(mDNS, port));
					// bind(DAAPResource.class).toInstance(new DAAPResource(mDNS, port));
					// bind(DefaultResource.class);
				}
				catch(IOException e)
				{
					addError(e);
				}

				// Must configure at least one JAX-RS resource or the
				// server will fail to start.

				// bind(JmDNSProvder.class).asEagerSingleton();
				// bind(JmDNS.class).toProvider(new TypeLiteral<JmDNSProvder>() {});

				// Route all requests through GuiceContainer
				serve("/*").with(GuiceContainer.class);
			}
		});
	}

	static class JmDNSProvder implements Provider<JmDNS>
	{

		private JmDNS jmDNS;

		public JmDNSProvder() throws IOException
		{
			jmDNS = JmDNSImpl.create();
		}

		@Override
		public JmDNS get()
		{
			return jmDNS;
		}

	}
}
