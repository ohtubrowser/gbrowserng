package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import gles.SoulGL2;

public interface VisualComponent {
	public void draw(SoulGL2 gl);
	public void tick(float dt);
}
