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
 * Digital Audio Access Protocol (DAAP) Library
 * Copyright (C) 2004-2010 Roger Kapsi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dyndns.jkiddo.dmp.model;

import java.util.Collection;

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
	private Collection<Database> databases;

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

	public Collection<Database> getDatabases()
	{
		return databases;
	}

	public Database getDatabase(final long id)
	{
		return Iterables.find(databases, new Predicate<Database>() {

			@Override
			public boolean apply(Database input)
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
	public void addDatabase(Database database)
	{
		databases.add(database);
	}
}
