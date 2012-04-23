package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Handles KeyEvents in Overview window.
 * Commenting needs work - work in progress
 * Code also still a bit confusing and not well named - working on that, too
 *
 * @author
 * Paloheimo
 */
public class KeyEventHandler extends EventHandler {

	private KeyEvent event;
	public KeyEventHandler(OverView overview) {
		super(overview);
	}

	public boolean handle(KeyEvent event) {
		this.event = event;
		updateVariables();
		
		if (contextMenu != null) {
			if (handleContextMenuCommands()) return true;
		}

		/*
		if (!activeSessions.isEmpty()) {
			if (handleMouseOverCapsule()) return true;
		}
		*/

		if (arcHighlightLocked) {
			handleLockedArch();
		}

		handleOtherKeyEvents();
		
		return false;

	}

	private boolean handleContextMenuCommands() {	
			if ((event.getKeyCode() == KeyEvent.VK_DOWN
				|| event.getKeyCode() == KeyEvent.VK_UP
				|| event.getKeyCode() == KeyEvent.VK_ENTER)) {

			contextMenu.handle(event);

			if (contextMenu.close()) {
				overview.closeContextMenu();
			}
			return true;
		}
			return false;
	}

	private void handleLockedArch() {
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				trackviewManager.clearContainer();
				trackviewManager.openLinkSession(linkSelection.getActiveLink());
				trackviewManager.toggleVisible();

				//sessions.add(trackviewManager.generateLinkCapsule(overview));
			}
			linkSelection.handle(event);
	}

	private void handleOtherKeyEvents() {
		if (KeyEvent.VK_D == event.getKeyCode()) {
			overview.setDrawArcs();
		} else if (KeyEvent.VK_Z == event.getKeyCode() && event.getID() == KeyEvent.KEY_PRESSED) {
			overview.geneCircle.setSize(Math.max(0.0f, overview.geneCircle.getSize() + 0.01f));
			overview.updateCircleSize();
		} else if (KeyEvent.VK_A == event.getKeyCode() && event.getID() == KeyEvent.KEY_PRESSED) {
			overview.geneCircle.setSize(Math.max(0.0f, overview.geneCircle.getSize() - 0.01f));
			overview.updateCircleSize();
		} else if (KeyEvent.VK_SPACE == event.getKeyCode() && event.getID() == KeyEvent.KEY_PRESSED) {
			Random r = new Random();
			for (int i = 0; i < 1000; ++i) {
				ViewChromosome begin = Genome.getChromosome(r.nextInt(Genome.getNumChromosomes()));
				ViewChromosome end = Genome.getChromosome(r.nextInt(Genome.getNumChromosomes()));
				linkCollection.addToQueue(begin, end, r.nextInt((int) begin.length()), r.nextInt((int) end.length()));
			}
		}
	}
}
