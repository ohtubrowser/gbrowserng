/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng;


import com.soulaim.tech.gles.Color;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GlobalVariablesTest {
	
	public GlobalVariablesTest() {
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

	@Test
	public void testGlobalVariables() {
		System.out.println("initialize global variables");
		GlobalVariables variables = new GlobalVariables();
		assertNotNull(variables);
	}
	
	@Test
	public void testGenomeColors() {
		System.out.println("genome colors");
		GlobalVariables variables = new GlobalVariables();
//		Map<Character, Color> colors = variables.getGenomeColors();
//		assertNotNull(colors);
	}
}
