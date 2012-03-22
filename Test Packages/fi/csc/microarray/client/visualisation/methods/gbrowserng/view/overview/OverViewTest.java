/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GenoFPSCounter;
import javax.media.opengl.GL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class OverViewTest {
	
	public OverViewTest() {
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
	 * Test of childComponentCall method, of class OverView.
	 */
	@Test
	public void testChildComponentCall() {
		System.out.println("childComponentCall");
		String who = "";
		String what = "";
		OverView instance = null;
		instance.childComponentCall(who, what);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class OverView.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
		MouseEvent event = null;
		float x = 0.0F;
		float y = 0.0F;
		OverView instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event, x, y);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class OverView.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
		KeyEvent event = null;
		OverView instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of draw method, of class OverView.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
		GL2 gl = null;
		OverView instance = null;
		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of userTick method, of class OverView.
	 */
	@Test
	public void testUserTick() {
		System.out.println("userTick");
		float dt = 0.0F;
		OverView instance = null;
		instance.userTick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDrawCounter method, of class OverView.
	 */
	@Test
	public void testGetDrawCounter() {
		System.out.println("getDrawCounter");
		OverView instance = null;
		GenoFPSCounter expResult = null;
		GenoFPSCounter result = instance.getDrawCounter();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTickCounter method, of class OverView.
	 */
	@Test
	public void testGetTickCounter() {
		System.out.println("getTickCounter");
		OverView instance = null;
		GenoFPSCounter expResult = null;
		GenoFPSCounter result = instance.getTickCounter();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
