/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.CascadingComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.SessionViewCapsule;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class SpaceDividerTest {
	
	public SpaceDividerTest() {
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
	 * Test of constructor
	 */
	@Test
	public void testConstructor() {
		System.out.println("constructor");
		SpaceDivider divider = new SpaceDivider(1, 2, 3);
		float maximumSize = divider.getMaximumSize();
		float minimumSize = divider.getMinimumSize();
		int myDirection = divider.getMyDirection();
		assertEquals(myDirection, 1);
		assertEquals(minimumSize, 2, 0.01);
		assertEquals(maximumSize, 3, 0.01);
		
	}
	
	/**
	 * Test of insertComponent method, of class SpaceDivider.
	 */
	@Test
	public void testInsertComponent() {
		System.out.println("insertComponent");
//		
//		CascadingComponent component = which component to put here?
//		SpaceDivider instance = null;
//		instance.insertComponent(component);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of calculate method, of class SpaceDivider.
	 */
	@Test
	public void testCalculate() {
		System.out.println("calculate");
		SpaceDivider divider = new SpaceDivider(1, 2, 3);
		divider.calculate();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
