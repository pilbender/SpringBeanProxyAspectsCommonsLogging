package net.raescott.logging;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Richard Scott Smith <rsmith@harriscomputer.com>
 */
public class LogTest {

    public LogTest() {
    }

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

	/**
	 * Test of main method, of class Log.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		String[] args = null;
		Log.main(args);
		// TODO review the generated test code and remove the default call to fail.
		//fail("The test case is a prototype.");
	}

}