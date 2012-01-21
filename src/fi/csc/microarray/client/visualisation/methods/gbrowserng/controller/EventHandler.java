package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.NEWTEvent;
import com.jogamp.newt.event.WindowEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;

import java.util.concurrent.BlockingQueue;

public class EventHandler {

	private BlockingQueue<NEWTEvent> eventQueue = null;
	private final GenoWindow window;
	private GenoEvent genoEvent = new GenoEvent(800, 600);
	private final GenosideComponent client;

	public EventHandler(GenoWindow hostWindow, GenosideComponent client, BlockingQueue<NEWTEvent> eventQueue) {
		this.client = client;
		this.eventQueue = eventQueue;
		this.window = hostWindow;
	}

	public void toggleFullscreen() {
	}

	public void handleEvents() throws InterruptedException {
		for (;;) {
			NEWTEvent event = this.eventQueue.take();
			genoEvent.event = event;

			if (event instanceof KeyEvent) {
				KeyEvent keyEvent = (KeyEvent) event;
				if (keyEvent.getKeyChar() == KeyEvent.VK_ESCAPE) {
					return;
				} else if (keyEvent.getKeyChar() == 'f') {
					toggleFullscreen();
				} else {
					client.handle((KeyEvent) event);
				}
			} else if (genoEvent.event instanceof MouseEvent) {
				client.handle((MouseEvent) (genoEvent.event), genoEvent.getMouseGLX(), genoEvent.getMouseGLY());
			} else if (event instanceof WindowEvent) {
				if (event.getEventType() == WindowEvent.EVENT_WINDOW_RESIZED) {
					genoEvent.setScreenSize(window.window.getWidth(), window.window.getHeight());
				} 
				else if(event.getEventType() == WindowEvent.EVENT_WINDOW_DESTROYED)
					return;
			}
		}
	}
}
