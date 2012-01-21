package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.newt.event.MouseEvent;

public class MouseTracker {

	private float dragging_dx = 0;
	private float dragging_dy = 0;
	private float dragging_prev_x = 0;
	private float dragging_prev_y = 0;

	public float getDragging_dx() {
		return dragging_dx;
	}

	public float getDragging_dy() {
		return dragging_dy;
	}

	public void handle(MouseEvent event, float screen_x, float screen_y) {
		// dragging begins
		if(event.getEventType() == MouseEvent.EVENT_MOUSE_PRESSED) {
			dragging_prev_x = screen_x;
			dragging_prev_y = screen_y;
		}

		// dragging is active
		if(event.getEventType() == MouseEvent.EVENT_MOUSE_DRAGGED) {
			dragging_dx = screen_x - dragging_prev_x;
			dragging_prev_x = screen_x;

			dragging_dy = screen_y - dragging_prev_y;
			dragging_prev_y = screen_y;
		}
	}

}
