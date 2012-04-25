package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager.GBrowserPreview;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.SessionViewCapsule;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Capsulemanager manages the opened capsules and their position on the screen.
 */

public class CapsuleManager implements PreviewManager.PreviewUpdateListener {
	
	private static final int SLOTS_PER_QUAD = 4;
	private static final float SLOT_HEIGHT = 1.0f/SLOTS_PER_QUAD;
	
	private int nextFreeTopleft = 0;
	private int nextFreeBtmleft = 0;
	private int nextFreeTopright = 0;
	private int nextFreeBtmright = 0;
	
	private GBrowserPreview previewSession;

	private ConcurrentHashMap<Integer, SessionViewCapsule> sessions = new ConcurrentHashMap<Integer, SessionViewCapsule>();
	LinkedList<SessionViewCapsule> recentlyUpdatedCapsules = new LinkedList<SessionViewCapsule>();
	
	private boolean waitingForUpdate = false;

	public ConcurrentHashMap<Integer, SessionViewCapsule> getSessions() {
		return sessions;
	}

	private void replaceFirst(SessionViewCapsule c, int startID) {
		c.setCapsulePosition(sessions.get(startID).getCapsulePosition().x, sessions.get(startID).getCapsulePosition().y);
		for(int i=startID+SLOTS_PER_QUAD-1; i>startID; i--) {
			sessions.replace(i, sessions.get(i-1));
			sessions.get(i).setCapsulePosition(sessions.get(i).getCapsulePosition().x, sessions.get(i).getCapsulePosition().y-SLOT_HEIGHT);
		}
		sessions.replace(startID, c);
	}

	/**
	 * AddCapsule adds the capsule to the internal data structure.
	 * @param c Capsule to be added
	 * @param linkx Capsule's LinkGFX's x position. (For aligning the capsule to correct quad).
	 * @param linky Capsule's LinkGFX's y position. (For aligning the capsule to correct quad).
	 */
	public void addCapsule(SessionViewCapsule c, float linkx, float linky) {
		int id=0;
		float x=0, y=0;
		if(linkx < 0 && linky > 0) {
			if(nextFreeTopleft != SLOTS_PER_QUAD) {
				x=-0.99f + (c.getDimensions().x/2);
				y=(1f - (c.getDimensions().y))-nextFreeTopleft*SLOT_HEIGHT;
				id=nextFreeTopleft;
				nextFreeTopleft++;
			}
			else {
				replaceFirst(c, 0);
				return;
			}
		}
		else if(linkx < 0 && linky < 0) {
			if(nextFreeBtmleft!=SLOTS_PER_QUAD) {
				x=-0.99f + (c.getDimensions().x/2);
				y=(0f - (c.getDimensions().y / 2))-nextFreeBtmleft*SLOT_HEIGHT;
				id=SLOTS_PER_QUAD + nextFreeBtmleft;
				nextFreeBtmleft++;
			}
			else {
				replaceFirst(c, SLOTS_PER_QUAD);
				return;
			}
		}
		else if(linkx > 0 && linky > 0) {
			if(nextFreeTopright!=SLOTS_PER_QUAD) {
				x=0.99f - (c.getDimensions().x/2);
				y=(1f - (c.getDimensions().y))-nextFreeTopright*SLOT_HEIGHT;
				id=(SLOTS_PER_QUAD*2) + nextFreeTopright;
				nextFreeTopright++;
			}
			else {
				replaceFirst(c, SLOTS_PER_QUAD*2);
				return;
			}
		}
		else if(linkx > 0 && linky < 0) {
			if(nextFreeBtmright!=SLOTS_PER_QUAD) {
				x=0.99f - (c.getDimensions().x/2);
				y=(0f - (c.getDimensions().y / 2))-nextFreeBtmright*SLOT_HEIGHT;
				id=(SLOTS_PER_QUAD*3) + nextFreeBtmright;
				nextFreeBtmright++;
			}
			else {
				replaceFirst(c, SLOTS_PER_QUAD*3);
				return;
			}
		}
		c.setCapsulePosition(x,y);
		sessions.put(id, c);
		if(!waitingForUpdate)
			updateCapsuleImage(null);
	}
	
	private boolean replaceFrom(int id, int endID, int nextfree) {
		int end = Math.min(endID-SLOTS_PER_QUAD-1 + nextfree, endID-1);
		// This shouldn't be called when end==0, but for some reason it is.
		System.out.println(nextfree);
		if(end>0) {
			for(int i=id; i<end; ++i) {
				sessions.replace(i, sessions.get(i+1));
				sessions.get(i).setCapsulePosition(sessions.get(i).getCapsulePosition().x, sessions.get(i).getCapsulePosition().y+SLOT_HEIGHT);
			}
			sessions.remove(end);
			return true;
		}
		else
		{
			sessions.remove(end);
			return false;
		}
	}

	/**
	 * removeCapsule removes SessionViewCapsule from the internal data structure.
	 * @param c Capsule to be removed.
	 */
	public void removeCapsule(SessionViewCapsule c) {
		// removeCapsule gets called multiple times... Concurrency?
		Iterator it = sessions.keySet().iterator();
		while(it.hasNext()) {
			int id = (Integer)it.next();
			if(sessions.get(id) == c) {
				if(id<SLOTS_PER_QUAD) replaceFrom(id, SLOTS_PER_QUAD, nextFreeTopleft--);
				else if(id>=SLOTS_PER_QUAD && id<SLOTS_PER_QUAD*2) replaceFrom(id, SLOTS_PER_QUAD*2, nextFreeBtmleft--);
				else if(id>=SLOTS_PER_QUAD*2 && id<SLOTS_PER_QUAD*3) replaceFrom(id, SLOTS_PER_QUAD*3, nextFreeTopright--);
				else if(id>=SLOTS_PER_QUAD*3 && id<SLOTS_PER_QUAD*4) replaceFrom(id, SLOTS_PER_QUAD*4, nextFreeBtmright--);
				if(sessions.isEmpty())
				{
					recentlyUpdatedCapsules.clear();
					waitingForUpdate = false;
				}
				return;
			}
		}
	}

	public void setPreviewSession(GBrowserPreview p) {
		previewSession = p;
		previewSession.addPreviewUpdateListener(this);
	}
	
	public void updateCapsuleImage(BufferedImage i) {
		boolean updated = false;
		if(!recentlyUpdatedCapsules.isEmpty() && i != null) {
			System.out.println(recentlyUpdatedCapsules.getLast().getRegion1().toString() + " <--");
			recentlyUpdatedCapsules.getLast().setPreviewImage(i);
		}
		for(SessionViewCapsule c : sessions.values()) {
			if(!recentlyUpdatedCapsules.contains(c))
			{
				recentlyUpdatedCapsules.add(c);
				updated = true;
				try {
					previewSession.setRegion(c.getRegion1());
				} catch (URISyntaxException ex) {
					Logger.getLogger(CapsuleManager.class.getName()).log(Level.SEVERE, null, ex);
				}
				waitingForUpdate = true;
				break;
			}
		}
		if(!updated && !recentlyUpdatedCapsules.isEmpty()) {
			recentlyUpdatedCapsules.clear();
			updateCapsuleImage(i);
		}
	}
	
	@Override
	public void PreviewUpdated() {
		System.out.println(previewSession.getRegion().toString());
		updateCapsuleImage(previewSession.getPreview());
	}
	
}
