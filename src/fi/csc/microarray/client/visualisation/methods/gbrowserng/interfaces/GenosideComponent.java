package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import javax.media.opengl.GL2;

public abstract class GenosideComponent implements VisualComponent, InteractiveComponent {

	private static int idCounter = 0;
	private final int id;
	public final Object tickdrawLock = new Object();

	public GenosideComponent(GenosideComponent parent) {
		id = idCounter++;
	}

	public int getId() {
		return id;
	}

	public abstract boolean handle(MouseEvent event, float screen_x, float screen_y);

	public abstract boolean handle(KeyEvent event);

	public abstract void draw(GL2 gl);

	public abstract void userTick(float dt);

	public void tick(float dt) {
		synchronized (tickdrawLock) {
			userTick(dt);
		}
	}
}
