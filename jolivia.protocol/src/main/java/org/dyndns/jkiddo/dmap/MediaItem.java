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

package org.dyndns.jkiddo.dmap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.dyndns.jkiddo.dmap.chunks.BooleanChunk;
import org.dyndns.jkiddo.dmap.chunks.Chunk;
import org.dyndns.jkiddo.dmap.chunks.DateChunk;
import org.dyndns.jkiddo.dmap.chunks.LongChunk;
import org.dyndns.jkiddo.dmap.chunks.SByteChunk;
import org.dyndns.jkiddo.dmap.chunks.SShortChunk;
import org.dyndns.jkiddo.dmap.chunks.StringChunk;
import org.dyndns.jkiddo.dmap.chunks.UByteChunk;
import org.dyndns.jkiddo.dmap.chunks.UIntChunk;
import org.dyndns.jkiddo.dmap.chunks.UShortChunk;
import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmap.chunks.audio.SongBeatsPerMinute;
import org.dyndns.jkiddo.dmap.chunks.audio.SongBitrate;
import org.dyndns.jkiddo.dmap.chunks.audio.SongCategory;
import org.dyndns.jkiddo.dmap.chunks.audio.SongCodecSubtype;
import org.dyndns.jkiddo.dmap.chunks.audio.SongCodecType;
import org.dyndns.jkiddo.dmap.chunks.audio.SongComment;
import org.dyndns.jkiddo.dmap.chunks.audio.SongCompilation;
import org.dyndns.jkiddo.dmap.chunks.audio.SongComposer;
import org.dyndns.jkiddo.dmap.chunks.audio.SongContentDescription;
import org.dyndns.jkiddo.dmap.chunks.audio.SongContentRating;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDataKind;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDataUrl;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDateAdded;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDateModified;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDescription;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDisabled;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDiscCount;
import org.dyndns.jkiddo.dmap.chunks.audio.SongDiscNumber;
import org.dyndns.jkiddo.dmap.chunks.audio.SongEqPreset;
import org.dyndns.jkiddo.dmap.chunks.audio.SongFormat;
import org.dyndns.jkiddo.dmap.chunks.audio.SongGenre;
import org.dyndns.jkiddo.dmap.chunks.audio.SongGrouping;
import org.dyndns.jkiddo.dmap.chunks.audio.SongKeywords;
import org.dyndns.jkiddo.dmap.chunks.audio.SongLongDescription;
import org.dyndns.jkiddo.dmap.chunks.audio.SongRelativeVolume;
import org.dyndns.jkiddo.dmap.chunks.audio.SongSampleRate;
import org.dyndns.jkiddo.dmap.chunks.audio.SongSize;
import org.dyndns.jkiddo.dmap.chunks.audio.SongStartTime;
import org.dyndns.jkiddo.dmap.chunks.audio.SongStopTime;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTime;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTrackCount;
import org.dyndns.jkiddo.dmap.chunks.audio.SongTrackNumber;
import org.dyndns.jkiddo.dmap.chunks.audio.SongUserRating;
import org.dyndns.jkiddo.dmap.chunks.audio.SongYear;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.HasVideo;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSArtistId;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSComposerId;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSGenreId;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSPlaylistId;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSSongId;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.ITMSStorefrontId;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.NormVolume;
import org.dyndns.jkiddo.dmap.chunks.audio.extension.Podcast;
import org.dyndns.jkiddo.dmap.chunks.media.ContainerItemId;
import org.dyndns.jkiddo.dmap.chunks.media.ItemId;
import org.dyndns.jkiddo.dmap.chunks.media.ItemKind;
import org.dyndns.jkiddo.dmap.chunks.media.ItemName;
import org.dyndns.jkiddo.dmap.chunks.media.PersistentId;

/**
 * There isn't much to say: a Song is a Song.
 * <p>
 * Note: although already mentioned in StringChunk I'd like to point out that <code>null</code> is a valid value for DAAP. Use it to reset Strings. See StringChunk for more information!
 * </p>
 * 
 * @author Roger Kapsi
 */
public class MediaItem
{

	/** songId is an 32bit unsigned value! */
	private static final AtomicLong SONG_ID = new AtomicLong(1024);

	private static final SongFormat FORMAT = new SongFormat(SongFormat.MP3);

	private static final SongSampleRate SAMPLE_RATE = new SongSampleRate(SongSampleRate.KHZ_44100);

	private final Map<String, Chunk> chunks = new HashMap<String, Chunk>();

	private final ItemKind itemKind;
	private final ItemId itemId = new ItemId(SONG_ID.getAndIncrement());
	private final ItemName itemName = new ItemName();
	private final ContainerItemId containerItemId = new ContainerItemId();
	private final PersistentId persistentId = new PersistentId();

	private SongAlbum album;
	private SongArtist artist;
	private SongBeatsPerMinute bpm;
	private SongBitrate bitrate;
	private SongComment comment;
	private SongCompilation compilation;
	private SongComposer composer;
	private SongDataKind dataKind;
	private SongDataUrl dataUrl;
	private SongDateAdded dateAdded;
	private SongDateModified dateModified;
	private SongDescription description;
	private SongDisabled disabled;
	private SongDiscCount discCount;
	private SongDiscNumber discNumber;
	private SongEqPreset eqPreset;
	private SongFormat format;
	private SongGenre genre;
	private SongRelativeVolume relativeVolume;
	private SongSampleRate sampleRate;
	private SongSize size;
	private SongStartTime startTime;
	private SongStopTime stopTime;
	private SongTime time;
	private SongTrackCount trackCount;
	private SongTrackNumber trackNumber;
	private SongUserRating userRating;
	private SongYear year;
	private SongGrouping grouping;
	private NormVolume normVolume;
	private SongCodecType codecType;
	private SongCodecSubtype codecSubtype;

	private ITMSArtistId itmsArtistId;
	private ITMSComposerId itmsComposerId;
	private ITMSGenreId itmsGenreId;
	private ITMSPlaylistId itmsPlaylistId;
	private ITMSStorefrontId itmsStorefrontId;
	private ITMSSongId itmsSongId;

	// @since iTunes 5.0
	private Podcast podcast;
	private SongCategory category;
	private SongContentDescription contentDescription;
	private SongContentRating contentRating;
	private SongKeywords keywords;
	private SongLongDescription longDescription;

	// @since iTunes 6.0.2
	private HasVideo hasVideo;

	/** An arbitrary Object (most likely a File) */
	private Object attachment;

	/**
	 * Creates a new Song
	 */
	public MediaItem(ItemKind itemKind)
	{
		this.itemKind = itemKind;
		persistentId.setValue(itemId.getValue());
		containerItemId.setValue(itemId.getValue());
		init();
	}
	
	/*public Item()
	{
		this.itemKind = new ItemKind(ItemKind.AUDIO);
		persistentId.setValue(itemId.getValue());
		containerItemId.setValue(itemId.getValue());
		init();
	}*/

	/**
	 * Creates a new Song with the provided name
	 */
	/*public MediaItem(String name)
	{
		this();
		itemName.setValue(name);
	}*/

	private void init()
	{
		addChunk(itemKind);
		addChunk(itemName);
		addChunk(itemId);
		addChunk(containerItemId);

		// Some clients do not init format (implicit mp3)
		// and use uninitialized garbage instead
		addChunk(FORMAT);

		// VLC requires the sample rate
		addChunk(SAMPLE_RATE);
		
		//Added as part of debug
//		addChunk(new SongDisabled(true));
//		addChunk(new AlbumArtist());
//		addChunk(new EMediaKind(1));
//		addChunk(new DownloadStatus(true));
		
	}

	/**
	 * Returns the unique id of this song
	 */
	protected long getItemId()
	{
		return itemId.getUnsignedValue();
	}

	/**
	 * Returns the id of this Songs container. Note: same as getId()
	 */
	protected long getContainerId()
	{
		return containerItemId.getUnsignedValue();
	}

	/**
	 * Returns the name of this Song
	 */
	public String getName()
	{
		return getStringValue(itemName);
	}

	/**
	 * Sets the name of this Song
	 */
	public void setName(Transaction txn, String itemName)
	{
		setStringValue(txn, "itemName", itemName);
	}

	/**
	 * Sets the album of this Song
	 */
	public void setAlbum(Transaction txn, String album)
	{
		setStringValue(txn, "album", album);
	}

	/**
	 * Returns the album of this Song
	 */
	public String getAlbum()
	{
		return getStringValue(album);
	}

	/**
	 * Sets the artist of this Song
	 */
	public void setArtist(Transaction txn, String artist)
	{
		setStringValue(txn, "artist", artist);
	}

	/**
	 * Returns the artist of this Song
	 */
	public String getArtist()
	{
		return getStringValue(artist);
	}

	/**
	 * Sets the beats per minute of this Song
	 */
	public void setBeatsPerMinute(Transaction txn, int bpm)
	{
		setUShortValue(txn, "bpm", bpm);
	}

	/**
	 * Returns the beats per minute of this Song
	 */
	public int getBeatsPerMinute()
	{
		return getUShortValue(bpm);
	}

	/**
	 * Sets the bitrate of this Song
	 */
	public void setBitrate(Transaction txn, int bitrate)
	{
		setUShortValue(txn, "bitrate", bitrate);
	}

	/**
	 * Returns the bitrate of this Song
	 */
	public int getBitrate()
	{
		return getUShortValue(bitrate);
	}

	/**
	 * Sets the comment of this Song
	 */
	public void setComment(Transaction txn, String comment)
	{
		setStringValue(txn, "comment", comment);
	}

	/**
	 * Returns the comment of this Song
	 */
	public String getComment()
	{
		return getStringValue(comment);
	}

	/**
	 * Sets if this Song is a compilation
	 */
	public void setCompilation(Transaction txn, boolean compilation)
	{
		setBooleanValue(txn, "compilation", compilation);
	}

	/**
	 * Returns <tt>true</tt> if this Song is a compilation
	 */
	public boolean isCompilation()
	{
		return getBooleanValue(compilation);
	}

	/**
	 * Sets the composer of this Song
	 **/
	public void setComposer(Transaction txn, String composer)
	{
		setStringValue(txn, "composer", composer);
	}

	/**
	 * Returns the composer of this Song
	 */
	public String getComposer()
	{
		return getStringValue(composer);
	}

	/**
	 * Sets whether this Song is a Radio or a DAAP stream. See SongDataKind for more information. Note: you must set the DataUrl with setDataUrl() if dataKind is Radio!
	 */
	public void setDataKind(Transaction txn, int dataKind)
	{
		setUByteValue(txn, "dataKind", dataKind);
	}

	/**
	 * Returns the kind of this Song
	 */
	public int getDataKind()
	{
		return getUByteValue(dataKind);
	}

	/**
	 * Sets the URL of this Song
	 */
	public void setDataUrl(Transaction txn, String dataUrl)
	{
		setStringValue(txn, "dataUrl", dataUrl);
	}

	/**
	 * Returns the URL of this Song
	 */
	public String getDataUrl()
	{
		return getStringValue(dataUrl);
	}

	/**
	 * Sets the date when this Song was added to the Library. Note: the date is in seconds since 1970. <code>(int)(System.currentTimeMillis()/1000)</code>
	 */
	public void setDateAdded(Transaction txn, long dateAdded)
	{
		setDateValue(txn, "dateAdded", dateAdded);
	}

	/**
	 * Returns the date when this Song was added to the Library
	 */
	public long getDateAdded()
	{
		return getDateValue(dateAdded);
	}

	/**
	 * Sets the date when this Song was modified. Note: the date is in seconds since 1970. <code>(int)(System.currentTimeMillis()/1000)</code>
	 */
	public void setDateModified(Transaction txn, long dateModified)
	{
		setDateValue(txn, "dateModified", dateModified);
	}

	/**
	 * Returns the date when this song was modified
	 */
	public long getDateModified()
	{
		return getDateValue(dateModified);
	}

	/**
	 * Sets the description of this Song. Note: the description of a Song is its file format. The description of a MP3 file is for example 'MPEG Audio file'. See SongDescription for more information.
	 */
	public void setDescription(Transaction txn, String description)
	{
		setStringValue(txn, "description", description);
	}

	/**
	 * Returns the description of this Song
	 */
	public String getDescription()
	{
		return getStringValue(description);
	}

	/**
	 * Sets if this Song is either disabled or enabled. This is indicated in iTunes by the small checkbox next to the Song name.
	 */
	public void setDisabled(Transaction txn, boolean disabled)
	{
		setBooleanValue(txn, "disabled", disabled);
	}

	/**
	 * Returns <tt>true</tt> if this Song is disabled
	 */
	public boolean isDisabled()
	{
		return getBooleanValue(disabled);
	}

	/**
	 * Sets the number of discs of this Song
	 */
	public void setDiscCount(Transaction txn, int discCount)
	{
		setUShortValue(txn, "discCount", discCount);
	}

	/**
	 * Returns the number of discs
	 */
	public int getDiscCount()
	{
		return getUShortValue(discCount);
	}

	/**
	 * Sets the disc number of this Song
	 */
	public void setDiscNumber(Transaction txn, int discNumber)
	{
		setUShortValue(txn, "discNumber", discNumber);
	}

	/**
	 * Returns the disc number of this Song
	 */
	public int getDiscNumber()
	{
		return getUShortValue(discNumber);
	}

	/**
	 * Sets the equalizer of this Song. Note: See SongEqPreset for more information
	 */
	public void setEqPreset(Transaction txn, String eqPreset)
	{
		setStringValue(txn, "eqPreset", eqPreset);
	}

	/**
	 * Returns the equalizer of this Song
	 */
	public String getEqPreset()
	{
		return getStringValue(eqPreset);
	}

	/**
	 * Sets the format of this Song. Note: See SongFormat for more information
	 */
	public void setFormat(Transaction txn, String format)
	{
		setStringValue(txn, "format", format);
	}

	/**
	 * Returns the format of this Song
	 */
	public String getFormat()
	{
		return getStringValue(format);
	}

	/**
	 * Sets the genre of this Song. Note: See SongGenre for more information
	 */
	public void setGenre(Transaction txn, String genre)
	{
		setStringValue(txn, "genre", genre);
	}

	/**
	 * Returns the genre of this Song
	 */
	public String getGenre()
	{
		return getStringValue(genre);
	}

	/**
	 * Unknown purpose
	 */
	public void setRelativeVolume(Transaction txn, int relativeVolume)
	{
		setSByteValue(txn, "relativeVolume", relativeVolume);
	}

	/**
	 * Unknown purpose
	 */
	public int getRelativeVolume()
	{
		return getSByteValue(relativeVolume);
	}

	/**
	 * Sets the sample rate of this Song in kHz
	 */
	public void setSampleRate(Transaction txn, long sampleRate)
	{
		setUIntValue(txn, "sampleRate", sampleRate);
	}

	/**
	 * Returns the sample rate of this Song
	 */
	public long getSampleRate()
	{
		return getUIntValue(sampleRate);
	}

	/**
	 * Sets the file size of this Song
	 */
	public void setSize(Transaction txn, long size)
	{
		setUIntValue(txn, "size", size);
	}

	/**
	 * Returns the file size of this Song
	 */
	public long getSize()
	{
		return getUIntValue(size);
	}

	/**
	 * Sets the start time of this Song in <tt>milliseconds</tt>.
	 */
	public void setStartTime(Transaction txn, long startTime)
	{
		setUIntValue(txn, "startTime", startTime);
	}

	/**
	 * Returns the start time of this Song
	 */
	public long getStartTime()
	{
		return getUIntValue(startTime);
	}

	/**
	 * Sets the stop time of this Song in <tt>milliseconds</tt>.
	 */
	public void setStopTime(Transaction txn, long stopTime)
	{
		setUIntValue(txn, "stopTime", stopTime);
	}

	/**
	 * Returns the stop time of this Song
	 */
	public long getStopTime()
	{
		return getUIntValue(stopTime);
	}

	/**
	 * Sets the time (length) of this Song in <tt>milliseconds</tt>.
	 */
	public void setTime(Transaction txn, long time)
	{
		setUIntValue(txn, "time", time);
	}

	/**
	 * Returns the time (length) of this Song
	 */
	public long getTime()
	{
		return getUIntValue(time);
	}

	/**
	 * Sets the track count of this Song
	 */
	public void setTrackCount(Transaction txn, int trackCount)
	{
		setUShortValue(txn, "trackCount", trackCount);
	}

	/**
	 * Returns the track count of this Song
	 */
	public int getTrackCount()
	{
		return getUShortValue(trackCount);
	}

	/**
	 * Sets the track number of this Song
	 */
	public void setTrackNumber(Transaction txn, int trackNumber)
	{
		setUShortValue(txn, "trackNumber", trackNumber);
	}

	/**
	 * Returns the track number of this Song
	 */
	public int getTrackNumber()
	{
		return getUShortValue(trackNumber);
	}

	/**
	 * Sets the user rating of this Song. Note: See SongUserRating for more informations
	 */
	public void setUserRating(Transaction txn, int userRating)
	{
		setUByteValue(txn, "userRating", userRating);
	}

	/**
	 * Returns the user rating of this Song
	 */
	public int getUserRating()
	{
		return getUByteValue(userRating);
	}

	/**
	 * Sets the year of this Song
	 */
	public void setYear(Transaction txn, int year)
	{
		setUShortValue(txn, "year", year);
	}

	/**
	 * Returns the year of this Song
	 */
	public int getYear()
	{
		return getUShortValue(year);
	}

	/**
	 * Sets the grouping of this Song
	 */
	public void setGrouping(Transaction txn, String grouping)
	{
		setStringValue(txn, "grouping", grouping);
	}

	/**
	 * Returns the grouping of this Song
	 */
	public String getGrouping()
	{
		return getStringValue(grouping);
	}

	/**
	 * Sets the ITMS Artist Id
	 */
	public void setITMSArtistId(Transaction txn, long itmsArtistId)
	{
		setUIntValue(txn, "itmsArtistId", itmsArtistId);
	}

	/**
	 * Returns the ITMS Artist Id
	 */
	public long getITMSArtistId()
	{
		return getUIntValue(itmsArtistId);
	}

	/**
	 * Sets the ITMS Composer Id
	 */
	public void setITMSComposerId(Transaction txn, long itmsComposerId)
	{
		setUIntValue(txn, "itmsComposerId", itmsComposerId);
	}

	/**
	 * Returns the ITMS Composer Id
	 */
	public long getITMSComposerId()
	{
		return getUIntValue(itmsComposerId);
	}

	/**
	 * Sets the ITMS Genre Id
	 */
	public void setITMSGenreId(Transaction txn, long itmsGenreId)
	{
		setUIntValue(txn, "itmsGenreId", itmsGenreId);
	}

	/**
	 * Returns the ITMS Genre Id
	 */
	public long getITMSGenreId()
	{
		return getUIntValue(itmsGenreId);
	}

	/**
	 * Sets the ITMS Playlist (=Album) Id
	 */
	public void setITMSPlaylistId(Transaction txn, long itmsPlaylistId)
	{
		setUIntValue(txn, "itmsPlaylistId", itmsPlaylistId);
	}

	/**
	 * Returns the ITMS Playlist (=Album) Id
	 */
	public long getITMSPlaylistId()
	{
		return getUIntValue(itmsPlaylistId);
	}

	/**
	 * Sets the ITMS Storefront Id
	 */
	public void setITMSStorefrontId(Transaction txn, long itmsStorefrontId)
	{
		setUIntValue(txn, "itmsStorefrontId", itmsStorefrontId);
	}

	/**
	 * Returns the ITMS Storefront Id
	 */
	public long getITMSStrorefrontId()
	{
		return getUIntValue(itmsStorefrontId);
	}

	/**
	 * Sets the ITMS Song Id
	 */
	public void setITMSSongId(Transaction txn, long itmsSongId)
	{
		setUIntValue(txn, "itmsSongId", itmsSongId);
	}

	/**
	 * Returns the ITMS Song Id
	 */
	public long getITMSSongId()
	{
		return getUIntValue(itmsSongId);
	}

	/**
	 * Sets the codec type
	 */
	public void setCodecType(Transaction txn, long codecType)
	{
		setUIntValue(txn, "codecType", codecType);
	}

	/**
	 * Returns the codec type
	 */
	public long getCodecType()
	{
		return getUIntValue(codecType);
	}

	/**
	 * Sets the codec subtype
	 */
	public void setCodecSubtype(Transaction txn, long codecSubtype)
	{
		setUIntValue(txn, "codecSubtype", codecSubtype);
	}

	/**
	 * Returns the codec subtype
	 */
	public long getCodecSubtype()
	{
		return getUIntValue(codecSubtype);
	}

	/**
	 * Sets the norm volume
	 */
	public void setNormVolume(Transaction txn, long normVolume)
	{
		setUIntValue(txn, "normVolume", normVolume);
	}

	/**
	 * Returns the norm volume of this Song
	 */
	public long getNormVolume()
	{
		return getUIntValue(normVolume);
	}

	/**
     *
     */
	public void setPodcast(Transaction txn, boolean podcast)
	{
		setBooleanValue(txn, "podcast", podcast);
	}

	/**
     *
     */
	public boolean isPodcast()
	{
		return getBooleanValue(podcast);
	}

	/**
     *
     */
	public void setCategory(Transaction txn, String category)
	{
		setStringValue(txn, "category", category);
	}

	/**
     *
     */
	public String getCategory()
	{
		return getStringValue(category);
	}

	/**
     *
     */
	public void setContentDescription(Transaction txn, String contentDescription)
	{
		setStringValue(txn, "contentDescription", contentDescription);
	}

	/**
     *
     */
	public String getContentDescription()
	{
		return getStringValue(contentDescription);
	}

	/**
     *
     */
	public void setContentRating(Transaction txn, int contentRating)
	{
		setUByteValue(txn, "contentRating", contentRating);
	}

	/**
     *
     */
	public int getContentRating()
	{
		return getUByteValue(contentRating);
	}

	/**
     *
     */
	public void setKeywords(Transaction txn, String keywords)
	{
		setStringValue(txn, "keywords", keywords);
	}

	/**
     *
     */
	public String getKeywords()
	{
		return getStringValue(keywords);
	}

	/**
     *
     */
	public void setLongDescription(Transaction txn, String longDescription)
	{
		setStringValue(txn, "longDescription", longDescription);
	}

	/**
     *
     */
	public String getLongDescription()
	{
		return getStringValue(longDescription);
	}

	/**
	 * Sets wheather or not this Song contains Video data
	 */
	public void setHasVideo(Transaction txn, boolean hasVideo)
	{
		setBooleanValue(txn, "hasVideo", hasVideo);
	}

	/**
	 * Returns wheather or not this Song contains Video data
	 */
	public boolean hasVideo()
	{
		return getBooleanValue(hasVideo);
	}

	/**
*
*/
	public void addChunk(Chunk chunk)
	{
		if(chunk != null)
		{
			chunks.put(chunk.getName(), chunk);
		}
	}

	public Chunk getChunk(String name)
	{
		return chunks.get(name);
	}
	
	public Collection<Chunk> getChunks()
	{
		return Collections.unmodifiableCollection(chunks.values());
	}

	/**
	 * Sets a new attachment and returns the old attachment. The attachment is an arbitrary Object but most likely a File, path, URI etc.
	 */
	public Object setAttachment(Object attachment)
	{
		Object old = this.attachment;
		this.attachment = attachment;
		return old;
	}

	/**
	 * Returns the attachment.
	 */
	public Object getAttachment()
	{
		return attachment;
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append("Name: ").append(getName()).append("\n");
		buffer.append("ID: ").append(getItemId()).append("\n");
		buffer.append("Artist: ").append(getArtist()).append("\n");
		buffer.append("Album: ").append(getAlbum()).append("\n");
		buffer.append("Bitrate: ").append(getBitrate()).append("\n");
		buffer.append("Genre: ").append(getGenre()).append("\n");
		buffer.append("Comment: ").append(getComment()).append("\n");

		return buffer.append("\n").toString();
	}

	@Override
	public int hashCode()
	{
		return (int) getItemId();
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof MediaItem))
		{
			return false;
		}

		return ((MediaItem) o).getItemId() == getItemId();
	}

	protected void setBooleanValue(Transaction txn, String fieldName, boolean value)
	{
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setUByteValue(Transaction txn, String fieldName, int value)
	{
		UByteChunk.checkUByteRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setSByteValue(Transaction txn, String fieldName, int value)
	{
		SByteChunk.checkSByteRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setUShortValue(Transaction txn, String fieldName, int value)
	{
		UShortChunk.checkUShortRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setSShortValue(Transaction txn, String fieldName, int value)
	{
		SShortChunk.checkSShortRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setUIntValue(Transaction txn, String fieldName, long value)
	{
		UIntChunk.checkUIntRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setSIntValue(Transaction txn, String fieldName, int value)
	{
		// SIntChunk.checkSIntRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setULongValue(Transaction txn, String fieldName, long value)
	{
		// ULongChunk.checkULongRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setSLongValue(Transaction txn, String fieldName, long value)
	{
		// SLongChunk.checkSLongRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setStringValue(Transaction txn, String fieldName, String value)
	{
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected void setDateValue(Transaction txn, String fieldName, long value)
	{
		DateChunk.checkDateRange(value);
		if(txn != null)
		{
			txn.addTxn(this, createNewTxn(fieldName, value));
		}
		else
		{
			setValue(fieldName, value);
		}
	}

	protected boolean getBooleanValue(BooleanChunk chunk)
	{
		return (chunk != null) ? chunk.getBooleanValue() : false;
	}

	protected int getSByteValue(SByteChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : 0;
	}

	protected int getUByteValue(UByteChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : 0;
	}

	protected int getUShortValue(UShortChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : 0;
	}

	protected long getUIntValue(UIntChunk chunk)
	{
		return (chunk != null) ? chunk.getUnsignedValue() : 0;
	}

	protected long getLongValue(LongChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : 0;
	}

	protected long getDateValue(DateChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : 0;
	}

	protected String getStringValue(StringChunk chunk)
	{
		return (chunk != null) ? chunk.getValue() : null;
	}

	protected Txn createNewTxn(final String name, boolean value)
	{
		return createNewTxn(name, boolean.class, new Boolean(value));
	}

	protected Txn createNewTxn(final String name, int value)
	{
		return createNewTxn(name, int.class, new Integer(value));
	}

	protected Txn createNewTxn(final String name, long value)
	{
		return createNewTxn(name, long.class, new Long(value));
	}

	protected Txn createNewTxn(final String name, String value)
	{
		return createNewTxn(name, String.class, value);
	}

	protected Txn createNewTxn(final String fieldName, final Class<?> valueClass, final Object value)
	{
		Txn txn = new Txn() {
			@Override
			public void commit(Transaction txn)
			{
				setValue(fieldName, valueClass, value);
			}
		};

		return txn;
	}

	protected void setValue(String fieldName, boolean value)
	{
		setValue(fieldName, boolean.class, new Boolean(value));
	}

	protected void setValue(String fieldName, int value)
	{
		setValue(fieldName, int.class, new Integer(value));
	}

	protected void setValue(String fieldName, long value)
	{
		setValue(fieldName, long.class, new Long(value));
	}

	protected void setValue(String fieldName, String value)
	{
		setValue(fieldName, String.class, value);
	}

	protected void setValue(String fieldName, Class<?> valueClass, Object value)
	{
		try
		{

			Field field = MediaItem.class.getDeclaredField(fieldName);
			field.setAccessible(true);

			Chunk chunk = (Chunk) field.get(this);
			if(chunk == null)
			{
				Constructor<?> con = field.getType().getConstructor(new Class[] { valueClass });
				chunk = (Chunk) con.newInstance(new Object[] { value });
				field.set(this, chunk);
				addChunk(chunk);
			}
			else
			{
				Method method = field.getType().getMethod("setValue", new Class[] { valueClass });
				method.invoke(chunk, new Object[] { value });
			}

		}
		catch(SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch(IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch(NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
		catch(NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch(InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
		catch(InstantiationException e)
		{
			throw new RuntimeException(e);
		}
	}
}
