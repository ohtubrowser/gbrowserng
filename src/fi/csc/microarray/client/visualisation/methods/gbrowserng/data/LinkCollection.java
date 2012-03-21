package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
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
	private float timeUntilSync = GlobalVariables.linkSyncInterval;

	public LinkCollection() {
	}
	
	// TODO : maybe need to account for invalidation of existing iterators once this happens
	public void syncAdditions() {
		if(newLinksToAdd.isEmpty())
			return;
		synchronized(linkSyncLock) {
			// Depending on the relative sizes of newLinksToAdd and links, this will often be faster than standard mergesort-like merge
			for(GeneralLink link : newLinksToAdd) {
				int insertIndex = Math.abs(Collections.binarySearch(links, link));
				links.add(insertIndex-1, link);
			}
			newLinksToAdd.clear();
		}
	}
	
	public void addToQueue(GeneCircle geneCircle, ViewChromosome aChromosome, ViewChromosome bChromosome, long aStart, long bStart) {
		synchronized(linkSyncLock) {
			GeneralLink a = new GeneralLink(aChromosome, bChromosome, aStart, bStart, true);
			GeneralLink b = new GeneralLink(aChromosome, bChromosome, aStart, bStart, false);
			a.calculatePositions(geneCircle);
			b.calculatePositions(geneCircle);
			newLinksToAdd.add(a);
			newLinksToAdd.add(b);
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
		
		return new LinkRangeIterator(this, startIndex, endIndex);
	}
	
	public ArrayList<GeneralLink> getLinks() {
		return links;
	}
	
	public int numLinks() {
		return links.size();
	}

	public void tick(float dt) {
		timeUntilSync -= dt;
		if(timeUntilSync < 0) {
			timeUntilSync = GlobalVariables.linkSyncInterval;
			syncAdditions();
		}
	}
}
