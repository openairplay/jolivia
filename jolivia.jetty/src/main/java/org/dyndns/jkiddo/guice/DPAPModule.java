package org.dyndns.jkiddo.guice;

import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.service.dpap.server.DPAPResource;
import org.dyndns.jkiddo.service.dpap.server.IImageLibrary;
import org.dyndns.jkiddo.service.dpap.server.ImageItemManager;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class DPAPModule extends AbstractModule
{

	private final Integer hostingPort;
	private final IImageStoreReader imageStoreReader;

	public DPAPModule(final Integer hostingPort, final IImageStoreReader imageStoreReader)
	{
		super();
		this.hostingPort = hostingPort;
		this.imageStoreReader = imageStoreReader;
	}

	@Override
	protected void configure()
	{
		bind(Integer.class).annotatedWith(Names.named(DPAPResource.DPAP_SERVER_PORT_NAME)).toInstance(hostingPort);
		bind(IImageLibrary.class).to(DPAPResource.class).asEagerSingleton();
		bind(ImageItemManager.class).annotatedWith(Names.named(DPAPResource.DPAP_RESOURCE)).to(ImageItemManager.class);
		bind(IImageStoreReader.class).toInstance(imageStoreReader);
	}

}
