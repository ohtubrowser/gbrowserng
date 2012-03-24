package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import com.jogamp.newt.event.NEWTEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration.ChipsterInterface;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;

import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Kristiina Paloheimo
 */
public class GenomeBrowserNG {

	private BlockingQueue<NEWTEvent> eventQueue;
	private GenoWindowListener windowListener;
	private GenoGLListener glListener;
	private GenoWindow genoWindow;
	private EventHandler eventHandler;

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public BlockingQueue<NEWTEvent> getEventQueue() {
		return eventQueue;
	}

	public GenoWindow getGenoWindow() {
		return genoWindow;
	}

	public GenoGLListener getGlListener() {
		return glListener;
	}

	public GenoWindowListener getWindowListener() {
		return windowListener;
	}

	public static void useSmallData() {
		AbstractGenome.setName("Bogus Genome");
		AbstractGenome.addChromosome(new ViewChromosome(1, 600));
		AbstractGenome.addChromosome(new ViewChromosome(2, 300));
		AbstractGenome.addChromosome(new ViewChromosome(3, 900));
		AbstractGenome.addChromosome(new ViewChromosome(4, 1200));
		AbstractGenome.addChromosome(new ViewChromosome(5, 100));
		AbstractGenome.addChromosome(new ViewChromosome(6, 400));
		AbstractGenome.addChromosome(new ViewChromosome(7, 500));
	}

	public static void useBigData() {
		AbstractGenome.setName("Bogus Genome");
		AbstractGenome.addChromosome(new ViewChromosome(1, 247000000));
		AbstractGenome.addChromosome(new ViewChromosome(2, 243000000));
		AbstractGenome.addChromosome(new ViewChromosome(3, 199000000));
		AbstractGenome.addChromosome(new ViewChromosome(4, 191000000));
		AbstractGenome.addChromosome(new ViewChromosome(5, 181000000));
		AbstractGenome.addChromosome(new ViewChromosome(6, 171000000));
		AbstractGenome.addChromosome(new ViewChromosome(7, 159000000));
		AbstractGenome.addChromosome(new ViewChromosome(8, 146000000));
		AbstractGenome.addChromosome(new ViewChromosome(9, 140000000));
		AbstractGenome.addChromosome(new ViewChromosome(10, 135000000));
		AbstractGenome.addChromosome(new ViewChromosome(11, 134000000));
		AbstractGenome.addChromosome(new ViewChromosome(12, 132000000));
		AbstractGenome.addChromosome(new ViewChromosome(13, 114000000));
		AbstractGenome.addChromosome(new ViewChromosome(14, 106000000));
		AbstractGenome.addChromosome(new ViewChromosome(15, 100000000));
		AbstractGenome.addChromosome(new ViewChromosome(16, 89000000));
		AbstractGenome.addChromosome(new ViewChromosome(17, 79000000));
		AbstractGenome.addChromosome(new ViewChromosome(18, 76000000));
		AbstractGenome.addChromosome(new ViewChromosome(19, 63000000));
		AbstractGenome.addChromosome(new ViewChromosome(20, 62000000));
		AbstractGenome.addChromosome(new ViewChromosome(21, 4700000));
		AbstractGenome.addChromosome(new ViewChromosome(22, 5000000));
		AbstractGenome.addChromosome(new ViewChromosome(23, 155000000));
	}

	public static boolean useChipsterDataRat() {
//                ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData("ftp://ftp.ensembl.org/pub/release-65/mysql/rattus_norvegicus_core_65_34/karyotype.txt.gz",
//                        " ftp://ftp.ensembl.org/pub/release-65/mysql/rattus_norvegicus_core_65_34/seq_region.txt.gz",
//				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
//						"13", "14", "15", "16", "17", "18", "19", "20", "X"});
//               


		ConcurrentLinkedQueue<ViewChromosome> chromosomeData = ChipsterInterface.getChromosomes("karyotype.txt", "seq_region.txt",
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
					"13", "14", "15", "16", "17", "18", "19", "20", "X"});

		for (ViewChromosome c : chromosomeData) {
			AbstractGenome.addChromosome(c);
		}
		return true;

	}

	public static boolean useChipsterDataHuman() {
//                ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData(
//				"ftp://ftp.ensembl.org/pub/release-65/mysql/homo_sapiens_core_65_37/karyotype.txt.gz", 
//				"ftp://ftp.ensembl.org/pub/release-65/mysql/homo_sapiens_core_65_37/seq_region.txt.gz", 
//				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
//						"13", "14", "15", "16", "17", "18", "19", "20", "X"});
//               
		ConcurrentLinkedQueue<ViewChromosome> chromosomeData = ChipsterInterface.getChromosomes("karyotypeHuman.txt", "seq_regionHuman.txt",
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
					"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X"});
		for (ViewChromosome c : chromosomeData) {
			AbstractGenome.addChromosome(c);
		}
		return true;
	}

	public GenomeBrowserNG(int width, int height) {

		//fill with bogus data
		//useSmallData();
		//useBigData();
		//useChipsterData();
		//useChipsterDataHuman();
		useChipsterDataRat();

		this.eventQueue = new LinkedBlockingQueue<NEWTEvent>();
		this.genoWindow = new GenoWindow(width, height);
		OverView overView = new OverView(this.genoWindow);
		this.genoWindow.addContainer(overView.trackviewManager);

		this.glListener = new GenoGLListener(overView);
		this.eventHandler = new EventHandler(this.genoWindow, this.glListener.getRoot(), eventQueue, width, height);

		addListeners();
	}

	public void addListeners() {
		this.windowListener = new GenoWindowListener(eventQueue);
		this.genoWindow.window.addKeyListener(new Keyboard(eventQueue));
		this.genoWindow.window.addMouseListener(new Mouse(eventQueue));
		this.genoWindow.window.addWindowListener(windowListener);
		this.genoWindow.window.addGLEventListener(glListener);
	}

	public void run() throws InterruptedException {
		this.genoWindow.open();
		Thread t = new Thread(glListener);
		t.start();

		this.eventHandler.handleEvents();
		glListener.die();
		t.join();
		this.genoWindow.close();
	}

	public static void main(String[] s) throws InterruptedException {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		double fraction = 0.8d;
		new GenomeBrowserNG((int) (dim.width * fraction), (int) (dim.height * fraction)).run();
	}

}
