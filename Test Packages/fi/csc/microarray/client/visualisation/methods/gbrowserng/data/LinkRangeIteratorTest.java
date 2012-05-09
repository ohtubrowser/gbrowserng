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
	 * Test of rewind method, of class LinkRangeIterator. 
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
		LinkRangeIterator instance = new LinkRangeIterator(collection, 0, 4);
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
		aB = new GeneralLink(aChromosome, bChromosome, 10, 20, false);
		bA = new GeneralLink(bChromosome, aChromosome, 300, 400, true);
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
	 * Test of decrement method, of class LinkRangeIterator.
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
	 * Test of decrement method, of class LinkRangeIterator.
	 */
	@Test
	public void testDecrementOverZero() {
		System.out.println("decrementOverZero");
		LinkRangeIterator instance = getIterator();
		System.out.println(instance.currentIndex);
		instance.increment();
		instance.increment();
		instance.decrement();
		instance.decrement();
		instance.decrement();
		assertEquals(3, instance.currentIndex);
	}

	/**
	 * Test of value method, of class LinkRangeIterator.
	 */
	@Test
	public void testValue_intChr1() {
		System.out.println("value");
		int i = 0;
		LinkRangeIterator instance = getIterator();
		GeneralLink expected = aB;
		GeneralLink result = instance.value(i);
		assertEquals(expected.getAChromosome().getChromosomeNumber(), result.getAChromosome().getChromosomeNumber());
	}

	
	/**
	 * Test of value method, of class LinkRangeIterator.
	 */
	@Test
	public void testValue_intChr2() {
		System.out.println("value");
		int i = 1;
		LinkRangeIterator instance = getIterator();
		GeneralLink expected = bA;
		GeneralLink result = instance.value(i);
		assertEquals(expected.getAChromosome().getChromosomeNumber(), result.getAChromosome().getChromosomeNumber());
	}

	/**
	 * Test of inRange method, of class LinkRangeIterator.
	 */
	@Test
	public void testInRange_GeneralLink_ExistingLink() {
		System.out.println("inRange");
		LinkRangeIterator instance = getIterator();
		GeneralLink link = aB;
		boolean expResult = true;
		boolean result = instance.inRange(link);
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of inRange method, of class LinkRangeIterator.
	 */
	@Test
	public void testInRange_GeneralLink_NonExistingLink() {
		System.out.println("inRange");
		LinkRangeIterator instance = getIterator();
		ViewChromosome a2Chromosome = new ViewChromosome(3, "3", 500, 500);
		ViewChromosome b2Chromosome = new ViewChromosome(4, "4", 500, 500);
		GeneralLink link = new GeneralLink(a2Chromosome, b2Chromosome, 10, 20, false);;
		boolean expResult = false;
		boolean result = instance.inRange(link);
		assertEquals(expResult, result);
	}

	/**
	 * Test of inRange method, of class LinkRangeIterator.
	 */
	@Test
	public void testInRange_int_true() {
		System.out.println("inRange_int_true");
		int index = 0;
		LinkRangeIterator instance = getIterator();
		boolean expResult = true;
		boolean result = instance.inRange(index);
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of inRange method, of class LinkRangeIterator.
	 */
	@Test
	public void testInRange_int_false() {
		System.out.println("inRange_int_false");
		int index = 5;
		LinkRangeIterator instance = getIterator();
		boolean expResult = false;
		boolean result = instance.inRange(index);
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of inRange method, of class LinkRangeIterator.
	 */
	@Test
	public void testInRange_int_negative() {
		System.out.println("inRange_int_negative");
		int index = -1;
		LinkRangeIterator instance = getIterator();
		boolean expResult = false;
		boolean result = instance.inRange(index);
		assertEquals(expResult, result);
	}
}
