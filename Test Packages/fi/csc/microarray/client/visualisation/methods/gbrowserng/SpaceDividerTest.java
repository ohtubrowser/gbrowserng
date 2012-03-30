/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ReferenceSequence;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.CascadingComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.GenoVisualBorder;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.SessionViewCapsule;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import java.util.ArrayList;
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
	 * Test of insertComponent method
	 */
	@Test
	public void testInsertComponent() {
		System.out.println("insertComponent");
		SessionViewCapsule capsule = generateCapsule();
		SpaceDivider instance = new SpaceDivider(1, 2, 3);
		instance.insertComponent(capsule);
		int size = instance.getComponents().size();
		System.out.println(size);
		ArrayList<CascadingComponent> listOfComponents = instance.getComponents();
		boolean empty = listOfComponents.isEmpty();
		assertFalse(empty);
	}
	
	
	/**
	 * Generates SessionViewCapsule that implements CascadingComponent (abstract). 
	 * All code is needed to be able to create a SessionViewCapsule - better suggestions accepted.
	 * @return generated Capsule
	 */
	private SessionViewCapsule generateCapsule() {
		ReferenceSequence sequence = new ReferenceSequence(1, 1);
		fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session session = 
				new fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session(sequence, 1);
		GenoVisualBorder border = null;
		border = new GenoVisualBorder(border);
		SessionView view = new SessionView(session, border);
		AbstractGenome.addChromosome(new ViewChromosome(1, 1));
		return new SessionViewCapsule(view, 0, new GeneCircle()); 
	}

	/**
	 * Test of calculate method. 
	 */
	@Test
	public void testCalculateHorizontal() {
		System.out.println("calculate");
		
		SpaceDivider divider = new SpaceDivider(0, 2, 3);
		SessionViewCapsule capsule = generateCapsule();
		divider.insertComponent(capsule);
		divider.calculate();
		
		float componentSize = Math.max(2, Math.min(2.0f / 1, 3));
		float pos = 0.5f * componentSize - 1.0f;
		
		Vector2 position = capsule.getPosition();
		Vector2 dimensions = capsule.getTargetDimensions();
		Vector2 assumedPosition = new Vector2(pos, 0);
		Vector2 assumedDimensions = new Vector2(componentSize, 2);
		assertEquals(position.toString(), assumedPosition.toString());
		assertEquals(dimensions.toString(), assumedDimensions.toString());
		
	}
	
		/**
	 * Test of calculate method. 
	 */
	@Test
	public void testCalculateVertical() {
		System.out.println("calculate");

		SpaceDivider instance = new SpaceDivider(1, 2, 3);
		SessionViewCapsule capsule = generateCapsule();
		instance.insertComponent(capsule);
		instance.calculate();

		float componentSize = Math.max(2, Math.min(2.0f / 1, 3));
		float pos = 0.5f * componentSize - 1.0f;
		
		Vector2 position = capsule.getPosition();
		Vector2 dimensions = capsule.getTargetDimensions();
		Vector2 assumedPosition = new Vector2(0, pos);
		Vector2 assumedDimensions = new Vector2(2, componentSize);
		assertEquals(position.toString(), assumedPosition.toString());
		assertEquals(dimensions.toString(), assumedDimensions.toString());
		
	}
}
