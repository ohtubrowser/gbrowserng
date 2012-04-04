package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

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
import java.io.File;

public class TrackviewManager extends Container {

	private static final File BAM_DATA_FILE;
	private static final File BAI_DATA_FILE;
	private static final File CYTOBAND_FILE;
	private static final File CYTOBAND_REGION_FILE;
	private static final File GTF_ANNOTATION_FILE;
	private static final String dataPath;

	static {

		dataPath = "/cs/group2/home/gbrowsng/";

//		dataPath = "";
		BAM_DATA_FILE = new File(dataPath + "ohtu-within-chr.bam");
		BAI_DATA_FILE = new File(dataPath + "ohtu-within-chr.bam.bai");
		CYTOBAND_FILE = new File(dataPath + "karyotypeHuman.txt");
		CYTOBAND_REGION_FILE = new File(dataPath + "seq_regionHuman.txt");

//ftp://ftp.ensembl.org/pub/release-65/gtf/homo_sapiens/Homo_sapiens.GRCh37.65.gtf.gz
		GTF_ANNOTATION_FILE = new File(dataPath + "Homo_sapiens.GRCh37.65.gtf");
	}
	private GenoWindow genoWindow;
	private PreviewManager previewManager = new PreviewManager();
	private GBrowserPreview sessionA, sessionB;
	private Region regionA = new Region(0l, 1l, new Chromosome("1")), regionB = new Region(0l, 1l, new Chromosome("1")); // TODO : need some good defaults
	public final Object switchLock = new Object();
	private GeneralLink currentLink;

	public TrackviewManager(GenoWindow window) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(window.window.getWidth(), window.window.getHeight()));
		genoWindow = window;
		// Comment next lines if you don't have the files
		sessionA = previewManager.createPreview(regionA, BAM_DATA_FILE, BAI_DATA_FILE, CYTOBAND_FILE, CYTOBAND_REGION_FILE, GTF_ANNOTATION_FILE);
		sessionB = previewManager.createPreview(regionB, BAM_DATA_FILE, BAI_DATA_FILE, CYTOBAND_FILE, CYTOBAND_REGION_FILE, GTF_ANNOTATION_FILE);
	}

	public void clearContainer() {
		removeAll();
	}

	public void openLinkSession(GeneralLink l) {
		synchronized (switchLock) {
			clearContainer();
			ViewChromosome a = l.getAChromosome();
			ViewChromosome b = l.getBChromosome();
			regionA = new Region(l.getaStart(), l.getaStart() + 10000, new Chromosome(a.getName()));
			regionB = new Region(l.getbStart(), l.getbStart() + 10000, new Chromosome(b.getName()));
			sessionA.setRegion(regionA);
			sessionB.setRegion(regionB);

			add(previewManager.getSplitJComponent(sessionA, sessionB), BorderLayout.CENTER);
			validate();
			currentLink = l;
		}
	}

	public void openAreaSession(ViewChromosome c, long start, long end) {
		synchronized (switchLock) {
			clearContainer();
			regionA = new Region(start, end, new Chromosome(c.getName()));
			sessionA.setRegion(regionA);
			add(sessionA.getJComponent(), BorderLayout.CENTER);
			validate();
			currentLink = null;
		}
	}

	public SessionViewCapsule generateLinkCapsule(OverView overview) {
		return new SessionViewCapsule(currentLink.getEndChromosome(), currentLink.getEndPosition(), currentLink, overview.getGeneCircle());
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
