package test;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.dyndns.jkiddo.Jolivia;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems;
import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtworkCount;
import org.dyndns.jkiddo.dmap.chunks.audio.SongExtraData;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTime;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTrackNumber;
import org.dyndns.jkiddo.dmap.chunks.audio.SongUserRating;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ArtworkChecksum;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.dpap.chunks.picture.FileData;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.RemoteControl;
import org.dyndns.jkiddo.service.daap.client.RequestHelper;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.dyndns.jkiddo.service.daap.client.Speaker;
import org.junit.Test;

public class Noop
{
	//@Test
	public void usage() throws Exception
	{

		// As soon as you have entered the pairing code '1337' in iTunes the
		// registerNewSession will be invoked and the pairing will be stored in
		// a local db file and in iTunes as well. Clear the pairing in iTunes by
		// clearing all remotes in iTunes as usual. Clear the pairing in Jolivia
		// by deleting the db
		// file. Once paired every time you start iTunes this method will be
		// called. Every time the iTunes instance is
		// closed the tearDownSession will be invoked.
		new Jolivia.JoliviaBuilder().clientSessionListener(new IClientSessionListener() {

			private Session session;

			@Override
			public void tearDownSession(final String server, final int port)
			{
				// Maybe do some clean up?
				try
				{
					session.logout();
				}
				catch(final Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void registerNewSession(final Session session) throws Exception
			{
				doShowCase(session);
			}
		}).build();
		System.out.println("");
	}
	
	@Test
	public void remoteClientTest() throws Exception
	{
		doShowCase(new TestSession("localhost", 3689));
	}
	
	private void doShowCase(final Session session) throws Exception
	{
		// Showcase on some actions you can do on a session ...
		// ////////////////////////////////////////

		
		// Do something - e.g doubleclick a track in iTunes
		session.getUpdateBlocking();

		final Database itunesDatabase = session.getDatabase();

		itunesDatabase.getContainers();

		// Traverse the library for eg. all tracks
		for(final SongArtist artist : session.getLibrary().getAllArtists().getBrowseArtistListing().getSongArtists())
		{
			System.out.println(artist.getValue());
		}

		long itemId = 0;

		// Extract information from a generic listing
		for(final ListingItem item : session.getLibrary().getAllTracks().getListing().getListingItems())
		{
			try
			{
				itemId = item.getSpecificChunk(ItemId.class).getValue();
				System.out.println(item.getSpecificChunk(SongAlbum.class).getValue());
				System.out.println(item.getSpecificChunk(SongArtist.class).getValue());
				System.out.println(item.getSpecificChunk(SongTime.class).getValue());
				System.out.println(item.getSpecificChunk(SongTrackNumber.class).getValue());
				System.out.println(item.getSpecificChunk(SongUserRating.class).getValue());
				System.out.println(item.getSpecificChunk(ItemName.class).getValue());
				System.out.println(item.getSpecificChunk(ItemKind.class).getValue());
				System.out.println(item.getSpecificChunk(ItemId.class).getValue());
				
				if(item.getSpecificChunk(SongExtraData.class).getValue() > 0)
				{
					item.getSpecificChunk(ArtworkChecksum.class).getValue();
					item.getSpecificChunk(SongArtworkCount.class).getValue();
				}
			}
			catch(final Exception e)
			{
				e.printStackTrace();
			}
		}
		session.getRemoteControl().playQueue(50);

		// Showcase on some actions you can do on speakers ...
		// ////////////////////////////////////////
		final RemoteControl remoteControl = session.getRemoteControl();
		// Set min volume
		remoteControl.setVolume(0);
		// Set max volume
		remoteControl.setVolume(100);

		remoteControl.setVolume(0);
		// Get the master volume
		remoteControl.getMasterVolume();

		// Get all speakers visible to iTunes instance
		Collection<Speaker> speakers = remoteControl.getSpeakers();

		// Mark all speakers active meaning they are prepared for being
		// used for the iTunes instance
		for(final Speaker s : speakers)
		{
			s.setActive(true);
		}
		// Assign all the active speakers to the iTunes instance. This
		// means that all the speakers will now be used for output
		remoteControl.setSpeakers(speakers);

		// Change the volume individually on each speaker
		speakers = remoteControl.getSpeakers();
		for(final Speaker s : speakers)
		{
			remoteControl.setSpeakerVolume(s.getId(), 60, 50, 40, 30, 100);
		}

		session.getLibrary().getAlbumArtworkAsDatabase(itemId, 320, 320);
		session.getRemoteControl().fetchCover(320, 320);
	}

	//@Test
	public void thumbResponse() throws Exception
	{
		final String requestBase = String.format("http://%s:%d", "192.168.1.26", 5000);
		final String url = String.format("%s/databases/1/items?session-id=1101478641&meta=dpap.thumb,dmap.itemid,dpap.filedata&query=('dmap.itemid:1024','dmap.itemid:1025')", requestBase);
		final DatabaseItems di = RequestHelper.requestParsed(url);
		final ListingItem item = di.getListing().getListingItems().iterator().next();
		final byte[] data = item.getSpecificChunk(FileData.class).getValue();

		final BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));

		// Debugging ...
		try
		{
			final JFrame frame = new JFrame("Image loaded from ImageInputStream");
			final JLabel label = new JLabel(new ImageIcon(image));
			frame.getContentPane().add(label, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
		catch(final Exception e)
		{
			e.printStackTrace();
		}

	}

	//@Test
	public void hiresResponse() throws Exception
	{
		final String requestBase = String.format("http://%s:%d", "192.168.1.26", 5000);
		final String url = String.format("%s/databases/1/items?session-id=1101478641&meta=dpap.hires,dmap.itemid,dpap.filedata&query=('dmap.itemid:1024','dmap.itemid:1025')", requestBase);
		final DatabaseItems di = RequestHelper.requestParsed(url);
		final ListingItem item = di.getListing().getListingItems().iterator().next();
		final byte[] data = item.getSpecificChunk(FileData.class).getValue();

		final BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));

		// Debugging ...
		try
		{
			final JFrame frame = new JFrame("Image loaded from ImageInputStream");
			final JLabel label = new JLabel(new ImageIcon(image));
			frame.getContentPane().add(label, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
		catch(final Exception e)
		{
			e.printStackTrace();
		}

	}
}
