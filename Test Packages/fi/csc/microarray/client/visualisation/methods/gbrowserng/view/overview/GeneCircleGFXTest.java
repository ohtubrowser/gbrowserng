/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import javax.media.opengl.GL2;
import math.Matrix4;
import math.Vector2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GeneCircleGFXTest {
	
	public GeneCircleGFXTest() {
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
	 * Test of draw method, of class GeneCircleGFX.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
		GL2 gl = null;
		Matrix4 modelMatrix = null;
		Vector2 mousePosition = null;
		GeneCircleGFX instance = null;
		instance.draw(gl, modelMatrix, mousePosition);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of drawChromosomeSeparators method, of class GeneCircleGFX.
	 */
	@Test
	public void testDrawChromosomeSeparators() {
		System.out.println("drawChromosomeSeparators");
		GL2 gl = null;
		GeneCircleGFX instance = null;
		instance.drawChromosomeSeparators(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of drawCentromeres method, of class GeneCircleGFX.
	 */
	@Test
	public void testDrawCentromeres() {
		System.out.println("drawCentromeres");
		GL2 gl = null;
		GeneCircleGFX instance = null;
		instance.drawCentromeres(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tick method, of class GeneCircleGFX.
	 */
	@Test
	public void testTick() {
		System.out.println("tick");
		float dt = 0.0F;
		GeneCircleGFX instance = null;
		instance.tick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setHilight method, of class GeneCircleGFX.
	 */
	@Test
	public void testSetHilight() {
		System.out.println("setHilight");
		float value = 0.0F;
		GeneCircleGFX instance = null;
		instance.setHilight(value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
