package org.dyndns.jkiddo.dmp.model;
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


import java.util.Collection;

import org.dyndns.jkiddo.dmp.IDatabase;
import org.dyndns.jkiddo.dmp.ILibrary;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "libraries")
public class Library implements ILibrary
{
	@DatabaseField(generatedId = true)
	private int itemId;

	@DatabaseField
	private String name;

	@DatabaseField
	private int revision;

	@ForeignCollectionField(eager = true)
	private Collection<IDatabase> databases;

	public Library()
	{}

	public Library(String name)
	{
		this.name = name;
		this.revision = 1;
		databases = Sets.newHashSet();
	}

	public String getName()
	{
		return name;
	}

	public Collection<IDatabase> getDatabases()
	{
		return databases;
	}

	public IDatabase getDatabase(final long id)
	{
		return Iterables.find(databases, new Predicate<IDatabase>() {

			@Override
			public boolean apply(IDatabase input)
			{
				return input.getItemId() == id;
			}
		});
	}

	@Override
	public int getRevision()
	{
		return revision;
	}

	@Override
	public void addDatabase(IDatabase database)
	{
		databases.add(database);
	}
}
