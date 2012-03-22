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
public class EventHandlerTest {
	
	public EventHandlerTest() {
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
	 * Test of toggleFullscreen method, of class EventHandler.
	 */
	@Test
	public void testToggleFullscreen() {
		System.out.println("toggleFullscreen");
		EventHandler instance = null;
		instance.toggleFullscreen();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of resolution method, of class EventHandler.
	 */
	@Test
	public void testResolution() {
		System.out.println("resolution");
		int width = 0;
		int height = 0;
		EventHandler instance = null;
		instance.resolution(width, height);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toggleVisible method, of class EventHandler.
	 */
	@Test
	public void testToggleVisible() {
		System.out.println("toggleVisible");
		EventHandler instance = null;
		boolean expResult = false;
		boolean result = instance.toggleVisible();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handleEvents method, of class EventHandler.
	 */
	@Test
	public void testHandleEvents() throws Exception {
		System.out.println("handleEvents");
		EventHandler instance = null;
		instance.handleEvents();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
