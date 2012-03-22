/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import javax.media.opengl.GL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class CoordinateRendererTest {
	
	public CoordinateRendererTest() {
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
	 * Test of childComponentCall method, of class CoordinateRenderer.
	 */
	@Test
	public void testChildComponentCall() {
		System.out.println("childComponentCall");
		String who = "";
		String what = "";
		CoordinateRenderer instance = null;
		instance.childComponentCall(who, what);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of draw method, of class CoordinateRenderer.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
		GL2 gl = null;
		CoordinateRenderer instance = null;
		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class CoordinateRenderer.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
		MouseEvent event = null;
		float screenX = 0.0F;
		float screenY = 0.0F;
		CoordinateRenderer instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event, screenX, screenY);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class CoordinateRenderer.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
		KeyEvent event = null;
		CoordinateRenderer instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of userTick method, of class CoordinateRenderer.
	 */
	@Test
	public void testUserTick() {
		System.out.println("userTick");
		float dt = 0.0F;
		CoordinateRenderer instance = null;
		instance.userTick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
