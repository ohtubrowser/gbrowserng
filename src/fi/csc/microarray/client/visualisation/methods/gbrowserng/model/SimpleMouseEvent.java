package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.newt.event.MouseEvent;

public class SimpleMouseEvent {
	private float x;
	private float y;
	private long when;

	public SimpleMouseEvent(float x, float y, long when) {
		this.x=x;
		this.y=y;
		this.when=when;
	}

	public float getX() { return x; }
	public float getY() { return y; }
	public long getWhen() { return when; }
}
