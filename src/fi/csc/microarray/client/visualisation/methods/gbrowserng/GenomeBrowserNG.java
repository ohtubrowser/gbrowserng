package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jogamp.newt.event.NEWTEvent;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.EventHandler;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.GenoWindowListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.Keyboard;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.controller.Mouse;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoGLListener;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.TrackView;

public class GenomeBrowserNG {
	
	BlockingQueue<NEWTEvent> eventQueue;
	GenoWindowListener windowListener;
	GenoGLListener glListener;
	GenoWindow genoWindow;
	EventHandler eventHandler;
	
	public GenomeBrowserNG(int width, int height) {
		Session session = new Session();
		OverView overView = new OverView(session);
		
		this.eventQueue = new LinkedBlockingQueue<NEWTEvent>();
		this.windowListener = new GenoWindowListener(eventQueue);
		this.glListener = new GenoGLListener(overView);
		this.genoWindow = new GenoWindow(width, height);
		this.eventHandler = new EventHandler(this.glListener.getRoot(), eventQueue);

		this.genoWindow.window.addKeyListener(new Keyboard(eventQueue));
		this.genoWindow.window.addMouseListener(new Mouse(eventQueue));
		this.genoWindow.window.addGLEventListener(glListener);
		this.genoWindow.window.addWindowListener(windowListener);
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