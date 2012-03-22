/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class TrackSessionTest {
	
	public TrackSessionTest() {
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
	 * Test of getHeatMap method, of class TrackSession.
	 */
	@Test
	public void testGetHeatMap() {
		System.out.println("getHeatMap");
		TrackSession instance = null;
		HeatMap expResult = null;
		HeatMap result = instance.getHeatMap();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getReads method, of class TrackSession.
	 */
	@Test
	public void testGetReads() {
		System.out.println("getReads");
		TrackSession instance = null;
		ArrayList expResult = null;
		ArrayList result = instance.getReads();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
