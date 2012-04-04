/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.AnimatedValues;
import javax.media.opengl.GL2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GenosideComponentTest {
	
	public GenosideComponentTest() {
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
	 * Test of getId method, of class GenosideComponent.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
//		GenosideComponent instance = null;
//		int expResult = 0;
//		int result = instance.getId();
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of childComponentCall method, of class GenosideComponent.
	 */
	@Test
	public void testChildComponentCall() {
		System.out.println("childComponentCall");
//		String who = "";
//		String what = "";
//		GenosideComponent instance = null;
//		instance.childComponentCall(who, what);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class GenosideComponent.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
//		MouseEvent event = null;
//		float screen_x = 0.0F;
//		float screen_y = 0.0F;
//		GenosideComponent instance = null;
//		boolean expResult = false;
//		boolean result = instance.handle(event, screen_x, screen_y);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class GenosideComponent.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
//		KeyEvent event = null;
//		GenosideComponent instance = null;
//		boolean expResult = false;
//		boolean result = instance.handle(event);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of draw method, of class GenosideComponent.
	 */
	@Test
	public void testDraw() {
		System.out.println("draw");
//		GL2 gl = null;
//		GenosideComponent instance = null;
//		instance.draw(gl);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of userTick method, of class GenosideComponent.
	 */
	@Test
	public void testUserTick() {
		System.out.println("userTick");
//		float dt = 0.0F;
//		GenosideComponent instance = null;
//		instance.userTick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAnimatedValues method, of class GenosideComponent.
	 */
	@Test
	public void testGetAnimatedValues() {
		System.out.println("getAnimatedValues");
//		GenosideComponent instance = null;
//		AnimatedValues expResult = null;
//		AnimatedValues result = instance.getAnimatedValues();
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tick method, of class GenosideComponent.
	 */
	@Test
	public void testTick() {
		System.out.println("tick");
//		float dt = 0.0F;
//		GenosideComponent instance = null;
//		instance.tick(dt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getParent method, of class GenosideComponent.
	 */
	@Test
	public void testGetParent() {
		System.out.println("getParent");
//		GenosideComponent instance = null;
//		GenosideComponent expResult = null;
//		GenosideComponent result = instance.getParent();
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class GenosideComponentImpl extends GenosideComponent {

		public GenosideComponentImpl() {
			super(null);
		}

		public void childComponentCall(String who, String what) {
		}

		public boolean handle(MouseEvent event, float screen_x, float screen_y) {
			return false;
		}

		public boolean handle(KeyEvent event) {
			return false;
		}

		public void draw(GL2 gl) {
		}

		public void tick(float dt) {
		}
	}
}
