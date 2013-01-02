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

import android.graphics.Bitmap;
import android.os.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Status handles status information, including background timer thread also subscribes to keep-alive event updates.
 * <p/>
 */
public class Status
{

	public final static String TAG = Status.class.toString();

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
	private final static int MAX_FAILURES = 10;

	/**
	 * Fields
	 */
	public boolean coverEmpty = true;
	public Bitmap coverCache = null;
	public String albumId = "";
	protected int repeatStatus = REPEAT_OFF, shuffleStatus = SHUFFLE_OFF, playStatus = STATE_PAUSED;
	protected boolean visualizer = false, fullscreen = false, geniusSelectable = false;
	protected final AtomicBoolean destroyThread = new AtomicBoolean(false);
	private long rating = -1;
	private long databaseId = 0;
	private long playlistId = 0;
	private long containerItemId = 0;
	private long trackId = 0;
	private String trackName = "", trackArtist = "", trackAlbum = "", trackGenre = "";
	private long progressTotal = 0, progressRemain = 0;
	private final Session session;
	private final AtomicInteger failures = new AtomicInteger(0);
	private long revision = 1;

	private Handler update;
	public static String lastActivity, lastPlaylistId, lastPlaylistPersistentId;
	public static String[] lastAlbum;

	// This is used to fetch higher quality covers on high DPI devices
	// Has to be initialised in ControlActivity
	public static int screenHeight = 320;

	public static final Logger logger = LoggerFactory.getLogger(Status.class);

	/**
	 * Constructor accepts a Session and UI Handler to update the UI.
	 * 
	 * @param session
	 * @param update
	 */
	public Status(Session session, Handler update)
	{
		this.session = session;
		this.update = update;

		// create two threads, one for backend keep-alive updates
		// and a second one to update running time and fire gui events

		this.progress.start();
		this.keepalive.start();

		// keep our status updated with server however we need to
		// end thread when getting any 404 responses, etc
	}

	public void updateHandler(Handler handler)
	{
		this.update = handler;
	}

	protected final Thread progress = new Thread(new Runnable() {
		@Override
		public void run()
		{
			while(true)
			{
				// when in playing state, keep moving progress forward
				while(playStatus == STATE_PLAYING)
				{
					// logger.debug(TAG, "thread entering playing loop");
					if(destroyThread.get())
						break;
					long anchor = System.currentTimeMillis();
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException e)
					{
						// someone jolted us awake during playing, which means
						// track
						// changed
						logger.debug(TAG, "someone jolted us during STATE_PLAYING loop");
						continue;
					}

					if(destroyThread.get())
						break;

					// update progress and gui
					progressRemain -= (System.currentTimeMillis() - anchor);
					if(update != null)
						update.sendEmptyMessage(UPDATE_PROGRESS);

					// trigger a forced update if we seem to gone past end of
					// song
					if(progressRemain <= 0)
					{
						logger.debug(TAG, "suggesting that we fetch new song");
						fetchUpdate();
					}
				}

				// keep sleeping while in paused state
				while(playStatus == STATE_PAUSED)
				{
					logger.debug(TAG, "thread entering paused loop");
					try
					{
						Thread.sleep(10000);
					}
					catch(InterruptedException e)
					{
						// someone probably jolted us awake with a status update
						logger.debug(TAG, "someone jolted us during STATE_PAUSED loop");
					}

					if(destroyThread.get())
						break;
				}

				// one final sleep to make sure we behave nicely in case of
				// unknown
				// status
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e)
				{
					logger.debug(TAG, "someone jolted us during OVERALL loop");
				}

				if(destroyThread.get())
					break;
			}
			logger.warn(TAG, "Status Progress Thread Killed!");
		}
	});

	protected final Thread keepalive = new Thread(new Runnable() {
		@Override
		public void run()
		{
			while(true)
			{
				try
				{
					// sleep a few seconds to make sure we dont kill stuff
					Thread.sleep(1000);
					if(destroyThread.get())
						break;

					// try fetching next revision update using socket keepalive
					// approach
					// using the next revision-number will make itunes keepalive
					// until something happens
					// http://192.168.254.128:3689/ctrl-int/1/playstatusupdate?revision-number=1&session-id=1034286700
					parseUpdate(RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), revision, session.sessionId), true));
				}
				catch(Exception e)
				{
					logger.debug(TAG, String.format("Exception in keepalive thread, so killing try# %d", failures.get()), e);
					if(failures.incrementAndGet() > MAX_FAILURES)
						destroy();
				}
			}
			logger.warn(TAG, "Status KeepAlive Thread Killed!");
		}
	});

	public void destroy()
	{
		// destroy our internal thread
		logger.warn(TAG, "trying to destroy internal status thread");
		if(this.destroyThread.get())
			return;
		this.destroyThread.set(true);
		this.progress.interrupt();
		this.keepalive.interrupt();
	}

	public void fetchUpdate()
	{
		logger.debug(TAG, "Fetching Update From Server...");
		// force a status update, will pass along to parseUpdate()
		ThreadExecutor.runTask(new Runnable() {
			@Override
			public void run()
			{
				try
				{
					// using revision-number=1 will make sure we return
					// instantly
					// http://192.168.254.128:3689/ctrl-int/1/playstatusupdate?revision-number=1&session-id=1034286700
					parseUpdate(RequestHelper.requestParsed(String.format("%s/ctrl-int/1/playstatusupdate?revision-number=%d&session-id=%s", session.getRequestBase(), 1, session.sessionId), false));
				}
				catch(Exception e)
				{
					logger.warn(TAG, e);
					if(failures != null && failures.incrementAndGet() > MAX_FAILURES)
					{
						destroy();
					}
				}
			}
		});

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

		// send off updated event to gui
		if(update != null)
			this.update.sendEmptyMessage(updateType);
	}

	public void fetchCover()
	{
		if(coverCache == null)
		{
			// spawn thread to fetch coverart
			ThreadExecutor.runTask(new Runnable() {
				@Override
				public void run()
				{
					try
					{
						if(screenHeight > 640)
						{
							screenHeight = 640;
						}
						// http://192.168.254.128:3689/ctrl-int/1/nowplayingartwork?mw=320&mh=320&session-id=1940361390
						coverCache = RequestHelper.requestBitmap(String.format("%s/ctrl-int/1/nowplayingartwork?mw=" + screenHeight + "&mh=" + screenHeight + "&session-id=%s", session.getRequestBase(), session.sessionId));
					}
					catch(Exception e)
					{
						logger.error(TAG, "Fetch Cover Exception:" + e.getMessage());
					}
					coverEmpty = (coverCache == null);
					if(update != null)
						update.sendEmptyMessage(UPDATE_COVER);
				}
			});
		}
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

	// fetch rating of current playing item
	public void fetchRating()
	{
		// spawn thread to fetch rating
		ThreadExecutor.runTask(new Runnable() {
			@Override
			public void run()
			{
				try
				{
					Response resp = RequestHelper.requestParsed(String.format("%s/databases/%d/items?session-id=%s&meta=daap.songuserrating&type=music&query='dmap.itemid:%d'", session.getRequestBase(), databaseId, session.sessionId, trackId), false);

					if(update != null)
					{
						// 2 different responses possible!
						Response entry = resp.getNested("adbs"); // iTunes style
						if(entry == null)
						{
							entry = resp.getNested("apso"); // MonkeyTunes style
						}
						rating = entry.getNested("mlcl").getNested("mlit").getNumberLong("asur");
						update.sendEmptyMessage(UPDATE_RATING);
					}

				}
				catch(Exception e)
				{
					logger.error(TAG, "Fetch Rating Exception:" + e.getMessage());
				}
			}
		});
	}

	public long getVolume()
	{
		try
		{
			// http://192.168.254.128:3689/ctrl-int/1/getproperty?properties=dmcp.volume&session-id=130883770
			Response resp = RequestHelper.requestParsed(String.format("%s/ctrl-int/1/getproperty?properties=dmcp.volume&session-id=%s", session.getRequestBase(), session.sessionId), false);
			return resp.getNested("cmgt").getNumberLong("cmvo");
		}
		catch(Exception e)
		{
			logger.error(TAG, "Fetch Volume Exception:" + e.getMessage());
		}
		return -1;
	}

	/**
	 * Reads the list of available speakers
	 * 
	 * @return list of available speakers
	 */
	public List<Speaker> getSpeakers(final List<Speaker> speakers)
	{
		try
		{
			logger.debug(TAG, "getSpeakers() requesting...");

			String temp = String.format("%s/ctrl-int/1/getspeakers?session-id=%s", session.getRequestBase(), session.sessionId);

			byte[] raw = RequestHelper.request(temp, false);

			Response response = ResponseParser.performParse(raw);

			Response casp = response.getNested("casp");
			if(casp != null)
			{
				speakers.clear();
			}

			List<Response> mdclArray = casp.findArray("mdcl");

			// Master volume is required to compute the speakers' absolute volume
			long masterVolume = getVolume();

			for(Response mdcl : mdclArray)
			{
				Speaker speaker = new Speaker();
				speaker.setName(mdcl.getString("minm"));
				long id = mdcl.getNumberLong("msma");
				speaker.setId(id);
				logger.debug(TAG, "Speaker = " + speaker.getName());
				int relativeVolume = (int) mdcl.getNumberLong("cmvo");
				boolean isActive = mdcl.containsKey("caia");
				speaker.setActive(isActive);
				// mastervolume/100 * relativeVolume/100 * 100
				int absoluteVolume = isActive ? (int) masterVolume * relativeVolume / 100 : 0;
				speaker.setAbsoluteVolume(absoluteVolume);
				speakers.add(speaker);
			}

		}
		catch(Exception e)
		{
			logger.error(TAG, "Could not get speakers: ", e);
			speakers.clear();
			Speaker speaker = new Speaker();
			speaker.setName("Computer");
			speaker.setId(1);
			speaker.setActive(true);
			speaker.setAbsoluteVolume(50);
			speakers.add(speaker);
		}

		return speakers;

	}

	/**
	 * Sets (activates or deactivates) the speakers as defined in the given list.
	 * 
	 * @param speakers
	 *            all speakers to read the active flag from
	 */
	public void setSpeakers(List<Speaker> speakers)
	{

		try
		{
			logger.debug(TAG, "setSpeakers() requesting...");

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

			String url = String.format("%s/ctrl-int/1/setspeakers?speaker-id=%s&session-id=%s", session.getRequestBase(), idsString, session.sessionId);

			RequestHelper.request(url, false);

		}
		catch(Exception e)
		{
			logger.error(TAG, "Could not set speakers: ", e);
		}
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
	 */
	public void setSpeakerVolume(long speakerId, int newVolume, int formerVolume, int speakersMaxVolume, int secondMaxVolume, long masterVolume)
	{
		try
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
		catch(Exception e)
		{
			logger.error(TAG, "Error when setting speaker volume: ", e);
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
		String url;
		url = String.format("%s/ctrl-int/1/setproperty?dmcp.volume=%d&include-speaker-id=%s" + "&session-id=%s", session.getRequestBase(), absoluteVolume, speakerId, session.sessionId);
		RequestHelper.request(url, false);
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
		String url;
		url = String.format("%s/ctrl-int/1/setproperty?speaker-id=%s&dmcp.volume=%d" + "&session-id=%s", session.getRequestBase(), speakerId, relativeVolume, session.sessionId);
		RequestHelper.request(url, false);
	}

	public int getProgress()
	{
		return (int) ((this.progressTotal - this.progressRemain) / 1000);
	}

	public int getRemaining()
	{
		return (int) (this.progressRemain / 1000);
	}

	public int getProgressTotal()
	{
		return (int) (this.progressTotal / 1000);
	}

	public int getShuffle()
	{
		return this.shuffleStatus;
	}

	public int getRepeat()
	{
		return this.repeatStatus;
	}

	public int getPlayStatus()
	{
		return this.playStatus;
	}

	public String getTrackName()
	{
		return this.trackName;
	}

	public String getTrackArtist()
	{
		return this.trackArtist;
	}

	public String getTrackAlbum()
	{
		return this.trackAlbum;
	}

	public String getTrackGenre()
	{
		return this.trackGenre;
	}

	public long getContainerItemId()
	{
		return this.containerItemId;
	}

	public long getDatabaseId()
	{
		return this.databaseId;
	}

	public long getPlaylistId()
	{
		return this.playlistId;
	}

	public long getRating()
	{
		return this.rating;
	}

	public String getAlbumId()
	{
		return this.albumId;
	}

	public long getTrackId()
	{
		return this.trackId;
	}

	public boolean isVisualizerOn()
	{
		return this.visualizer;
	}

	public boolean isFullscreen()
	{
		return this.fullscreen;
	}

	public boolean isGeniusSelectable()
	{
		return this.geniusSelectable;
	}
}
