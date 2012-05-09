/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

//import math.Matrix4;
//import math.Vector2;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.CoordinateManager;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class CoordinateManagerTest {
	
	public CoordinateManagerTest() {
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
	 * Test of getCircleMatrix method, of class CoordinateManager.
	 */
	@Test
	public void testGetCircleMatrix_DefaultAspectRatio() {
		System.out.println("getCircleMatrix_DefaultAR");
		GlobalVariables variables = new GlobalVariables();
		Matrix4 expResult = new Matrix4();
		Matrix4 result = CoordinateManager.getCircleMatrix(variables);
		assertEquals(expResult.toString(), result.toString());
	}
	
	/**
	 * Test of getCircleMatrix method, of class CoordinateManager.
	 * 
	 */
	@Test
	public void testGetCircleMatrix_ChangedAspectRatio() {
		System.out.println("getCircleMatrix_ChangedAR");
		GlobalVariables variables = new GlobalVariables();
		variables.aspectRatio = 0.2f;
		Matrix4 expResult = new Matrix4();
		expResult.makeScaleMatrix(1.0f, variables.aspectRatio, 1.0f);
		Matrix4 result = CoordinateManager.getCircleMatrix(variables);
		assertEquals(expResult.toString(), result.toString());
	}

	/**
	 * Test of toCircleCoords method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoords_DefaultAspectRatio() {
		System.out.println("toCircleCoords_DefaultAR");
		GlobalVariables variables = new GlobalVariables();
		Vector2 v = new Vector2();
		v.x = 0.0f;
		v.y = 0.1f;
		Vector2 expResult = new Vector2();
		expResult.x = 0.0f;
		expResult.y = 0.1f;
		Vector2 result = CoordinateManager.toCircleCoords(variables, v);
		assertEquals(expResult.x, result.x, 0.0000001);
		assertEquals(expResult.y, result.y, 0.0000001);
	}

		/**
	 * Test of toCircleCoords method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoords_ChangedAspectRatio() {
		System.out.println("toCircleCoords_ChangedAR");
		GlobalVariables variables = new GlobalVariables();
		variables.aspectRatio = 0.2f;
		Vector2 v = new Vector2();
		v.x = 0.0f;
		v.y = 0.1f;
		Vector2 expResult = new Vector2();
		expResult.x = 0.0f;
		expResult.y = 0.1f * variables.aspectRatio;
		Vector2 result = CoordinateManager.toCircleCoords(variables, v);
		assertEquals(expResult.x, result.x, 0.0000001);
		assertEquals(expResult.y, result.y, 0.0000001);
	}
	
	
	/**
	 * Test of toCircleCoordsX method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoordsX_DefaultAspectRatio() {
		System.out.println("toCircleCoordsX_DefaultAR");
		GlobalVariables variables = new GlobalVariables();
		float x = 0.0F;
		float expResult = 0.0F;
		float result = CoordinateManager.toCircleCoordsX(variables, x);
		assertEquals(expResult, result, 0.0000001);
	}
	
		
	/**
	 * Test of toCircleCoordsX method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoordsX_ChangedAspectRatio() {
		System.out.println("toCircleCoordsX_ChangedAR");
		GlobalVariables variables = new GlobalVariables();
		variables.aspectRatio = 0.2f;
		float x = 0.0F;
		float expResult = 0.0F;
		float result = CoordinateManager.toCircleCoordsX(variables, x);
		assertEquals(expResult, result, 0.0000001);
	}

	/**
	 * Test of toCircleCoordsY method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoordsY_ChangedAR() {
		System.out.println("toCircleCoordsY_ChangedAR");
		GlobalVariables variables = new GlobalVariables();
		variables.aspectRatio = 0.2f;
		float y = 0.0F;
		float expResult = 0.0F;
		float result = CoordinateManager.toCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.0000001);
	}
	
	/**
	 * Test of toCircleCoordsY method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoordsY_ChangedAR2() {
		System.out.println("toCircleCoordsY_ChangedAR");
		GlobalVariables variables = new GlobalVariables();
		variables.aspectRatio = 0.2f;
		float y = 1.0F;
		float expResult = 0.2F;
		float result = CoordinateManager.toCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.0000001);
	}
	
	
	
	/**
	 * Test of toCircleCoordsY method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoordsY_DefaultAR() {
		System.out.println("toCircleCoordsY_DefaultAR");
		GlobalVariables variables = new GlobalVariables();
		float y = 0.0F;
		float expResult = 0.0F;
		float result = CoordinateManager.toCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.0000001);
	}
	
	/**
	 * Test of toCircleCoordsY method, of class CoordinateManager.
	 */
	@Test
	public void testToCircleCoordsY_DefaultAR2() {
		System.out.println("toCircleCoordsY_DefaultAR");
		GlobalVariables variables = new GlobalVariables();
		float y = 0.2F;
		float expResult = 0.2F;
		float result = CoordinateManager.toCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.1);
	}

	/**
	 * Test of fromCircleCoordsX method, of class CoordinateManager.
	 */
	@Test
	public void testFromCircleCoordsX() {
		System.out.println("fromCircleCoordsX");
		GlobalVariables variables = new GlobalVariables();
		float x = 0.0F;
		float expResult = 0.0F;
		float result = CoordinateManager.fromCircleCoordsX(variables, x);
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of fromCircleCoordsY method, of class CoordinateManager.
	 */
	@Test
	public void testFromCircleCoordsY_DefaultAspectRatio() {
		System.out.println("fromCircleCoordsY");
		GlobalVariables variables = new GlobalVariables();
		float y = 0.0F;
		float expResult = 0.0F;
		float result = CoordinateManager.fromCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.0);
	}
	
		/**
	 * Test of fromCircleCoordsY method, of class CoordinateManager.
	 */
	@Test
	public void testFromCircleCoordsY_DefaultAspectRatio2() {
		System.out.println("fromCircleCoordsY");
		GlobalVariables variables = new GlobalVariables();
		float y = 0.2F;
		float expResult = 0.2F;
		float result = CoordinateManager.fromCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.1);
	}
	
	@Test
	public void testFromCircleCoordsY_ChangedAspectRatio() {
		System.out.println("fromCircleCoordsY");
		GlobalVariables variables = new GlobalVariables();
		variables.aspectRatio = 0.2f;
		float y = 0.2F;
		float expResult = 1.0F;
		float result = CoordinateManager.fromCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.1);
	}
	
	@Test
	public void testFromCircleCoordsY_ChangedAspectRatio2() {
		System.out.println("fromCircleCoordsY");
		GlobalVariables variables = new GlobalVariables();
		variables.aspectRatio = 0.2f;
		float y = 0.0F;
		float expResult = 0.0F;
		float result = CoordinateManager.fromCircleCoordsY(variables, y);
		assertEquals(expResult, result, 0.1);
	}
}
