package org.dyndns.jkiddo.guice;

import org.dyndns.jkiddo.raop.server.IPlayingInformation;
import org.dyndns.jkiddo.raop.server.RAOPResourceWrapper;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class RAOPModule extends AbstractModule
{

	private final IPlayingInformation iplayingInformation;
	private final Integer airplayPort;

	public RAOPModule(final IPlayingInformation iplayingInformation, final Integer airplayPort)
	{
		super();
		this.iplayingInformation = iplayingInformation;
		this.airplayPort = airplayPort;
	}

	@Override
	protected void configure()
	{
		bind(IPlayingInformation.class).toInstance(iplayingInformation);
		bind(Integer.class).annotatedWith(Names.named(RAOPResourceWrapper.RAOP_PORT_NAME)).toInstance(airplayPort);
		bind(RAOPResourceWrapper.class).asEagerSingleton();
	}

}
