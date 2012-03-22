/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class OverViewStateTest {
	
	public OverViewStateTest() {
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
	 * Test of values method, of class OverViewState.
	 */
	@Test
	public void testValues() {
		System.out.println("values");
		OverViewState[] expResult = null;
		OverViewState[] result = OverViewState.values();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of valueOf method, of class OverViewState.
	 */
	@Test
	public void testValueOf() {
		System.out.println("valueOf");
		String name = "";
		OverViewState expResult = null;
		OverViewState result = OverViewState.valueOf(name);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
