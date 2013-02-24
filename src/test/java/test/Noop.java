package test;

import org.junit.Test;

public class Noop
{
	@Test
	public void dummy() throws Exception
	{
		TestSession session = new TestSession("localhost", 3689, "70963BE9D698E147");
		// session.getRemoteControl().pause();
		// session.getRemoteControl().play();
		session.getLibrary().getAllTracks();
		// [qtp1843518171-31] INFO org.dyndns.jkiddo.service.dmap.MDNSResource - Unknown chunk type: dmap.parentcontainerid
		// [qtp1843518171-31] INFO org.dyndns.jkiddo.service.dmap.MDNSResource - Unknown chunk type: com.apple.itunes.is-podcast-playlist
		// [qtp1843518171-31] INFO org.dyndns.jkiddo.service.dmap.MDNSResource - Unknown chunk type: com.apple.itunes.special-playlist
		// [qtp1843518171-31] INFO org.dyndns.jkiddo.service.dmap.MDNSResource - Unknown chunk type: com.apple.itunes.smart-playlist
		// [qtp1843518171-31] INFO org.dyndns.jkiddo.service.dmap.MDNSResource - Unknown chunk type: dmap.haschildcontainers
		// [qtp1843518171-31] INFO org.dyndns.jkiddo.service.dmap.MDNSResource - Unknown chunk type: com.apple.itunes.saved-genius

//		/databases/1/containers?session-id=42&revision-number=1&delta=0&meta=dmap.itemid,dmap.itemname,dmap.persistentid,dmap.parentcontainerid,com.apple.itunes.is-podcast-playlist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,dmap.haschildcontainers,com.apple.itunes.saved-genius
		Object oo = session.fire(String.format("/databases/%d/containers/%d/items?session-id=%smeta=dmap.itemid,dmap.parentcontainerid", session.getTheDatabase().getItemId(), session.getTheDatabase().getMasterContainer().getItemId(), session.getSessionId()));
		System.out.println(oo);
	}
}
