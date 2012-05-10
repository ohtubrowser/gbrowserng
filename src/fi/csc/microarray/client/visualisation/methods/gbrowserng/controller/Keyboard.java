package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;


import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.BlockingQueue;

/**
 * Class for adding keyboard actions to the event queue.
 * @author 
 */
public class Keyboard implements KeyListener {
	private BlockingQueue<AWTEvent> eventQueue;

	public Keyboard(BlockingQueue<AWTEvent> eventQueue) {
		this.eventQueue = eventQueue;
	}

	public void keyPressed(KeyEvent keyEvent) {
		try {
			this.eventQueue.put(keyEvent);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void keyReleased(KeyEvent keyEvent) {
		try {
			this.eventQueue.put(keyEvent);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void keyTyped(KeyEvent keyEvent) {
		try {
			this.eventQueue.put(keyEvent);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
