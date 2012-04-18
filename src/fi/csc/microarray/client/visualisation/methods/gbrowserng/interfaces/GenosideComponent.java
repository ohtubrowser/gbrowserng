package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.media.opengl.GL2;

public abstract class GenosideComponent {

	public abstract boolean handle(MouseEvent event, float screen_x, float screen_y);

	public abstract boolean handle(KeyEvent event);

	public abstract void draw(GL2 gl);

	public abstract void tick(float dt);

}
