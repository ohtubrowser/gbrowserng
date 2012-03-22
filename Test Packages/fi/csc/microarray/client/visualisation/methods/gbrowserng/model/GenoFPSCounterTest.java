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
public class GenoFPSCounterTest {
	
	public GenoFPSCounterTest() {
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
	 * Test of tick method, of class GenoFPSCounter.
	 */
	@Test
	public void testTick() {
		System.out.println("tick");
		float dt = 0.0F;
		GenoFPSCounter instance = new GenoFPSCounter();
		instance.tick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFps method, of class GenoFPSCounter.
	 */
	@Test
	public void testGetFps() {
		System.out.println("getFps");
		GenoFPSCounter instance = new GenoFPSCounter();
		String expResult = "";
		String result = instance.getFps();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addNano method, of class GenoFPSCounter.
	 */
	@Test
	public void testAddNano() {
		System.out.println("addNano");
		long nano = 0L;
		GenoFPSCounter instance = new GenoFPSCounter();
		instance.addNano(nano);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMillis method, of class GenoFPSCounter.
	 */
	@Test
	public void testGetMillis() {
		System.out.println("getMillis");
		GenoFPSCounter instance = new GenoFPSCounter();
		String expResult = "";
		String result = instance.getMillis();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
