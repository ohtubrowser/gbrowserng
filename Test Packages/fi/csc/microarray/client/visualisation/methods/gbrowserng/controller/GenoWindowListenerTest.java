/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowUpdateEvent;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GenoWindowListenerTest {
	
	public GenoWindowListenerTest() {
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
	 * Test of windowDestroyNotify method, of class GenoWindowListener.
	 */
	@Test
	public void testWindowDestroyNotify() {
		System.out.println("windowDestroyNotify");
		WindowEvent e = null;
		GenoWindowListener instance = null;
		instance.windowDestroyNotify(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of windowDestroyed method, of class GenoWindowListener.
	 */
	@Test
	public void testWindowDestroyed() {
		System.out.println("windowDestroyed");
		WindowEvent e = null;
		GenoWindowListener instance = null;
		instance.windowDestroyed(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of windowGainedFocus method, of class GenoWindowListener.
	 */
	@Test
	public void testWindowGainedFocus() {
		System.out.println("windowGainedFocus");
		WindowEvent e = null;
		GenoWindowListener instance = null;
		instance.windowGainedFocus(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of windowLostFocus method, of class GenoWindowListener.
	 */
	@Test
	public void testWindowLostFocus() {
		System.out.println("windowLostFocus");
		WindowEvent e = null;
		GenoWindowListener instance = null;
		instance.windowLostFocus(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of windowMoved method, of class GenoWindowListener.
	 */
	@Test
	public void testWindowMoved() {
		System.out.println("windowMoved");
		WindowEvent e = null;
		GenoWindowListener instance = null;
		instance.windowMoved(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of windowRepaint method, of class GenoWindowListener.
	 */
	@Test
	public void testWindowRepaint() {
		System.out.println("windowRepaint");
		WindowUpdateEvent e = null;
		GenoWindowListener instance = null;
		instance.windowRepaint(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of windowResized method, of class GenoWindowListener.
	 */
	@Test
	public void testWindowResized() {
		System.out.println("windowResized");
		WindowEvent e = null;
		GenoWindowListener instance = null;
		instance.windowResized(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
