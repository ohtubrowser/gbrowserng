package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import java.util.ArrayList;
import java.util.Collections;

public class LinkCollection {

	/**
	 * Class for storing connections aims to provide fast access for range
	 * queries
	 */
	private ArrayList<GeneralLink> newLinksToAdd = new ArrayList<GeneralLink>(), links = new ArrayList<GeneralLink>();
	public final Object linkSyncLock = new Object();
	private float timeUntilSync = GlobalVariables.linkSyncInterval;
	private GeneCircle geneCircle;

	public LinkCollection(GeneCircle geneCircle) {
		this.geneCircle = geneCircle;
	}

	// TODO : maybe need to account for invalidation of existing iterators once this happens
	public void syncAdditions() {
		if (newLinksToAdd.isEmpty()) return;

		synchronized (linkSyncLock) {
			for (GeneralLink link : newLinksToAdd)
				link.calculatePositions(geneCircle);

			if (newLinksToAdd.size() > links.size()) {
				links.addAll(newLinksToAdd);
				Collections.sort(links);
			} else {
				for (GeneralLink link : newLinksToAdd) {
					int insertIndex = Collections.binarySearch(links, link);
					if (insertIndex < 0) insertIndex = -(insertIndex + 1);
					links.add(insertIndex, link);
				}
			}
			newLinksToAdd.clear();
		}
	}

	public void addToQueue(ViewChromosome aChromosome, ViewChromosome bChromosome, long aStart, long bStart) {
		synchronized (linkSyncLock) {
			GeneralLink a = new GeneralLink(aChromosome, bChromosome, aStart, bStart, true);
			GeneralLink b = new GeneralLink(aChromosome, bChromosome, aStart, bStart, false);
			newLinksToAdd.add(a);
			newLinksToAdd.add(b);
		}
	}

	public GeneralLink valueAt(int index) {
		return links.get(index);
	}

	public LinkRangeIterator getRangeIterator(float relativeStart, float relativeEnd) {
		ViewChromosome a = geneCircle.getChromosomeByRelativePosition(relativeStart);
		ViewChromosome b = geneCircle.getChromosomeByRelativePosition(relativeEnd);
		long posA = geneCircle.getPositionInChr(a, relativeStart);
		long posB = geneCircle.getPositionInChr(b, relativeEnd);

		GeneralLink tempA = GeneralLink.createComparisonObject(a,b,posA,posB, true);
		GeneralLink tempB = GeneralLink.createComparisonObject(a,b,posA,posB, false);

		int startIndex = Collections.binarySearch(links, tempA);
		if(startIndex < 0) startIndex = -(startIndex + 1);
		int endIndex = Collections.binarySearch(links, tempB);
		if(endIndex < 0) endIndex = -(endIndex + 1);

		startIndex = Math.min(links.size() - 1, startIndex);
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
		if (timeUntilSync < 0) {
			timeUntilSync = GlobalVariables.linkSyncInterval;
			syncAdditions();
		}
	}

	public void addToQueue(GeneralLink l) {
		addToQueue(l.getAChromosome(), l.getBChromosome(), l.getaStart(), l.getbStart());
	}

	private boolean isSorted() {
		for(int i = 1; i < links.size(); ++i) {
			if(links.get(i).compareTo(links.get(i-1)) < 0)
				return false;
		}
		return true;
	}
	
	public void updateLinkPositions(GeneCircle geneCircle) {
		synchronized(linkSyncLock) {
			for(GeneralLink link : links)
				link.calculatePositions(geneCircle);
		}
	}
}
