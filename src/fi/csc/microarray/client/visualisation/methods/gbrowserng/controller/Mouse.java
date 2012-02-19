package fi.csc.microarray.client.visualisation.methods.gbrowserng.controller;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.NEWTEvent;

import java.util.concurrent.BlockingQueue;

public class Mouse implements MouseListener
{
	private BlockingQueue<NEWTEvent> eventQueue;
	
	public Mouse(BlockingQueue<NEWTEvent> eventQueue)
	{
		this.eventQueue = eventQueue;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		try {
			this.eventQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{	
	}

	public void mousePressed(MouseEvent e)
	{
		try {
			this.eventQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		try {
			this.eventQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		try {
			this.eventQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void mouseMoved(MouseEvent e)
	{
		try {
			this.eventQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void mouseWheelMoved(MouseEvent e)
	{
            	try {
			this.eventQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
