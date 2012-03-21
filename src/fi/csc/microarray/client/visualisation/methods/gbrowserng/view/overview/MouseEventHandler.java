package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.ChromoName;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.LinkSelection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.SimpleMouseEvent;
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

    boolean handle(MouseEvent event, float x, float y) {
        SessionViewCapsule hoverCapsule = overview.getHoverCapsule();
        GeneCircle geneCircle = overview.getGeneCircle();
        
        initializeGlobalVariables();
        checkIfLastMouseClickNull(x, y, event);
        setHoverCapsules(x, y);
        
        if (handOverResponsiblityToPossibleActiveSession(hoverCapsule, event, x, y)) return hoverCapsule.getSession().handle(event, x, y);

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
            if (pointOnCircle(geneCircle, x, y)) {
                selection.update(pointerGenePosition);
            } else {
                selection.reset();
            }
        }

        // allow capsules to update their states
        for (SessionViewCapsule capsule : sessions) {
            capsule.handle(event, x, y);
        }
        if (showEvent(event, x, y, selection, pointerGenePosition, geneCircle)) {
            return true;
        }
        return false;
    }

    private boolean handOverResponsiblityToPossibleActiveSession(SessionViewCapsule hoverCapsule, MouseEvent event, float x, float y) {
        // if there is an active session, let it handle input.
        if (!activeSessions.isEmpty() && hoverCapsule != null) {
            return true;
        }
        return false;
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

    private boolean showEvent(MouseEvent event, float x, float y, LinkSelection selection, float pointerGenePosition, GeneCircle geneCircle) {
        // then see if they actually want the event
        if (MouseEvent.EVENT_MOUSE_CLICKED == event.getEventType()) {
            if (event.getButton() == 1) {
                for (SessionViewCapsule capsule : sessions) {
                    if (capsule.isDying()) {
                        continue;
                    }
                    if (capsule.handle(event, x, y)) {
                        overview.openSession(capsule);
                        return true;
                    }
                }
                if (pointOnCircle(geneCircle, x, y)) //				 respond to mouse click
                {
                    overview.setArcHighlightLocked(true);
                    selection.update(pointerGenePosition);
                    selection.updateArea(overview.getLinkCollection());
                } else if(overview.isArcHighlightLocked() && pointInsideCircle(geneCircle, x, y)) {
					overview.getTrackviewManager().clearContainer();
					overview.getTrackviewManager().openLinkSession(overview.getLinkSelection().getActiveLink());
					overview.getTrackviewManager().toggleVisible();
				} else {
                    if (overview.isArcHighlightLocked()) {
                        overview.setArcHighlightLocked(false);
                        selection.deactivate();
                    } else {
                        Session session = new Session(geneCircle.getChromosome().getReferenceSequence(), geneCircle.getChromosomePosition());
                        SessionView sessionView = new SessionView(session, overview);
                        SessionViewCapsule capsule = new SessionViewCapsule(sessionView, pointerGenePosition, geneCircle);
                        capsule.getSession().setDimensions(0.4f, 0.2f);
                        capsule.getSession().setPosition(x, y);
                        sessions.add(capsule);
                        //openSession(capsule);
                        LinkedList<SessionViewCapsule> textureUpdateListLock = overview.getTextureUpdateListLock();
                        synchronized (textureUpdateListLock) {
                            overview.addCapsuleToTextureUpdateList(capsule);
                            capsule.setNeedsTextureUpdate();
                        }
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
                ViewChromosome chromosome = geneCircle.getChromosome();
                if (chromosome.isMinimized()) {
                    chromosome.setMinimized(false);
                } else {
                    chromosome.setMinimized(true);
                }
                if ((chromosome.isAnimating() && !chromosome.isMinimized())
                        && lastMouseClick.getX() == x && lastMouseClick.getY() == y
                        && lastMouseClick.getWhen() + 250 > event.getWhen()) {
                    overview.minimizeAllButOne(chromosome);
                } else if ((chromosome.isAnimating() && chromosome.isMinimized())
                        && lastMouseClick.getX() == x && lastMouseClick.getY() == y
                        && lastMouseClick.getWhen() + 250 > event.getWhen()) {
                    for (int i = 0; i < AbstractGenome.getNumChromosomes(); ++i) {
                        AbstractGenome.getChromosome(i).setMinimized(false);
                    }
                }
                geneCircle.animating = true;
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
		selection.mouseMove(pointInsideCircle(geneCircle, x, y), x, y);
        overview.setMousePositionX(CoordinateManager.toCircleCoordsY(x), CoordinateManager.toCircleCoordsX(y));
        return false;
    }

	public boolean pointOnCircle(GeneCircle geneCircle, float x, float y) {
		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(size);
		float b = CoordinateManager.toCircleCoordsY(size);
		float s = Math.abs(x*x/(a*a) + y*y/(b*b));
		return (s < 1.0f && s > 0.8f);
	}


	private boolean pointInsideCircle(GeneCircle geneCircle, float x, float y) {
		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(size);
		float b = CoordinateManager.toCircleCoordsY(size);
		float s = Math.abs(x*x/(a*a) + y*y/(b*b));
		if (s < 0.9f)
		{
			return true;
		}
		return false;
	}
}
