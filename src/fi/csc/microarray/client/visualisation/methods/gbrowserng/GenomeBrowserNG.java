package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import com.jogamp.newt.event.NEWTEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.EventHandler;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.GenoWindowListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.Keyboard;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.Mouse;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoGLListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GenomeBrowserNG {

	BlockingQueue<NEWTEvent> eventQueue;
	GenoWindowListener windowListener;
	GenoGLListener glListener;
	GenoWindow genoWindow;
	EventHandler eventHandler;

	public static void useSmallData() {
		AbstractGenome.setName("Bogus Genome");
		AbstractGenome.addChromosome(new AbstractChromosome(0, 600));
		AbstractGenome.addChromosome(new AbstractChromosome(1, 300));
		AbstractGenome.addChromosome(new AbstractChromosome(2, 900));
		AbstractGenome.addChromosome(new AbstractChromosome(3, 1200));
		AbstractGenome.addChromosome(new AbstractChromosome(4, 100));
		AbstractGenome.addChromosome(new AbstractChromosome(5, 400));
		AbstractGenome.addChromosome(new AbstractChromosome(6, 500));
	}

	public static void useBigData() {
		AbstractGenome.setName("Bogus Genome");
		AbstractGenome.addChromosome(new AbstractChromosome(1, 24500000));
		AbstractGenome.addChromosome(new AbstractChromosome(2, 24300000));
		AbstractGenome.addChromosome(new AbstractChromosome(3, 19900000));
		AbstractGenome.addChromosome(new AbstractChromosome(4, 19100000));
		AbstractGenome.addChromosome(new AbstractChromosome(5, 18000000));
		AbstractGenome.addChromosome(new AbstractChromosome(6, 17000000));
		AbstractGenome.addChromosome(new AbstractChromosome(7, 15800000));
		AbstractGenome.addChromosome(new AbstractChromosome(8, 14500000));
		AbstractGenome.addChromosome(new AbstractChromosome(9, 13400000));
		AbstractGenome.addChromosome(new AbstractChromosome(10, 13500000));
		AbstractGenome.addChromosome(new AbstractChromosome(11, 13400000));
		AbstractGenome.addChromosome(new AbstractChromosome(12, 13300000));
		AbstractGenome.addChromosome(new AbstractChromosome(13, 11400000));
		AbstractGenome.addChromosome(new AbstractChromosome(14, 10500000));
		AbstractGenome.addChromosome(new AbstractChromosome(15, 10000000));
		AbstractGenome.addChromosome(new AbstractChromosome(16, 8900000));
		AbstractGenome.addChromosome(new AbstractChromosome(17, 8100000));
		AbstractGenome.addChromosome(new AbstractChromosome(18, 7700000));
		AbstractGenome.addChromosome(new AbstractChromosome(19, 6300000));
		AbstractGenome.addChromosome(new AbstractChromosome(20, 63000000));
		AbstractGenome.addChromosome(new AbstractChromosome(21, 4600000));
		AbstractGenome.addChromosome(new AbstractChromosome(22, 4900000));
		AbstractGenome.addChromosome(new AbstractChromosome(23, 5000000));
	}

	public GenomeBrowserNG(int width, int height) {

		// fill with bogus data
		useSmallData();
		//useBigData();

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
		new GenomeBrowserNG(1024, 768).run();
	}
}
