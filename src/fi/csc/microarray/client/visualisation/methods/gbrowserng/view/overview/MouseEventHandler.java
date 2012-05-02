package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.CapsuleManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.CoordinateManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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

	private MouseEvent event;
	private float mouseX, mouseY;
	private GeneCircle geneCircle;
	private float pointerGenePosition;

	public MouseEventHandler(OverView overview) {
		super(overview);
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
		setHoverCapsule(x, y);

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
		for (SessionViewCapsule capsule : sessions.values()) {
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

	private void setHoverCapsule(float x, float y) {
		overview.setHoverCapsule(null);
		for (SessionViewCapsule capsule : sessions.values()) {
			if (capsule.inCapsule(x, y)) {
				overview.setHoverCapsule(capsule);
				break;
			}
		}
	}

	private void calculatePointerGenePosition() {
		double cx = CoordinateManager.toCircleCoordsX(globals, mouseY);
		// x-axis is negated to make tracking begin from the mathematical zero angle.
		double cy = CoordinateManager.toCircleCoordsY(globals, -mouseX);

		// Maps position of the mouse to a relative position on a circle.
		// Using range [0,1) counterclockwise, starting from mathematical zero angle.
		float v = (float) (Math.atan2(cx, cy) / Math.PI);
		pointerGenePosition = 1.0f - (v * 0.5f + 0.5f);

		// Locks the relative position on a certain point if
		// the mouse cursor is over a chromosome name.
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

		if (MouseEvent.MOUSE_CLICKED == event.getID()) {
			if (event.getButton() == 1) {
				return handleLeftClick();
			} else if (event.getButton() == 3) {
				return handleRightClick();
			}
		}

		if (MouseEvent.MOUSE_WHEEL == event.getID()) {
			MouseWheelEvent e = (MouseWheelEvent) event;
			if (overview.isArcHighlightLocked()) {
				linkSelection.updateArea(0.001f * e.getWheelRotation(), overview.getLinkCollection());
			} else {
				geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() + e.getWheelRotation() * 0.05f));
				overview.updateCircleSize();
			}
		}
		
		linkSelection.mouseMove(pointInsideCircle(mouseX, mouseY, geneCircle), mouseX, mouseY);
		overview.setMousePositionX(CoordinateManager.toCircleCoordsY(globals, mouseX), CoordinateManager.toCircleCoordsX(globals, mouseY));
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

		// Open session if clicked on a capsule.
		// Dying capsule won't be opened.
		for (SessionViewCapsule capsule : sessions.values()) {
			if (capsule.isDying()) {
				continue;
			}
			if (capsule.handle(event, mouseX, mouseY)) {
				capsule.openSession();
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
			GeneralLink cl = overview.getLinkSelection().getActiveLink();
			float relativePosition = geneCircle.getRelativePosition(cl.getEndChromosome().getChromosomeNumber() - 1, ((float) cl.getEndPosition()) / cl.getEndChromosome().length());

			SessionViewCapsule c = openNewAreaCapsule(relativePosition, cl, geneCircle);
			c.openSession();

		} else if (pointInsideCircle(mouseX, mouseY, geneCircle)) {
			GeneralLink cl = overview.activeLink;
			float relativePosition = geneCircle.getRelativePosition(cl.getEndChromosome().getChromosomeNumber() - 1, ((float) cl.getEndPosition()) / cl.getEndChromosome().length());

			SessionViewCapsule c = openNewAreaCapsule(relativePosition, cl, geneCircle);
			c.openSession();
		}
		else {
			if (overview.isArcHighlightLocked()) {
				overview.setArcHighlightLocked(false);
				linkSelection.deactivate();
			} else {
				openNewAreaCapsule(pointerGenePosition, null, geneCircle);
			}
		}
	}
	
	private boolean handleRightClick() {
		for (SessionViewCapsule capsule : sessions.values()) {
			if (capsule.isDying()) {
				continue;
			}
			
			if (capsule.handle(event, mouseX, mouseY)) {
				capsule.die();
				return true;
			}
		}
		overview.openContextMenu(mouseX, mouseY);
		return true;
	}

	/**
	 * Opens a new capsule in the overview window.
	 * Instantiates needed objects for creating Capsule, then creates Capsule.
	 * Adds Capsule to the list of capsules in Overview-window, then sets Capsule to be updated so drawn on screen.
	 * @param pointerGenePosition point on circle from where line from circle to Capsule begins
	 * @param link Link associated with this capsule (null if this is an area capsule)
	 * @param geneCircle GeneCircle to which to attach capsule
	 */
	private SessionViewCapsule openNewAreaCapsule(float pointerGenePosition, GeneralLink link, GeneCircle geneCircle) {
		SessionViewCapsule capsule = new SessionViewCapsule(overview, link, pointerGenePosition, geneCircle);
		CapsuleManager.addCapsule(capsule, capsule.getLinkGfX().getXYPosition().x, capsule.getLinkGfX().getXYPosition().y);

		synchronized (overview.textureUpdateListLock) {
			overview.addCapsuleToTextureUpdateList(capsule);
			capsule.setNeedsTextureUpdate();
		}
		return capsule;
	}

	public boolean pointOnCircle(float x, float y, GeneCircle geneCircle) {
		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(globals, size);
		float b = CoordinateManager.toCircleCoordsY(globals, size);
		float s = Math.abs(x * x / (a * a) + y * y / (b * b));
		return (s < 1.0f && s > 0.8f);
	}

	private boolean pointInsideCircle(float x, float y, GeneCircle geneCircle) {
		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(globals, size);
		float b = CoordinateManager.toCircleCoordsY(globals, size);
		float s = Math.abs(x * x / (a * a) + y * y / (b * b));
		return (s < 0.9f);
	}

}