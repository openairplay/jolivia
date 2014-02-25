package test.db;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "containers")
public class Container
{
	@ForeignCollectionField()
	private Collection<MediaItem> mediaItems;

	@DatabaseField(generatedId = true)
	private int itemId;

	@DatabaseField(foreign = true)
	private Database database;

	public Container()
	{}

	public void addMediaItem(MediaItem mediaItem)
	{
		mediaItems.add(mediaItem);
	}
	public Collection<MediaItem> getMediaItems()
	{
		return mediaItems;
	}
	public int getItemId()
	{
		return itemId;
	}

}
