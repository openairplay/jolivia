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

package org.dyndns.jkiddo.dmp.chunks.media;

import java.util.NoSuchElementException;

import org.dyndns.jkiddo.dmp.chunks.Chunk;
import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Used to group ListingItems objects.
 * 
 * @see ListingItem
 * @author Roger Kapsi
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapChunkDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapChunkDefinition.mlcl)
public class Listing extends ContainerChunk
{
	public final static Logger logger = LoggerFactory.getLogger(Listing.class);

	public Listing()
	{
		super("mlcl", "dmap.listing");
	}

	public Iterable<ListingItem> getListingItems()
	{
		return getMultipleChunks(ListingItem.class);
	}

	public Iterable<ListingItem> getListingItems(Predicate<ListingItem> predicate)
	{
		return Iterables.filter(getListingItems(), predicate);
	}

	public ListingItem getSingleListingItemContainingClass(final Class<? extends Chunk> clazz)
	{
		return getSingleListingItem(new Predicate<ListingItem>() {
			@Override
			public boolean apply(ListingItem input)
			{
				return input.getSpecificChunk(clazz) != null ? true : false;
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListingItem getSingleListingItem(Predicate predicate)
	{
		Iterable<ListingItem> filteredItems = Iterables.filter(getListingItems(), predicate);

		if(Iterables.size(filteredItems) < 1)
		{
			throw new NoSuchElementException("Found no ListingItems fullfilling predicate");
		}
		if(Iterables.size(filteredItems) > 1)
		{
			logger.info("Found more than one ListingItem fullfilling predicate - returning the first one");
		}

		return filteredItems.iterator().next();
	}
}
