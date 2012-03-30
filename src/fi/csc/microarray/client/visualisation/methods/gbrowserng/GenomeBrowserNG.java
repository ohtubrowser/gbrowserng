
package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import com.jogamp.newt.event.NEWTEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration.ChipsterInterface;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main class of program. Opens the overview window containing the genome circle and shows links between chromosomes
 * Contains main and run methods, which together open the program. Contains methods to use either human or rat data for genome and links
 * @author Kristiina Paloheimo
 */
public class GenomeBrowserNG {

	private BlockingQueue<NEWTEvent> eventQueue;
	private GenoWindowListener windowListener;
	private GenoGLListener glListener;
	private GenoWindow genoWindow;
	private EventHandler eventHandler;

	/**
	 * Returns the created EventHandler
	 * @return
	 */
	public EventHandler getEventHandler() {
		return eventHandler;
	}

	/**
	 * Returns the created BlockingQueue of user created Events
	 * @return
	 */
	public BlockingQueue<NEWTEvent> getEventQueue() {
		return eventQueue;
	}

	/**
	 * Returns the created GenoWindow
	 * @return
	 */
	public GenoWindow getGenoWindow() {
		return genoWindow;
	}

	/**
	 * Returns the created GLListener
	 * @return 
	 */
	public GenoGLListener getGlListener() {
		return glListener;
	}

	/**
	 * Returns windowListener
	 * @return the created WindowListener
	 */
	public GenoWindowListener getWindowListener() {
		return windowListener;
	}

/**
	 * Loads all links between chrosomes to a queue and puts all chromosomes in the Genome-class for Rat data.
	 * For the Genome of the organism needed files are karyotype and seq_region.
	 * For the links between chromosomes needed files are chrs.bam and chrs.bam.bai
	 * @return a queue of links between chromosomes
	 */
	public static LinkCollection useChipsterDataRat() {
//                ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData("ftp://ftp.ensembl.org/pub/release-65/mysql/rattus_norvegicus_core_65_34/karyotype.txt.gz",
//                        " ftp://ftp.ensembl.org/pub/release-65/mysql/rattus_norvegicus_core_65_34/seq_region.txt.gz",
		ConcurrentLinkedQueue<ViewChromosome> chromosomeData = ChipsterInterface.getChromosomes("karyotype.txt", "seq_region.txt",
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
					"13", "14", "15", "16", "17", "18", "19", "20", "X"});

		for (ViewChromosome c : chromosomeData) {
			AbstractGenome.addChromosome(c);
		}
		return ChipsterInterface.getConnections(chromosomeData);
	}

	/**
	 * Loads all links between chrosomes to a queue and puts all chromosomes in the Genome-class for Human data.
	 * For the Genome of the organism needed files are karyotype and seq_region.
	 * For the links between chromosomes needed files are chrs.bam and chrs.bam.bai
	 * @return a queue of links between chromosomes
	 */
	public static LinkCollection useChipsterDataHuman() {
//                ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData(
//				"ftp://ftp.ensembl.org/pub/release-65/mysql/homo_sapiens_core_65_37/karyotype.txt.gz", 
//				"ftp://ftp.ensembl.org/pub/release-65/mysql/homo_sapiens_core_65_37/seq_region.txt.gz", 
		ConcurrentLinkedQueue<ViewChromosome> chromosomeData = ChipsterInterface.getChromosomes("karyotypeHuman.txt", "seq_regionHuman.txt",
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
					"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X"});
		for (ViewChromosome c : chromosomeData) {
			AbstractGenome.addChromosome(c);
		}
		return ChipsterInterface.getConnections(chromosomeData);
	}

	/**
	 * Initializes all needed components for opening GenoWindow.
	 * Here choice can be made whether to use Rat or Human data. Links between chromosomes, and gehome, are loaded based on this choice.
	 * @param width	screen width in pixels
	 * @param height screen height in pixels
	 */
	public GenomeBrowserNG(int width, int height) throws ArrayIndexOutOfBoundsException {
		LinkCollection links = useChipsterDataRat();

		this.eventQueue = new LinkedBlockingQueue<NEWTEvent>();
		this.genoWindow = new GenoWindow(width, height);
		OverView overView = new OverView(this.genoWindow, links);
		this.genoWindow.addContainer(overView.trackviewManager);

		this.glListener = new GenoGLListener(overView);
		this.eventHandler = new EventHandler(this.genoWindow, this.glListener.getRoot(), eventQueue, width, height);

		addListeners();
	}

	/**
	 * Initialize all needed listeners and add them to the GenoWindow
	 * Added listeners are: GenoWindowListener, KeyListener, MouseListener, WindowListener and GLEventListener
	 */
	public void addListeners() {
		this.windowListener = new GenoWindowListener(eventQueue);
		this.genoWindow.window.addKeyListener(new Keyboard(eventQueue));
		this.genoWindow.window.addMouseListener(new Mouse(eventQueue));
		this.genoWindow.window.addWindowListener(windowListener);
		this.genoWindow.window.addGLEventListener(glListener);
	}

	/**
	 * Opens GenoWindow and calls on required parties so GenoWindow becomes operational.
	 * Thread started for GLEventListener. Upon exit from program by user, program terminated.
	 * @throws InterruptedException
	 */
	public void run() throws InterruptedException {
		this.genoWindow.open();
		Thread t = new Thread(glListener);
		t.start();

		this.eventHandler.handleEvents();
		glListener.die();
		t.join();
		this.genoWindow.close();
	}
	
	/**
	 * Main method of program, calls on class to run program, based on screen size.
	 * @param args no string input parameters as of this time of development
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		double fraction = 0.8d;
		new GenomeBrowserNG((int) (dim.width * fraction), (int) (dim.height * fraction)).run();
	}


}
