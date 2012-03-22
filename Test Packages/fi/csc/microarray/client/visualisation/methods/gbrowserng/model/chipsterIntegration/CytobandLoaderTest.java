/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowser.message.AreaResult;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class CytobandLoaderTest {
	
	public CytobandLoaderTest() {
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
	 * Test of getChromosomes method, of class CytobandLoader.
	 */
	@Test
	public void testGetChromosomes() {
		System.out.println("getChromosomes");
		CytobandLoader instance = null;
		ConcurrentLinkedQueue expResult = null;
		ConcurrentLinkedQueue result = instance.getChromosomes();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of processAreaResult method, of class CytobandLoader.
	 */
	@Test
	public void testProcessAreaResult() {
		System.out.println("processAreaResult");
		AreaResult areaResult = null;
		CytobandLoader instance = null;
		instance.processAreaResult(areaResult);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of main method, of class CytobandLoader.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		String[] args = null;
		CytobandLoader.main(args);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
