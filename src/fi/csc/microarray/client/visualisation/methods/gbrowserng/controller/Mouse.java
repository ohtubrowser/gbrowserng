package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import java.awt.AWTEvent;
import java.awt.event.*;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for adding mouse events to the event queue.
 * @author 
 */
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private BlockingQueue<AWTEvent> eventQueue;
	public Mouse(BlockingQueue<AWTEvent> eventQueue)
	{
		this.eventQueue = eventQueue;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		try {
			eventQueue.put(me);
		} catch (InterruptedException ex) {
			Logger.getLogger(Mouse.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
		try {
			eventQueue.put(me);
		} catch (InterruptedException ex) {
			Logger.getLogger(Mouse.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		try {
			eventQueue.put(me);
		} catch (InterruptedException ex) {
			Logger.getLogger(Mouse.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mouseDragged(MouseEvent me) {
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		try {
			eventQueue.put(me);
		} catch (InterruptedException ex) {
			Logger.getLogger(Mouse.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent me) {
		try {
			eventQueue.put(me);
		} catch (InterruptedException ex) {
			Logger.getLogger(Mouse.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
}
