/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GenoEventTest {
	
	public GenoEventTest() {
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
	 * Test of setScreenSize method, of class GenoEvent.
	 */
	@Test
	public void testSetScreenSize() {
		System.out.println("setScreenSize");
		int width = 0;
		int height = 0;
		GenoEvent instance = null;
		instance.setScreenSize(width, height);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMouseGLX method, of class GenoEvent.
	 */
	@Test
	public void testGetMouseGLX() {
		System.out.println("getMouseGLX");
		GenoEvent instance = null;
		float expResult = 0.0F;
		float result = instance.getMouseGLX();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMouseGLY method, of class GenoEvent.
	 */
	@Test
	public void testGetMouseGLY() {
		System.out.println("getMouseGLY");
		GenoEvent instance = null;
		float expResult = 0.0F;
		float result = instance.getMouseGLY();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
