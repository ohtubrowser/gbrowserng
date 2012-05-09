
package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Testing of class GeneCircle
 * @author Kristiina
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
	
		private GlobalVariables getGlobalVariables_2Chrs() {
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
		assertEquals(chr.getChromosomeNumber(), expChr.getChromosomeNumber());
		Long expPosition = 14L;

		assertEquals(expPosition, position);
		
	}
	
	@Test
	public void testUpdatePosition_Chr2() {
		System.out.println("updatePosition_2Chr");
		float pointerGenePosition = 0.6F;
		GlobalVariables variables = this.getGlobalVariables_2Chrs();
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
	public void testGetChromosomeByRelativePosition_1Chr() {
		System.out.println("getChromosomeByRelativePosition");
		float relativePosition = 0.1F;
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		ViewChromosome expResult = new ViewChromosome(1, 100L);
		ViewChromosome result = instance.getChromosomeByRelativePosition(relativePosition);
		assertEquals(expResult.getChromosomeNumber(), result.getChromosomeNumber());
	}
	
	
	/**
	 * Test of getChromosomeByRelativePosition method, 2 chromosmes, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomeByRelativePosition_2Chr() {
		System.out.println("getChromosomeByRelativePosition_2Chr");
		GlobalVariables variables = this.getGlobalVariables_2Chrs();
		GeneCircle instance = new GeneCircle(variables);
		float relativePosition = 0.2F;
		ViewChromosome expResult = new ViewChromosome(2, 100L);
		ViewChromosome result = instance.getChromosomeByRelativePosition(relativePosition);
		assertEquals(expResult.getChromosomeNumber(), result.getChromosomeNumber());
	}

	/**
	 * Test of getChromosome method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosome_2Chr() {
		System.out.println("getChromosome_2Chr");
		GlobalVariables variables = this.getGlobalVariables_2Chrs();
		GeneCircle instance = new GeneCircle(variables);
		ViewChromosome expResult = new ViewChromosome(1, 100L);
		ViewChromosome result = instance.getChromosome();
		assertEquals("Chromosome shoule be 1 at beginning", expResult.getChromosomeNumber(), result.getChromosomeNumber());
		
		instance.updatePosition(0.6F);
		expResult = new ViewChromosome(2, 100L);
		result = instance.getChromosome();
		assertEquals("Chromosome should be 2 after changing posititon to 0.6F", expResult.getChromosomeNumber(), result.getChromosomeNumber());
		
		instance.updatePosition((0.1F));
		expResult = new ViewChromosome(1, 100L);
		result = instance.getChromosome();
		assertEquals("Chromosome should be 1 after changing position to 0.1F", expResult.getChromosomeNumber(), result.getChromosomeNumber());
	}
	
	/**
	 * Test of getChromosome method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosome_1Chr() {
		System.out.println("getChromosome");
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		ViewChromosome expResult = new ViewChromosome(1, 100L);
		ViewChromosome result = instance.getChromosome();
		assertEquals("Chromosome should be 1, when only 1 chromosome exists", expResult.getChromosomeNumber(), result.getChromosomeNumber());

	}
	

	/**
	 * Test of getChromosomePosition method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomePosition() {
		System.out.println("getChromosomePosition");
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		long expResult = 0L;
		long result = instance.getChromosomePosition();
		assertEquals(expResult, result);
	}

		/**
	 * Test of getChromosomePosition method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomePosition_2Chr() {
		System.out.println("getChromosomePosition_2Chr");
		GlobalVariables variables = this.getGlobalVariables_2Chrs();
		GeneCircle instance = new GeneCircle(variables);
		long expResult = 0L;
		long result = instance.getChromosomePosition();
		assertEquals(expResult, result);
		
		instance.updatePosition(0.6F);
		expResult = 76L;
		result = instance.getChromosomePosition();
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of getChromosomeBoundaries method, 2 chromosomes, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomeBoundaries_1Chr() {
		System.out.println("getChromosomeBoundaries_1Chr");
		GlobalVariables variables = this.getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		float[] result = instance.getChromosomeBoundaries();
		assertNotNull("chromosomeBoundaries should not be null", result);
		int resultSize = result.length;
		int expSize = 2;
		assertEquals("Expected size of chromsomeboundaries for 1 chromsome circle is 2", expSize, resultSize);
	}
	
	/**
	 * Test of getChromosomeBoundaries method, 2 chromosomes, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomeBoundaries_2Chr() {
		System.out.println("getChromosomeBoundaries_2Chr");
		GlobalVariables variables = this.getGlobalVariables_2Chrs();
		GeneCircle instance = new GeneCircle(variables);
		float[] result = instance.getChromosomeBoundaries();
		assertNotNull("chromosomeBoundaries should not be null", result);
		int resultSize = result.length;
		int expSize = 3;
		assertEquals("Expected size of chromsomeboundaries for 1 chromsome circle is 2", expSize, resultSize);
	}

	/**
	 * Test of getChromosomeBoundariesPositions method, of class GeneCircle.
	 */
	@Test
	public void testGetChromosomeBoundariesPositions() {
		System.out.println("getChromosomeBoundariesPositions");
		GlobalVariables variables = this.getGlobalVariables_2Chrs();
		GeneCircle instance = new GeneCircle(variables);
		instance.setSize(50f);
		Vector2[] expResult = new Vector2[2];
		Vector2 vecOne = new Vector2();
		Vector2 vecTwo = new Vector2();
		vecOne.x = -0.0f;
		vecTwo.x = 20.33f;
		vecOne.y = 50.0f;
		vecTwo.y = -45.67f; 
		expResult[0] = vecOne;
		expResult[1] = vecTwo;
		
		Vector2[] result = instance.getChromosomeBoundariesPositions();
		System.out.println(result[0]);
		System.out.println(result[1]);
		assertEquals("boundary 1, x", expResult[0].x, result[0].x, 0.1);
		assertEquals("boundary 1, y", expResult[0].y, result[0].y, 0.1);
		
		assertEquals("boundary 2, x", expResult[1].x, result[1].x, 0.1);
		assertEquals("boundary 2, y", expResult[1].y, result[1].y, 0.1);

	}

	/**
	 * Test of getRelativePosition method, of class GeneCircle.
	 */
	@Test
	public void testGetRelativePosition() {
		System.out.println("getRelativePosition");
		int chromosome = 0;
		float relativeChromosomePosition = 0.0F;
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		float expResult = 1.24F;
		float result = instance.getRelativePosition(chromosome, relativeChromosomePosition);
		assertEquals(expResult, result, 0.1);
	}
	
	/**
	 * Test of getRelativePosition method, of class GeneCircle.
	 */
	@Test
	public void testGetRelativePosition_2Chr() {
		System.out.println("getRelativePosition_2Chr");
		int chromosome = 1;
		float relativeChromosomePosition = 0.6F;
		GlobalVariables variables = getGlobalVariables_2Chrs();
		GeneCircle instance = new GeneCircle(variables);
		float expResult = 0.47F;
		float result = instance.getRelativePosition(chromosome, relativeChromosomePosition);
		assertEquals(expResult, result, 0.1);
	}

	/**
	 * Test of getXYPosition method, of class GeneCircle.
	 */
	@Test
	public void testGetXYPosition_Size0() {
		System.out.println("getXYPosition_Size0");
		float relativeCirclePos = 0.0F;
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		Vector2 expResult = new Vector2();
		expResult.x = -0.0f;
		expResult.y = 0.0f;
		Vector2 result = instance.getXYPosition(relativeCirclePos);
		assertEquals(expResult.x, result.x, 0.000001);
		assertEquals(expResult.y, result.y, 0.000001);
	}
	
	/**
	 * Test of getXYPosition method, of class GeneCircle.
	 */
	@Test
	public void testGetXYPosition_Size50() {
		System.out.println("getXYPosition_Size50");
		float relativeCirclePos = 0.6F;
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		Vector2 expResult = new Vector2();
		expResult.x = 29.39f;
		expResult.y = -40.45f;
		instance.setSize(50F);
		Vector2 result = instance.getXYPosition(relativeCirclePos);
		assertEquals(expResult.x, result.x, 0.1);
		assertEquals(expResult.y, result.y, 0.1);
	}

	/**
	 * Test of getSize method, of class GeneCircle.
	 */
	@Test
	public void testGetSize() {
		System.out.println("getSize");
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		float expResult = 0.0F;
		float result = instance.getSize();
		assertEquals(expResult, result, 0.00000001);

	}

	/**
	 * Test of setSize method, of class GeneCircle.
	 */
	@Test
	public void testSetSize() {
		System.out.println("setSize");
		float expected = 1.0F;
		GlobalVariables variables = getGlobalVariables();
		GeneCircle instance = new GeneCircle(variables);
		instance.setSize(expected);
		float result = instance.getSize();
		assertEquals(expected, result, 0.000001);
	}
}
