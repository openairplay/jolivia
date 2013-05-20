package org.dyndns.jkiddo;

import java.io.IOException;

import javax.jmdns.JmmDNS;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.service.daap.server.DAAPResource;
import org.dyndns.jkiddo.service.daap.server.MusicItemManager;

import com.sun.jersey.spi.resource.Singleton;

@Path("/")
@Singleton
public class DAAPServlet extends DAAPResource
{
	public DAAPServlet() throws IOException, Exception
	{
		super(JmmDNS.Factory.getInstance(), 4000, "Jolivia", new MusicItemManager("Jolivia", new DeskMusicStoreReader()));
	}

	public DAAPServlet(JmmDNS mDNS, Integer port, String applicationName, MusicItemManager itemManager) throws IOException
	{
		super(mDNS, port, applicationName, itemManager);
	}
	
	@Path("/doStuff")
	@GET
	public String doStuff()
	{
		return "skdfjl";
	}
}
