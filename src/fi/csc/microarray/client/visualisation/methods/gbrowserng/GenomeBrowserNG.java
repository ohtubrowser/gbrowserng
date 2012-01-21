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
	
	public GenomeBrowserNG(int width, int height) {

        // fill with bogus data
        AbstractGenome.setName("Bogus Genome");
        AbstractGenome.addChromosome(new AbstractChromosome(0, 600));
        AbstractGenome.addChromosome(new AbstractChromosome(1, 300));
        AbstractGenome.addChromosome(new AbstractChromosome(2, 900));
        AbstractGenome.addChromosome(new AbstractChromosome(3, 1200));
        AbstractGenome.addChromosome(new AbstractChromosome(4, 100));
        AbstractGenome.addChromosome(new AbstractChromosome(5, 400));
        AbstractGenome.addChromosome(new AbstractChromosome(6, 500));

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

		this.eventHandler.handleEvents();

		this.genoWindow.close();
	}
	
	public static void main(String[] s) throws InterruptedException {
		new GenomeBrowserNG(800, 600).run();
	}
}
