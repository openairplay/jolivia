/*
    TunesRemote+ - http://code.google.com/p/tunesremote-plus/
    
    Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
    Copyright (C) 2010 TunesRemote+, http://code.google.com/p/tunesremote-plus/
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    The Initial Developer of the Original Code is Jeffrey Sharkey.
    Portions created by Jeffrey Sharkey are
    Copyright (C) 2008. Jeffrey Sharkey, http://jsharkey.org/
    All Rights Reserved.
 */

package org.dyndns.jkiddo.daap.client;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.ardverk.daap.chunks.impl.dmap.Dictionary;
import org.ardverk.daap.chunks.impl.dmap.ItemName;
import org.ardverk.daap.chunks.impl.unknown.RelativeVolume;
import org.ardverk.daap.chunks.impl.unknown.SpeakerActive;
import org.ardverk.daap.chunks.impl.unknown.SpeakerList;
import org.ardverk.daap.chunks.impl.unknown.UnknownGT;
import org.ardverk.daap.chunks.impl.unknown.UnknownMA;
import org.ardverk.daap.chunks.impl.unknown.StatusRevision;
import org.ardverk.daap.chunks.impl.unknown.UnknownST;
import org.ardverk.daap.chunks.impl.unknown.UnknownVD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import android.graphics.Bitmap;

/**
 * Status handles status information, including background timer thread also subscribes to keep-alive event updates.
 * <p/>
 */
public class Status
{

	/**
	 * Constants
	 */
	public final static int REPEAT_OFF = 0;
	public final static int REPEAT_SINGLE = 1;
	public final static int REPEAT_ALL = 2;
	public final static int SHUFFLE_OFF = 0;
	public final static int SHUFFLE_ON = 1;
	public final static int STATE_PAUSED = 3;
	public final static int STATE_PLAYING = 4;
	public final static int UPDATE_PROGRESS = 2;
	public final static int UPDATE_STATE = 3;
	public final static int UPDATE_TRACK = 4;
	public final static int UPDATE_COVER = 5;
	public final static int UPDATE_RATING = 6;
	public static final int UPDATE_SPEAKERS = 7;

	public Status(Session session)
	{
		this.session = session;
	}
	/**
	 * Fields
	 */
	public boolean coverEmpty = true;
	public Bitmap coverCache = null;
	protected int repeatStatus = REPEAT_OFF, shuffleStatus = SHUFFLE_OFF, playStatus = STATE_PAUSED;
	protected boolean visualizer = false, fullscreen = false, geniusSelectable = false;
	private final Session session;
	private long revision = 1;

	public static String lastActivity, lastPlaylistId, lastPlaylistPersistentId;
	public static String[] lastAlbum;

	public static final Logger logger = LoggerFactory.getLogger(Status.class);

	protected final Thread keepalive = new Thread(new Runnable() {
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					Thread.sleep(1000);

					// try fetching next revision update using socket keepalive
					// approach
					// using the next revision-number will make itunes keepalive
					// until something happens
					// http://192.168.254.128:3689/ctrl-int/1/playstatusupdate?revision-number=1&session-id=1034286700
					parseUpdate(RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), revision, session.getSessionId()), true));
				}
				catch(Exception e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}
	});

	public void fetchUpdate() throws Exception
	{
		// using revision-number=1 will make sure we return
		// instantly
		// http://192.168.254.128:3689/ctrl-int/1/playstatusupdate?revision-number=1&session-id=1034286700
		UnknownST state = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), 1, session.getSessionId()));
		revision = state.getSpecificChunk(StatusRevision.class).getValue();
	}

	protected void parseUpdate(Response resp) throws Exception
	{
		// keep track of the worst update that could happen
		int updateType = UPDATE_PROGRESS;

		resp = resp.getNested("cmst");
		this.revision = resp.getNumberLong("cmsr");

		// store now playing info
		long databaseId = this.databaseId;
		long playlistId = this.playlistId;
		long containerItemId = this.containerItemId;
		long trackId = this.trackId;

		// update now playing info
		byte[] canp = resp.getRaw("canp");
		if(canp != null)
			extractNowPlaying(canp);

		int playStatus = (int) resp.getNumberLong("caps");
		int shuffleStatus = (int) resp.getNumberLong("cash");
		int repeatStatus = (int) resp.getNumberLong("carp");
		boolean visualizer = (resp.getNumberLong("cavs") > 0);
		boolean fullscreen = (resp.getNumberLong("cafs") > 0);
		boolean geniusSelectable = resp.containsKey("ceGS");

		// update state if changed
		if(playStatus != this.playStatus || shuffleStatus != this.shuffleStatus || repeatStatus != this.repeatStatus || visualizer != this.visualizer || fullscreen != this.fullscreen || geniusSelectable != this.geniusSelectable)
		{

			updateType = UPDATE_STATE;
			this.playStatus = playStatus;
			this.shuffleStatus = shuffleStatus;
			this.repeatStatus = repeatStatus;
			this.visualizer = visualizer;
			this.fullscreen = fullscreen;
			this.geniusSelectable = geniusSelectable;

			logger.debug(TAG, "about to interrupt #1");
			this.progress.interrupt();
		}

		final String trackName = resp.getString("cann");
		final String trackArtist = resp.getString("cana");
		final String trackAlbum = resp.getString("canl");
		final String trackGenre = resp.getString("cang");

		this.albumId = resp.getNumberString("asai");

		// update if track changed
		if(trackId != this.trackId || containerItemId != this.containerItemId || playlistId != this.playlistId || databaseId != this.databaseId)
		{

			updateType = UPDATE_TRACK;
			this.trackName = trackName;
			this.trackArtist = trackArtist;
			this.trackAlbum = trackAlbum;
			this.trackGenre = trackGenre;

			// clear any coverart cache
			this.coverCache = null;
			this.fetchCover();

			// clear rating
			this.rating = -1;
			this.fetchRating();

			// tell our progress updating thread about a new track
			// this makes sure he doesnt count progress from last song against
			// this
			// new one
			logger.debug(TAG, "about to interrupt #2");
			this.progress.interrupt();
		}

		this.progressRemain = resp.getNumberLong("cant");
		this.progressTotal = resp.getNumberLong("cast");
	}

	public void fetchCover() throws Exception
	{
		if(screenHeight > 640)
		{
			screenHeight = 640;
		}
		// http://192.168.254.128:3689/ctrl-int/1/nowplayingartwork?mw=320&mh=320&session-id=1940361390
		coverCache = RequestHelper.requestBitmap(String.format("%s/ctrl-int/1/nowplayingartwork?mw=" + screenHeight + "&mh=" + screenHeight + "&session-id=%s", session.getRequestBase(), session.getSessionId()));
	}

	private void extractNowPlaying(byte[] bs)
	{
		// This is a PITA in Java....
		databaseId = 0;
		databaseId = (bs[0] & 0xff) << 24;
		databaseId |= (bs[1] & 0xff) << 16;
		databaseId |= (bs[2] & 0xff) << 8;
		databaseId |= bs[3] & 0xff;

		playlistId = 0;
		playlistId = (bs[4] & 0xff) << 24;
		playlistId |= (bs[5] & 0xff) << 16;
		playlistId |= (bs[6] & 0xff) << 8;
		playlistId |= bs[7] & 0xff;

		containerItemId = 0;
		containerItemId = (bs[8] & 0xff) << 24;
		containerItemId |= (bs[9] & 0xff) << 16;
		containerItemId |= (bs[10] & 0xff) << 8;
		containerItemId |= bs[11] & 0xff;

		trackId = 0;
		trackId = (bs[12] & 0xff) << 24;
		trackId |= (bs[13] & 0xff) << 16;
		trackId |= (bs[14] & 0xff) << 8;
		trackId |= bs[15] & 0xff;
	}

	public void fetchRating() throws Exception
	{
		Object o = RequestHelper.requestParsed(String.format("%s/databases/%d/items?session-id=%s&meta=daap.songuserrating&type=music&query='dmap.itemid:%d'", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId(), trackId));
		Response resp = RequestHelper.requestParsed(String.format("%s/databases/%d/items?session-id=%s&meta=daap.songuserrating&type=music&query='dmap.itemid:%d'", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId(), trackId));
		// 2 different responses possible!
		Response entry = resp.getNested("adbs"); // iTunes style
		if(entry == null)
		{
			entry = resp.getNested("apso"); // MonkeyTunes style
		}
		// rating = entry.getNested("mlcl").getNested("mlit").getNumberLong("asur");
	}

	public long getMasterVolume() throws Exception
	{
		UnknownGT unknown = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/getproperty?properties=dmcp.volume&session-id=%s", session.getRequestBase(), session.getSessionId()));
		return unknown.getMasterVolume().getUnsignedValue();
	}

	/**
	 * Reads the list of available speakers
	 * 
	 * @return list of available speakers
	 * @throws Exception
	 */
	public List<Speaker> getSpeakers() throws Exception
	{
		List<Speaker> speakers = Lists.newArrayList();

		SpeakerList speakerList = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/getspeakers?session-id=%s", session.getRequestBase(), session.getSessionId()));
		for(Dictionary dictonary : speakerList.getDictionaries())
		{
			Speaker speaker = new Speaker();
			String name = dictonary.getSpecificChunk(ItemName.class).getValue();
			long speakerId = dictonary.getSpecificChunk(UnknownMA.class).getValue();
			int relativeVolume = dictonary.getSpecificChunk(RelativeVolume.class).getValue();

			SpeakerActive isActive = dictonary.getSpecificChunk(SpeakerActive.class);
			if(dictonary.getSpecificChunk(UnknownVD.class) != null)
			{
				int vd = dictonary.getSpecificChunk(UnknownVD.class).getValue();
			}

			speaker.setActive(isActive != null ? isActive.getBooleanValue() : false);
			speaker.setId(speakerId);
			speaker.setName(name);
			speaker.setAbsoluteVolume(speaker.isActive() ? (int) getMasterVolume() * relativeVolume / 100 : 0);
			speakers.add(speaker);
		}

		return speakers;
	}

	/**
	 * Sets (activates or deactivates) the speakers as defined in the given list.
	 * 
	 * @param speakers
	 *            all speakers to read the active flag from
	 * @throws Exception
	 */
	public void setSpeakers(List<Speaker> speakers) throws Exception
	{
		String idsString = "";
		boolean first = true;
		// The list of speakers to activate is a comma-separated string with
		// the hex versions of the speakers' IDs
		for(Speaker speaker : speakers)
		{
			if(speaker.isActive())
			{
				if(!first)
				{
					idsString += ",";
				}
				else
				{
					first = false;
				}
				idsString += speaker.getIdAsHex();
			}
		}

		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setspeakers?speaker-id=%s&session-id=%s", session.getRequestBase(), idsString, session.getSessionId()));
	}

	/**
	 * Sets the volume of a single speaker. To recreate the behaviour of the original iOS Remote App, there are some additional information required because there is some hassle between relative and master volume.
	 * 
	 * @param speakerId
	 *            ID of the speaker to set the volume of
	 * @param newVolume
	 *            the new volume to set
	 * @param formerVolume
	 *            the former volume of this speaker
	 * @param speakersMaxVolume
	 *            the maximum volume of all available speakers
	 * @param secondMaxVolume
	 *            the volume of the second loudest speaker
	 * @param masterVolume
	 *            the current master volume
	 * @throws Exception
	 */
	public void setSpeakerVolume(long speakerId, int newVolume, int formerVolume, int speakersMaxVolume, int secondMaxVolume, long masterVolume) throws Exception
	{
		/*************************************************************
		 * If this speaker will become or is currently the loudest or is the only activated speaker, it will be controlled via the master volume.
		 *************************************************************/
		if(newVolume > masterVolume || formerVolume == speakersMaxVolume)
		{
			if(newVolume < secondMaxVolume)
			{
				// First equalize the volume of this speaker with the second
				// loudest
				setAbsoluteVolume(speakerId, secondMaxVolume);
				int relativeVolume = newVolume * 100 / secondMaxVolume;
				// then go on by decreasing the relative volume of this speaker
				setRelativeVolume(speakerId, relativeVolume);
			}
			else
			{
				// the speaker will remain the loudest, so just control the
				// absolute volume (master volume)
				setAbsoluteVolume(speakerId, newVolume);
			}
		}
		/*************************************************************
		 * Otherwise its relative volume will be controlled
		 *************************************************************/
		else
		{
			int relativeVolume = newVolume * 100 / (int) masterVolume;
			setRelativeVolume(speakerId, relativeVolume);
		}
	}

	/**
	 * Helper to control a speakers's absolute volume. This uses the URL parameters <code>setproperty?dmcp.volume=%d&include-speaker-id=%s</code> which results in iTunes controlling the master volume and the selected speaker synchronously.
	 * 
	 * @param speakerId
	 *            ID of the speaker to control
	 * @param absoluteVolume
	 *            the volume to set absolutely
	 * @throws Exception
	 */
	private void setAbsoluteVolume(long speakerId, int absoluteVolume) throws Exception
	{
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dmcp.volume=%d&include-speaker-id=%s" + "&session-id=%s", session.getRequestBase(), absoluteVolume, speakerId, session.getSessionId()));
	}

	/**
	 * Helper to control a speaker's relative volume. This relative volume is a value between 0 and 100 describing the relative volume of a speaker in comparison to the master volume. For this the URL parameters <code>%s/ctrl-int/1/setproperty?speaker-id=%s&dmcp.volume=%d</code> are used.
	 * 
	 * @param speakerId
	 *            ID of the speaker to control
	 * @param relativeVolume
	 *            the relative volume to set
	 * @throws Exception
	 */
	private void setRelativeVolume(long speakerId, int relativeVolume) throws Exception
	{
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?speaker-id=%s&dmcp.volume=%d" + "&session-id=%s", session.getRequestBase(), speakerId, relativeVolume, session.getSessionId()));
	}

}
