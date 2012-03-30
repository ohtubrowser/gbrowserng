package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.ContextMenu;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MouseEventHandler {

	private SimpleMouseEvent lastMouseClick;
	private OverView overview;
	private ConcurrentLinkedQueue<SessionViewCapsule> sessions;
	private ConcurrentLinkedQueue<SessionViewCapsule> activeSessions;

	public MouseEventHandler(OverView overview) {
		this.lastMouseClick = null;
		this.overview = overview;
	}

	public boolean handle(MouseEvent event, float x, float y, GeneCircle geneCircle) {
		SessionViewCapsule hoverCapsule = overview.getHoverCapsule();
		ContextMenu contextMenu = overview.getContextMenu();

		if (contextMenu != null) {
			if (contextMenu.inComponent(x, y)) {
				contextMenu.handle(event, x, y);
				if (contextMenu.close()) {
					overview.closeContextMenu();
				}
				return true;
			}
		}

		initializeGlobalVariables();
		checkIfLastMouseClickNull(x, y, event);
		setHoverCapsules(x, y);

		if (!activeSessions.isEmpty() && hoverCapsule != null) {
			return hoverCapsule.getSession().handle(event, x, y);
		}

		// note, x axis is negated to make tracking begin from the mathematical zero angle.
		float pointerGenePosition = 1.0f - ((float) (Math.atan2(CoordinateManager.toCircleCoordsX(y), CoordinateManager.toCircleCoordsY(-x)) / Math.PI) * 0.5f + 0.5f);

		for (ChromoName chromoName : overview.getChromoNames()) {
			if (chromoName.isOver(x, y)) {
				synchronized (geneCircle.tickdrawLock) {
					int id = chromoName.getChromosome().getChromosomeNumber();
					float[] bounds = geneCircle.getChromosomeBoundaries();
					pointerGenePosition = bounds[id - 1] + (bounds[id] - bounds[id - 1]) / 2 + 0.25f;
				}
			}
		}

		geneCircle.updatePosition(pointerGenePosition);

		LinkSelection selection = overview.getLinkSelection();
		if (!overview.isArcHighlightLocked()) {
			if (pointOnCircle(x, y, geneCircle)) {
				selection.update(pointerGenePosition, overview.getLinkCollection());
			} else {
				selection.reset();
			}
		}

		// allow capsules to update their states
		for (SessionViewCapsule capsule : sessions) {
			capsule.handle(event, x, y);
		}
		return handleClick(event, x, y, selection, pointerGenePosition, geneCircle);
	}

	private void setHoverCapsules(float x, float y) {
		for (SessionViewCapsule capsule : sessions) { // TODO : hoverCapsule is calculated many times in this function
			if (capsule.getSession().inComponent(x, y)) {
				overview.setHoverCapsule(capsule);
				break;
			}
		}
	}

	private void initializeGlobalVariables() {
		sessions = overview.getSessions();
		activeSessions = overview.getActiveSessions();
		overview.setHoverCapsule(null);
	}

	private void checkIfLastMouseClickNull(float x, float y, MouseEvent event) {
		if (lastMouseClick == null) {
			lastMouseClick = new SimpleMouseEvent(x, y, event.getWhen());
		}
	}

	private boolean handleClick(MouseEvent event, float x, float y, LinkSelection selection, float pointerGenePosition, GeneCircle geneCircle) {
		// then see if they actually want the event
		if (MouseEvent.EVENT_MOUSE_CLICKED == event.getEventType()) {
			if (event.getButton() == 1) {
				ContextMenu contextMenu = overview.getContextMenu();
				if (contextMenu != null) { // context menu selection
					if (contextMenu.inComponent(x, y)) {
						if (contextMenu.handle(event, x, y)) {
							overview.closeContextMenu();
							return true;
						}
					}
					overview.closeContextMenu();
					return true;
				}
				for (SessionViewCapsule capsule : sessions) {
					if (capsule.isDying()) {
						continue;
					}
					if (capsule.handle(event, x, y)) {
						overview.openSession(capsule);
						return true;
					}
				}
				if (pointOnCircle(x, y, geneCircle)) {
					overview.setArcHighlightLocked(true);
					selection.update(pointerGenePosition, overview.getLinkCollection());
				} else if (overview.isArcHighlightLocked() && pointInsideCircle( x, y, geneCircle)) {
					this.openTrackview(geneCircle);
				} else {
					if (overview.isArcHighlightLocked()) {
						overview.setArcHighlightLocked(false);
						selection.deactivate();
					} else {
						this.openNewCapsule(pointerGenePosition, x, y, geneCircle);
					}
				}
			} else if (event.getButton() == 3) {
				for (SessionViewCapsule capsule : sessions) {
					if (capsule.isDying()) {
						continue;
					}
					if (capsule.handle(event, x, y)) {
						capsule.die();
						capsule.deactivate();
						return true;
					}
				}
				overview.openContextMenu(x, y);
				lastMouseClick = new SimpleMouseEvent(x, y, event.getWhen());
				return true;
			}
		}
		// Wheel
		if (MouseEvent.EVENT_MOUSE_WHEEL_MOVED == event.getEventType()) {
			if (overview.isArcHighlightLocked()) {
				selection.updateArea(0.001f * event.getWheelRotation(), overview.getLinkCollection());
			} else {
				geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() + event.getWheelRotation() * 0.05f));
				overview.updateCircleSize();
			}
		}
		selection.mouseMove(pointInsideCircle(x, y, geneCircle), x, y);
		overview.setMousePositionX(CoordinateManager.toCircleCoordsY(x), CoordinateManager.toCircleCoordsX(y));
		return false;
	}

	private void openTrackview(GeneCircle geneCircle) {
		overview.getTrackviewManager().clearContainer();

		// get currently selected link and open Swingwindow for this link. Trackview manager handles this
		GeneralLink activeLink = overview.getLinkSelection().getActiveLink();
		overview.getTrackviewManager().openLinkSession(activeLink);
		overview.getTrackviewManager().toggleVisible();

		// get coordinates of ending point of link (away from point on chrosome where scrolling)
		float[] linkEndCoordinates = activeLink.getLinkEndPositions();
		float x = linkEndCoordinates[0];
		float y = linkEndCoordinates[1];
		// no clue - just copy-pasted from somewhere else in code. Ask original writer. (who?)
		float pointerGenePosition = 1.0f - ((float) (Math.atan2(CoordinateManager.toCircleCoordsX(y), 
				CoordinateManager.toCircleCoordsY(-x)) / Math.PI) * 0.5f + 0.5f);
		
		// capsule for opening trackview again opened
		openNewCapsule(pointerGenePosition, 0.5f, 0.5f, geneCircle);
	}

	/**
	 * Opens a new capsule in the overview window.
	 * Instantiates needed objects for creating Capsule, then creates Capsule.
	 * Adds Capsule to the list of capsules in Overview-window, then sets Capsule to be updated so drawn on screen.
	 * @param pointerGenePosition point on circle from where line from circle to Capsule begins
	 * @param x ask writer - location of capsule?
	 * @param y ask writer - location of capsule?
	 * @param geneCircle GeneCircle to which to attach capsule
	 */
	private void openNewCapsule(float pointerGenePosition, float x, float y, GeneCircle geneCircle) {
		Session session = new Session(geneCircle.getChromosome().getReferenceSequence(), geneCircle.getChromosomePosition());
		SessionView sessionView = new SessionView(session, overview);
		
		SessionViewCapsule capsule = new SessionViewCapsule(sessionView, pointerGenePosition, geneCircle);
		capsule.getSession().setDimensions(0.4f, 0.2f);
		capsule.getSession().setPosition(x, y);
		
		sessions.add(capsule);
		LinkedList<SessionViewCapsule> textureUpdateListLock = overview.getTextureUpdateListLock();
		synchronized (textureUpdateListLock) {
			overview.addCapsuleToTextureUpdateList(capsule);
			capsule.setNeedsTextureUpdate();
		}
	}

	public boolean pointOnCircle(float x, float y, GeneCircle geneCircle) {
		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(size);
		float b = CoordinateManager.toCircleCoordsY(size);
		float s = Math.abs(x * x / (a * a) + y * y / (b * b));
		return (s < 1.0f && s > 0.8f);
	}

	private boolean pointInsideCircle(float x, float y, GeneCircle geneCircle) {
		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(size);
		float b = CoordinateManager.toCircleCoordsY(size);
		float s = Math.abs(x * x / (a * a) + y * y / (b * b));
		return (s < 0.9f);
	}
}
