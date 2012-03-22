/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import math.Vector2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class ChromoNameTest {
	
	public ChromoNameTest() {
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
	 * Test of isOver method, of class ChromoName.
	 */
	@Test
	public void testIsOver() {
		System.out.println("isOver");
		float x = 0.0F;
		float y = 0.0F;
		ChromoName instance = null;
		boolean expResult = false;
		boolean result = instance.isOver(x, y);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosome method, of class ChromoName.
	 */
	@Test
	public void testGetChromosome() {
		System.out.println("getChromosome");
		ChromoName instance = null;
		ViewChromosome expResult = null;
		ViewChromosome result = instance.getChromosome();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPosition method, of class ChromoName.
	 */
	@Test
	public void testSetPosition() {
		System.out.println("setPosition");
		float x = 0.0F;
		float y = 0.0F;
		ChromoName instance = null;
		instance.setPosition(x, y);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPosition method, of class ChromoName.
	 */
	@Test
	public void testGetPosition() {
		System.out.println("getPosition");
		ChromoName instance = null;
		Vector2 expResult = null;
		Vector2 result = instance.getPosition();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSize method, of class ChromoName.
	 */
	@Test
	public void testGetSize() {
		System.out.println("getSize");
		ChromoName instance = null;
		Vector2 expResult = null;
		Vector2 result = instance.getSize();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSize method, of class ChromoName.
	 */
	@Test
	public void testSetSize() {
		System.out.println("setSize");
		float w = 0.0F;
		float h = 0.0F;
		ChromoName instance = null;
		instance.setSize(w, h);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isActive method, of class ChromoName.
	 */
	@Test
	public void testIsActive() {
		System.out.println("isActive");
		ChromoName instance = null;
		boolean expResult = false;
		boolean result = instance.isActive();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
