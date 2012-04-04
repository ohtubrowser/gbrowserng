package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;


import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.ContextMenu;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.LinkSelection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.TrackviewManager;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Superclass for all EventHandlers. Contains variables used and update of these
 * 
 * @author Paloheimo
 */
public class EventHandler {
	
	protected OverView overview;
	protected ConcurrentLinkedQueue<SessionViewCapsule> sessions;
	protected ConcurrentLinkedQueue<SessionViewCapsule> activeSessions;
	protected ContextMenu contextMenu;
	protected SessionViewCapsule hoverCapsule;
	protected Vector2 mousePosition = new Vector2();
	protected boolean arcHighlightLocked;
	protected TrackviewManager trackviewManager;
	protected LinkSelection linkSelection;
	protected LinkCollection linkCollection;
	
	public EventHandler(OverView overview) {
		this.overview = overview;
		updateVariables();
	}
	
	protected void updateVariables() {
		this.sessions = overview.getSessions();
		this.activeSessions = overview.getActiveSessions();
		this.contextMenu = overview.getContextMenu();
		this.hoverCapsule = overview.getHoverCapsule();
		this.mousePosition = overview.getMousePosition();
		this.arcHighlightLocked = overview.arcHighlightLocked;
		this.trackviewManager = overview.getTrackviewManager();
		this.linkSelection = overview.getLinkSelection();
		this.linkCollection = overview.getLinkCollection();
	}
}