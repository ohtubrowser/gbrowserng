/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import math.Vector2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class CascadingComponentTest {
	
	public CascadingComponentTest() {
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
	 * Test of inComponent method, of class CascadingComponent.
	 */
	@Test
	public void testInComponent() {
		System.out.println("inComponent");
		float x = 0.0F;
		float y = 0.0F;
		CascadingComponent instance = null;
		boolean expResult = false;
		boolean result = instance.inComponent(x, y);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of inScreen method, of class CascadingComponent.
	 */
	@Test
	public void testInScreen() {
		System.out.println("inScreen");
		CascadingComponent instance = null;
		boolean expResult = false;
		boolean result = instance.inScreen();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getParent method, of class CascadingComponent.
	 */
	@Test
	public void testGetParent() {
		System.out.println("getParent");
		CascadingComponent instance = null;
		CascadingComponent expResult = null;
		CascadingComponent result = instance.getParent();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of cascadingTick method, of class CascadingComponent.
	 */
	@Test
	public void testCascadingTick() {
		System.out.println("cascadingTick");
		float dt = 0.0F;
		CascadingComponent instance = null;
		instance.cascadingTick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPosition method, of class CascadingComponent.
	 */
	@Test
	public void testGetPosition() {
		System.out.println("getPosition");
		CascadingComponent instance = null;
		Vector2 expResult = null;
		Vector2 result = instance.getPosition();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDimensions method, of class CascadingComponent.
	 */
	@Test
	public void testGetDimensions() {
		System.out.println("getDimensions");
		CascadingComponent instance = null;
		Vector2 expResult = null;
		Vector2 result = instance.getDimensions();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTargetDimensions method, of class CascadingComponent.
	 */
	@Test
	public void testGetTargetDimensions() {
		System.out.println("getTargetDimensions");
		CascadingComponent instance = null;
		Vector2 expResult = null;
		Vector2 result = instance.getTargetDimensions();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of glx method, of class CascadingComponent.
	 */
	@Test
	public void testGlx() {
		System.out.println("glx");
		float x = 0.0F;
		CascadingComponent instance = null;
		float expResult = 0.0F;
		float result = instance.glx(x);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of gly method, of class CascadingComponent.
	 */
	@Test
	public void testGly() {
		System.out.println("gly");
		float y = 0.0F;
		CascadingComponent instance = null;
		float expResult = 0.0F;
		float result = instance.gly(y);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of glySize method, of class CascadingComponent.
	 */
	@Test
	public void testGlySize() {
		System.out.println("glySize");
		float y = 0.0F;
		CascadingComponent instance = null;
		float expResult = 0.0F;
		float result = instance.glySize(y);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of glxSize method, of class CascadingComponent.
	 */
	@Test
	public void testGlxSize() {
		System.out.println("glxSize");
		float x = 0.0F;
		CascadingComponent instance = null;
		float expResult = 0.0F;
		float result = instance.glxSize(x);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPosition method, of class CascadingComponent.
	 */
	@Test
	public void testSetPosition() {
		System.out.println("setPosition");
		float x = 0.0F;
		float y = 0.0F;
		CascadingComponent instance = null;
		instance.setPosition(x, y);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDimensions method, of class CascadingComponent.
	 */
	@Test
	public void testSetDimensions() {
		System.out.println("setDimensions");
		float w = 0.0F;
		float h = 0.0F;
		CascadingComponent instance = null;
		instance.setDimensions(w, h);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of modifyPosition method, of class CascadingComponent.
	 */
	@Test
	public void testModifyPosition() {
		System.out.println("modifyPosition");
		float x = 0.0F;
		float y = 0.0F;
		CascadingComponent instance = null;
		instance.modifyPosition(x, y);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of modifyDimensions method, of class CascadingComponent.
	 */
	@Test
	public void testModifyDimensions() {
		System.out.println("modifyDimensions");
		float x = 0.0F;
		float y = 0.0F;
		CascadingComponent instance = null;
		instance.modifyDimensions(x, y);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class CascadingComponentImpl extends CascadingComponent {

		public CascadingComponentImpl() {
			super(null);
		}
	}
}
