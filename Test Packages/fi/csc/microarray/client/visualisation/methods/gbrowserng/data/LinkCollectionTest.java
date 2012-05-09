/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
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
	 * Test of filterLinks method, of class LinkCollection.
	 */
	@Test
	public void testFilterLinks() {
		System.out.println("filterLinks");
		long minDistance = 100L;
		LinkCollection instance = getCollection();
		ArrayList<GeneralLink> links = getLinks();
		ArrayList<GeneralLink> resultLinks = instance.filterLinks(minDistance, links);
		int expected = 1;
		int result = resultLinks.size();
		assertEquals(expected, result);
	}

	/**
	 * Test of addToQueue method, of class LinkCollection.
	 */
	@Test
	public void testAddToQueue_4args() {
		System.out.println("addToQueue");
		ViewChromosome aChromosome = new ViewChromosome(1, "1", 500, 500);
		ViewChromosome bChromosome = new ViewChromosome(2, "2", 500, 500);
		long aStart = 1L;
		long bStart = 1L;
		GlobalVariables variables = new GlobalVariables();
		LinkCollection instance = new LinkCollection(variables);
		instance.addToQueue(aChromosome, bChromosome, aStart, bStart);
		assertEquals(instance.queueSize(), 2);
	}

	/**
	 * Test of getLinks method, of class LinkCollection.
	 */
	@Test
	public void testGetLinks() {
		System.out.println("getLinks");
		LinkCollection instance = getCollection();
		ArrayList result = instance.getLinks();
		assertTrue(result.isEmpty());
	}

	/**
	 * Test of numLinks method, of class LinkCollection.
	 */
	@Test
	public void testNumLinksEmpty() {
		System.out.println("numLinks");
		LinkCollection instance = getCollection();
		int expResult = 0;
		int result = instance.numLinks();
		assertEquals(expResult, result);
	}

	
	private LinkCollection getCollection() {
		GlobalVariables variables = new GlobalVariables();
		LinkCollection collection = new LinkCollection(variables);
		return collection;
	}

	/**
	 * Test of addToQueue method, of class LinkCollection.
	 */
	@Test
	public void testAddToQueue_GeneralLink() {
		System.out.println("addToQueue");
		GeneralLink link = getGeneralLink();
		LinkCollection instance = getCollection();
		instance.addToQueue(link);
		int expected = 2;
		int result = instance.queueSize();
		assertEquals(expected, result);
	}
	
	
	/**
	 * Test of addToQueue method, of class LinkCollection.
	 */
	@Test
	public void testAddToQueue_HundredThousand_GeneralLinks() {
		System.out.println("addToQueue");
		LinkCollection instance = getCollection();
		for (int i = 0; i < 100000; i++) {
			GeneralLink link = getGeneralLink();
			instance.addToQueue(link);
		}
		int expected = 200000;
		int result = instance.queueSize();
		assertEquals(expected, result);
	}
	
	
	
	/**
	 * Test of addToQueue method, of class LinkCollection.
	 */
	@Test
	public void testAddToQueue_Million_GeneralLinks() {
		System.out.println("addToQueue");
		LinkCollection instance = getCollection();
		for (int i = 0; i < 1000000; i++) {
		GeneralLink link = getGeneralLink();
		instance.addToQueue(link);
		}
		int expected = 2000000;
		int result = instance.queueSize();
		assertEquals(expected, result);
	}
		
	private GeneralLink getGeneralLink() {
		ViewChromosome aChr = new ViewChromosome(1, 50);
			ViewChromosome bChr = new ViewChromosome(2, 50);
		GeneralLink link = new GeneralLink(aChr, bChr, 100, 100, true);
		return link;
	}

	private ArrayList<GeneralLink> getLinks() {
		
		ViewChromosome aChr = new ViewChromosome(1, 500);
		ViewChromosome bChr = new ViewChromosome(2, 500);
		GeneralLink linkA = new GeneralLink(aChr, bChr, 100, 450, true);
		GeneralLink linkB = new GeneralLink(aChr, bChr, 150, 400, true);
		ArrayList<GeneralLink> links = new ArrayList();
		links.add(linkA);
		links.add(linkB);
		return links;
	}
}
