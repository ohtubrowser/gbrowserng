package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.ContextMenu;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;

public class MouseEventHandler extends EventHandler {

	private SimpleMouseEvent lastMouseClick;

	public MouseEventHandler(OverView overview) {
		super(overview);
		this.lastMouseClick = null;
	}
	
	public SimpleMouseEvent getLastMouseClick() {
		return lastMouseClick;
	}

	public OverView getOverview() {
		return overview;
	}
	
	public boolean handle(MouseEvent event, float x, float y, GeneCircle geneCircle) {
		
		// update hoverCapsule and contextMenu before handling event
		hoverCapsule = overview.getHoverCapsule();
		contextMenu = overview.getContextMenu();
		
		// if there is a contextMenu s
		if (contextMenu != null) {
			if (contextMenu.inComponent(x, y)) {
				contextMenu.handle(event, x, y);
				if (contextMenu.close()) {
					overview.closeContextMenu();
				}
				return true;
			}
		}

		hoverCapsule = null;
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

	private void checkIfLastMouseClickNull(float x, float y, MouseEvent event) {
		if (lastMouseClick == null) {
			lastMouseClick = new SimpleMouseEvent(x, y, event.getWhen());
		}
	}

	private boolean handleClick(MouseEvent event, float mouseCursorX, float mouseCursorY , LinkSelection selection, float pointerGenePosition, GeneCircle geneCircle) {
		// then see if they actually want the event
		if (MouseEvent.EVENT_MOUSE_CLICKED == event.getEventType()) {
			if (event.getButton() == 1) {
				ContextMenu contextMenu = overview.getContextMenu();
				if (contextMenu != null) { // context menu selection
					if (contextMenu.inComponent(mouseCursorX, mouseCursorY)) {
						if (contextMenu.handle(event, mouseCursorX, mouseCursorY)) {
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
					if (capsule.handle(event, mouseCursorX, mouseCursorY)) {
						overview.openSession(capsule);
						return true;
					}
				}
				if (pointOnCircle(mouseCursorX, mouseCursorY, geneCircle)) {
					overview.setArcHighlightLocked(true);
					selection.update(pointerGenePosition, overview.getLinkCollection());
				} else if (overview.isArcHighlightLocked() && pointInsideCircle(mouseCursorX, mouseCursorY, geneCircle)) {
					overview.getTrackviewManager().openLinkSession(overview.getLinkSelection().getActiveLink());
					overview.getTrackviewManager().toggleVisible();
					
					GeneralLink cl = overview.getTrackviewManager().getLink();
					float relativePosition = 0;
					
					// calcualted position on circle where to draw capsule
					if (cl.isaocc()) {
						relativePosition = geneCircle.getRelativePosition(cl.getBChromosome().getChromosomeNumber()-1, ((float) cl.getbStart())/cl.getBChromosome().length());
					} else {
						relativePosition = geneCircle.getRelativePosition(cl.getAChromosome().getChromosomeNumber()-1, ((float) cl.getaStart())/cl.getAChromosome().length());
					}
					
					openNewAreaCapsule(relativePosition, mouseCursorX, mouseCursorY, cl, geneCircle);
					
				} else {
					if (overview.isArcHighlightLocked()) {
						overview.setArcHighlightLocked(false);
						selection.deactivate();
					} else {
						openNewAreaCapsule(pointerGenePosition, mouseCursorX, mouseCursorY, null, geneCircle);
					}
				}
			} else if (event.getButton() == 3) {
				for (SessionViewCapsule capsule : sessions) {
					if (capsule.isDying()) {
						continue;
					}
					if (capsule.handle(event, mouseCursorX, mouseCursorY)) {
						capsule.die();
						capsule.deactivate();
						return true;
					}
				}
				overview.openContextMenu(mouseCursorX, mouseCursorY);
				lastMouseClick = new SimpleMouseEvent(mouseCursorX, mouseCursorY, event.getWhen());
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
		selection.mouseMove(pointInsideCircle(mouseCursorX, mouseCursorY, geneCircle), mouseCursorX, mouseCursorY);
		overview.setMousePositionX(CoordinateManager.toCircleCoordsY(mouseCursorX), CoordinateManager.toCircleCoordsX(mouseCursorY));
		return false;
	}
	
	/**
	 * Opens a new capsule in the overview window.
	 * Instantiates needed objects for creating Capsule, then creates Capsule.
	 * Adds Capsule to the list of capsules in Overview-window, then sets Capsule to be updated so drawn on screen.
	 * @param pointerGenePosition point on circle from where line from circle to Capsule begins
	 * @param x ask writer - location of capsule?
	 * @param y ask writer - location of capsule?
	 * @param link Link associated with this capsule (null if this is an area capsule)
	 * @param geneCircle GeneCircle to which to attach capsule
	 */
	private void openNewAreaCapsule(float pointerGenePosition, float x, float y, GeneralLink link, GeneCircle geneCircle) {
		Session session = new Session(geneCircle.getChromosome().getReferenceSequence(), geneCircle.getChromosomePosition());
		SessionView sessionView = new SessionView(session, overview);
		
		SessionViewCapsule capsule = new SessionViewCapsule(sessionView, link, pointerGenePosition, geneCircle);
		capsule.getSession().setDimensions(0.4f, 0.2f);
		capsule.getSession().setPosition(x, y);
		
		sessions.add(capsule);
		synchronized (overview.textureUpdateListLock) {
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
