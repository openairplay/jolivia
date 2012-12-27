/* TunesRemote SE Android Compatibility */

package android.graphics;

public class BitmapFactory
{

	/**
	 * Copy a bitmap from the specified byte array.
	 * 
	 * @param data
	 *            byte array of compressed image data
	 * @param offset
	 *            offset into imageData for where the decoder should begin parsing.
	 * @param length
	 *            the number of bytes, beginning at offset, to parse
	 * @return The decoded bitmap, or null if the image could not be decode.
	 */
	public static Bitmap decodeByteArray(byte[] data, int offset, int length)
	{
		if(length > 0)
		{
			byte[] datacopy = new byte[length];
			System.arraycopy(data, offset, datacopy, 0, length);
			return new Bitmap(datacopy);
		}
		return null;
	}

}
