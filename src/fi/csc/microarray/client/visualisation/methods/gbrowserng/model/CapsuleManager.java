package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.SessionViewCapsule;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Capsulemanager manages the opened capsules and their position on the screen.
 */

public class CapsuleManager {

	private GlobalVariables globals;
	private int nextFreeTopleft = 0;
	private int nextFreeBtmleft = 0;
	private int nextFreeTopright = 0;
	private int nextFreeBtmright = 0;

	private ConcurrentHashMap<Integer, SessionViewCapsule> sessions = new ConcurrentHashMap<Integer, SessionViewCapsule>();
	
	public CapsuleManager(GlobalVariables g) {
		this.globals = g;
	}
	
	public ConcurrentHashMap<Integer, SessionViewCapsule> getSessions() {
		return sessions;
	}

	private void replaceFirst(SessionViewCapsule c, int startID) {
		c.setCapsulePosition(sessions.get(startID).getCapsulePosition().x, sessions.get(startID).getCapsulePosition().y);
		for(int i=startID+globals.SLOTS_PER_QUAD-1; i>startID; i--) {
			sessions.replace(i, sessions.get(i-1));
			sessions.get(i).setCapsulePosition(sessions.get(i).getCapsulePosition().x, sessions.get(i).getCapsulePosition().y-globals.SLOT_HEIGHT);
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
			if(nextFreeTopleft != globals.SLOTS_PER_QUAD) {
				x=-0.99f + (c.getDimensions().x/2);
				y=(1f - (c.getDimensions().y))-nextFreeTopleft*globals.SLOT_HEIGHT;
				id=nextFreeTopleft;
				nextFreeTopleft++;
			}
			else {
				replaceFirst(c, 0);
				return;
			}
		}
		else if(linkx < 0 && linky < 0) {
			if(nextFreeBtmleft!=globals.SLOTS_PER_QUAD) {
				x=-0.99f + (c.getDimensions().x/2);
				y=(0f - (c.getDimensions().y / 2))-nextFreeBtmleft*globals.SLOT_HEIGHT;
				id=globals.SLOTS_PER_QUAD + nextFreeBtmleft;
				nextFreeBtmleft++;
			}
			else {
				replaceFirst(c, globals.SLOTS_PER_QUAD);
				return;
			}
		}
		else if(linkx > 0 && linky > 0) {
			if(nextFreeTopright!=globals.SLOTS_PER_QUAD) {
				x=0.99f - (c.getDimensions().x/2);
				y=(1f - (c.getDimensions().y))-nextFreeTopright*globals.SLOT_HEIGHT;
				id=(globals.SLOTS_PER_QUAD*2) + nextFreeTopright;
				nextFreeTopright++;
			}
			else {
				replaceFirst(c, globals.SLOTS_PER_QUAD*2);
				return;
			}
		}
		else if(linkx > 0 && linky < 0) {
			if(nextFreeBtmright!=globals.SLOTS_PER_QUAD) {
				x=0.99f - (c.getDimensions().x/2);
				y=(0f - (c.getDimensions().y / 2))-nextFreeBtmright*globals.SLOT_HEIGHT;
				id=(globals.SLOTS_PER_QUAD*3) + nextFreeBtmright;
				nextFreeBtmright++;
			}
			else {
				replaceFirst(c, globals.SLOTS_PER_QUAD*3);
				return;
			}
		}
		c.setCapsulePosition(x,y);
		sessions.put(id, c);
	}

	private boolean replaceFrom(int id, int endID, int nextfree) {
		int end = Math.min(endID-globals.SLOTS_PER_QUAD-1 + nextfree, endID-1);
		System.out.println(nextfree);
		if(end>0) {
			for(int i=id; i<end; ++i) {
				sessions.replace(i, sessions.get(i+1));
				sessions.get(i).setCapsulePosition(sessions.get(i).getCapsulePosition().x, sessions.get(i).getCapsulePosition().y+globals.SLOT_HEIGHT);
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
		Iterator it = sessions.keySet().iterator();
		while(it.hasNext()) {
			int id = (Integer)it.next();
			if(sessions.get(id) == c) {
				if(id<globals.SLOTS_PER_QUAD) replaceFrom(id, globals.SLOTS_PER_QUAD, nextFreeTopleft--);
				else if(id>=globals.SLOTS_PER_QUAD && id<globals.SLOTS_PER_QUAD*2) replaceFrom(id, globals.SLOTS_PER_QUAD*2, nextFreeBtmleft--);
				else if(id>=globals.SLOTS_PER_QUAD*2 && id<globals.SLOTS_PER_QUAD*3) replaceFrom(id, globals.SLOTS_PER_QUAD*3, nextFreeTopright--);
				else if(id>=globals.SLOTS_PER_QUAD*3 && id<globals.SLOTS_PER_QUAD*4) replaceFrom(id, globals.SLOTS_PER_QUAD*4, nextFreeBtmright--);
				return;
			}
		}
	}
}
