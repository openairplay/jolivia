package test;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseItems;
import org.dyndns.jkiddo.dmap.chunks.audio.ItemsContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases;
import org.dyndns.jkiddo.dmp.Database;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.LoginResponse;
import org.dyndns.jkiddo.dmp.chunks.media.ServerInfoResponse;
import org.dyndns.jkiddo.dpap.chunks.picture.FileData;
import org.dyndns.jkiddo.service.daap.client.RequestHelper;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class TestImage
{

	private String host;
	private Integer port;
	private Integer sessionId;
	private Database database;

	public TestImage()
	{
		host = "192.168.1.75";
//		host = "localhost";
		port = 8770;
	}

	@Test
	public void testSession() throws Exception
	{
		// -----------------------------------------------------------------------------------------------------
		ServerInfoResponse serverInfoResponse = RequestHelper.requestParsed(String.format("%s/server-info", this.getRequestBase()));

		LoginResponse loginResponse = RequestHelper.requestParsed(String.format("%s/login", this.getRequestBase()));

		sessionId = loginResponse.getSessionId().getValue();

		// http://192.168.254.128:3689/databases?session-id=1301749047
		ServerDatabases serverDatabases = RequestHelper.requestParsed(String.format("%s/databases?session-id=%s", this.getRequestBase(), this.sessionId));

		// For now, the LocalDatabase is sufficient
		ListingItem localDatabase = serverDatabases.getListing().getListingItems().iterator().next();

		String databaseName = localDatabase.getSpecificChunk(ItemName.class).getValue();
		int databaseId = localDatabase.getSpecificChunk(ItemId.class).getValue();

		DatabaseContainerns containers = RequestHelper.requestParsed(String.format("%s/databases/%d/containers?session-id=%s", this.getRequestBase(), databaseId, this.sessionId));
		int containerId = containers.getListing().getSingleListingItemContainingClass(BaseContainer.class).getSpecificChunk(ItemId.class).getValue();
		// fetch playlists to find the overall magic "Music" playlist
		ItemsContainer items = RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&meta=dpap.aspectratio,dmap.itemid,dmap.itemname,dpap.imagefilename,dpap.imagefilesize,dpap.creationdate,dpap.imagepixelwidth,dpap.imagepixelheight,dpap.imageformat,dpap.imagerating,dpap.imagecomments,dpap.imagelargefilesize&type=photo", this.getRequestBase(), databaseId, containerId, this.sessionId));
		// -----------------------------------------------------------------------------------------------------

		Collection<String> itemIds = Collections2.transform(Lists.newArrayList(items.getListing().getListingItems()), new Function<ListingItem, String>() {

			@Override
			@Nullable
			public String apply(@Nullable ListingItem input)
			{
				return "'dmap.itemid:" + input.getSpecificChunk(ItemId.class).getValue() + "'";
			}
		});
		String queryIds = Joiner.on(",").join(itemIds);
		// Get thumbs
		DatabaseItems queriedItem = RequestHelper.requestParsed(String.format("%s/databases/%d/items?session-id=%s&meta=dpap.thumb,dmap.itemid,dpap.filedata&query=(" + queryIds + ")", this.getRequestBase(), databaseId, this.sessionId));
		for(ListingItem li : queriedItem.getListing().getListingItems())
		{
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(li.getSpecificChunk(FileData.class).getValue()));
			JFrame frame = new JFrame();
			frame.getContentPane().setLayout(new FlowLayout());
			frame.getContentPane().add(new JLabel(new ImageIcon(image)));
			frame.pack();
			frame.setVisible(true);
		}

		// Get hires
		for(String id : itemIds)
		{
			DatabaseItems hiresItems = RequestHelper.requestParsed(String.format("%s/databases/%d/items?session-id=%s&meta=dpap.hires,dmap.itemid,dpap.filedata&query=(" + id + ")", this.getRequestBase(), databaseId, this.sessionId));
			for(ListingItem li : hiresItems.getListing().getListingItems())
			{
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(li.getSpecificChunk(FileData.class).getValue()));
				JFrame frame = new JFrame();
				frame.getContentPane().setLayout(new FlowLayout());
				frame.getContentPane().add(new JLabel(new ImageIcon(image)));
				frame.pack();
				frame.setVisible(true);
			}
		}
	}

	private String getRequestBase()
	{
		return String.format("http://%s:%d", host, port);
	}
}
