/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GenoSideTimerTest {
	
	public GenoSideTimerTest() {
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
	 * Test of getDT method, of class GenoSideTimer.
	 */
	@Test
	public void testGetDT() {
		System.out.println("getDT");
		GenoSideTimer instance = new GenoSideTimer();
		float expResult = 0.0F;
		float result = instance.getDT();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of start method, of class GenoSideTimer.
	 */
	@Test
	public void testStart() {
		System.out.println("start");
		GenoSideTimer instance = new GenoSideTimer();
		instance.start();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of end method, of class GenoSideTimer.
	 */
	@Test
	public void testEnd() {
		System.out.println("end");
		GenoSideTimer instance = new GenoSideTimer();
		long expResult = 0L;
		long result = instance.end();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
