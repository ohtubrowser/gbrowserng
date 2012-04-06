package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class LinkCollection {

	/**
	 * Class for storing connections
	 * aims to provide fast access
	 * for range queries
	 */
	private ArrayList<GeneralLink> newLinksToAdd = new ArrayList<GeneralLink>(), links = new ArrayList<GeneralLink>();
	public final Object linkSyncLock = new Object();
	private float timeUntilSync = GlobalVariables.linkSyncInterval;
	public boolean loading = false;

	public LinkCollection() {
	}
	
	// TODO : maybe need to account for invalidation of existing iterators once this happens
	public void syncAdditions() {
		if (newLinksToAdd.isEmpty()) {
			return;
		}
		synchronized (linkSyncLock) {
			loading = true;
			if (newLinksToAdd.size() > links.size()) {
				newLinksToAdd.addAll(links);
				Collections.sort(newLinksToAdd);
				ArrayList<GeneralLink> t = links;
				links = newLinksToAdd;
				newLinksToAdd = t;
			} else {
				for (GeneralLink link : newLinksToAdd) {
					int insertIndex = Collections.binarySearch(links, link);
					if (insertIndex < 0) {
						insertIndex = -(insertIndex + 1);
					}
					links.add(insertIndex, link);
				}
			}
			newLinksToAdd.clear();
		}
		filterLinks(GlobalVariables.filtering);
		updateColors();
		loading = false;
	}

	public void filterLinks(long minDistance) {
		synchronized (linkSyncLock) {
			BitSet removeIndex = new BitSet(links.size());
			for (int i = 0; i < links.size(); ++i) {
				if (!isOk(i, minDistance, removeIndex)) {
					removeIndex.set(i);
				}
			}
			ArrayList<GeneralLink> newLinks = new ArrayList<GeneralLink>(links.size() - removeIndex.cardinality());
			for (int i = 0; i < links.size(); ++i) {
				if (!removeIndex.get(i)) {
					newLinks.add(links.get(i));
				}
			}
			links = newLinks;
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

	public ArrayList<GeneralLink> getLinks() {
		return links;
	}

	public int numLinks() {
		return links.size();
	}

	public void tick(float dt, GeneCircle geneCircle) {
		timeUntilSync -= dt;
		if (timeUntilSync < 0) {
			timeUntilSync = GlobalVariables.linkSyncInterval;
			updateNewLinkPositions(geneCircle);
			syncAdditions();
		}
	}

	public void addToQueue(GeneralLink l) {
		addToQueue(l.getAChromosome(), l.getBChromosome(), l.getaStart(), l.getbStart());
	}

	private boolean isSorted() {
		for (int i = 1; i < links.size(); ++i) {
			if (links.get(i).compareTo(links.get(i - 1)) < 0) {
				return false;
			}
		}
		return true;
	}

	public void updateNewLinkPositions(GeneCircle geneCircle) {
		synchronized (linkSyncLock) {
			for (GeneralLink link : newLinksToAdd) {
				link.calculatePositions(geneCircle);
			}
		}
	}

	private boolean isOk(int i, long minDistance, BitSet removeIndex) {
		for (int j = i - 1; j > 0; --j) {
			if (removeIndex.get(j)) {
				continue;
			}
			if (startDistance(i, j) > minDistance) {
				links.get(j).addCounter(links.get(i).getCounter());
				break;
			}
			if (endDistance(i, j) < minDistance) {
				return false;
			}
		}
		for (int j = i + 1; j < links.size(); ++j) {
			if (removeIndex.get(j)) {
				continue;
			}
			if (startDistance(i, j) > minDistance) {
				links.get(j).addCounter(links.get(i).getCounter());
				break;
			}
			if (endDistance(i, j) < minDistance) {
				return false;
			}
		}
		return true;
	}

	private long startDistance(int i, int j) {
		GeneralLink a = links.get(i),
				b = links.get(j);
		ViewChromosome aChr = a.getStartChromosome(), bChr = b.getStartChromosome();
		if (aChr != bChr) {
			return Integer.MAX_VALUE;
		}

		long aPos = a.getStartPosition(), bPos = b.getStartPosition();
		return Math.abs(aPos - bPos);
	}

	private long endDistance(int i, int j) {
		GeneralLink a = links.get(i),
				b = links.get(j);
		ViewChromosome aChr = a.getEndChromosome(),
				bChr = b.getEndChromosome();
		if (aChr != bChr) {
			return Integer.MAX_VALUE;
		}

		long aPos = a.getEndPosition(),
				bPos = b.getEndPosition();
		return Math.abs(aPos - bPos);
	}
	
	private void updateColors() {
		// must be synced with linksynclock
		long avgCounter = 0;
		for(GeneralLink l : links)
			avgCounter += l.getCounter();
		avgCounter /= numLinks();
		
		for(GeneralLink l : links)
			l.setColorByCounter(avgCounter);
	}
}
