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

import org.dyndns.jkiddo.protocol.dmap.chunks.daap.DatabaseSongs;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.SongAlbumId;
import org.dyndns.jkiddo.protocol.dmap.chunks.daap.SongUserRating;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.FullscreenStatus;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.GeniusSelectable;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.NowPlaying;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.PlayStatus;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.ProgressRemain;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.ProgressTotal;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.RelativeVolume;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.RepeatStatus;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.ShuffleStatus;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.SpeakerActive;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.SpeakerList;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.StatusRevision;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.TrackAlbum;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.TrackArtist;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.TrackGenre;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.TrackName;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.UnknownGT;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.UnknownMA;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.UnknownST;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.UnknownVD;
import org.dyndns.jkiddo.protocol.dmap.chunks.dacp.VisualizerStatus;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.Dictionary;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ItemName;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ListingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;

import com.google.common.collect.Lists;

/**
 * Status handles status information, including background timer thread also subscribes to keep-alive event updates.
 * <p/>
 */
public class Status
{
	public static final Logger logger = LoggerFactory.getLogger(Status.class);

	private final Session session;
	private long revision = 1;
	private int screenHeight = 640;

	public Status(Session session)
	{
		this.session = session;
	}

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
					UnknownST state = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), revision, session.getSessionId()), true);
					parseUpdate(state);
				}
				catch(Exception e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}
	});

	protected final Thread update = new Thread(new Runnable() {
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					// sleep a few seconds to make sure we dont kill stuff
					Thread.sleep(1000);

					// try fetching next revision update using socket keepalive
					// approach
					// using the next revision-number will make itunes keepalive
					// until something happens
					// GET /update?revision-number=1&daap-no-disconnect=1&session-id=1250589827
					UnknownST state = RequestHelper.requestParsed(String.format("%s/update?revision-number=%d&daap-no-disconnect=1&session-id=%s", session.getRequestBase(), revision, session.getSessionId()), true);
					parseUpdate(state);
				}
				catch(Exception e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}
	});

	public long fetchUpdate() throws Exception
	{
		// using revision-number=1 will make sure we return
		// instantly
		// http://192.168.254.128:3689/ctrl-int/1/playstatusupdate?revision-number=1&session-id=1034286700
		UnknownST state = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), 1, session.getSessionId()));
		return parseUpdate(state);
	}

	public long parseUpdate(UnknownST state) throws Exception
	{
		revision = state.getSpecificChunk(StatusRevision.class).getValue();

		state.getSpecificChunk(PlayStatus.class);
		state.getSpecificChunk(ShuffleStatus.class);
		state.getSpecificChunk(RepeatStatus.class);
		state.getSpecificChunk(VisualizerStatus.class);
		state.getSpecificChunk(FullscreenStatus.class);
		state.getSpecificChunk(GeniusSelectable.class);

		long trackId = state.getSpecificChunk(NowPlaying.class).getTrackId();
		state.getSpecificChunk(TrackName.class);
		state.getSpecificChunk(TrackArtist.class);
		state.getSpecificChunk(TrackAlbum.class);
		state.getSpecificChunk(TrackGenre.class);
		state.getSpecificChunk(SongAlbumId.class);

		fetchCover();
		fetchRating(trackId);

		state.getSpecificChunk(ProgressRemain.class);
		state.getSpecificChunk(ProgressTotal.class);

		return trackId;
	}

	public Bitmap fetchCover() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/nowplayingartwork?mw=320&mh=320&session-id=1940361390
		return RequestHelper.requestBitmap(String.format("%s/ctrl-int/1/nowplayingartwork?mw=" + screenHeight + "&mh=" + screenHeight + "&session-id=%s", session.getRequestBase(), session.getSessionId()));
	}

	public long fetchRating(long trackId) throws Exception
	{
		// MonkeyTunes style would be with PlaylistSongs instead of DatabaseSongs
		final DatabaseSongs databaseSongs = RequestHelper.requestParsed(String.format("%s/databases/%d/items?session-id=%s&meta=daap.songuserrating&type=music&query='dmap.itemid:%d'", session.getRequestBase(), session.getDatabase().getItemId(), session.getSessionId(), trackId));
		final ListingItem listingItem = databaseSongs.getListing().getSingleListingItemContainingClass(SongUserRating.class);
		return listingItem.getSpecificChunk(SongUserRating.class).getValue();
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
