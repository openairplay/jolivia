/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
/*
 TunesRemote+ - http://code.google.com/p/tunesremote-plus/

 Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
 Copyright (C) 2010 TunesRemote+, http://code.google.com/p/tunesremote-plus/

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 The Initial Developer of the Original Code is Jeffrey Sharkey.
 Portions created by Jeffrey Sharkey are
 Copyright (C) 2008. Jeffrey Sharkey, http://jsharkey.org/
 All Rights Reserved.
 */

package org.dyndns.jkiddo.service.daap.client;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.dyndns.jkiddo.dmap.chunks.audio.BaseContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.DatabaseContainerns;
import org.dyndns.jkiddo.dmap.chunks.audio.ItemsContainer;
import org.dyndns.jkiddo.dmap.chunks.audio.ServerDatabases;
import org.dyndns.jkiddo.dmcp.chunks.media.audio.DataControlInt;
import org.dyndns.jkiddo.dmp.chunks.media.ContentCodesResponse;
import org.dyndns.jkiddo.dmp.chunks.media.DatabaseShareType;
import org.dyndns.jkiddo.dmp.chunks.media.ItemId;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.chunks.media.LoginResponse;
import org.dyndns.jkiddo.dmp.chunks.media.PersistentId;
import org.dyndns.jkiddo.dmp.chunks.media.ServerInfoResponse;
import org.dyndns.jkiddo.dmp.chunks.media.UpdateResponse;
import org.dyndns.jkiddo.dmp.model.Container;
import org.dyndns.jkiddo.dmp.model.Database;
import org.dyndns.jkiddo.service.dmap.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class Session
{
	public final static Logger logger = LoggerFactory.getLogger(Session.class);

	private final String host;
	private long revision = 1;
	private final int port;

	private final int sessionId;

	protected final Database database, radioDatabase;

	private final Library library;
	private final RemoteControl remoteControl;

	private final String homeSharingGid;

	private byte[] cert;

	public Session(final String host, final int port, final String username, final String password) throws Exception
	{
		// start a session with the iTunes server
		this.host = host;
		this.port = port;

		getServerInfo();

		logger.debug(String.format("trying login for host=%s", host));
		final NSDictionary nsDic = Util.requestPList(username, password);
		homeSharingGid = "&hsgid=" + ((NSString) nsDic.get("sgid")).getContent();
		final LoginResponse loginResponse = doLoginWithHomeSharingGid(((NSString) nsDic.get("sgid")).getContent());

		sessionId = loginResponse.getSessionId().getValue();

		getControlInt();


		fp_setup_first();
		fp_setup_second();


		//final String hspid = ((NSString) nsDic.get("spid")).getContent();
		final String hspid = "0";

		verifyHomeShare(hspid);
		// Update revision at once. As the initial call, this does not block but simply updates the revision.

		// See if adding hasFP=1 and hsgid could resolve the following calls

		// updateServerRevision();
		// getUpdateBlocking();

		library = new Library(this);
		remoteControl = new RemoteControl(this);

		final ServerDatabases serverDatabases = getServerDatabases();
		database = getLocalDatabase(serverDatabases);
		radioDatabase = getRadioDatabase(serverDatabases);
	}

	public Session(final String host, final int port, final String pairingGuid) throws Exception
	{
		homeSharingGid = "";
		// start a session with the iTunes server
		this.host = host;
		this.port = port;

		getServerInfo();

		// http://192.168.254.128:3689/login?pairing-guid=0x0000000000000001
		logger.debug(String.format("trying login for host=%s and guid=%s", host, pairingGuid));
		final LoginResponse loginResponse = doLogin(pairingGuid);

		sessionId = loginResponse.getSessionId().getValue();

		getControlInt();
		// Update revision at once. As the initial call, this does not block but simply updates the revision.

		// updateServerRevision();
		getUpdateBlocking();

		library = new Library(this);
		remoteControl = new RemoteControl(this);

		final ServerDatabases serverDatabases = getServerDatabases();
		database = getLocalDatabase(serverDatabases);
		radioDatabase = getRadioDatabase(serverDatabases);
	}

	public long getRevision()
	{
		return revision;
	}

	public int getSessionId()
	{
		return sessionId;
	}

	public Database getDatabase()
	{
		return database;
	}

	public Database getRadioDatabase()
	{
		return radioDatabase;
	}

	public Library getLibrary()
	{
		return library;
	}

	public RemoteControl getRemoteControl()
	{
		return remoteControl;
	}

	protected Database getRadioDatabase(final ServerDatabases serverDatabases) throws Exception
	{
		// Radio database
		final ListingItem database;
		try
		{
			database = serverDatabases.getListing().getSingleListingItem(new Predicate<ListingItem>() {
				@Override
				public boolean apply(final ListingItem input)
				{
					return DatabaseShareType.RADIO == input.getSpecificChunk(DatabaseShareType.class).getValue();
				}
			});
		}
		catch(final NoSuchElementException nee)
		{
			logger.debug("No radio databases found", nee);
			return null;
		}

		final String databaseName = database.getSpecificChunk(ItemName.class).getValue();
		final int itemId = database.getSpecificChunk(ItemId.class).getValue();
		final long persistentId = database.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue();
		final Database rd = new Database(databaseName, itemId, persistentId);

		// The following causes iTunes to hang ... TODO why?
		// DatabaseContainerns allPlaylists = getMasterDatabaseContainerList(itemId);
		//
		// Iterable<ListingItem> items = allPlaylists.getListing().getListingItems();
		// for(ListingItem item : items)
		// {
		// Container playlist = new Container(item.getSpecificChunk(ItemName.class).getValue(), 0, item.getSpecificChunk(ItemId.class).getUnsignedValue(), item.getSpecificChunk(ItemCount.class).getUnsignedValue());
		// logger.debug(String.format("found radio genre=%s", playlist.getName()));
		// rd.addPlaylist(playlist);
		// }
		return rd;

	}

	protected Database getLocalDatabase(final ServerDatabases serverDatabases) throws Exception
	{
		// Local database
		// For now, the LocalDatabase is sufficient
		final ListingItem localDatabase = serverDatabases.getListing().getSingleListingItem(new Predicate<ListingItem>() {
			@Override
			public boolean apply(final ListingItem input)
			{
				return DatabaseShareType.LOCAL == input.getSpecificChunk(DatabaseShareType.class).getValue();
			}
		});

		final String databaseName = localDatabase.getSpecificChunk(ItemName.class).getValue();
		final int itemId = localDatabase.getSpecificChunk(ItemId.class).getValue();
		final long persistentId = localDatabase.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue();

		// fetch playlists to find the overall magic "Music" playlist
		final DatabaseContainerns allPlaylists = getDatabaseContainerList(itemId);

		for(final ListingItem container : allPlaylists.getListing().getListingItems())
		{
			getContainerDetails(itemId, container.getSpecificChunk(ItemId.class).getValue());
		}

		// For now, the BasePlayList is sufficient
		final ListingItem item = allPlaylists.getListing().getSingleListingItemContainingClass(BaseContainer.class);
		final Container playlist = new Container(item.getSpecificChunk(ItemName.class).getValue(), item.getSpecificChunk(PersistentId.class).getUnsignedValue().longValue(), item.getSpecificChunk(ItemId.class).getUnsignedValue(), item.getSpecificChunk(BaseContainer.class).getValue());
		return new Database(databaseName, itemId, persistentId, playlist);
	}

	private ItemsContainer getContainerDetails(final int databaseId, final int containerId) throws Exception
	{
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers/%d/items?session-id=%s&type=music&meta=dmap.itemkind,dmap.itemid,dmap.containeritemid" + homeSharingGid, this.getRequestBase(), databaseId, containerId, sessionId));
	}

	protected DatabaseContainerns getDatabaseContainerList(final int databaseId) throws Exception
	{
		return RequestHelper.requestParsed(String.format("%s/databases/%d/containers?session-id=%s&meta=dmap.itemid,dmap.itemname,dmap.persistentid,dmap.parentcontainerid,com.apple.itunes.is-podcast-playlist,com.apple.itunes.special-playlist,com.apple.itunes.smart-playlist,dmap.haschildcontainers,com.apple.itunes.saved-genius" + homeSharingGid, this.getRequestBase(), databaseId, this.sessionId));
	}

	protected ServerDatabases getServerDatabases() throws Exception
	{
		return RequestHelper.requestParsed(String.format("%s/databases?session-id=%s" + homeSharingGid, this.getRequestBase(), this.sessionId));
	}

	private LoginResponse doLoginWithHomeSharingGid(final String gid) throws Exception
	{
		return RequestHelper.requestParsed(String.format("%s/login?hasFP=1&hsgid=%s", this.getRequestBase(), gid));
	}

	protected LoginResponse doLogin(final String pairingGuid) throws Exception
	{
		return RequestHelper.requestParsed(String.format("%s/login?pairing-guid=0x%s", this.getRequestBase(), pairingGuid));
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
		return RequestHelper.requestParsed(String.format("%s/server-info", this.getRequestBase()));
	}

	public DataControlInt getControlInt() throws Exception
	{
		if("".equals(homeSharingGid))
			return RequestHelper.requestParsed(String.format("%s/ctrl-int", this.getRequestBase()));
		else
			return RequestHelper.requestParsed(String.format("%s/ctrl-int?" + homeSharingGid.subSequence(1, homeSharingGid.length()), this.getRequestBase()));
	}

	// Query the media server about the content codes it handles
	public ContentCodesResponse getContentCodes() throws Exception
	{
		return RequestHelper.requestParsed(String.format("%s/content-codes?session-id=%s" + homeSharingGid, this.getRequestBase(), this.sessionId));
	}

	/**
	 * What is currently known is that pausing a playing number does not release it, but eg. changing to next song does.
	 * 
	 * @return
	 * @throws Exception
	 */
	public UpdateResponse getUpdateBlocking() throws Exception
	{
		// try fetching next revision update using socket keepalive
		// approach
		// using the next revision-number will make itunes keepalive
		// until something happens
		// GET /update?revision-number=1&daap-no-disconnect=1&session-id=1250589827

		final UpdateResponse state = RequestHelper.requestParsed(String.format("%s/update?revision-number=%d&daap-no-disconnect=1&session-id=%s" + homeSharingGid, this.getRequestBase(), revision, sessionId), true);
		revision = state.getServerRevision().getUnsignedValue();
		return state;
	}

	public UpdateResponse updateServerRevision() throws Exception
	{
		final UpdateResponse state = RequestHelper.requestParsed(String.format("%s/update?session-id=%s&revision-number=%s&delta=0" + homeSharingGid, this.getRequestBase(), sessionId, revision), true);
		revision = state.getServerRevision().getUnsignedValue();
		return state;
	}

	private void verifyHomeShare(final String hspid) throws Exception
	{
		RequestHelper.requestParsed(String.format("%s/home-share-verify?hspid=" + hspid + "&session-id=%s" + homeSharingGid, this.getRequestBase(), this.sessionId));
	}

	private void fp_setup_first() throws Exception
	{

		final byte[] value = new byte[] { 2, 0, 2, (byte) 187 };
		final byte[] nr = { 1 };
		final ArrayList<byte[]> bytes = Lists.newArrayList("FPLYd".getBytes(), new byte[] { 1 }, nr, new byte[] { 0, 0, 0, 0 }, new byte[] { (byte) value.length }, value);
		cert = RequestHelper.requestPost(String.format("%s/fp-setup?session-id=%s" + homeSharingGid, this.getRequestBase(), this.sessionId), concatenateByteArrays(bytes));
		System.out.println(Util.toHex(cert));
		return;
	}

	private void fp_setup_second() throws Exception
	{
		RequestHelper.requestPost(String.format("%s/fp-setup?session-id=%s" + homeSharingGid, this.getRequestBase(), this.sessionId), cert);

	}

	private static byte[] concatenateByteArrays(final List<byte[]> blocks)
	{
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		for(final byte[] b : blocks)
		{
			os.write(b, 0, b.length);
		}
		return os.toByteArray();
	}
}
