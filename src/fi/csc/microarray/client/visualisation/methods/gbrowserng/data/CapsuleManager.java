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

	public static void addCapsule(SessionViewCapsule c, float linkx, float linky) {
		float x=0, y=0;
		if(linkx < 0 && linky > 0 && nextFreeTopleft!=SLOTS_PER_QUAD) {
			x=-1f + c.getDimensions().x;
			y=(1f - (c.getDimensions().y / 2))-nextFreeTopleft*SLOT_HEIGHT;
			nextFreeTopleft++;
		}
		else if(linkx < 0 && linky < 0 && nextFreeBtmleft!=SLOTS_PER_QUAD) {
			x=-1f + c.getDimensions().x;
			y=(0f - (c.getDimensions().y / 2))-nextFreeBtmleft*SLOT_HEIGHT;
			nextFreeBtmleft++;
		}
		else if(linkx > 0 && linky > 0 && nextFreeTopright!=SLOTS_PER_QUAD) {
			x=1f - c.getDimensions().x;
			y=(1f - (c.getDimensions().y / 2))-nextFreeTopright*SLOT_HEIGHT;
			nextFreeTopright++;
		}
		else if(linkx > 0 && linky < 0 && nextFreeBtmright!=SLOTS_PER_QUAD) {
			x=1f - c.getDimensions().x;
			y=(0f - (c.getDimensions().y / 2))-nextFreeBtmright*SLOT_HEIGHT;
			nextFreeBtmright++;
		}
		c.setCapsulePosition(x,y);
		sessions.put(nextFreeBtmleft, c);
	}
}
