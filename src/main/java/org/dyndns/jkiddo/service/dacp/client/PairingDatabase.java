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
package org.dyndns.jkiddo.service.dacp.client;

import java.io.UnsupportedEncodingException;

import org.dyndns.jkiddo.Jolivia;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class PairingDatabase implements IPairingDatabase
{
	public static final String DB_URL = "DB_URL";

	private final String serviceguid = Jolivia.toHex("Jolivia".getBytes("UTF-8"));
	private final String randomPairCode = Jolivia.toHex("Jolivia!".getBytes("UTF-8"));

	public String getRandomPairCode() {
		return randomPairCode;
	}

	@Inject
	public PairingDatabase(@Named(DB_URL) String url) throws ClassNotFoundException, UnsupportedEncodingException
	{
		Class.forName("org.sqlite.JDBC");

		dbi = new DBI(url);
		dbHandler = dbi.open().attach(PairingDatabaseCommands.class);
		dbHandler.createTable();

		// assert random pair code
		Preconditions.checkState(randomPairCode.length() == 16, "Random paircode did not match expected length");
		dbHandler.updateEntry(KEY_PAIRING_CODE, randomPairCode);

		// assert the guid that uniquely identifies this remote
		Preconditions.checkState(serviceguid.length() == 14, "Service GUID did not match expected length");
		dbHandler.updateEntry(KEY_SERVICE_GUID, serviceguid);
	}

	private final DBI dbi;
	private final PairingDatabaseCommands dbHandler;

	private final static String TABLE_PAIR = "pairing";
	private final static String FIELD_PAIR_SERVICENAME = "servicename";
	private final static String FIELD_PAIR_GUID = "guid";
	private final static String KEY_PAIRING_CODE = "pair";
	private final static String KEY_SERVICE_GUID = "serviceguid";

	private interface PairingDatabaseCommands
	{
		@SqlUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_PAIR + " (" + FIELD_PAIR_SERVICENAME + " text primary key, " + FIELD_PAIR_GUID + " text)")
		public void createTable();

		@SqlUpdate("insert or replace into " + TABLE_PAIR + " (" + FIELD_PAIR_SERVICENAME + "," + FIELD_PAIR_GUID + ") VALUES (:" + FIELD_PAIR_SERVICENAME + ", :" + FIELD_PAIR_GUID + ")")
		public void updateEntry(@Bind(FIELD_PAIR_SERVICENAME) String serviceName, @Bind(FIELD_PAIR_GUID) String guid);

		@SqlQuery("select " + FIELD_PAIR_GUID + " from " + TABLE_PAIR + " where " + FIELD_PAIR_SERVICENAME + " = :" + FIELD_PAIR_SERVICENAME + "")
		public String getCode(@Bind(FIELD_PAIR_SERVICENAME) String servicename);
	}

	@Override
	public String findCode(String serviceName)
	{
		return dbHandler.getCode(serviceName);
	}

	@Override
	public void updateCode(String serviceName, String guid)
	{
		if(serviceName != null && guid != null)
		{
			dbHandler.updateEntry(serviceName, guid);
		}
	}
	@Override
	public String getPairCode()
	{
		return findCode(KEY_PAIRING_CODE);
	}

	@Override
	public String getServiceGuid()
	{
		return findCode(KEY_SERVICE_GUID);
	}
}
