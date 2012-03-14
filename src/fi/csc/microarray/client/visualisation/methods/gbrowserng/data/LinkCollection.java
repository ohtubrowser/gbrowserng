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
	
	public LinkRangeIterator getRangeIterator(ViewChromosome startChr, ViewChromosome endChr, long startPos, long endPos) {
		GeneralLink tempA = new GeneralLink(startChr, endChr, startPos, endPos, true);
		GeneralLink tempB = new GeneralLink(endChr, startChr, startPos, endPos, false);
		int startIndex = Math.abs(Collections.binarySearch(links, tempA));
		int endIndex = Math.abs(Collections.binarySearch(links, tempB));
		return new LinkRangeIterator(this, startIndex, endIndex);
	}
	
	public LinkRangeIterator getRangeIterator(GeneCircle geneCircle, float relativeStart, float relativeEnd) {
		ViewChromosome startChr = geneCircle.getChromosomeByRelativePosition(relativeStart),
				endChr = geneCircle.getChromosomeByRelativePosition(relativeEnd);
		long startPos = geneCircle.getChromosomePosition(startChr, relativeStart),
				endPos = geneCircle.getChromosomePosition(endChr, relativeEnd);
		return getRangeIterator(startChr, endChr, startPos, endPos);
	}

	public ArrayList<GeneralLink> getLinks() {
		return links;
	}
}
