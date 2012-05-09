package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;


import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.ContextMenu;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.CapsuleManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.LinkSelection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.TrackviewManager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Superclass for all EventHandlers. Contains variables used and update of these
 * 
 * @author Paloheimo
 */
public class EventHandler {
	
	protected OverView overview;
	protected ConcurrentHashMap<Integer, SessionViewCapsule> sessions;
	protected ContextMenu contextMenu;
	protected SessionViewCapsule hoverCapsule;
	protected Vector2 mousePosition = new Vector2();
	protected boolean arcHighlightLocked;
	protected TrackviewManager trackviewManager;
	protected LinkSelection linkSelection;
	protected LinkCollection linkCollection;
	public GlobalVariables globals;
	public CapsuleManager CapsuleManager;
	
	public EventHandler(OverView overview) {
		this.globals = overview.globals;
		this.CapsuleManager = overview.CapsuleManager;
		this.overview = overview;
		updateVariables();
	}
	
	protected void updateVariables() {
		this.sessions = overview.getSessions();
		this.contextMenu = overview.getContextMenu();
		this.hoverCapsule = overview.getHoverCapsule();
		this.mousePosition = overview.getMousePosition();
		this.arcHighlightLocked = overview.arcHighlightLocked;
		this.trackviewManager = overview.getTrackviewManager();
		this.linkSelection = overview.getLinkSelection();
		this.linkCollection = overview.getLinkCollection();
	}
}
