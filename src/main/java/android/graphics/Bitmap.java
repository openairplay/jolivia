/* TunesRemote SE Android Compatibility */

package android.graphics;

public class Bitmap
{

	private byte[] data;

	public Bitmap(byte[] bitmapdata)
	{
		setData(bitmapdata);
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public byte[] getData()
	{
		return data;
	}

}
