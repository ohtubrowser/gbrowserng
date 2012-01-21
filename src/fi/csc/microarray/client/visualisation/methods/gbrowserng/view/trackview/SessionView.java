package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.MouseTracker;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.GenoButton;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.GenoVisualBorder;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import gles.SoulGL2;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SessionView extends GenosideComponent {

	private boolean active = false;
	private final GenoVisualBorder border = new GenoVisualBorder(this);
	private final GenoButton quitButton = new GenoButton(this, "QUIT_BUTTON", 1.0f, 1.0f, -0.04f, -0.04f, GenoTexID.QUIT_BUTTON);
	private final GenoButton shrinkButton = new GenoButton(this, "SHRINK_BUTTON", 1.0f, 1.0f, -0.08f, -0.04f, GenoTexID.SHRINK_BUTTON);
	private final GenoButton openReadFileButton = new GenoButton(this, "OPENREADFILE_BUTTON", 1.0f, 1.0f, -0.12f, -0.04f, GenoTexID.OPENFILE_BUTTON);
	private final GenoButton openAnotherSessionButton = new GenoButton(this, "ANOTHERSESSION_BUTTON", 1.0f, 1.0f, -0.16f, -0.04f, GenoTexID.OPENFILE_BUTTON);

	private final ConcurrentLinkedQueue<TrackView> trackViews = new ConcurrentLinkedQueue<TrackView>();
	private CoordinateRenderer coordinateView;
	private final Session session;

	private final MouseTracker mouseTracker = new MouseTracker();

	public SessionView(Session session, GenosideComponent parent) {
		super(parent);
		this.session = session;
		
		TrackView trackView1 = new TrackView(this, this.session);
		TrackView trackView2 = new TrackView(this, this.session);
		this.addTrackView(trackView1);
		this.addTrackView(trackView2);

		this.getAnimatedValues().setAnimatedValue("ZOOM", session.targetZoomLevel);
		this.getAnimatedValues().setAnimatedValue("POSITION", session.position);

		this.coordinateView = new CoordinateRenderer(this);
		this.coordinateView.setPosition(0, -0.95f);
		this.coordinateView.setDimensions(2f, 0.1f);
	}

	public void addTrackView(TrackView view) {
		this.trackViews.add(view);
		this.recalculateTrackPositions();
	}

	public int getTrackID(String what) {
		int id = Integer.parseInt(what);
		return id;
	}

	// TODO: Think about what to do with inactive trackviews.
	/*
	public void recalculateTrackPositions() {
		int absents = 0;
		SpaceDivider divider = new SpaceDivider(SpaceDivider.VERTICAL, 0.4f, 2.0f);

		for (TrackView t : this.trackViews) {
			if (t.isActive()) {
				divider.insertComponent(t);
			} else {
				t.setPosition(-0.7f, 0.8f - absents++ * 0.20f);
				t.setDimensions(0.4f, 0.2f);
			}
		}
		divider.calculate();
	}
	*/
	
	public void recalculateTrackPositions() {
		int minimized, reads, heatMaps;
		minimized = reads = heatMaps = 0;
		
		for (TrackView t : this.trackViews) {
			if (t.getTrackViewMode() == TrackView.READ) {
				++reads;
			} else if (t.getTrackViewMode() == TrackView.HEATMAP) {
				++heatMaps;
			}
		}
		
		float total_y = 1.9f;
		float heatSize = 0.2f;
		float readSize = (total_y - heatMaps * heatSize) / reads;
		float y = 1.0f;
		
		for (TrackView t : this.trackViews) {
			if (t.getTrackViewMode() == TrackView.READ) {
				t.setPosition(0, y - readSize/2);
				t.setDimensions(2, readSize);
				y -= readSize;
			} else if (t.getTrackViewMode() == TrackView.HEATMAP) {
				t.setPosition(0, y - heatSize/2);
				t.setDimensions(2, heatSize);
				y -= heatSize;
			} else {
				t.setPosition(-0.7f, 0.8f - minimized++ * 0.20f);
				t.setDimensions(0.4f, 0.2f);
			}
		}
	}

	@Override
	public void childComponentCall(String who, String what) {

		if (who.equals("TRACKVIEW") && (what.equals("MINIMIZE"))) {
			recalculateTrackPositions();
		}
		else if (who.equals("MODESWITCH")) {
			recalculateTrackPositions();
		}
		else if (who.equals("DELETE"))
		{
			int id = this.getTrackID(what);
			Iterator<TrackView> it = this.trackViews.iterator();
			while (it.hasNext())
			{
				TrackView t = it.next();
				if (t.getId() == id)
				{
					it.remove();
					break;
				}
			}

			recalculateTrackPositions();
		}

		else if (who.equals("SHRINK_BUTTON")) {
			if (what.equals("PRESSED")) {
				getParent().childComponentCall("SESSION", "SHRINK");
			}
		}

		else if (who.equals("QUIT_BUTTON")) {
			if (what.equals("PRESSED")) {
				getParent().childComponentCall("SESSION", "KILL");
			}
		}

		else if (who.equals("OPENREADFILE_BUTTON")) {
			if (what.equals("PRESSED")) {
				TrackView t = new TrackView(this, this.session);
				this.addTrackView(t);
			}
		}

		else if(who.equals("ANOTHERSESSION_BUTTON")) {
			if (what.equals("PRESSED")) {
				getParent().childComponentCall("SESSION", "OPEN_ANOTHER");
			}
		}

	}

	public boolean handle(KeyEvent event) {

		// does this view want to handle the input?
		if (event.getKeyChar() == 'b') {
			getParent().childComponentCall("SESSION", "SHRINK");
			return true;
		}


		if (KeyEvent.VK_LEFT == event.getKeyCode()) {
			this.session.position -= 0.05f / this.getAnimatedValues().getAnimatedValue("ZOOM");
			this.getAnimatedValues().setAnimatedValue("POSITION", this.session.position);
			return true;
		} else if (KeyEvent.VK_RIGHT == event.getKeyCode()) {
			this.session.position += 0.05f / this.getAnimatedValues().getAnimatedValue("ZOOM");
			this.getAnimatedValues().setAnimatedValue("POSITION", this.session.position);
			return true;
		} else if (KeyEvent.VK_UP == event.getKeyCode()) {
			zoom(0.9f / 1.0f);
			return true;
		} else if (KeyEvent.VK_DOWN == event.getKeyCode()) {
			zoom(1.0f / 0.9f);
			return true;
		}

		// child views want to handle this?
		boolean handled = false;
		for (TrackView t : trackViews) {
			if (t.handle(event))
				handled = true;
		}

		return handled;
	}
	
	private void zoom(float v) {
		this.session.targetZoomLevel *= v;
		this.getAnimatedValues().setAnimatedValue("ZOOM", this.session.targetZoomLevel);
	}

	public boolean handle(MouseEvent event, float screen_x, float screen_y) {

		if (quitButton.handle(event, screen_x, screen_y))
			return true;
		if (shrinkButton.handle(event, screen_x, screen_y))
			return true;
		if (openReadFileButton.handle(event, screen_x, screen_y))
			return true;
		if (openAnotherSessionButton.handle(event, screen_x, screen_y))
			return true;

		mouseTracker.handle(event, screen_x, screen_y);

		if(event.getEventType() == MouseEvent.EVENT_MOUSE_DRAGGED) {
			this.session.position -= mouseTracker.getDragging_dx() / this.getAnimatedValues().getAnimatedValue("ZOOM");
			this.session.halfSizeY *= 1.0f + mouseTracker.getDragging_dy();
			this.getAnimatedValues().setAnimatedValue("POSITION", session.position);
			return true;
		}

		// child views want to handle this?
		for (TrackView t : trackViews) {
			if (t.getTrackViewMode() == TrackView.MINIMIZED &&
				t.handle(event, screen_x, screen_y))
					return true;
		}
		
		for (TrackView t : trackViews) {
			if (t.getTrackViewMode() != TrackView.MINIMIZED &&
				t.handle(event, screen_x, screen_y))
					return true;
		}

		return false;
	}

	public void draw(SoulGL2 gl) {
		if(!inScreen())
			return;

		if(active)
		// first draw all the internal views
		for (TrackView t : trackViews) {
			t.draw(gl);
		}

		// then draw whatever this session view wants to draw.
		if(active) {
		quitButton.draw(gl);
		shrinkButton.draw(gl);
		openReadFileButton.draw(gl);
		openAnotherSessionButton.draw(gl);
		coordinateView.draw(gl);
		}
		border.draw(gl);
	}

	@Override
	public void userTick(float dt) {
		for (TrackView t : trackViews) {
			t.tick(dt);
		}

		quitButton.tick(dt);
		shrinkButton.tick(dt);
		openReadFileButton.tick(dt);
		openAnotherSessionButton.tick(dt);
		coordinateView.tick(dt);

		this.session.halfSizeX = this.getAnimatedValues().getAnimatedValue("ZOOM");
	}

	public Session getSession() {
		return session;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
}
