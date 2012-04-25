package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager.GBrowserPreview;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Region;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.SessionViewCapsule;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;

public class TrackviewManager extends Container {

	private static URL BAM_DATA_FILE;
	private static URL BAI_DATA_FILE;
	private static URL CYTOBAND_FILE;
	private static URL CYTOBAND_REGION_FILE;
	private static URL GTF_ANNOTATION_FILE;
	private static URL CYTOBAND_COORD_SYSTEM_FILE;
	private static String dataPath;

	static {
		try {
			dataPath = "/cs/group2/home/gbrowsng/";

			//		dataPath = "";
			BAM_DATA_FILE = new File(dataPath + "ohtu-within-chr.bam").toURI().toURL();
			BAI_DATA_FILE = new File(dataPath + "ohtu-within-chr.bam.bai").toURI().toURL();
			CYTOBAND_FILE = new File(dataPath + "karyotypeHuman.txt").toURI().toURL();
			CYTOBAND_REGION_FILE = new File(dataPath + "seq_regionHuman.txt").toURI().toURL();

			//ftp://ftp.ensembl.org/pub/release-65/gtf/homo_sapiens/Homo_sapiens.GRCh37.65.gtf.gz
			GTF_ANNOTATION_FILE = new File(dataPath + "Homo_sapiens.GRCh37.65.gtf").toURI().toURL();
			CYTOBAND_COORD_SYSTEM_FILE = new File(dataPath + "coord_system.txt").toURI().toURL();
		} catch (MalformedURLException ex) {
			Logger.getLogger(TrackviewManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private GenoWindow genoWindow;
	private PreviewManager previewManager = new PreviewManager();
	private GBrowserPreview sessionA, sessionB, sessionC;
	private Region regionA = new Region(0l, 1l, new Chromosome("1")), regionB = new Region(0l, 1l, new Chromosome("1")); // TODO : need some good defaults
	public final Object switchLock = new Object();
	private GeneralLink currentLink;
	private JButton closeButton = new JButton("Close");

	public TrackviewManager(GenoWindow window) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(window.c.getWidth(), window.c.getHeight()));
		genoWindow = window;
		try {
			// Comment next lines if you don't have the files
			sessionA = previewManager.createPreview(regionA, BAM_DATA_FILE, BAI_DATA_FILE, CYTOBAND_FILE, CYTOBAND_REGION_FILE, CYTOBAND_COORD_SYSTEM_FILE, GTF_ANNOTATION_FILE);
			sessionB = previewManager.createPreview(regionB, BAM_DATA_FILE, BAI_DATA_FILE, CYTOBAND_FILE, CYTOBAND_REGION_FILE, CYTOBAND_COORD_SYSTEM_FILE, GTF_ANNOTATION_FILE);
			sessionC = previewManager.createPreview(regionB, BAM_DATA_FILE, BAI_DATA_FILE, CYTOBAND_FILE, CYTOBAND_REGION_FILE, CYTOBAND_COORD_SYSTEM_FILE, GTF_ANNOTATION_FILE);
			genoWindow.overView.CapsuleManager.setPreviewSession(sessionC);
		} catch (URISyntaxException ex) {
			Logger.getLogger(TrackviewManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				genoWindow.overView.activeCapsule.setRegion(sessionA.getRegion(), sessionB.getRegion());
				toggleVisible();
			}
		});
	}

	public void clearContainer() {
		removeAll();
		add(closeButton, BorderLayout.NORTH);
	}

	public void openLinkSession(Region a, Region b) {
		clearContainer();
		regionA = a;
		regionB = b;

		try {
			sessionA.setRegion(regionA);
			sessionB.setRegion(regionB);

			add(previewManager.getSplitJComponent(sessionA, sessionB), BorderLayout.CENTER);
			validate();
			repaint();
		} catch (URISyntaxException ex) {
			Logger.getLogger(TrackviewManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void openAreaSession(Region r) {
		clearContainer();
		try {
			regionA = r.clone();
			sessionA.setRegion(regionA);
			add(sessionA.getJComponent(), BorderLayout.CENTER);
			validate();
			repaint();
			currentLink = null;
		} catch (Exception ex) {
			Logger.getLogger(TrackviewManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public SessionViewCapsule generateLinkCapsule(OverView overview) {
		return new SessionViewCapsule(genoWindow.overView, currentLink.getEndChromosome(), currentLink.getEndPosition(), currentLink, overview.getGeneCircle());
	}

	public void toggleVisible() {
		genoWindow.toggleVisible();
	}

	@Override
	public void paint(Graphics g) {
		synchronized (switchLock) {
			super.paint(g);
		}
	}

	public GeneralLink getLink() {
		return currentLink;
	}

}
