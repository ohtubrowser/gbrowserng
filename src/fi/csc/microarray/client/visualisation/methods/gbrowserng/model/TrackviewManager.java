package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager.GBrowserPreview;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Region;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

public class TrackviewManager extends Container {

	private static final File BAM_DATA_FILE;
	private static final File BAI_DATA_FILE;
	private static final File CYTOBAND_FILE;
	private static final File CYTOBAND_REGION_FILE;
	private static final File GTF_ANNOTATION_FILE;
	private static final String dataPath;

	static {

		dataPath = "/cs/group2/home/gbrowsng/";

		BAM_DATA_FILE = new File(dataPath + "ohtu-within-chr.bam");
		BAI_DATA_FILE = new File(dataPath + "ohtu-within-chr.bam.bai");
		CYTOBAND_FILE = new File(dataPath + "karyotypeHuman.txt");
		CYTOBAND_REGION_FILE = new File(dataPath + "seq_regionHuman.txt");

//ftp://ftp.ensembl.org/pub/release-65/gtf/homo_sapiens/Homo_sapiens.GRCh37.65.gtf.gz
		GTF_ANNOTATION_FILE = new File(dataPath + "Homo_sapiens.GRCh37.65.gtf");
	}
	private GenoWindow genoWindow;
	private ArrayList<GBrowserPreview> sessions = new ArrayList<GBrowserPreview>(), newSessions = new ArrayList<GBrowserPreview>();
	private PreviewManager previewManager = new PreviewManager();
	public final Object switchLock = new Object();

	public TrackviewManager(GenoWindow window) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(window.window.getWidth(), window.window.getHeight()));
		genoWindow = window;
	}

	public void clearContainer() {
		for (GBrowserPreview p : newSessions) {
			previewManager.removePreview(p);
		}
		sessions.clear();
		removeAll();
	}

	public void openLinkSession(GeneralLink l) {
		synchronized (switchLock) {
			clearContainer();
			ViewChromosome a = l.getAChromosome();
			ViewChromosome b = l.getBChromosome();
			Region regiona = new Region(l.getaStart(), l.getaStart() + 10000, new Chromosome(a.getName()));
			Region regionb = new Region(l.getbStart(), l.getbStart() + 10000, new Chromosome(b.getName()));
			GBrowserPreview sessionA = previewManager.createPreview(regiona, BAM_DATA_FILE, BAI_DATA_FILE, CYTOBAND_FILE, CYTOBAND_REGION_FILE, GTF_ANNOTATION_FILE);
			GBrowserPreview sessionB = previewManager.createPreview(regionb, BAM_DATA_FILE, BAI_DATA_FILE, CYTOBAND_FILE, CYTOBAND_REGION_FILE, GTF_ANNOTATION_FILE);
			sessions.add(sessionA);
			sessions.add(sessionB);
			add(previewManager.getSplitJComponent(sessionA, sessionB), BorderLayout.CENTER);
			validate();
		}
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
}
