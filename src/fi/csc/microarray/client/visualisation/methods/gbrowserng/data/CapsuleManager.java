package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.SessionViewCapsule;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Capsulemanager manages the opened capsules and their position on the screen.
 */

public class CapsuleManager {
	
	private static final int SLOTS_PER_QUAD = 4;
	private static final float SLOT_HEIGHT = 1.0f/SLOTS_PER_QUAD;
	
	private static int nextFreeTopleft = 0;
	private static int nextFreeBtmleft = 0;
	private static int nextFreeTopright = 0;
	private static int nextFreeBtmright = 0;

	private static ConcurrentHashMap<Integer, SessionViewCapsule> sessions = new ConcurrentHashMap<Integer, SessionViewCapsule>();
	
	public static ConcurrentHashMap<Integer, SessionViewCapsule> getSessions() {
		return sessions;
	}

	private static void replaceFirst(SessionViewCapsule c, int startID) {
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
	public static void addCapsule(SessionViewCapsule c, float linkx, float linky) {
		int id=0;
		float x=0, y=0;
		if(linkx < 0 && linky > 0) {
			if(nextFreeTopleft != SLOTS_PER_QUAD) {
				x=-1f + c.getDimensions().x;
				y=(1f - (c.getDimensions().y / 2))-nextFreeTopleft*SLOT_HEIGHT;
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
				x=-1f + c.getDimensions().x;
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
				x=1f - c.getDimensions().x;
				y=(1f - (c.getDimensions().y / 2))-nextFreeTopright*SLOT_HEIGHT;
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
				x=1f - c.getDimensions().x;
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
	}

	private static boolean replaceFrom(int id, int endID, int nextfree) {
		int end = Math.min(endID-SLOTS_PER_QUAD-1 + nextfree, endID-1);
		// This shouldn't be called when end==0, but for some reason it is.
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
	public static void removeCapsule(SessionViewCapsule c) {
		// removeCapsule gets called multiple times... Concurrency?
		Iterator it = sessions.keySet().iterator();
		while(it.hasNext()) {
			int id = (Integer)it.next();
			if(sessions.get(id) == c) {
				if(id<SLOTS_PER_QUAD) {if(replaceFrom(id, SLOTS_PER_QUAD, nextFreeTopleft)) nextFreeTopleft--;}
				else if(id>=SLOTS_PER_QUAD && id<SLOTS_PER_QUAD*2) {if(replaceFrom(id, SLOTS_PER_QUAD*2, nextFreeBtmleft)) nextFreeBtmleft--;}
				else if(id>=SLOTS_PER_QUAD*2 && id<SLOTS_PER_QUAD*3) {if(replaceFrom(id, SLOTS_PER_QUAD*3, nextFreeTopright)) nextFreeTopright--;}
				else if(id>=SLOTS_PER_QUAD*3 && id<SLOTS_PER_QUAD*4) {if(replaceFrom(id, SLOTS_PER_QUAD*4, nextFreeBtmright)) nextFreeBtmright--;}
				return;
			}
		}
	}
}
