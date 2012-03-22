/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class InteractiveComponentTest {
	
	public InteractiveComponentTest() {
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
	 * Test of handle method, of class InteractiveComponent.
	 */
	@Test
	public void testHandle_3args() {
		System.out.println("handle");
		MouseEvent event = null;
		float x = 0.0F;
		float y = 0.0F;
		InteractiveComponent instance = new InteractiveComponentImpl();
		boolean expResult = false;
		boolean result = instance.handle(event, x, y);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of handle method, of class InteractiveComponent.
	 */
	@Test
	public void testHandle_KeyEvent() {
		System.out.println("handle");
		KeyEvent event = null;
		InteractiveComponent instance = new InteractiveComponentImpl();
		boolean expResult = false;
		boolean result = instance.handle(event);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class InteractiveComponentImpl implements InteractiveComponent {

		public boolean handle(MouseEvent event, float x, float y) {
			return false;
		}

		public boolean handle(KeyEvent event) {
			return false;
		}
	}
}
