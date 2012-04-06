/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.soulaim.tech.gles.Color;
import javax.media.opengl.GL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class PrimitiveRendererTest {
	
	public PrimitiveRendererTest() {
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
	 * Test of drawRectangle method, of class PrimitiveRenderer.
	 */
	@Test
	public void testDrawRectangle() {
		System.out.println("drawRectangle");
		float x = 0.0F;
		float y = 0.0F;
		float width = 0.0F;
		float height = 0.0F;
		GL2 gl = null;
		Color color = null;
		PrimitiveRenderer.drawRectangle(x, y, width, height, gl, color);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
