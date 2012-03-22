/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common;

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
public class GenoButtonTest {
	
	public GenoButtonTest() {
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
	 * Test of childComponentCall method, of class GenoButton.
	 */
	@Test
	public void testChildComponentCall() {
		System.out.println("childComponentCall");
		String who = "";
		String what = "";
		GenoButton instance = null;
		instance.childComponentCall(who, what);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class GenoButton.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
		MouseEvent event = null;
		float screen_x = 0.0F;
		float screen_y = 0.0F;
		GenoButton instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event, screen_x, screen_y);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class GenoButton.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
		KeyEvent event = null;
		GenoButton instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of draw method, of class GenoButton.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
		GL2 gl = null;
		GenoButton instance = null;
		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of userTick method, of class GenoButton.
	 */
	@Test
	public void testUserTick() {
		System.out.println("userTick");
		float dt = 0.0F;
		GenoButton instance = null;
		instance.userTick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
