package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;

public interface InteractiveComponent {
	public boolean handle(MouseEvent event, float x, float y);
	public boolean handle(KeyEvent event);
}
