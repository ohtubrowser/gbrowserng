/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import javax.media.opengl.GL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class TrackViewTest {
	
	public TrackViewTest() {
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
	 * Test of getGenePosition method, of class TrackView.
	 */
	@Test
	public void testGetGenePosition() {
		System.out.println("getGenePosition");
		TrackView instance = null;
		float expResult = 0.0F;
		float result = instance.getGenePosition();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHalfWidth method, of class TrackView.
	 */
	@Test
	public void testGetHalfWidth() {
		System.out.println("getHalfWidth");
		TrackView instance = null;
		float expResult = 0.0F;
		float result = instance.getHalfWidth();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSession method, of class TrackView.
	 */
	@Test
	public void testGetSession() {
		System.out.println("getSession");
		TrackView instance = null;
		Session expResult = null;
		Session result = instance.getSession();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of draw method, of class TrackView.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
		GL2 gl = null;
		TrackView instance = null;
		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of childComponentCall method, of class TrackView.
	 */
	@Test
	public void testChildComponentCall() {
		System.out.println("childComponentCall");
		String who = "";
		String what = "";
		TrackView instance = null;
		instance.childComponentCall(who, what);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of userTick method, of class TrackView.
	 */
	@Test
	public void testUserTick() {
		System.out.println("userTick");
		float dt = 0.0F;
		TrackView instance = null;
		instance.userTick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class TrackView.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
		MouseEvent event = null;
		float screen_x = 0.0F;
		float screen_y = 0.0F;
		TrackView instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event, screen_x, screen_y);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class TrackView.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
		KeyEvent event = null;
		TrackView instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTrackViewMode method, of class TrackView.
	 */
	@Test
	public void testSetTrackViewMode() {
		System.out.println("setTrackViewMode");
		int mode = 0;
		TrackView instance = null;
		instance.setTrackViewMode(mode);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isActive method, of class TrackView.
	 */
	@Test
	public void testIsActive() {
		System.out.println("isActive");
		TrackView instance = null;
		boolean expResult = false;
		boolean result = instance.isActive();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTrackViewMode method, of class TrackView.
	 */
	@Test
	public void testGetTrackViewMode() {
		System.out.println("getTrackViewMode");
		TrackView instance = null;
		int expResult = 0;
		int result = instance.getTrackViewMode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setActive method, of class TrackView.
	 */
	@Test
	public void testSetActive() {
		System.out.println("setActive");
		boolean active = false;
		TrackView instance = null;
		instance.setActive(active);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
