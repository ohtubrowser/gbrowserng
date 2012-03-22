/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.newt.event.MouseEvent;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class MouseTrackerTest {
	
	public MouseTrackerTest() {
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
	 * Test of getDragging_dx method, of class MouseTracker.
	 */
	@Test
	public void testGetDragging_dx() {
		System.out.println("getDragging_dx");
		MouseTracker instance = new MouseTracker();
		float expResult = 0.0F;
		float result = instance.getDragging_dx();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDragging_dy method, of class MouseTracker.
	 */
	@Test
	public void testGetDragging_dy() {
		System.out.println("getDragging_dy");
		MouseTracker instance = new MouseTracker();
		float expResult = 0.0F;
		float result = instance.getDragging_dy();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class MouseTracker.
	 */
	@Test
	public void testHandle() {
		System.out.println("handle");
		MouseEvent event = null;
		float screen_x = 0.0F;
		float screen_y = 0.0F;
		MouseTracker instance = new MouseTracker();
		instance.handle(event, screen_x, screen_y);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
