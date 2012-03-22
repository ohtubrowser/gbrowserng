/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import com.jogamp.newt.event.KeyEvent;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class KeyboardTest {
	
	public KeyboardTest() {
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
	 * Test of keyPressed method, of class Keyboard.
	 */
	@Test
	public void testKeyPressed() {
		System.out.println("keyPressed");
		KeyEvent keyEvent = null;
		Keyboard instance = null;
		instance.keyPressed(keyEvent);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of keyReleased method, of class Keyboard.
	 */
	@Test
	public void testKeyReleased() {
		System.out.println("keyReleased");
		KeyEvent keyEvent = null;
		Keyboard instance = null;
		instance.keyReleased(keyEvent);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of keyTyped method, of class Keyboard.
	 */
	@Test
	public void testKeyTyped() {
		System.out.println("keyTyped");
		KeyEvent keyEvent = null;
		Keyboard instance = null;
		instance.keyTyped(keyEvent);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
