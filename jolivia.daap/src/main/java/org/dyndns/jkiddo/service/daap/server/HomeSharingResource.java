package org.dyndns.jkiddo.service.daap.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.MusicSharingVersion;
import org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.dpap.chunks.picture.PictureProtocolVersion;
import org.dyndns.jkiddo.service.dmap.IItemManager;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;

public class HomeSharingResource extends DAAPResource
{
	static String HOME_SHARING_SERVICE_TYPE = "_home-sharing._tcp.local.";
	static PictureProtocolVersion _pictureProtocolVersion = new PictureProtocolVersion(DmapUtil.PPRO_VERSION_201);
	static AudioProtocolVersion _audioProtocolVersion = new AudioProtocolVersion(DmapUtil.APRO_VERSION_3012);
	static MediaProtocolVersion _mediaProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_2010);
	static MusicSharingVersion _musicSharingVersion = new MusicSharingVersion(DmapUtil.MUSIC_SHARING_VERSION_3011);

	@Inject
	public HomeSharingResource(final IZeroconfManager mDNS, @Named(DAAP_PORT_NAME) final Integer port, @Named(Util.APPLICATION_NAME) final String applicationName, @Named(DAAPResource.DAAP_RESOURCE) final IItemManager itemManager) throws IOException
	{
		super(mDNS, port, applicationName, itemManager);
	}

	@Override
	protected IZeroconfManager.ServiceInfo getServiceInfoToRegister()
	{
		final String hexedHostname;
		try
		{
			hexedHostname = Util.toHex(hostname.getBytes("UTF-8"));
		}
		catch(final UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
		final HashMap<String, String> records = new HashMap<String, String>();
		records.put("MID", "0xC707B87BC1118B88");
//		records.put("OSsi", "0x86A672");
		records.put("OSsi", "0x148B63");
		records.put(DATABASE_ID_KEY, "B2ACB1E13A42634D");
		
		records.put("hC", "eee1c1ab-3d29-4502-a170-0e0b8fde9d60"); //spid
		records.put("DvTy", "iTunes");
		records.put("hQ", "102");
		records.put("PrVs", "65538");
		records.put("dmv", _mediaProtocolVersion.getValue() + "");
		records.put(MACHINE_ID_KEY, "99FF8329BAC9");
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(ITSH_VERSION_KEY, _musicSharingVersion.getValue() + "");
		records.put(VERSION_KEY, _audioProtocolVersion.getValue() + "");
		records.put("hG", "00000000-0b18-4f5c-44e5-e6110e914cd4");
		records.put("iCSV", "65540");
		records.put(MACHINE_NAME_KEY, name);
		records.put("DvSv", "3120");
		return new IZeroconfManager.ServiceInfo(HOME_SHARING_SERVICE_TYPE, name, port, records);
	}
}
