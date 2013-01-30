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
package org.dyndns.jkiddo.dmap.chunks.dpap;

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
	// public pfdt(File f, boolean asThumb)
	// {
	// super(PARAM_NAME);
	// try
	// {
	// in = new FileInputStream(f);
	// size = (int) f.length();
	// if(asThumb)
	// {
	//
	// BufferedImage image = ImageIO.read(in);
	// int max = Math.max(image.getWidth(), image.getHeight());
	// float scale = 240.0f / max;
	// int newW = (int) (image.getWidth() * scale);
	// int newH = (int) (image.getHeight() * scale);
	// BufferedImage scaledImage = new BufferedImage(newW, newH, image.getType());
	//
	// Graphics g = scaledImage.getGraphics();
	// g.drawImage(image, 0, 0, newW, newH, null);
	// ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
	// ImageIO.write(scaledImage, "jpeg", tmpOut);
	// g.dispose();
	//
	// in = new ByteArrayInputStream(tmpOut.toByteArray());
	// size = tmpOut.size();
	// }
	// }
	// catch(IOException e)
	// {
	// e.printStackTrace();
	// }
	// }
}
