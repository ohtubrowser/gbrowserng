package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import java.util.concurrent.BlockingQueue;

public class EventHandler {

	private BlockingQueue<AWTEvent> eventQueue = null;
	private final GenoWindow window;
	private GenoEvent genoEvent;
	private final GenosideComponent client;

	public EventHandler(GenoWindow hostWindow, GenosideComponent client, BlockingQueue<AWTEvent> eventQueue, int width, int height) {
		this.client = client;
		this.eventQueue = eventQueue;
		this.window = hostWindow;
		this.genoEvent = new GenoEvent(width, height);
	}

	public void toggleFullscreen() {
		this.window.toggleFullscreen();
		//genoEvent.setScreenSize(GlobalVariables.width, GlobalVariables.height);
		System.out.println("Screen size " + GlobalVariables.width + "x" + GlobalVariables.height);
	}

	public void resolution(int width, int height) {
		genoEvent.setScreenSize(width, height);
	}

	public boolean toggleVisible() {
		if (window.isOverviewVisible()) {
			return true;
		} else {
			window.toggleVisible();
			return false;
		}
	}

	public void handleEvents() throws InterruptedException {
		for (;;) {
			AWTEvent event = this.eventQueue.take();
			
			genoEvent.event = event;
			if (event instanceof KeyEvent) {
				KeyEvent keyEvent = (KeyEvent) event;

				if (keyEvent.getKeyChar() == KeyEvent.VK_ESCAPE) {
					if (toggleVisible()) {
						return;
					}
				} else if (keyEvent.getKeyChar() == 'f') {
					toggleFullscreen();
				} else if (keyEvent.getKeyChar() == 't' && keyEvent.getID() == KeyEvent.KEY_PRESSED) {
					window.toggleVisible();
				} else {
					client.handle((KeyEvent) event);
				}
			} else if (genoEvent.event instanceof MouseEvent) {
				client.handle((MouseEvent) (genoEvent.event), genoEvent.getMouseGLX(), genoEvent.getMouseGLY());
			} else if (event instanceof WindowEvent) {
				if (event.getID() == WindowEvent.COMPONENT_RESIZED) {
					genoEvent.setScreenSize(window.c.getWidth(), window.c.getHeight());
				} else if (event.getID() == WindowEvent.WINDOW_CLOSED || event.getID() == WindowEvent.WINDOW_CLOSING) {
					if (toggleVisible()) {
						return;
					}
				}
			}
		}
	}
}
