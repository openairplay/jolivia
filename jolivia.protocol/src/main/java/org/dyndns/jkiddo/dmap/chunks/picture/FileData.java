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
package org.dyndns.jkiddo.dmap.chunks.picture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;

import org.dyndns.jkiddo.dmap.DmapUtil;
import org.dyndns.jkiddo.dmap.chunks.RawChunk;

/**
 * DPAP.FileDate This tag holds the raw file data of a picture or thumb nail. The data that is stored depends on what is requested by the client. This differs from the DAAP protocol where the file is requested separately from the tag.
 * 
 * @author Charles Ikeson
 */
public class FileData extends RawChunk
{

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public FileData()
	{
		this(new byte[] {});
	}

	/**
	 * Creates the parameter and points it to the original file.
	 * 
	 * @param f
	 *            The file to read from.
	 * @param asThumb
	 *            Resizes the image to a max of 240x240 before sending it back.
	 */

	public FileData(byte[] array)
	{
		// dpap.thumb
		// dpap.hires
		super("pfdt", "dpap.filedata", array);
	}

	// /**
	// * Creates the parameter and points it to the original file.
	// *
	// * @param f
	// * The file to read from.
	// * @param asThumb
	// * Resizes the image to a max of 240x240 before sending it back.
	// */
	public FileData(URI f, boolean asThumb)
	{
		super("pfdt", "dpap.filedata", new byte[] {});
		try
		{
			if(asThumb)
			{
				byte[] array = DmapUtil.uriTobuffer(f);
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(array));
				int max = Math.max(image.getWidth(), image.getHeight());
				float scale = 240.0f / max;
				int newW = (int) (image.getWidth() * scale);
				int newH = (int) (image.getHeight() * scale);
				BufferedImage scaledImage = new BufferedImage(newW, newH, image.getType());

				Graphics g = scaledImage.getGraphics();
				g.drawImage(image, 0, 0, newW, newH, null);
				ByteArrayOutputStream downscaledBytes = new ByteArrayOutputStream();
				ImageIO.write(scaledImage, "jpeg", downscaledBytes);
				g.dispose();

				setValue(downscaledBytes.toByteArray());
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
