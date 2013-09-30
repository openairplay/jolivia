package test;

import org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns;
import org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases;
import org.dyndns.jkiddo.dmp.Container;
import org.dyndns.jkiddo.dmp.Database;
import org.dyndns.jkiddo.dmp.chunks.media.DatabaseShareType;
import org.dyndns.jkiddo.dmp.chunks.media.ItemCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.LoginResponse;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmp.chunks.media.ServerInfoResponse;
import org.dyndns.jkiddo.service.daap.client.RequestHelper;
import org.junit.Test;

import com.google.common.base.Predicate;

public class TestImage {

	private String host;
	private Integer port;
	private Integer sessionId;
	private Database database;

	public TestImage() {
		host = "localhost";
		port = 8770;
	}

	@Test
	public void testSession() throws Exception {
		// logger.debug(String.format("trying login for host=%s and guid=%s",
		// host, pairingGuid));
		ServerInfoResponse serverInfoResponse = RequestHelper
				.requestParsed(String.format("%s/server-info",
						this.getRequestBase()));

		LoginResponse loginResponse = RequestHelper.requestParsed(String
				.format("%s/login", this.getRequestBase()));

		sessionId = loginResponse.getSessionId().getValue();

		// http://192.168.254.128:3689/databases?session-id=1301749047
		ServerDatabases serverDatabases = RequestHelper.requestParsed(String
				.format("%s/databases?session-id=%s", this.getRequestBase(),
						this.sessionId));

		// For now, the LocalDatabase is sufficient
		ListingItem localDatabase = serverDatabases.getListing()
				.getSingleListingItem(new Predicate<ListingItem>() {
					@Override
					public boolean apply(ListingItem input) {
						return DatabaseShareType.LOCAL == input
								.getSpecificChunk(DatabaseShareType.class)
								.getValue();
					}
				});

		String databaseName = localDatabase.getSpecificChunk(ItemName.class)
				.getValue();
		int itemId = localDatabase.getSpecificChunk(ItemId.class).getValue();
		long persistentId = localDatabase.getSpecificChunk(PersistentId.class)
				.getUnsignedValue().longValue();

		// fetch playlists to find the overall magic "Music" playlist
		DatabaseContainerns allPlaylists = RequestHelper
				.requestParsed(String
						.format("%s/databases/%d/containers?session-id=%s&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported",
								this.getRequestBase(), itemId, this.sessionId));

		// For now, the BasePlayList is sufficient
		ListingItem item = allPlaylists.getListing()
				.getSingleListingItemContainingClass(BaseContainer.class);

		Container playlist = new Container(item
				.getSpecificChunk(ItemName.class).getValue(), item
				.getSpecificChunk(PersistentId.class).getUnsignedValue()
				.longValue(), item.getSpecificChunk(ItemId.class)
				.getUnsignedValue(), item.getSpecificChunk(ItemCount.class)
				.getUnsignedValue());
		database = new Database(databaseName, itemId, persistentId, playlist);
	}

	private String getRequestBase() {
		return String.format("http://%s:%d", host, port);
	}
}
