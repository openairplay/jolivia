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

package org.dyndns.jkiddo.service.daap.client;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.dyndns.jkiddo.dmap.chunks.control.PlayingStatus;
import org.dyndns.jkiddo.dmap.chunks.control.RelativeVolume;
import org.dyndns.jkiddo.dmap.chunks.control.SpeakerActive;
import org.dyndns.jkiddo.dmap.chunks.control.SpeakerList;
import org.dyndns.jkiddo.dmap.chunks.control.UnknownGT;
import org.dyndns.jkiddo.dmap.chunks.control.UnknownVD;
import org.dyndns.jkiddo.dmap.chunks.media.Dictionary;
import org.dyndns.jkiddo.dmap.chunks.media.ItemName;
import org.dyndns.jkiddo.dmap.chunks.media.SpeakerMacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Status handles status information, including background timer thread also subscribes to keep-alive event updates.
 * <p/>
 */
public class RemoteControl
{
	public static final Logger logger = LoggerFactory.getLogger(RemoteControl.class);

	private final Session session;
	private int screenHeight = 640;

	RemoteControl(Session session) throws Exception
	{
		this.session = session;
	}

	/**
	 * This call blocks until something happens in iTunes, eg. pushing play.
	 * 
	 * @return
	 * @throws Exception
	 */
	public PlayingStatus getPlayStatusUpdateBlocking() throws Exception
	{
		// try fetching next revision update using socket keepalive
		// approach
		// using the next revision-number will make itunes keepalive
		// until something happens
		// http://192.168.254.128:3689/ctrl-int/1/playstatusupdate?revision-number=1&session-id=1034286700
		return RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), session.getRevision(), session.getSessionId()), true);
	}

	public PlayingStatus getPlayStatusUpdate() throws Exception
	{
		// using revision-number=1 will make sure we return
		// instantly
		// http://192.168.254.128:3689/ctrl-int/1/playstatusupdate?revision-number=1&session-id=1034286700
		return RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), 1, session.getSessionId()));
	}

	public byte[] fetchCover() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/nowplayingartwork?mw=320&mh=320&session-id=1940361390
		return RequestHelper.requestBitmap(String.format("%s/ctrl-int/1/nowplayingartwork?mw=" + screenHeight + "&mh=" + screenHeight + "&session-id=%s", session.getRequestBase(), session.getSessionId()));
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
	public Collection<Speaker> getSpeakers() throws Exception
	{
		List<Speaker> speakers = Lists.newArrayList();

		SpeakerList speakerList = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/getspeakers?session-id=%s", session.getRequestBase(), session.getSessionId()));
		for(Dictionary dictonary : speakerList.getDictionaries())
		{
			Speaker speaker = new Speaker();
			String name = dictonary.getSpecificChunk(ItemName.class).getValue();
			long speakerId = dictonary.getSpecificChunk(SpeakerMacAddress.class).getValue();
			int relativeVolume = dictonary.getSpecificChunk(RelativeVolume.class).getValue();

			SpeakerActive isActive = dictonary.getSpecificChunk(SpeakerActive.class);
			if(dictonary.getSpecificChunk(UnknownVD.class) != null)
			{
				@SuppressWarnings("unused")
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
	public void setSpeakers(Collection<Speaker> speakers) throws Exception
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

	public PlayingStatus getNowPlaying(String albumid) throws Exception
	{
		// Try Wilco (Alex W)'s nowplaying extension /ctrl-int/1/items
		try
		{
			return RequestHelper.requestParsed(String.format("%s/ctrl-int/1/items?session-id=%s&meta=dmap.itemname,dmap.itemid,daap.songartist,daap.songalbum,daap.songalbum,daap.songtime,daap.songuserrating,daap.songtracknumber&type=music&sort=album&query='daap.songalbumid:%s'", session.getRequestBase(), session.getSessionId(), albumid), false);
		}
		catch(IOException e)
		{
			logger.debug(e.getMessage(), e);
			return getNowPlaying();
		}
	}

	public PlayingStatus getNowPlaying() throws Exception
	{
		// reads the current playing song as a one-item playlist
		// Refactor response into one that looks like a normal items request
		// and trigger listener
		return RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=1&session-id=%s", session.getRequestBase(), session.getSessionId(), false));
	}

	public void pause() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/pause?session-id=130883770
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/pause?session-id=%s", session.getRequestBase(), session.getSessionId()));
	}

	public void play() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/playpause?session-id=130883770
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/playpause?session-id=%s", session.getRequestBase(), session.getSessionId()));
	}

	public void next() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/nextitem?session-id=130883770
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/nextitem?session-id=%s", session.getRequestBase(), session.getSessionId()));
	}

	public void previous() throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/previtem?session-id=130883770
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/previtem?session-id=%s", session.getRequestBase(), session.getSessionId()));
	}

	public void setVolume(long volume) throws Exception
	{
		if(volume > 100 || volume < 0)
		{
			logger.debug("Volume should be in the range 0 to 100");
		}
		// http://192.168.254.128:3689/ctrl-int/1/setproperty?dmcp.volume=100.000000&session-id=130883770
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dmcp.volume=%s&session-id=%s", session.getRequestBase(), volume, session.getSessionId()));
	}

	public void setProgress(int progressSeconds) throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/setproperty?dacp.playingtime=82784&session-id=130883770
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dacp.playingtime=%d&session-id=%s", session.getRequestBase(), progressSeconds * 1000, session.getSessionId()));
	}

	public void setShuffle(int shuffleMode) throws Exception
	{
		// /ctrl-int/1/setproperty?dacp.shufflestate=1&session-id=1873217009
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dacp.shufflestate=%d&session-id=%s", session.getRequestBase(), shuffleMode, session.getSessionId()));
	}

	public void setRepeat(int repeatMode) throws Exception
	{
		// /ctrl-int/1/setproperty?dacp.repeatstate=2&session-id=1873217009
		// HTTP/1.1
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dacp.repeatstate=%d&session-id=%s", session.getRequestBase(), repeatMode, session.getSessionId()));
	}

	/**
	 * Sets the rating stars of a particular song 0-100.
	 * <p/>
	 * 
	 * @param rating
	 *            the rating 0-100 to set for rating stars
	 * @param trackId
	 *            the id of the track to update the rating for
	 * @throws Exception
	 */
	public void setRating(final long rating, final long trackId) throws Exception
	{
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dacp.userrating=%d&song-spec='dmap.itemid:%d'&session-id=%s", session.getRequestBase(), rating, trackId, session.getSessionId()));
	}

	/**
	 * Command to clear the Now Playing cue.
	 * 
	 * @throws Exception
	 */
	private void clearCue() throws Exception
	{
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=clear&session-id=%s", session.getRequestBase(), session.getSessionId()));
	}

	public void playAlbum(final long albumId, final int tracknum) throws Exception
	{

		// http://192.168.254.128:3689/ctrl-int/1/cue?command=clear&session-id=130883770
		// http://192.168.254.128:3689/ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:32')+'daap.songartist:Family%20Force%205')&index=0&sort=album&session-id=130883770
		// /ctrl-int/1/cue?command=play&query='daap.songalbumid:16621530181618739404'&index=11&sort=album&session-id=514488449

		// GET
		// /ctrl-int/1/playspec?database-spec='dmap.persistentid:16621530181618731553'&playlist-spec='dmap.persistentid:9378496334192532210'&dacp.shufflestate=1&session-id=514488449
		// (zero based index into playlist)

		clearCue();
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=play&query='daap.songalbumid:%s'&index=%d&sort=album&session-id=%s", session.getRequestBase(), albumId, tracknum, session.getSessionId()));

	}

	public void queueAlbum(final long albumId) throws Exception
	{
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=add&query='daap.songalbumid:%s'&session-id=%s", session.getRequestBase(), albumId, session.getSessionId()));
	}

	public void playArtist(String artist, int index) throws Exception
	{
		// http://192.168.254.128:3689/ctrl-int/1/cue?command=clear&session-id=130883770
		// /ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:32')+'daap.songartist:Family%20Force%205')&index=0&sort=album&session-id=130883770
		// /ctrl-int/1/cue?command=play&query='daap.songartist:%s'&index=0&sort=album&session-id=%s

		final String encodedArtist = RequestHelper.escapeUrlString(artist);
		final int encodedIndex = index;

		clearCue();
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=play&query='daap.songartist:%s'&index=%d&sort=album&session-id=%s", session.getRequestBase(), encodedArtist, encodedIndex, session.getSessionId()));
	}

	public void queueArtist(String artist) throws Exception
	{
		final String encodedArtist = RequestHelper.escapeUrlString(artist);
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=add&query='daap.songartist:%s'&session-id=%s", session.getRequestBase(), encodedArtist, session.getSessionId()));
	}

	public void queueTrack(final long trackId) throws Exception
	{
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=add&query='dmap.itemid:%s'&session-id=%s", session.getRequestBase(), trackId, session.getSessionId()));
	}

	public void playTrack(final long trackId) throws Exception
	{
		clearCue();
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=play&query='dmap.itemid:%s'&session-id=%s", session.getRequestBase(), trackId, session.getSessionId()));
	}

	public void playSearch(final String search, final int index) throws Exception
	{
		// /ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+'dmap.itemname:*F*')&index=4&sort=name&session-id=1550976127
		final String encodedSearch = RequestHelper.escapeUrlString(search);
		clearCue();
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=play&query=(('com.apple.itunes.mediakind:1','com.apple.itunes.mediakind:4','com.apple.itunes.mediakind:8')+('dmap.itemname:*%s*','daap.songartist:*%s*','daap.songalbum:*%s*'))&type=music&sort=name&index=%d&session-id=%s", session.getRequestBase(), encodedSearch, encodedSearch, encodedSearch, index, session.getSessionId()));
	}

	public void playPlaylist(final String playlistPersistentId, final String containerItemId) throws Exception
	{
		// /ctrl-int/1/playspec?database-spec='dmap.persistentid:0x9031099074C14E05'&container-spec='dmap.persistentid:0xA1E1854E0B9A1B'&container-item-spec='dmap.containeritemid:0x1b47'&session-id=7491138
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/playspec?database-spec='dmap.persistentid:0x%s'&container-spec='dmap.persistentid:0x%s'&container-item-spec='dmap.containeritemid:0x%s'&session-id=%s", session.getRequestBase(), session.getDatabase().getPersistentId(), playlistPersistentId, containerItemId, session.getSessionId()));
	}

	public void playIndex(final String albumid, final int tracknum) throws Exception
	{
		try
		{
			RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=play&index=%d&sort=album&session-id=%s", session.getRequestBase(), tracknum, session.getSessionId()));
			// on iTunes this generates a 501 Not Implemented response
		}
		catch(Exception e)
		{
			if(albumid != null && albumid.length() > 0)
			{
				// Fall back to choosing from the current album if there is
				// one
				clearCue();
				RequestHelper.dispatch(String.format("%s/ctrl-int/1/cue?command=play&query='daap.songalbumid:%s'&index=%d&sort=album&session-id=%s", session.getRequestBase(), albumid, tracknum, session.getSessionId()));
			}
		}
	}

	public void setVisualiser(final boolean enabled) throws Exception
	{
		// GET /ctrl-int/1/setproperty?dacp.visualizer=1&session-id=283658916
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dacp.visualizer=%d&session-id=%s", session.getRequestBase(), enabled ? 1 : 0, session.getSessionId()));
	}

	public void setFullscreen(final boolean enabled) throws Exception
	{
		// GET /ctrl-int/1/setproperty?dacp.fullscreen=1&session-id=283658916
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/setproperty?dacp.fullscreen=%d&session-id=%s", session.getRequestBase(), enabled ? 1 : 0, session.getSessionId()));
	}

	public void playRadio(final long genreId, final long itemId) throws Exception
	{
		playSpec(session.getRadioDatabase().getItemId(), genreId, itemId);
	}

	public void playSpec(final long databaseId, final long containerId, final long itemId) throws Exception
	{
		// GET
		// /ctrl-int/1/playspec?database-spec='dmap.itemid:0x6073'&container-spec='dmap.itemid:0x607B'&item-spec='dmap.itemid:0x7cbe'&session-id=345827905
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/playspec?" + "database-spec='dmap.itemid:0x%x'" + "&container-spec='dmap.itemid:0x%x'" + "&item-spec='dmap.itemid:0x%x'" + "&session-id=%s", session.getRequestBase(), databaseId, containerId, itemId, session.getSessionId()));
	}

	public void playQueueEdit(final long itemID, final long playlistId) throws Exception
	{
		RequestHelper.dispatch(String.format("%s/ctrl-int/1/playqueue-edit?command=add&query='dmap.itemid:" + itemID + "'&queuefilter=playlist:"+playlistId+"&sort=name&mode=1&session-id=%s", session.getRequestBase(), session.getSessionId()));
	}
}
