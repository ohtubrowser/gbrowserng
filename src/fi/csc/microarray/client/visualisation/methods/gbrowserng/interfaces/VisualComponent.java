package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import javax.media.opengl.GL2;

public interface VisualComponent {
	public void draw(GL2 gl);
	public void tick(float dt);
}
