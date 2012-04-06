/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class LinkCollectionTest {
	
	public LinkCollectionTest() {
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
	 * Test of syncAdditions method, of class LinkCollection.
	 */
	@Test
	public void testSyncAdditions() {
		System.out.println("syncAdditions");
		LinkCollection instance = new LinkCollection();
		instance.syncAdditions();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of filterLinks method, of class LinkCollection.
	 */
	@Test
	public void testFilterLinks() {
		System.out.println("filterLinks");
		long minDistance = 0L;
		LinkCollection instance = new LinkCollection();
		instance.filterLinks(minDistance);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToQueue method, of class LinkCollection.
	 */
	@Test
	public void testAddToQueue_4args() {
		System.out.println("addToQueue");
		ViewChromosome aChromosome = null;
		ViewChromosome bChromosome = null;
		long aStart = 0L;
		long bStart = 0L;
		LinkCollection instance = new LinkCollection();
		instance.addToQueue(aChromosome, bChromosome, aStart, bStart);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of valueAt method, of class LinkCollection.
	 */
	@Test
	public void testValueAt() {
		System.out.println("valueAt");
		int index = 0;
		LinkCollection instance = new LinkCollection();
		GeneralLink expResult = null;
		GeneralLink result = instance.valueAt(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLinks method, of class LinkCollection.
	 */
	@Test
	public void testGetLinks() {
		System.out.println("getLinks");
		LinkCollection instance = new LinkCollection();
		ArrayList expResult = null;
		ArrayList result = instance.getLinks();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of numLinks method, of class LinkCollection.
	 */
	@Test
	public void testNumLinks() {
		System.out.println("numLinks");
		LinkCollection instance = new LinkCollection();
		int expResult = 0;
		int result = instance.numLinks();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tick method, of class LinkCollection.
	 */
	@Test
	public void testTick() {
		System.out.println("tick");
		float dt = 0.0F;
		GeneCircle geneCircle = null;
		LinkCollection instance = new LinkCollection();
		instance.tick(dt, geneCircle);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToQueue method, of class LinkCollection.
	 */
	@Test
	public void testAddToQueue_GeneralLink() {
		System.out.println("addToQueue");
		GeneralLink l = null;
		LinkCollection instance = new LinkCollection();
		instance.addToQueue(l);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateNewLinkPositions method, of class LinkCollection.
	 */
	@Test
	public void testUpdateNewLinkPositions() {
		System.out.println("updateNewLinkPositions");
		GeneCircle geneCircle = null;
		LinkCollection instance = new LinkCollection();
		instance.updateNewLinkPositions(geneCircle);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
