package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.StringChunk;

public class NowPlaying extends StringChunk
{
	private long databaseId;
	private long playlistId;
	private long containerItemId;
	private long trackId;

	public final long getDatabaseId()
	{
		return databaseId;
	}

	public final long getPlaylistId()
	{
		return playlistId;
	}

	public final long getContainerItemId()
	{
		return containerItemId;
	}

	public final long getTrackId()
	{
		return trackId;
	}

	public NowPlaying()
	{
		this(null);
	}

	public NowPlaying(String value)
	{
		super("canp", "com.apple.itunes.unknown-np", value);
	}

	@Override
	public void setValue(String value)
	{
		super.setValue(value);
		if(value != null)
		{
			byte[] bs = this.getValue().getBytes();

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
	}
}
