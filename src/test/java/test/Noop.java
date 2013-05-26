package test;

import java.util.Collection;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.RemoteControl;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.dyndns.jkiddo.service.daap.client.Speaker;
import org.junit.Test;

public class Noop {
	@Test
	public void usage() throws Exception {

		// As soon as you have entered the pairing code '1337' in iTunes the
		// registerNewSession will be invoked and the pairning will be stored in
		// a local db file and in iTunes as well. Clear the pairing in iTunes by
		// clearing all remotes. Clear the pairing in Jolivia by deleting the db
		// file. Once paired every time you start iTunes this method will be
		// called. Every time the iTunes instance is
		// closed the tearDownSession will be invoked.
		new Jolivia(new IClientSessionListener() {

			@Override
			public void tearDownSession(String server, int port) {
				// Maybe do some clean up?
			}

			@Override
			public void registerNewSession(Session session) throws Exception {

				// Showcase on some actions you can do on speakers ...
				RemoteControl remoteControl = session.getRemoteControl();
				// Set min volume
				remoteControl.setVolume(0);
				// Set max volume
				remoteControl.setVolume(100);
				// Get the master volume
				remoteControl.getMasterVolume();

				// Get all speakers visible to iTunes instance
				Collection<Speaker> speakers = remoteControl.getSpeakers();

				// Mark all speakers active meaning they are prepared for being
				// used for the iTunes instance
				for (Speaker s : speakers) {
					s.setActive(true);
				}
				// Assign all the active speakers to the iTunes instance. This
				// means that all the speakers will now be used for output
				remoteControl.setSpeakers(speakers);

				// Change the volume individually on each speaker
				speakers = remoteControl.getSpeakers();
				for (Speaker s : speakers) {
					remoteControl.setSpeakerVolume(s.getId(), 60, 50, 40, 30,
							100);
				}
			}
		});
	}

	@Test
	public void dummy() throws Exception {
		try {
			TestSession session = new TestSession("localhost", 3689,
					"70963BE9D698E147");
			Object oo = session
					.fire(String
							.format("/databases/%d/containers/%d/items?session-id=%smeta=dmap.itemid,dmap.parentcontainerid",
									session.getTheDatabase().getItemId(),
									session.getTheDatabase()
											.getMasterContainer().getItemId(),
									session.getSessionId()));
			System.out.println(oo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
