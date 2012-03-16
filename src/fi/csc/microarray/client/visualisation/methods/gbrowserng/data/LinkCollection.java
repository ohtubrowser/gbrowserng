package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import java.util.ArrayList;
import java.util.Collections;

public class LinkCollection {
	/**
	 * Class for storing connections
	 * aims to provide fast access for range queries
	 */
	
	private ArrayList<GeneralLink> newLinksToAdd = new ArrayList<GeneralLink>(), links = new ArrayList<GeneralLink>();
	
	public final Object linkSyncLock = new Object();
	
	public LinkCollection() {
	}
	
	public void syncAdditions(GeneCircle geneCircle) {
		if(newLinksToAdd.isEmpty())
			return;
		synchronized(linkSyncLock) {
			for(GeneralLink link : newLinksToAdd)
				link.calculatePositions(geneCircle);
			links.addAll(newLinksToAdd);
			newLinksToAdd.clear();
			Collections.sort(links);
		}
	}
	
	public void addToQueue(ViewChromosome aChromosome, ViewChromosome bChromosome, long aStart, long bStart) {
		synchronized(linkSyncLock) {
			newLinksToAdd.add(new GeneralLink(aChromosome, bChromosome, aStart, bStart, true));
			newLinksToAdd.add(new GeneralLink(aChromosome, bChromosome, aStart, bStart, false));
		}
	}
	
	public GeneralLink valueAt(int index) {
		return links.get(index);
	}
	
	public LinkRangeIterator getRangeIterator(float relativeStart, float relativeEnd) {
		GeneralLink tempA = GeneralLink.createComparisonObject(relativeStart, relativeEnd, true);
		GeneralLink tempB = GeneralLink.createComparisonObject(relativeStart, relativeEnd, false);

		int startIndex = Math.abs(Collections.binarySearch(links, tempA));
		int endIndex = Math.abs(Collections.binarySearch(links, tempB));

		startIndex = Math.min(links.size()-1, startIndex);
		endIndex = Math.min(links.size(), endIndex);
		
		System.out.println(startIndex + " -- " + endIndex);
		
		return new LinkRangeIterator(this, startIndex, endIndex);
	}
	
	public ArrayList<GeneralLink> getLinks() {
		return links;
	}
	
	public int numLinks() {
		return links.size();
	}
}
