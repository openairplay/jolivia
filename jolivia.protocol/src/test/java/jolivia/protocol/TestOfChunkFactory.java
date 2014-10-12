package jolivia.protocol;

import java.util.Map;
import java.util.Map.Entry;

import org.dyndns.jkiddo.dmp.chunks.AbstractChunk;
import org.dyndns.jkiddo.dmp.chunks.ChunkFactory;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class TestOfChunkFactory
{

	@Test
	public void testOfEqualMaps()
	{
		final ChunkFactory factory = new ChunkFactory();
		
		final Map<Integer, Class<? extends AbstractChunk>> staticMap = factory.getStaticTypedMap();
		final Table<Integer, String, Class<? extends AbstractChunk>> dynamicTable = ChunkFactory.getCalculatedMap();

		for(final Cell<Integer, String, Class<? extends AbstractChunk>> e : dynamicTable.cellSet())
		{
			if(staticMap.containsValue(e.getValue()) == false)
			{
				System.out.println("Could not find " + e.getValue() + " in static map");
			}
			
			if(e.getRowKey().intValue() == 1634026323)
			{
				System.out.println("Hit");
			}
		}
		
		System.out.println();
		
		for(final Entry<Integer, Class<? extends AbstractChunk>> e : staticMap.entrySet())
		{
			if(dynamicTable.containsValue(e.getValue()) == false)
			{
				System.out.println("Could not find " + e.getValue() + " in dynamic map");
			}
		}
	}
}
