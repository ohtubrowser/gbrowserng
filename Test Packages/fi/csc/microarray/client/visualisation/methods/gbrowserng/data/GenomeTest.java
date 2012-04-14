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
public class GenomeTest {
	
	public GenomeTest() {
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
	 * Test of setName method, of class Genome.
	 */
	@Test
	public void testSetName() {
		System.out.println("setName");
		String name = "";
		Genome.setName(name);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getName method, of class Genome.
	 */
	@Test
	public void testGetName() {
		System.out.println("getName");
		String expResult = "";
		String result = Genome.getName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTotalLength method, of class Genome.
	 */
	@Test
	public void testGetTotalLength() {
		System.out.println("getTotalLength");
		long expResult = 0L;
		long result = Genome.getTotalLength();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStartPoint method, of class Genome.
	 */
	@Test
	public void testGetStartPoint() {
		System.out.println("getStartPoint");
		int id = 0;
		long expResult = 0L;
		long result = Genome.getStartPoint(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getEndPoint method, of class Genome.
	 */
	@Test
	public void testGetEndPoint() {
		System.out.println("getEndPoint");
//		int id = 0;
//		long expResult = 0L;
//		long result = Genome.getEndPoint(id);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addChromosome method, of class Genome.
	 */
	@Test
	public void testAddChromosome() {
		System.out.println("addChromosome");
		ViewChromosome chromosome = null;
		Genome.addChromosome(chromosome);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNumChromosomes method, of class Genome.
	 */
	@Test
	public void testGetNumChromosomes() {
		System.out.println("getNumChromosomes");
		int expResult = 0;
		int result = Genome.getNumChromosomes();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosomeByPosition method, of class Genome.
	 */
	@Test
	public void testGetChromosomeByPosition() {
		System.out.println("getChromosomeByPosition");
//		long position = 0L;
//		ViewChromosome expResult = null;
//		ViewChromosome result = Genome.getChromosomeByPosition(position);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosome method, of class Genome.
	 */
	@Test
	public void testGetChromosome() {
		System.out.println("getChromosome");
		int id = 0;
		ViewChromosome expResult = null;
		ViewChromosome result = Genome.getChromosome(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class Genome.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		Genome.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosomes method, of class Genome.
	 */
	@Test
	public void testGetChromosomes() {
		System.out.println("getChromosomes");
		ArrayList expResult = null;
		ArrayList result = Genome.getChromosomes();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
