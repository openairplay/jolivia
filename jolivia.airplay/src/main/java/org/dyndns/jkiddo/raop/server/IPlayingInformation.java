package org.dyndns.jkiddo.raop.server;

import java.awt.image.BufferedImage;

import org.dyndns.jkiddo.dmap.chunks.media.ListingItem;

public interface IPlayingInformation {

	void notify(BufferedImage image);

	void notify(ListingItem chunk);

}
