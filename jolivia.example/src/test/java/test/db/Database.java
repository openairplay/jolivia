package test.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "databases")
public class Database
{
	public int getItemId()
	{
		return itemId;
	}

	public Database()
	{
	}

	public Database(Library library)
	{
		this.library = library;
	}

	public Library getLibrary()
	{
		return library;
	}

	@DatabaseField(generatedId = true)
	private int itemId;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Library library;

	@DatabaseField
	private String name;

	@DatabaseField
	private String type;

	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
	private Container masterPlaylist;

	public Container getMasterPlaylist()
	{
		return masterPlaylist;
	}

	public void setMasterPlaylist(Container masterPlaylist)
	{
		this.masterPlaylist = masterPlaylist;
	}

	public void addMediaItem(MediaItem mediaItem)
	{
		masterPlaylist.addMediaItem(mediaItem);
	}
}
