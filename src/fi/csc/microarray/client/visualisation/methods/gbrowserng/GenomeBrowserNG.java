package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import com.jogamp.newt.event.NEWTEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.EventHandler;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.GenoWindowListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.Keyboard;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.Mouse;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration.ChipsterInterface;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoGLListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GenomeBrowserNG {

	BlockingQueue<NEWTEvent> eventQueue;
	GenoWindowListener windowListener;
	GenoGLListener glListener;
	GenoWindow genoWindow;
	EventHandler eventHandler;

	public static void useSmallData() {
		AbstractGenome.setName("Bogus Genome");
		AbstractGenome.addChromosome(new Chromosome(1, 600));
		AbstractGenome.addChromosome(new Chromosome(2, 300));
		AbstractGenome.addChromosome(new Chromosome(3, 900));
		AbstractGenome.addChromosome(new Chromosome(4, 1200));
		AbstractGenome.addChromosome(new Chromosome(5, 100));
		AbstractGenome.addChromosome(new Chromosome(6, 400));
		AbstractGenome.addChromosome(new Chromosome(7, 500));
	}

	public static void useBigData() {
		AbstractGenome.setName("Bogus Genome");
		AbstractGenome.addChromosome(new Chromosome(1, 247000000));
		AbstractGenome.addChromosome(new Chromosome(2, 243000000));
		AbstractGenome.addChromosome(new Chromosome(3, 199000000));
		AbstractGenome.addChromosome(new Chromosome(4, 191000000));
		AbstractGenome.addChromosome(new Chromosome(5, 181000000));
		AbstractGenome.addChromosome(new Chromosome(6, 171000000));
		AbstractGenome.addChromosome(new Chromosome(7, 159000000));
		AbstractGenome.addChromosome(new Chromosome(8, 146000000));
		AbstractGenome.addChromosome(new Chromosome(9, 140000000));
		AbstractGenome.addChromosome(new Chromosome(10, 135000000));
		AbstractGenome.addChromosome(new Chromosome(11, 134000000));
		AbstractGenome.addChromosome(new Chromosome(12, 132000000));
		AbstractGenome.addChromosome(new Chromosome(13, 114000000));
		AbstractGenome.addChromosome(new Chromosome(14, 106000000));
		AbstractGenome.addChromosome(new Chromosome(15, 100000000));
		AbstractGenome.addChromosome(new Chromosome(16, 89000000));
		AbstractGenome.addChromosome(new Chromosome(17, 79000000));
		AbstractGenome.addChromosome(new Chromosome(18, 76000000));
		AbstractGenome.addChromosome(new Chromosome(19, 63000000));
		AbstractGenome.addChromosome(new Chromosome(20, 62000000));
		AbstractGenome.addChromosome(new Chromosome(21, 4700000));
		AbstractGenome.addChromosome(new Chromosome(22, 5000000));
		AbstractGenome.addChromosome(new Chromosome(23, 155000000));
	}

	public static void useChipsterDataRat() {
		int i=1;
//                ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData("ftp://ftp.ensembl.org/pub/release-65/mysql/rattus_norvegicus_core_65_34/karyotype.txt.gz", 
//                        " ftp://ftp.ensembl.org/pub/release-65/mysql/rattus_norvegicus_core_65_34/seq_region.txt.gz",
//				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
//						"13", "14", "15", "16", "17", "18", "19", "20", "X"});
//               
                
                
		ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData("karyotype.txt", "seq_region.txt", 
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
						"13", "14", "15", "16", "17", "18", "19", "20", "X"});
		
		for(long[] data : chromosomeData) {
			long chromosomeLength = data[0];
			Chromosome chr = new Chromosome(i, chromosomeLength);
			long centromereCenter = data[2];
			if (data[2] != 0 ) {
				System.out.println("centromereposition: " + centromereCenter);
				chr.setCentromerePosition(centromereCenter);
			}
			
			AbstractGenome.addChromosome(chr);
			++i;
		}
	
	}
	
	public static void useChipsterDataHuman() {
		int i=1;
//                ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData(
//				"ftp://ftp.ensembl.org/pub/release-65/mysql/homo_sapiens_core_65_37/karyotype.txt.gz", 
//				"ftp://ftp.ensembl.org/pub/release-65/mysql/homo_sapiens_core_65_37/seq_region.txt.gz", 
//				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
//						"13", "14", "15", "16", "17", "18", "19", "20", "X"});
//               
		ConcurrentLinkedQueue<long[]> chromosomeData = ChipsterInterface.getData("karyotypeHuman.txt", "seq_regionHuman.txt",
				new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
						"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X"});
		for(long[] data : chromosomeData) {
			long chromosomeLength = data[0];
			Chromosome chr = new Chromosome(i, chromosomeLength);
			long centromereCenter = data[2];
			if (data[2] != 0 ) {
				System.out.println("centromereposition: " + centromereCenter);
				chr.setCentromerePosition(centromereCenter);
			}
			AbstractGenome.addChromosome(chr);
			++i;
		}
	}
	

	public GenomeBrowserNG(int width, int height) {

		//fill with bogus data
		//useSmallData();
		//useBigData();
		//useChipsterDataRat();
		useChipsterDataHuman();

		this.eventQueue = new LinkedBlockingQueue<NEWTEvent>();

		this.genoWindow = new GenoWindow(width, height);
		this.glListener = new GenoGLListener(new OverView());
		this.eventHandler = new EventHandler(this.genoWindow, this.glListener.getRoot(), eventQueue);
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
		new GenomeBrowserNG((int)(dim.width*fraction), (int)(dim.height*fraction)).run();
	}
}
