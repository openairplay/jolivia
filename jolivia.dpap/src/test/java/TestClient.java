import org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns;
import org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.DataControlInt;
import org.dyndns.jkiddo.dmp.IDatabase;
import org.dyndns.jkiddo.dmp.chunks.media.ContentCodesResponse;
import org.dyndns.jkiddo.dmp.chunks.media.DatabaseShareType;
import org.dyndns.jkiddo.dmp.chunks.media.ItemCount;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.LoginResponse;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmp.chunks.media.ServerInfoResponse;
import org.dyndns.jkiddo.dmp.model.Container;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.service.daap.client.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

public class TestClient
{

	class DPAPClient
	{
		public final Logger logger = LoggerFactory.getLogger(DPAPClient.class);
		private final String host;
		private long revision = 1;
		private final int port, sessionId;
		protected final IDatabase database;

		public long getRevision()
		{
			return revision;
		}

		public int getSessionId()
		{
			return sessionId;
		}

		public IDatabase getDatabase()
		{
			return database;
		}

		public DPAPClient(String host, int port) throws Exception
		{
			// start a session with the itunes server
			this.host = host;
			this.port = port;

			getServerInfo();

			// http://192.168.254.128:3689/login?pairing-guid=0x0000000000000001
			logger.debug(String.format("trying login for host=%s", host));
			LoginResponse loginResponse = RequestHelper.requestParsed(String.format("%s/login", this.getRequestBase()));

			sessionId = loginResponse.getSessionId().getValue();

			getControlInt();

			// Update revision at once. As the initial call, this does not block but simply updates the revision.

			// http://192.168.254.128:3689/databases?session-id=1301749047
			ServerDatabases serverDatabases = RequestHelper.requestParsed(String.format("%s/databases?session-id=%s", this.getRequestBase(), this.sessionId));

			// Local database
			{
				// For now, the LocalDatabase is sufficient
				ListingItem localDatabase = serverDatabases.getListing().getSingleListingItem(new Predicate<ListingItem>() {
					@Override
					public boolean apply(ListingItem input)
					{
						return DatabaseShareType.LOCAL == input.getSpecificChunk(DatabaseShareType.class).getValue();
					}
				});

				String databaseName = localDatabase.getSpecificChunk(ItemName.class).getValue();
				int itemId = localDatabase.getSpecificChunk(ItemId.class).getValue();
				long persistentId = localDatabase.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue();

				// fetch playlists to find the overall magic "Music" playlist
				DatabaseContainerns allPlaylists = RequestHelper.requestParsed(String.format("%s/databases/%d/containers?session-id=%s&meta=dmap.itemname,dmap.itemcount,dmap.itemid,dmap.persistentid,daap.baseplaylist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,com.apple.itunes.saved-genius,dmap.parentcontainerid,dmap.editcommandssupported", this.getRequestBase(), itemId, this.sessionId));

				// For now, the BasePlayList is sufficient
				ListingItem item = allPlaylists.getListing().getSingleListingItemContainingClass(BaseContainer.class);

				Container playlist = new Container(item.getSpecificChunk(ItemName.class).getValue(), item.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue(), item.getSpecificChunk(ItemId.class).getUnsignedValue(), item.getSpecificChunk(ItemCount.class).getUnsignedValue());
				database = new Database(databaseName, itemId, persistentId, playlist);
			}

		}

		protected String getRequestBase()
		{
			return String.format("http://%s:%d", host, port);
		}

		/**
		 * Logout method disconnects the session on the server. This is being a good DACP citizen that was not happening in previous versions.
		 * 
		 * @throws Exception
		 */
		public void logout() throws Exception
		{
			RequestHelper.dispatch(String.format("%s/logout?session-id=%s", this.getRequestBase(), this.sessionId));
		}

		public ServerInfoResponse getServerInfo() throws Exception
		{
			ServerInfoResponse serverInfoResponse = RequestHelper.requestParsed(String.format("%s/server-info", this.getRequestBase()));
			return serverInfoResponse;
		}

		public DataControlInt getControlInt() throws Exception
		{
			DataControlInt serverInfoResponse = RequestHelper.requestParsed(String.format("%s/ctrl-int", this.getRequestBase()));
			return serverInfoResponse;
		}

		// Query the media server about the content codes it handles
		public ContentCodesResponse getContentCodes() throws Exception
		{
			return RequestHelper.requestParsed(String.format("%s/content-codes?session-id=%s", this.getRequestBase(), this.sessionId));
		}
	}
}