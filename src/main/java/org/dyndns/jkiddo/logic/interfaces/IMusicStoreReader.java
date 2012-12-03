package org.dyndns.jkiddo.logic.interfaces;

import java.io.File;
import java.util.Collection;

import org.ardverk.daap.Song;

public interface IMusicStoreReader
{
	public Collection<Song> readTunes() throws Exception;

	public File getTune(Song tune) throws Exception;

	public String getLibraryName();
}
