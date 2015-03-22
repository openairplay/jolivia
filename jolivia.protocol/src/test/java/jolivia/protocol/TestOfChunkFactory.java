package jolivia.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.dyndns.jkiddo.dmp.DMAPAnnotation;
import org.dyndns.jkiddo.dmp.DmapInputStream;
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapTypeDefinition;
import org.dyndns.jkiddo.dmp.chunks.Chunk;
import org.dyndns.jkiddo.dmp.chunks.ChunkFactory;
import org.dyndns.jkiddo.dmp.chunks.media.ContentCodesName;
import org.dyndns.jkiddo.dmp.chunks.media.ContentCodesNumber;
import org.dyndns.jkiddo.dmp.chunks.media.ContentCodesResponse;
import org.dyndns.jkiddo.dmp.chunks.media.ContentCodesType;
import org.dyndns.jkiddo.dmp.chunks.media.Dictionary;
import org.dyndns.jkiddo.dmp.tools.ReflectionsHelper;
import org.junit.Test;

public class TestOfChunkFactory
{
	@Test
	public void generateDmapChunkDefinitions()
	{
		final Set<Class<? extends Object>> classes = ReflectionsHelper.getClasses("org.dyndns.jkiddo", DMAPAnnotation.class);
		
		for(final Class<? extends Object> c : classes)
		{
			final DMAPAnnotation annotation = c.getAnnotation(DMAPAnnotation.class);
			final String shortName = annotation.type().getShortname();
			final String longName = annotation.type().getLongname();
			final DmapTypeDefinition t = annotation.type().getType();
			System.out.println(shortName + "(\"" + shortName + "\",\"" + longName + "\",DmapTypeDefinition." + t  +","+c.getCanonicalName()+".class),");
		}		
	}
	
	@Test
	public void verifyModel() throws Exception
	{
		final DmapInputStream dis = new DmapInputStream(new ByteArrayInputStream(request("http://localhost:3689/content-codes")));
		final Iterator<Dictionary> contentCodes = dis.getChunk(ContentCodesResponse.class).getDictionaries().iterator();

		final ChunkFactory chunkFactory = new ChunkFactory();
		System.out.println("Listing codes ...");

		while(contentCodes.hasNext())
		{
			final Dictionary c = contentCodes.next();
			final String shortName = c.getSpecificChunk(ContentCodesNumber.class).getValueContentCode();
			final Integer type = c.getSpecificChunk(ContentCodesType.class).getValue();
			final String longName = c.getSpecificChunk(ContentCodesName.class).getValue();

			try
			{
				//final Chunk chunk = chunkFactory.newChunk(stringReadAsInt(shortName));
				final Chunk chunk = chunkFactory.getChunkClass(shortName).newInstance();
				System.out.println(shortName + " : " + type + " : " + longName + " : " + chunk.getClass().getSimpleName());
				if(chunk.getType() != type)
				{
					System.out.println("	Type mismatch! Type was " + chunk.getType() + ". Expected " + type);
				}
				if(!longName.equals(chunk.getName()))
				{
					System.out.println("		Longname mismatch! Longname was " + chunk.getName() + ". Expected " + longName);
				}
				if(!shortName.equals(chunk.getContentCodeString()))
				{
					System.out.println("			Shortname mismatch! Shortname was " + chunk.getContentCodeString() + ". Expected " + shortName + ". Code was " + c.getSpecificChunk(ContentCodesNumber.class).getValue());
				}
			}
			catch(final Exception e)
			{
				System.out.println("- " + shortName + " : " + type + " : " + longName + " : ");
				System.out.println("					Chunk could not be identified, having " + stringReadAsInt(shortName));
			}
		}
		
		dis.close();
	}
	private static int stringReadAsInt(final String s)
	{
		final ByteBuffer buffer = ByteBuffer.allocate(s.length());
		for(int i = 0; i < s.length(); i++)
		{
			buffer.put((byte) s.charAt(i));
		}
		buffer.position(0);
		return buffer.getInt();
	}
	
	private static byte[] request(final String remoteUrl) throws Exception
	{
		final byte[] buffer = new byte[1024];

		final HttpURLConnection connection = (HttpURLConnection) new URL(remoteUrl).openConnection();
		connection.setAllowUserInteraction(false);

		// Carefull either Client-DAAP or Client-DPAP
		// iTunes
		{
			connection.setRequestProperty("Viewer-Only-Client", "1");
			connection.setRequestProperty("Client-iTunes-Sharing-Version", "3.10");
			connection.setRequestProperty("Client-DAAP-Version", "3.12");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.setRequestProperty("User-Agent", "Remote/813");
		}

		// connection.setRequestProperty("Host", "192.168.1.75");

		// iPhoto
		{
			// connection.setRequestProperty("Client-DPAP-Version", "1.1");
			// connection.setRequestProperty("User-Agent", "iPhoto/9.4.3  (Macintosh; N; PPC)");
		}

		connection.setReadTimeout(READ_TIMEOUT);
		connection.connect();

		if(connection.getResponseCode() >= HttpURLConnection.HTTP_UNAUTHORIZED)
			throw new Exception("HTTP Error Response Code: " + connection.getResponseCode());

		// obtain the encoding returned by the server
		final String encoding = connection.getContentEncoding();

		InputStream inputStream = null;

		// create the appropriate stream wrapper based on the encoding type
		if(encoding != null && encoding.equalsIgnoreCase("gzip"))
		{
			inputStream = new GZIPInputStream(connection.getInputStream());
		}
		else if(encoding != null && encoding.equalsIgnoreCase("deflate"))
		{
			inputStream = new InflaterInputStream(connection.getInputStream(), new Inflater(true));
		}
		else
		{
			inputStream = connection.getInputStream();
		}

		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{
			int bytesRead;
			while((bytesRead = inputStream.read(buffer)) != -1)
			{
				os.write(buffer, 0, bytesRead);
			}
		}
		finally
		{
			os.flush();
			os.close();
			if(inputStream != null)
			{
				inputStream.close();
			}
		}

		return os.toByteArray();
	}
	
	private static final int READ_TIMEOUT = 0; // Infinite
}
