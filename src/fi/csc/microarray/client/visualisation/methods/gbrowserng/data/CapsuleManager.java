package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.SessionViewCapsule;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

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
			System.out.println("Replacing id " + i + " with " + (i-1));
			sessions.replace(i, sessions.get(i-1));
			sessions.get(i).setCapsulePosition(sessions.get(i).getCapsulePosition().x, sessions.get(i).getCapsulePosition().y-SLOT_HEIGHT);
		}
		sessions.replace(startID, c);
	}

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
		System.out.println("Adding capsule of id: " + id);
		sessions.put(id, c);
	}
}
