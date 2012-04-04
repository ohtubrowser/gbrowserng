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

	public KeyEventHandler(OverView overview) {
		super(overview);
	}

	public boolean handle(KeyEvent event) {

		contextMenu = overview.getContextMenu();
		if (contextMenu != null && (event.getKeyCode() == KeyEvent.VK_DOWN
				|| event.getKeyCode() == KeyEvent.VK_UP
				|| event.getKeyCode() == KeyEvent.VK_ENTER)) {

			contextMenu.handle(event);

			if (contextMenu.close()) {
				overview.closeContextMenu();
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

		if (KeyEvent.VK_D == event.getKeyCode()) {
			overview.setDrawArcs();
		} else if (KeyEvent.VK_Z == event.getKeyCode() && event.getEventType() == KeyEvent.EVENT_KEY_PRESSED) {
			overview.geneCircle.setSize(overview.geneCircle.getSize() + 0.01f);
		} else if (KeyEvent.VK_A == event.getKeyCode() && event.getEventType() == KeyEvent.EVENT_KEY_PRESSED) {
			overview.geneCircle.setSize(overview.geneCircle.getSize() - 0.01f);
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
