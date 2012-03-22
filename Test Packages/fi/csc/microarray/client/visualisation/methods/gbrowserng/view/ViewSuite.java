/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author
 * Mammutti
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.CommonSuite.class, fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.IdsSuite.class, fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManagerTest.class, fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindowTest.class, fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.TrackviewSuite.class, fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverviewSuite.class, fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoGLListenerTest.class})
public class ViewSuite {

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
}
