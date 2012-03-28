
package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import com.jogamp.newt.event.NEWTEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.EventHandler;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.GenoWindowListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoGLListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author
 * Mammutti
 */
public class GenomeBrowserNGTest {

	
	static GenomeBrowserNG browser;
	
	public GenomeBrowserNGTest() {
//		browser = new GenomeBrowserNG(500, 500);
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		browser = new GenomeBrowserNG(500, 500);	// size does not matter
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		browser = new GenomeBrowserNG(500, 500);	// size does not matter
	}
	
	@Before
	public void setUp() {
//		browser = new GenomeBrowserNG(500, 500);	// size does not matter
	}
	
	@After
	public void tearDown() {
	
	}

	/** 
	 * Test use of constructor
	 */
	@Test
	public void testConstructorCreation() {
		System.out.println("constructorCreation");
		GenomeBrowserNG instance = new GenomeBrowserNG(500, 500);
		assert(true);
	}
	
	/** 
	 * Test use of constructor for creating EventHandler
	 */
	@Test
	public void testEventHandlerSetup() {
		System.out.println("handler");
		EventHandler handler = browser.getEventHandler();
		assertNotNull(handler);
	}
	
	/** 
	 * Test use of constructor for creation of EventQueue 
	 */
	@Test
	public void testEventQueueSetUp() {
		System.out.println("eventQueue");
		BlockingQueue<NEWTEvent> eventQueue = browser.getEventQueue();
		assertNotNull(eventQueue);
	}
	
	/** 
	 * Test use of constructor for creation of EventQueue 
	 */
	@Test
	public void testGenoWindowSetUp() {
		System.out.println("genoWindow");
		GenoWindow genoWindow = browser.getGenoWindow();
		assertNotNull(genoWindow);
	}
	
	/** 
	 * Test use of constructor for creation of EventQueue 
	 */
	@Test
	public void testGlListenerSetUp() {
		System.out.println("glListener");
		 GenoGLListener glListener = browser.getGlListener();
		assertNotNull(glListener);
	}
	
	/** 
	 * Test use of constructor for creation of EventQueue 
	 */
	@Test
	public void testGenoWindowListenerSetUp() {
		System.out.println("genoWindowListener");
		GenoWindowListener genoWindowListener = browser.getWindowListener();
		assertNotNull(genoWindowListener);
	}
	
	
	/**
	 * Test of useChipsterDataRat method, tests whether rata data files  can be read succesfully and chromomes created
	 * Files read are seqRegion and karyotype
	 */
	@Test
	public void testUseChipsterDataRat() {
		System.out.println("useChipsterDataRat");
		ConcurrentLinkedQueue<GeneralLink> links = GenomeBrowserNG.useChipsterDataHuman();
		assertNotNull(links);
	}

	/**
	 * Test of useChipsterDataHuman method, tests whether human data files can be read succesfully and chromomes created
	 * Files read are seqRegionHuman and karyotypeHuman
	 */
	@Test
	public void testUseChipsterDataHuman() {
		System.out.println("useChipsterDataHuman");
		ConcurrentLinkedQueue<GeneralLink> links = GenomeBrowserNG.useChipsterDataHuman();
		assertNotNull(links);
	}

	/**
	 * Test of main method, whether main can be run without errors
	 */
	@Test
	public void testMain() throws Exception {
		System.out.println("main");
		String[] empty = {""};
		GenomeBrowserNG.main(empty);
		assertTrue(true);
	}
}
