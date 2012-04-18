
package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration.ChipsterInterface;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.media.opengl.GLProfile;

/**
 * Main class of program. Opens the overview window containing the genome circle and shows links between chromosomes
 * Contains main and run methods, which together open the program. Contains methods to use either human or rat data for genome and links
 * @author Kristiina Paloheimo
 */
public class GenomeBrowserNG {

	private BlockingQueue<AWTEvent> eventQueue;
	private GenoWindowListener windowListener;
	
	private GenoGLListener glListener;
	private GenoWindow genoWindow;
	private EventHandler eventHandler;
	private static String bam, bai;

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
	public BlockingQueue<AWTEvent> getEventQueue() {
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
		ConcurrentLinkedQueue<ViewChromosome> chromosomeData = ChipsterInterface.getChromosomes("karyotype.txt", "seq_region.txt", "coord_system.txt",
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
					"13", "14", "15", "16", "17", "18", "19", "20", "X"});

		for (ViewChromosome c : chromosomeData) {
			Genome.addChromosome(c);
		}
		return ChipsterInterface.getConnections(chromosomeData, bam, bai);
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
		ConcurrentLinkedQueue<ViewChromosome> chromosomeData = ChipsterInterface.getChromosomes("karyotypeHuman.txt", "seq_regionHuman.txt", "coord_systemHuman.txt",
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
					"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X"});
		for (ViewChromosome c : chromosomeData) {
			Genome.addChromosome(c);
		}
		return ChipsterInterface.getConnections(chromosomeData, bam, bai);
	}

	/**
	 * Initializes all needed components for opening GenoWindow.
	 * Here choice can be made whether to use Rat or Human data. Links between chromosomes, and gehome, are loaded based on this choice.
	 * @param width	screen width in pixels
	 * @param height screen height in pixels
	 * @param filtering
	 * @param data
	 * @throws ArrayIndexOutOfBoundsException  
	 */
	public GenomeBrowserNG(int width, int height, long filtering, int data, boolean debug, String bam, String bai) throws ArrayIndexOutOfBoundsException {
		this.bam = bam;
		this.bai = bai;
		LinkCollection links;
		if (data==0) {
			links = useChipsterDataHuman();
		} else if(data==1) {
			links = useChipsterDataRat();
		}else {
			links = useChipsterDataHuman();
		}
		GlobalVariables.filtering = filtering;
		GlobalVariables.debug = debug;

		this.eventQueue = new LinkedBlockingQueue<AWTEvent>();
		this.genoWindow = new GenoWindow(width, height);
		OverView overView = new OverView(this.genoWindow, links);
			
		this.genoWindow.addContainer(overView.getTrackviewManager());

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
		this.genoWindow.frame.addWindowListener(windowListener);
		this.genoWindow.frame.addComponentListener(windowListener);
		this.genoWindow.c.addMouseListener(new Mouse(eventQueue));
		this.genoWindow.c.addMouseMotionListener(new Mouse(eventQueue));
		this.genoWindow.c.addMouseWheelListener(new Mouse(eventQueue));
		this.genoWindow.c.addKeyListener(new Keyboard(eventQueue));
		this.genoWindow.c.addGLEventListener(glListener);
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
	 * @param args command line syntax:
	 * -f [number]		filtering range (0 turns filtering off)
	 * -g h/r			human or rat genome data
	 * -bam [bam-file]	opens a bam-file
	 * -bai [bai-file]	opens a bai-file
	 * -d				debug-mode
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		GLProfile.initSingleton();

		// default values
		boolean debug = false;
		long filtering = 100000;
		int genome = 0;
		String bam="ohtu-between-chrs.bam", bai="ohtu-between-chrs.bam.bai";
		for(int i=0;i<args.length;i++) {
			if(args[i].equals("-f") && args.length > i+1) {
				i++;
				filtering = Long.parseLong(args[i]);
			} else if(args[i].equals("-g") && args.length > i+1) {
				i++;
				if(args[i].equals("h"))
					genome = 0;
				else if(args[i].equals("r"))
					genome = 1;
			} else if(args[i].equals("-bam") && args.length > i+1) {
				i++;
				bam = args[i];
			} else if(args[i].equals("-bai") && args.length > i+1) {
				i++;
				bai = args[i];
			} else if(args[i].equals("-d")) {
				debug = true;
			}
		}
				
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		double fraction = 0.8d;
		new GenomeBrowserNG((int) (dim.width * fraction), (int) (dim.height * fraction), filtering, genome, debug, bam, bai).run();
	}


}
