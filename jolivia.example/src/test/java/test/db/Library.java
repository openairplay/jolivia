package test.db;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "libraries")
public class Library
{
	@DatabaseField(generatedId = true)
	private int itemId;

	public Library()
	{}

	public Library(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@DatabaseField
	private String name;

	public Collection<Database> getDatabases()
	{
		return databases;
	}

	public Database getDatabase(final int id)
	{
		return Iterables.find(databases, new Predicate<Database>() {

			@Override
			public boolean apply(Database input)
			{
				return input.getItemId() == id;
			}
		});
	}

	@ForeignCollectionField(eager = true)
	private Collection<Database> databases;
}
