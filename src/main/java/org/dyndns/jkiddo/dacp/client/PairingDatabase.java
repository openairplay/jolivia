package org.dyndns.jkiddo.dacp.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.io.File;

import com.google.inject.Singleton;

@Singleton
public class PairingDatabase implements IDatabase
{

	private static final String DB_NAME = "pairing.db";

	private final static String TABLE_PAIR = "pairing";
	private final static String FIELD_PAIR_SERVICENAME = "servicename";
	private final static String FIELD_PAIR_GUID = "guid";
	private final static String KEY_PAIRING_CODE = "pair";
	private final static String KEY_SERVICE_GUID = "serviceguid";
	private final static String KEY_LAST_SESSION = "lastsession";

	protected final static byte[] CHAR_TABLE = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

	private static String configDirectory = "";

	private Connection connection = null;

	public static void setConfigDirectory(String configDirectory)
	{
		if(configDirectory != null)
		{
			PairingDatabase.configDirectory = new String(configDirectory + File.separator);
		}
	}

	private void initDBConnection()
	{
		if(connection == null)
		{
			try
			{
				System.setProperty("sqlite.purejava", "true");
				Class.forName("org.sqlite.JDBC");

				// create a database connection
				try
				{
					Statement statement = null;

					this.connection = DriverManager.getConnection("jdbc:sqlite:" + configDirectory + DB_NAME);

					statement = connection.createStatement();
					statement.setQueryTimeout(30); // set timeout to 30 sec.

					statement.executeUpdate("create table if not exists " + TABLE_PAIR + "(" + FIELD_PAIR_SERVICENAME + " text primary key, " + FIELD_PAIR_GUID + " text)");

					Random random = new Random();

					// generate pair code
					byte[] pair = new byte[8];
					random.nextBytes(pair);
					statement.executeUpdate("insert or ignore into " + TABLE_PAIR + " values ('" + KEY_PAIRING_CODE + "', '" + toHex(pair) + "');");

					// generate remote guid
					// this is the thing that uniquely identifies this remote
					byte[] serviceguid = new byte[20];
					random.nextBytes(serviceguid);
					statement.executeUpdate("insert or ignore into " + TABLE_PAIR + " values ('" + KEY_SERVICE_GUID + "', '" + toHex(serviceguid) + "');");

					statement.close();
				}
				catch(SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				// sqlite distributed with app. real problems if we get here so exit
				System.exit(1);
			}
		}
	}

	@Override
	public String findCode(String serviceName)
	{
		initDBConnection();

		String result = null;

		if(serviceName != null)
		{
			try
			{
				Statement statement = connection.createStatement();

				ResultSet rs = statement.executeQuery("select " + FIELD_PAIR_GUID + " from " + TABLE_PAIR + " where " + FIELD_PAIR_SERVICENAME + " = '" + serviceName + "';");

				if(rs.next())
				{
					result = rs.getString(FIELD_PAIR_GUID);
				}

				statement.close();
			}
			catch(SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public void updateCode(String serviceName, String guid)
	{
		initDBConnection();

		if(serviceName != null && guid != null)
		{
			try
			{
				Statement statement = connection.createStatement();
				statement.executeUpdate("insert or replace into " + TABLE_PAIR + " values ('" + serviceName + "', '" + guid + "');");
				statement.close();
			}
			catch(SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	@Override
	public String getLastSession()
	{
		return findCode(KEY_LAST_SESSION);
	}

	@Override
	public void setLastSession(String serviceName)
	{
		updateCode(KEY_LAST_SESSION, serviceName);
	}

	public static String toHex(byte[] code)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b : code)
		{
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString().toUpperCase();
	}
}
