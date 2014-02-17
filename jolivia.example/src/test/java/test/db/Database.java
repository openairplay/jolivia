package test.db;

import java.util.ArrayList;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "databases")
public class Database
{
	public Database()
	{}
	
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
	
	@DatabaseField(foreign = true)
	private Library library;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String type;
	
	@ForeignCollectionField
	private Collection<MediaItem> mediaItems =  new ArrayList<MediaItem>();;

	private Container masterPlaylist;
	
	public Container getMasterPlaylist()
	{
		return masterPlaylist;
	}
	
	public void addMediaItem(MediaItem mediaItem)
	{
		masterPlaylist.addMediaItem(mediaItem);
	}
}
