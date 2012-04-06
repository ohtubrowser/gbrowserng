/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class LinkRangeIteratorTest {
	
	public LinkRangeIteratorTest() {
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
	 * Test of rewind method, of class LinkRangeIterator.
	 */
	@Test
	public void testRewind() {
		System.out.println("rewind");
		LinkRangeIterator instance = null;
		instance.rewind();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of value method, of class LinkRangeIterator.
	 */
	@Test
	public void testValue_0args() {
		System.out.println("value");
		LinkRangeIterator instance = null;
		GeneralLink expResult = null;
		GeneralLink result = instance.value();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of increment method, of class LinkRangeIterator.
	 */
	@Test
	public void testIncrement() {
		System.out.println("increment");
		LinkRangeIterator instance = null;
		instance.increment();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of decrement method, of class LinkRangeIterator.
	 */
	@Test
	public void testDecrement() {
		System.out.println("decrement");
		LinkRangeIterator instance = null;
		instance.decrement();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of value method, of class LinkRangeIterator.
	 */
	@Test
	public void testValue_int() {
		System.out.println("value");
		int i = 0;
		LinkRangeIterator instance = null;
		GeneralLink expResult = null;
		GeneralLink result = instance.value(i);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of inRange method, of class LinkRangeIterator.
	 */
	@Test
	public void testInRange_GeneralLink() {
		System.out.println("inRange");
		GeneralLink link = null;
		LinkRangeIterator instance = null;
		boolean expResult = false;
		boolean result = instance.inRange(link);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of inRange method, of class LinkRangeIterator.
	 */
	@Test
	public void testInRange_int() {
		System.out.println("inRange");
		int index = 0;
		LinkRangeIterator instance = null;
		boolean expResult = false;
		boolean result = instance.inRange(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
