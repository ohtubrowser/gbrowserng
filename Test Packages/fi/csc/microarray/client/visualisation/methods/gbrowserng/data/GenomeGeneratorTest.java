/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GenomeGeneratorTest {
	
	public GenomeGeneratorTest() {
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
	 * Test of generateSequence method, of class GenomeGenerator.
	 */
	@Test
	public void testGenerateSequence() {
		System.out.println("generateSequence");
		int size = 0;
		char[] expResult = null;
		char[] result = GenomeGenerator.generateSequence(size);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRandomGenomeLetter method, of class GenomeGenerator.
	 */
	@Test
	public void testGetRandomGenomeLetter() {
		System.out.println("getRandomGenomeLetter");
		char expResult = ' ';
		char result = GenomeGenerator.getRandomGenomeLetter();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of generateRead method, of class GenomeGenerator.
	 */
	@Test
	public void testGenerateRead() {
		System.out.println("generateRead");
		ReferenceSequence referenceSequence = null;
		Read expResult = null;
		Read result = GenomeGenerator.generateRead(referenceSequence);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
