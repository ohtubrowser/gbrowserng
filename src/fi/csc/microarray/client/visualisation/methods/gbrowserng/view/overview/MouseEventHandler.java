package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;

/**
 * Handles MouseEvents in Overview window. 
 * Deals with all actions by user performed with mouse.
 * Has knowledge of overview and those variables of it that is needs to for doing its job.
 * 
 * Code is still confusing and needs explanation as to why steps are taken in the order that they are (handle method)
 *
 * @author Paloheimo
 */
public class MouseEventHandler extends EventHandler {

	private SimpleMouseEvent lastMouseClick;
	private MouseEvent event;
	private float mouseX, mouseY;
	private GeneCircle geneCircle;
	private float pointerGenePosition;

	public MouseEventHandler(OverView overview) {
		super(overview);
		this.lastMouseClick = null;
	}

	// method hard to understand - what happens and why in this particular order? needs to be divided into logical units with explanatory names
	public boolean handle(MouseEvent event, float x, float y, GeneCircle geneCircle) {
		this.event = event;
		this.geneCircle = geneCircle;
		setMousePosition(x, y);

		// update all variables before handling event
		updateVariables();

		// if there is a contextMenu s
		if (contextMenu != null) {
			if (handleContextMenuCommands()) {
				return true;
			}
		}

		hoverCapsule = null;
		checkIfLastMouseClickNull(x, y, event);
		setHoverCapsules(x, y);

		if (!activeSessions.isEmpty() && hoverCapsule != null) {
			return hoverCapsule.handle(event, x, y);
		}

		calculatePointerGenePosition();
		geneCircle.updatePosition(pointerGenePosition);

		if (!overview.isArcHighlightLocked()) {
			if (pointOnCircle(x, y, geneCircle)) {
				linkSelection.update(pointerGenePosition, linkCollection);
			} else {
				linkSelection.reset();
			}
		}

		// allow capsules to update their states
		for (SessionViewCapsule capsule : sessions) {
			capsule.handle(event, mouseX, mouseY);
		}
		return handleClick();
	}

	private void setMousePosition(float x, float y) {
		this.mouseX = x;
		this.mouseY = y;
	}

	private boolean handleContextMenuCommands() {
		if (contextMenu.inComponent(mouseX, mouseY)) {
			contextMenu.handle(event, mouseX, mouseY);
			if (contextMenu.close()) {
				overview.closeContextMenu();
			}
			return true;
		}
		return false;
	}

	private void setHoverCapsules(float x, float y) {
		for (SessionViewCapsule capsule : sessions) { // TODO : hoverCapsule is calculated many times in this function
			if (capsule.inCapsule(x, y)) {
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

	private void calculatePointerGenePosition() {
		double xCoordinates = CoordinateManager.toCircleCoordsX(mouseY);		// right naming of variable?
		double yCoordinates = CoordinateManager.toCircleCoordsY(-mouseX);		// right naming of variable?
		float value1 = (float) (Math.atan2(xCoordinates, yCoordinates) / Math.PI);	// what calculation happens here?

		pointerGenePosition = 1.0f - (value1 * 0.5f + 0.5f);				// what calculation happens here?

		// explain what happens here 
		for (ChromoName chromoName : overview.getChromoNames()) {
			if (chromoName.isOver(mouseX, mouseY)) {
				synchronized (geneCircle.tickdrawLock) {
					int id = chromoName.getChromosome().getChromosomeNumber();
					float[] bounds = geneCircle.getChromosomeBoundaries();
					pointerGenePosition = bounds[id - 1] + (bounds[id] - bounds[id - 1]) / 2 + 0.25f;
				}
			}
		}
	}

	private boolean handleClick() {
		updateVariables();

		if (MouseEvent.EVENT_MOUSE_CLICKED == event.getEventType()) {
			if (event.getButton() == 1) {
				return handleLeftClick();
			} else if (event.getButton() == 3) {
				return handleRightClick();
			}
		}

		// MouseWheelEvents
		if (MouseEvent.EVENT_MOUSE_WHEEL_MOVED == event.getEventType()) {
			if (overview.isArcHighlightLocked()) {
				linkSelection.updateArea(0.001f * event.getWheelRotation(), overview.getLinkCollection());
			} else {
				geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() + event.getWheelRotation() * 0.05f));
				overview.updateCircleSize();
			}
		}
		
		linkSelection.mouseMove(pointInsideCircle(mouseX, mouseY, geneCircle), mouseX, mouseY);
		overview.setMousePositionX(CoordinateManager.toCircleCoordsY(mouseX), CoordinateManager.toCircleCoordsX(mouseY));
		return false;
	}
	
	private boolean handleLeftClick() {
		// contextMenu actions? own method
		if (contextMenu != null) { 
			if (contextMenu.inComponent(mouseX, mouseY)) {
				if (contextMenu.handle(event, mouseX, mouseY)) {
					overview.closeContextMenu();
					return true;
				}
			}
			overview.closeContextMenu();
			return true;
		}
		
		// what happens here ?
		for (SessionViewCapsule capsule : sessions) {
			if (capsule.isDying()) {
				continue;
			}
			if (capsule.handle(event, mouseX, mouseY)) {
				openSession(capsule);
				return true;
			}
		}

		handleArcSelection();
		return false;
	}
	
	private void handleArcSelection() {
	
		if (pointOnCircle(mouseX, mouseY, geneCircle)) {
			overview.setArcHighlightLocked(true);
			linkSelection.update(pointerGenePosition, linkCollection);
		} else if (overview.isArcHighlightLocked() && pointInsideCircle(mouseX, mouseY, geneCircle)) {
			trackviewManager.openLinkSession(overview.getLinkSelection().getActiveLink());
			trackviewManager.toggleVisible();

			GeneralLink cl = overview.getTrackviewManager().getLink();
			float relativePosition = 0;

			// calcualted position on circle where to draw capsule
			if (cl.isaocc()) {
				relativePosition = geneCircle.getRelativePosition(cl.getBChromosome().getChromosomeNumber() - 1, ((float) cl.getbStart()) / cl.getBChromosome().length());
			} else {
				relativePosition = geneCircle.getRelativePosition(cl.getAChromosome().getChromosomeNumber() - 1, ((float) cl.getaStart()) / cl.getAChromosome().length());
			}

			openNewAreaCapsule(relativePosition, mouseX, mouseY, cl, geneCircle);

		} else {
			if (overview.isArcHighlightLocked()) {
				overview.setArcHighlightLocked(false);
				linkSelection.deactivate();
			} else {
				openNewAreaCapsule(pointerGenePosition, mouseX, mouseY, null, geneCircle);
			}
		}
	}
	
	private boolean handleRightClick() {
		for (SessionViewCapsule capsule : sessions) {
			if (capsule.isDying()) {
				continue;
			}
			
			if (capsule.handle(event, mouseX, mouseY)) {
				capsule.die();
				return true;
			}
		}
		overview.openContextMenu(mouseX, mouseY);
		lastMouseClick = new SimpleMouseEvent(mouseX, mouseY, event.getWhen());
		return true;
	}

	private void openSession(SessionViewCapsule capsule) {
		if (capsule.isLinkSession()) {
			trackviewManager.openLinkSession(capsule.getLink());
		} else {
			trackviewManager.openAreaSession(capsule.getChromosome(), capsule.getChrPosition(), capsule.getChrPosition()+1000); // TODO : zoom level
		}
		trackviewManager.toggleVisible();
	}

	/**
	 * Opens a new capsule in the overview window.
	 * Instantiates needed objects for creating Capsule, then creates Capsule.
	 * Adds Capsule to the list of capsules in Overview-window, then sets Capsule to be updated so drawn on screen.
	 * @param pointerGenePosition point on circle from where line from circle to Capsule begins
	 * @param x
	 * @param y
	 * @param link Link associated with this capsule (null if this is an area capsule)
	 * @param geneCircle GeneCircle to which to attach capsule
	 */
	private void openNewAreaCapsule(float pointerGenePosition, float x, float y, GeneralLink link, GeneCircle geneCircle) {
		SessionViewCapsule capsule = new SessionViewCapsule(link, pointerGenePosition, geneCircle);
		capsule.setDimensions(0.2f, 0.1f);
		capsule.setCapsulePosition(x, y);

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