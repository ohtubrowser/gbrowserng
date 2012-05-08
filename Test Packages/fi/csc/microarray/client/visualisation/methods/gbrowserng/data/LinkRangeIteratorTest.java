/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class LinkRangeIteratorTest {

	GeneralLink aB;
	GeneralLink bA;

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
	 * Test
	 * of
	 * rewind
	 * method,
	 * of
	 * class
	 * LinkRangeIterator.
	 */
	@Test
	public void testRewind() {
		System.out.println("rewind");
		LinkRangeIterator instance = getIterator();
		instance.inRange(1);
		instance.rewind();
		assertEquals(instance.currentIndex, 0);
	}

	private LinkRangeIterator getIterator() {
		LinkCollection collection = getCollection();
		LinkRangeIterator instance = new LinkRangeIterator(collection, 0, 10);
		return instance;
	}

	private LinkCollection getCollection() {
		GlobalVariables variables = new GlobalVariables();
		variables.genome = new Genome();
		LinkCollection collection = new LinkCollection(variables);
		ViewChromosome aChromosome = new ViewChromosome(1, "1", 500, 500);
		ViewChromosome bChromosome = new ViewChromosome(2, "2", 500, 500);
		variables.genome.addChromosome(aChromosome);
		variables.genome.addChromosome(bChromosome);
		GeneralLink aB = new GeneralLink(aChromosome, bChromosome, 10, 20, false);
		GeneralLink bA = new GeneralLink(bChromosome, aChromosome, 300, 400, true);
		collection.addToQueue(aB);
		collection.addToQueue(bA);
		GenoWindow window = new GenoWindow(variables, 500, 500);
		OverView overView = new OverView(variables, window, new LinkCollection(variables));
		collection.setOverview(overView);
		collection.syncAdditions();
		return collection;
	}

	/**
	 * Test of increment method, of class LinkRangeIterator.
	 */
	@Test
	public void testIncrement() {
		System.out.println("increment");
		LinkRangeIterator instance = getIterator();
		instance.increment();
		assertEquals(instance.currentIndex, 1);
	}

	/**
	 * Test
	 * of
	 * decrement
	 * method,
	 * of
	 * class
	 * LinkRangeIterator.
	 */
	@Test
	public void testDecrement() {
		System.out.println("decrement");
		LinkRangeIterator instance = getIterator();
		instance.increment();
		instance.increment();
		instance.decrement();
		assertEquals(instance.currentIndex, 1);
	}

	/**
	 * Test
	 * of
	 * value
	 * method,
	 * of
	 * class
	 * LinkRangeIterator.
	 */
	@Test
	public void testValue_int() {
		System.out.println("value");
		int i = 0;
		LinkRangeIterator instance = getIterator();
		GeneralLink expected = aB;
		GeneralLink result = instance.value(i);
		assertEquals(expected, result);
	}

	/**
	 * Test
	 * of
	 * inRange
	 * method,
	 * of
	 * class
	 * LinkRangeIterator.
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
		fail("This test includes generation of actual Links, not done yet.");
	}

	/**
	 * Test
	 * of
	 * inRange
	 * method,
	 * of
	 * class
	 * LinkRangeIterator.
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
		fail("This test includes generation of actual Links, not done yet.");
	}
}
