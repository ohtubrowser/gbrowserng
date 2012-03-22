/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import javax.media.opengl.GL2;
import math.Vector2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class SessionViewRecentCapsuleTest {
	
	public SessionViewRecentCapsuleTest() {
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
	 * Test of getId method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		SessionViewRecentCapsule instance = null;
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setId method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testSetId() {
		System.out.println("setId");
		int id = 0;
		SessionViewRecentCapsule instance = null;
		instance.setId(id);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOldPosition method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testGetOldPosition() {
		System.out.println("getOldPosition");
		SessionViewRecentCapsule instance = null;
		Vector2 expResult = null;
		Vector2 result = instance.getOldPosition();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOldGeneCirclePosition method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testGetOldGeneCirclePosition() {
		System.out.println("getOldGeneCirclePosition");
		SessionViewRecentCapsule instance = null;
		Vector2 expResult = null;
		Vector2 result = instance.getOldGeneCirclePosition();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSessionView method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testGetSessionView() {
		System.out.println("getSessionView");
		SessionViewRecentCapsule instance = null;
		SessionView expResult = null;
		SessionView result = instance.getSessionView();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSession method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testGetSession() {
		System.out.println("getSession");
		SessionViewRecentCapsule instance = null;
		Session expResult = null;
		Session result = instance.getSession();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of childComponentCall method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testChildComponentCall() {
		System.out.println("childComponentCall");
		String who = "";
		String what = "";
		SessionViewRecentCapsule instance = null;
		instance.childComponentCall(who, what);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
		MouseEvent event = null;
		float screen_x = 0.0F;
		float screen_y = 0.0F;
		SessionViewRecentCapsule instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event, screen_x, screen_y);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
		KeyEvent event = null;
		SessionViewRecentCapsule instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hide method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testHide() {
		System.out.println("hide");
		SessionViewRecentCapsule instance = null;
		instance.hide();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of show method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testShow() {
		System.out.println("show");
		SessionViewRecentCapsule instance = null;
		instance.show();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of draw method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
		GL2 gl = null;
		SessionViewRecentCapsule instance = null;
		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of userTick method, of class SessionViewRecentCapsule.
	 */
	@Test
	public void testUserTick() {
		System.out.println("userTick");
		float dt = 0.0F;
		SessionViewRecentCapsule instance = null;
		instance.userTick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
