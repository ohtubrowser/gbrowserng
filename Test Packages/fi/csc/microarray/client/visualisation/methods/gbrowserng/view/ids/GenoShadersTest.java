/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids;

import gles.SoulGL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GenoShadersTest {
	
	public GenoShadersTest() {
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
	 * Test of createShaders method, of class GenoShaders.
	 */
	@Test
	public void testCreateShaders() {
		System.out.println("createShaders");
		SoulGL2 gl = null;
		GenoShaders.createShaders(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
