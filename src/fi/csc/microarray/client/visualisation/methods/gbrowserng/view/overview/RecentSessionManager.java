package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// This class is NOT thread-safe
public class RecentSessionManager implements Iterable<SessionViewRecentCapsule> {
	private final int MAXSIZE=4;
	private SessionViewRecentCapsule[] recentSessions;
	private int elements;

	public RecentSessionManager()
	{
		this.elements=0;
		this.recentSessions = new SessionViewRecentCapsule[MAXSIZE];
	}

	public SessionViewRecentCapsule[] getRecentSessions()
	{
		return recentSessions;
	}

	public void Add(SessionViewCapsule capsule)
	{
		SessionViewRecentCapsule recent=new SessionViewRecentCapsule(this.elements, capsule.getPosition(), capsule.getGeneCirclePosition(), capsule.getSession(), capsule.getSession().getSession());
		if(elements<MAXSIZE)
		{
			recentSessions[elements++]=recent;
		}
		else
		{
			Remove(0);
			Add(capsule);
		}
		recent.show();
	}
	public void Remove(SessionViewRecentCapsule capsule)
	{
		for(int i=0; i<recentSessions.length; ++i)
		{
			if(recentSessions[i]==capsule)
			{
				Remove(i);
				return;
			}
		}
	}
	public void Remove(int index)
	{
		elements--;
		if(index==elements) {recentSessions[elements]=null; return;}
		for(int i=index; i<elements; ++i)
		{
			recentSessions[i]=recentSessions[i+1];
			if(recentSessions[i]!=null) recentSessions[i].setId(i);
		}
		for(int i=elements; i<recentSessions.length; ++i) recentSessions[i]=null; // Just in case.
	}

	@Override
	public Iterator<SessionViewRecentCapsule> iterator() {
		ArrayList<SessionViewRecentCapsule> list=new ArrayList<SessionViewRecentCapsule>();
		for(int i=0; i<recentSessions.length; ++i)
		{
			if(recentSessions[i]!=null) list.add(recentSessions[i]);
		}
		return list.iterator();
	}
}
