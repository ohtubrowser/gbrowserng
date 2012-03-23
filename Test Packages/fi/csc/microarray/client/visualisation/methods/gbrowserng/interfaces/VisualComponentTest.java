/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import javax.media.opengl.GL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class VisualComponentTest {
	
	public VisualComponentTest() {
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
	 * Test of draw method, of class VisualComponent.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
//		GL2 gl = null;
//		VisualComponent instance = new VisualComponentImpl();
//		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tick method, of class VisualComponent.
	 */
	@Test
	public void testTick() {
		System.out.println("tick");
//		float dt = 0.0F;
//		VisualComponent instance = new VisualComponentImpl();
//		instance.tick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class VisualComponentImpl implements VisualComponent {

		public void draw(GL2 gl) {
		}

		public void tick(float dt) {
		}
	}
}
