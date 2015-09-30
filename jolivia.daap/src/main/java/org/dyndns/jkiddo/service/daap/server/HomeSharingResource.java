package org.dyndns.jkiddo.service.daap.server;

import java.util.HashMap;

import org.dyndns.jkiddo.dmap.chunks.audio.AudioProtocolVersion;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.MusicSharingVersion;
import org.dyndns.jkiddo.dmp.chunks.media.MediaProtocolVersion;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.dyndns.jkiddo.service.dmap.MDNSResource;
import org.dyndns.jkiddo.service.dmap.Util;
import org.dyndns.jkiddo.zeroconf.IZeroconfManager;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;

public class HomeSharingResource extends MDNSResource
{
	public static String HOME_SHARING_SERVICE_TYPE = "_home-sharing._tcp.local.";
	
	final static AudioProtocolVersion audioProtocolVersion = new AudioProtocolVersion(DmapUtil.APRO_VERSION_3012);
	final static MediaProtocolVersion mediaProtocolVersion = new MediaProtocolVersion(DmapUtil.MPRO_VERSION_2010);
	final static MusicSharingVersion musicSharingVersion = new MusicSharingVersion(DmapUtil.MUSIC_SHARING_VERSION_3011);

	private final String applicationName;
	private final String hsGid;
	private final String hsPid;

	public HomeSharingResource(final IZeroconfManager mDNS, final Integer port, final String applicationName, final String appleUsername, final String applePassword) throws Exception
	{
		super(mDNS, port);
		final NSDictionary propertyList = Util.requestPList(appleUsername, applePassword);
		hsGid = ((NSString) propertyList.get("sgid")).getContent();
		hsPid = ((NSString) propertyList.get("spid")).getContent();
		this.applicationName = applicationName;
		register();
	}

	@Override
	protected IZeroconfManager.ServiceInfo getServiceInfoToRegister()
	{
		final HashMap<String, String> records = new HashMap<String, String>();
		records.put("MID", MID_0X);
		records.put("OSsi", "0x15F"); //OS specific
		records.put(DATABASE_ID_KEY, DATABASE_ID);
		
		records.put("hC", hsPid);
		records.put("DvTy", "iTunes");
		records.put("hQ", "102");
		records.put("PrVs", "65538");
		records.put("dmv", mediaProtocolVersion.getValue() + "");
		records.put(MACHINE_ID_KEY, MACHINE_ID);
		records.put(TXT_VERSION_KEY, TXT_VERSION);
		records.put(ITSH_VERSION_KEY, musicSharingVersion.getValue() + "");
		records.put(VERSION_KEY, audioProtocolVersion.getValue() + "");
		records.put("hG", hsGid); //sgid
		records.put("iCSV", "65540");
		records.put(MACHINE_NAME_KEY, applicationName);
		records.put("DvSv", "3120");
		return new IZeroconfManager.ServiceInfo(HOME_SHARING_SERVICE_TYPE, applicationName, port, records);
	}
}
