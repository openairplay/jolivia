package test.db;

import java.util.ArrayList;
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "containers")
public class Container
{
	@ForeignCollectionField
	private Collection<MediaItem> mediaItems = new ArrayList<MediaItem>();

	@DatabaseField(generatedId = true)
	private String name;
	public Container()
	{
		// TODO Auto-generated constructor stub
	}
	public void addMediaItem(MediaItem mediaItem)
	{
		// TODO Auto-generated method stub

	}

}
