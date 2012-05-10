package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import java.awt.event.KeyEvent;

/**
 * Handles KeyEvents in Overview window.
 *
 */
public class KeyEventHandler extends EventHandler {

	private KeyEvent event;
	
	/**
	 * Constructor.
	 * @param overview OverView object the events are to be handled for
	 */
	public KeyEventHandler(OverView overview) {
		super(overview);
	}

	/**
	 * Handles keyboard events.
	 * @param event a keyboard event
	 * @return was the event handled by the context menu
	 */
	public boolean handle(KeyEvent event) {
		this.event = event;
		updateVariables();
		
		if (contextMenu != null) {
			if (handleContextMenuCommands()) return true;
		}

		if (arcHighlightLocked) {
			handleLockedArch();
		}

		handleOtherKeyEvents();
		
		return false;

	}

	/**
	 * Handles keyboard events for the context menu if it is open.
	 * @return was the keyboard event a context menu action
	 */
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

	/**
	 * Handles the selection of an arc (opens the trackview).
	 */
	private void handleLockedArch() {
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				trackviewManager.clearContainer();
				trackviewManager.openLinkSession(linkSelection.getActiveLink());
				trackviewManager.toggleVisible();
			}
			linkSelection.handle(event);
	}

	/**
	 * Handles other keyboard events. Z and A resize the circle.
	 */
	private void handleOtherKeyEvents() {
		if (KeyEvent.VK_D == event.getKeyCode()) {
			overview.setDrawArcs();
		} else if (KeyEvent.VK_Z == event.getKeyCode() && event.getID() == KeyEvent.KEY_PRESSED) {
			overview.geneCircle.setSize(Math.max(0.0f, overview.geneCircle.getSize() + 0.01f));
			overview.updateCircleSize();
		} else if (KeyEvent.VK_A == event.getKeyCode() && event.getID() == KeyEvent.KEY_PRESSED) {
			overview.geneCircle.setSize(Math.max(0.0f, overview.geneCircle.getSize() - 0.01f));
			overview.updateCircleSize();
		}
	}
}
