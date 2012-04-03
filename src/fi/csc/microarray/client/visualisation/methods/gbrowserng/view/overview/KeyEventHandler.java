package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jogamp.newt.event.KeyEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import java.util.Random;

/**
 * Handles KeyEvents in Overview window.
 * Commenting needs work - work inprogress
 *
 * @author
 * Paloheimo
 */
public class KeyEventHandler extends EventHandler {

	private boolean circleNeedsUpdate;
	private boolean zKeyDown, aKeyDown, drawArcs;

	public KeyEventHandler(OverView overview) {
		super(overview);
		this.aKeyDown = overview.getAKeyDown();
		this.zKeyDown = overview.getZKeyDown();
		this.drawArcs = overview.getDrawArcs();
	}

	public boolean handle(KeyEvent event) {

		contextMenu = overview.getContextMenu();
		if (contextMenu != null && (event.getKeyCode() == KeyEvent.VK_DOWN
				|| event.getKeyCode() == KeyEvent.VK_UP
				|| event.getKeyCode() == KeyEvent.VK_ENTER)) {

			contextMenu.handle(event);

			if (contextMenu.close()) {
				contextMenu = null;
			}
			return true;
		}

		if (!activeSessions.isEmpty()) {
			for (SessionViewCapsule capsule : activeSessions) {
				if (capsule.inComponent(mousePosition.x, mousePosition.y)) {
					return capsule.getSession().handle(event);
				}
			}
		}

		if (arcHighlightLocked) {
			if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				trackviewManager.clearContainer();
				trackviewManager.openLinkSession(linkSelection.getActiveLink());
				trackviewManager.toggleVisible();
				sessions.add(trackviewManager.generateLinkCapsule(overview));
			}
			linkSelection.handle(event);
		}

		// if-else horrow which would make Luukkainen pass out - will refactor once I get what goes on here (Kristiina)
		// maybe just do a a CASE -> DO thing here? would at least be more readable
		if (KeyEvent.VK_D == event.getKeyCode()) {
			overview.setDrawArcs();
		} else if (KeyEvent.VK_Z == event.getKeyCode()) {
			if (event.getEventType() == KeyEvent.EVENT_KEY_RELEASED) {
				overview.setCircleUpdate(true);
				zKeyDown = false;
			} else if (event.getEventType() == KeyEvent.EVENT_KEY_PRESSED) {
				zKeyDown = true;
			}
			
		} else if (KeyEvent.VK_A == event.getKeyCode()) {
			if (event.getEventType() == KeyEvent.EVENT_KEY_RELEASED) {
				circleNeedsUpdate = true;
				aKeyDown = false;
			} else if (event.getEventType() == KeyEvent.EVENT_KEY_PRESSED) {
				aKeyDown = true;
			}
			
		} else if (KeyEvent.VK_SPACE == event.getKeyCode() && event.getEventType() == KeyEvent.EVENT_KEY_PRESSED) {
			Random r = new Random();
			for (int i = 0; i < 1000; ++i) {
				ViewChromosome begin = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
				ViewChromosome end = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
				linkCollection.addToQueue(begin, end, r.nextInt((int) begin.length()), r.nextInt((int) end.length()));
			}
		}
		return false;

	}
}
