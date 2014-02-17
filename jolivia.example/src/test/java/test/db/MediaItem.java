package test.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mediaitems")
public class MediaItem
{
	@DatabaseField(generatedId = true)
	private int itemID;
	
	@DatabaseField(foreign = true)
	private Database database;
}
