package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import java.awt.AWTEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GenoWindowListener implements WindowListener {

	private BlockingQueue<AWTEvent> eventQueue = null;
	
	public GenoWindowListener(BlockingQueue<AWTEvent> eventQueue) {
		this.eventQueue = eventQueue;
	}
	
	@Override
	public void windowOpened(WindowEvent we) {
		try {
			eventQueue.put(we);
		} catch (InterruptedException ex) {
			Logger.getLogger(GenoWindowListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void windowClosing(WindowEvent we) {
		try {
			eventQueue.put(we);
		} catch (InterruptedException ex) {
			Logger.getLogger(GenoWindowListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void windowClosed(WindowEvent we) {
		try {
			eventQueue.put(we);
		} catch (InterruptedException ex) {
			Logger.getLogger(GenoWindowListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void windowIconified(WindowEvent we) {
	}

	@Override
	public void windowDeiconified(WindowEvent we) {
	}

	@Override
	public void windowActivated(WindowEvent we) {
	}

	@Override
	public void windowDeactivated(WindowEvent we) {
	}

	
}
