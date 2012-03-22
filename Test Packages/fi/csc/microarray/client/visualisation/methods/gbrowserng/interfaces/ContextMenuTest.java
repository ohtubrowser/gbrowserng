/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import javax.media.opengl.GL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class ContextMenuTest {
	
	public ContextMenuTest() {
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
	 * Test of getChromosome method, of class ContextMenu.
	 */
	@Test
	public void testGetChromosome() {
		System.out.println("getChromosome");
		ContextMenu instance = null;
		ViewChromosome expResult = null;
		ViewChromosome result = instance.getChromosome();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class ContextMenu.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
		MouseEvent event = null;
		float mx = 0.0F;
		float my = 0.0F;
		ContextMenu instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event, mx, my);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class ContextMenu.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
		KeyEvent event = null;
		ContextMenu instance = null;
		boolean expResult = false;
		boolean result = instance.handle(event);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of close method, of class ContextMenu.
	 */
	@Test
	public void testClose() {
		System.out.println("close");
		ContextMenu instance = null;
		boolean expResult = false;
		boolean result = instance.close();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of draw method, of class ContextMenu.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
		GL2 gl = null;
		ContextMenu instance = null;
		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tick method, of class ContextMenu.
	 */
	@Test
	public void testTick() {
		System.out.println("tick");
		float dt = 0.0F;
		ContextMenu instance = null;
		instance.tick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of inComponent method, of class ContextMenu.
	 */
	@Test
	public void testInComponent() {
		System.out.println("inComponent");
		float mx = 0.0F;
		float my = 0.0F;
		ContextMenu instance = null;
		boolean expResult = false;
		boolean result = instance.inComponent(mx, my);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
