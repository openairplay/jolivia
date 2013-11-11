package org.dyndns.jkiddo.logic.desk;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.dyndns.jkiddo.dmp.DmapUtil;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeskImageStoreReader implements IImageStoreReader
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3205613907987224359L;
	private static final Logger logger = LoggerFactory.getLogger(DeskImageStoreReader.class);
	private Map<IImageItem, File> mapOfImageToFile;
	private String path;

	public DeskImageStoreReader()
	{
		this(System.getProperty("user.dir") + System.getProperty("file.separator") + "etc");
		this.mapOfImageToFile = new HashMap<IImageItem, File>();
	}

	public DeskImageStoreReader(String path)
	{
		this.mapOfImageToFile = new HashMap<IImageItem, File>();
		this.path = path;
	}

	@Override
	public Set<IImageItem> readImages() throws Exception
	{
		try
		{
			traverseRootPathRecursively(new File(path));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return Collections.unmodifiableSet(mapOfImageToFile.keySet());
	}

	private static boolean isImage(File f)
	{
		if(f.getPath().endsWith(".jpg") || f.getPath().endsWith(".jpeg"))
			return true;
		return false;
	}

	private void traverseRootPathRecursively(File f) throws IOException
	{
		if(f.isDirectory())
		{
			File[] contents = f.listFiles();
			if(contents != null)
			{
				for(int i = 0; i < contents.length; i++)
				{
					traverseRootPathRecursively(contents[i]);
				}
			}
			else
				logger.debug("Symlink'ish ... " + f.getAbsolutePath());
		}
		else if(isImage(f))
		{
			mapOfImageToFile.put(populateImage(f), f);
		}
	}

	private IImageItem populateImage(final File f) throws IOException
	{
		final BufferedImage image = ImageIO.read(f);
		return new IImageItem() {
			@Override
			public String getImageFilename()
			{
				return f.getName();
			}

			@Override
			public long getSize()
			{
				return f.length();
			}

			@Override
			public String getFormat()
			{
				return "JPEG";
			}

			@Override
			public int getRating()
			{
				return 5;
			}

			@Override
			public Date getCreationDate()
			{
				return new Date();
			}

			@Override
			public int getImageWidth()
			{
				return image.getWidth();
			}

			@Override
			public int getImageHeight()
			{
				return image.getHeight();
			}
		};
	}
	@Override
	public URI getImage(IImageItem image) throws Exception
	{
		if(image != null)
		{
			logger.debug("Serving " + image.getImageFilename());
		}
		return mapOfImageToFile.get(image).toURI();
	}

	@Override
	public byte[] getImageThumb(IImageItem iimage) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Thumbnails.of(getImage(iimage).toURL()).size(360, 360).outputFormat("jpg").toOutputStream(baos);
		return baos.toByteArray();
	}

	private byte[] getImageThumbOld(IImageItem iimage) throws Exception
	{
		byte[] array = DmapUtil.uriTobuffer(getImage(iimage));

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

		return downscaledBytes.toByteArray();
	}
}