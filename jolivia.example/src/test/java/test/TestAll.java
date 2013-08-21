package test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

//@RunWith(Suite.class)
//@SuiteClasses({ Noop.class, TestFunctions.class, TestSampleBuffer.class, TestSignedness.class })
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