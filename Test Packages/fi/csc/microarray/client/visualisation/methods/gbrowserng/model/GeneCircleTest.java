/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;


import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
//import math.Vector2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GeneCircleTest {
	
	public GeneCircleTest() {
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
	 * Test of tick method, of class GeneCircle.
	 */
	@Test
	public void testTick() {
		System.out.println("tick");
		float dt = 0.0F;
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		instance.tick(dt);
		float minResult = instance.getMinimumChromosomeSlice();
		float minExpected = (0.6f / 1f);
		assertEquals(minResult, minExpected, 0.00000001);
		
		float[] resultBoundaries = instance.getChromosomeBoundaries();
		float[] expectedBoundaries = {1.0f};
		float resultBoundary = resultBoundaries[0];
		float expectedBoundary = expectedBoundaries[0];
		assertEquals(expectedBoundary, resultBoundary, 0.00001);
		
		Vector2[] resultPositions = instance.getChromosomeBoundariesPositions();
		Vector2 resultPosition = resultPositions[0];
		Vector2 expectedPosition = new Vector2();
		expectedPosition.x = -0.0f;
		expectedPosition.y = 0.0f;
		
		assertEquals(expectedPosition.x, resultPosition.x, 0.000001);
		assertEquals(expectedPosition.y, resultPosition.y, 0.000001);
		
	}

	private GlobalVariables getGlobalVariables() {
		GlobalVariables variables = new GlobalVariables();
		variables.genome = new Genome();
		variables.genome.addChromosome(new ViewChromosome(1, 100L));
		return variables;
	}
	
		private GlobalVariables getGlobalVariables_2Links() {
		GlobalVariables variables = new GlobalVariables();
		variables.genome = new Genome();
		variables.genome.addChromosome(new ViewChromosome(1, 100L));
		variables.genome.addChromosome(new ViewChromosome(2, 200L));
		return variables;
	}

	/**
	 * Test of updatePosition method, of class GeneCircle.
	 */
	@Test
	public void testUpdatePosition_Chr1() {
		System.out.println("updatePosition");
		float pointerGenePosition = 0.1F;
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		instance.updatePosition(pointerGenePosition);
		ViewChromosome chr = instance.getChromosome();
		Long position = instance.getChromosomePosition();

		ViewChromosome expChr = new ViewChromosome(1, 100L);
		Long expPosition = 14L;
		
		assertEquals(chr.getChromosomeNumber(), expChr.getChromosomeNumber());
		
		assertEquals(expPosition, position);
		
	}
	
	@Test
	public void testUpdatePosition_Chr2() {
		System.out.println("updatePosition");
		float pointerGenePosition = 0.6F;
		GlobalVariables variables = this.getGlobalVariables_2Links();
		GeneCircle instance = new GeneCircle(variables);
		instance.updatePosition(pointerGenePosition);
		ViewChromosome chr = instance.getChromosome();
		Long position = instance.getChromosomePosition();

		ViewChromosome expChr = new ViewChromosome(2, 100L);
		Long expPosition = 76L;
		
		assertEquals(chr.getChromosomeNumber(), expChr.getChromosomeNumber());
		
		assertEquals(expPosition, position);
		
	}
	

	/**
	 * Test of getChromosomeByRelativePosition method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomeByRelativePosition() {
		System.out.println("getChromosomeByRelativePosition");
//		float relativePosition = 0.0F;
//		GeneCircle instance = new GeneCircle();
//		ViewChromosome expResult = null;
//		ViewChromosome result = instance.getChromosomeByRelativePosition(relativePosition);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosome method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosome() {
		System.out.println("getChromosome");
//		GeneCircle instance = new GeneCircle();
//		ViewChromosome expResult = null;
//		ViewChromosome result = instance.getChromosome();
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosomePosition method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomePosition() {
		System.out.println("getChromosomePosition");
//		GeneCircle instance = new GeneCircle();
//		long expResult = 0L;
//		long result = instance.getChromosomePosition();
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosomeBoundaries method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomeBoundaries() {
		System.out.println("getChromosomeBoundaries");
//		GeneCircle instance = new GeneCircle();
//		float[] expResult = null;
//		float[] result = instance.getChromosomeBoundaries();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChromosomeBoundariesPositions method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomeBoundariesPositions() {
		System.out.println("getChromosomeBoundariesPositions");
//		GeneCircle instance = new GeneCircle();
//		Vector2[] expResult = null;
//		Vector2[] result = instance.getChromosomeBoundariesPositions();
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRelativePosition method, of class GeneCircle.
	 */
	@Test
	public void testGetRelativePosition() {
		System.out.println("getRelativePosition");
//		int chromosome = 0;
//		float relativeChromosomePosition = 0.0F;
//		GeneCircle instance = new GeneCircle();
//		float expResult = 0.0F;
//		float result = instance.getRelativePosition(chromosome, relativeChromosomePosition);
//		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getXYPosition method, of class GeneCircle.
	 */
	@Test
	public void testGetXYPosition() {
		System.out.println("getXYPosition");
//		float relativeCirclePos = 0.0F;
//		GeneCircle instance = new GeneCircle();
//		Vector2 expResult = null;
//		Vector2 result = instance.getXYPosition(relativeCirclePos);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSize method, of class GeneCircle.
	 */
	@Test
	public void testGetSize() {
		System.out.println("getSize");
//		GeneCircle instance = new GeneCircle();
//		float expResult = 0.0F;
//		float result = instance.getSize();
//		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSize method, of class GeneCircle.
	 */
	@Test
	public void testSetSize() {
		System.out.println("setSize");
//		float size = 0.0F;
//		GeneCircle instance = new GeneCircle();
//		instance.setSize(size);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
