package test;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.dyndns.jkiddo.dmap.chunks.audio.AlbumSearchContainer;
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
import org.dyndns.jkiddo.dmp.model.Container;
import org.dyndns.jkiddo.dmp.model.Database;
import org.junit.Test;

public class DacpTestBed
{

	//@Test
	public void doDaap() throws Exception
	{
		TestSession session = new TestSession();

		Database itunesDatabase = session.getDatabase();

		// Get all playlists. For now the playlists only contains the
		// master playlist. This is to be expanded
		Container playlists = itunesDatabase.getMasterContainer();

		session.getLibrary().getAllAlbums();
		session.getLibrary().getAllArtists();

		AlbumSearchContainer searchContainer = session.getLibrary().getAlbums("Abba");

		// Traverse the library for eg. all tracks
		for(SongArtist artist : session.getLibrary().getAllArtists().getBrowseArtistListing().getSongArtists())
		{
			System.out.println(artist.getValue());
		}

		long itemId = 0;

		// Extract information from a generic listing
		for(ListingItem item : session.getLibrary().getAllTracks().getListing().getListingItems())
		{
			System.out.println(item.getSpecificChunk(ItemId.class).getValue());
			System.out.println(item.getSpecificChunk(SongAlbum.class).getValue());
			System.out.println(item.getSpecificChunk(SongArtist.class).getValue());
			System.out.println(item.getSpecificChunk(SongTime.class).getValue());
			System.out.println(item.getSpecificChunk(SongTrackNumber.class).getValue());
			System.out.println(item.getSpecificChunk(SongUserRating.class).getValue());
			System.out.println(item.getSpecificChunk(ItemName.class).getValue());
			System.out.println(item.getSpecificChunk(ItemKind.class).getValue());
			itemId = item.getSpecificChunk(ItemId.class).getValue();

			session.getLibrary().getAlbumArtworkAsRemote(searchContainer.getListing().getListingItems().iterator().next().getSpecificChunk(ItemId.class).getValue(), 480, 480);
			byte[] art = session.getLibrary().getAlbumArtworkAsDatabase(itemId, 480, 480);

			BufferedImage image = ImageIO.read(new ByteArrayInputStream(art));

			// Debugging ...
			try
			{
				JFrame frame = new JFrame("Image loaded from ImageInputStream");
				JLabel label = new JLabel(new ImageIcon(image));
				frame.getContentPane().add(label, BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			if(item.getSpecificChunk(SongExtraData.class) != null)
			{
				item.getSpecificChunk(ArtworkChecksum.class).getValue();
				item.getSpecificChunk(SongArtworkCount.class).getValue();
			}
		}

	}
}
