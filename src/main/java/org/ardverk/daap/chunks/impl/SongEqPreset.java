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

package org.ardverk.daap.chunks.impl;

import org.ardverk.daap.chunks.StringChunk;

/**
 * The equalizer for the Song.
 * 
 * @author Roger Kapsi
 */
public class SongEqPreset extends StringChunk
{

	/** No equilizer selected */
	public static final String NONE = null;

	/** Acoustic */
	public static final String ACOUSTIC = "Acoustic";

	/** Bass Booster */
	public static final String BASS_BOOSTER = "Bass Booster";

	/** Bass Reducer */
	public static final String BASS_REDUCER = "Bass Reducer";

	/** Classical */
	public static final String CLASSICAL = "Classical";

	/** Dance */
	public static final String DANCE = "Dance";

	/** Deep */
	public static final String DEEP = "Deep";

	/** Electronic */
	public static final String ELECTRONIC = "Electronic";

	/** Flat */
	public static final String FLAT = "Flat";

	/** Hip-Hop */
	public static final String HIP_HOP = "Hip-Hop";

	/** Jazz */
	public static final String JAZZ = "Jazz";

	/** Latin */
	public static final String LATIN = "Latin";

	/** Loudness */
	public static final String LOUDNESS = "Loudness";

	/** Lounge */
	public static final String LOUNGE = "Lounge";

	/** Piano */
	public static final String PIANO = "Piano";

	/** Pop */
	public static final String POP = "Pop";

	/** R&amp;B */
	public static final String RB = "R&B";

	/** Rock */
	public static final String ROCK = "Rock";

	/** Small Speakers */
	public static final String SMALL_SPEAKERS = "Small Speakers";

	/** Spoken Word */
	public static final String SPOKEN_WORD = "Spoken Word";

	/** Treble Booster */
	public static final String TREBLE_BOOSTER = "Treble Booster";

	/** Treble Reducer */
	public static final String TREBLE_REDUCER = "Treble Reducer";

	/** Vocal Booster */
	public static final String VOCAL_BOOSTER = "Vocal Booster";

	/**
	 * Creates a new SongEqPreset where no equalizer is selected. You can change this value with {@see #setValue(String)}.
	 */
	public SongEqPreset()
	{
		this(NONE);
	}

	/**
	 * Creates a new SongEqPreset with the assigned equalizer. You can change this value with {@see #setValue(String)}.
	 * 
	 * @param <tt>equalizer</tt> the equalizer of this song.
	 */
	public SongEqPreset(String preset)
	{
		super("aseq", "daap.songeqpreset", preset);
	}
}
