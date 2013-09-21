package org.dyndns.jkiddo.raop.server;

import java.awt.image.BufferedImage;

import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;

public interface IPlayingInformation {

	void notify(BufferedImage image);

	void notify(ListingItem chunk);

}
