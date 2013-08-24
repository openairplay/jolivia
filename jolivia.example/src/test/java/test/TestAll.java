package test;

import org.dyndns.jkiddo.raop.server.AirReceiver.TestFunctions;
import org.dyndns.jkiddo.raop.server.AirReceiver.TestSampleBuffer;
import org.dyndns.jkiddo.raop.server.AirReceiver.TestSignedness;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Noop.class, TestFunctions.class, TestSampleBuffer.class, TestSignedness.class })
public class TestAll
{

	@BeforeClass
	public static void setUpClass()
	{
		System.out.println("Master setup");

	}

	@AfterClass
	public static void tearDownClass()
	{
		System.out.println("Master tearDown");

	}
}