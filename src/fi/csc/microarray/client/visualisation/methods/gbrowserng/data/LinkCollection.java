package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LinkCollection implements Runnable {

	/**
	 * Class for storing connections
	 * aims to provide fast access
	 * for range queries
	 */
	private ArrayList<GeneralLink> links = new ArrayList<GeneralLink>();
	private ConcurrentLinkedQueue<GeneralLink> newLinks = new ConcurrentLinkedQueue<GeneralLink>();
	private final Object linkSyncLock = new Object();
	public boolean loading = false;
	public GlobalVariables globals;
	private OverView overView;

	public LinkCollection(GlobalVariables globals) {
		this.globals = globals;
	}

	public void setOverview(OverView o) {
		overView = o;
	}

	// TODO : maybe need to account for invalidation of existing iterators once this happens
	public void syncAdditions() {
		updateNewLinkPositions();
		ArrayList<GeneralLink> temp = new ArrayList<GeneralLink>();
		temp.addAll(links);
		synchronized (linkSyncLock) {
			temp.addAll(newLinks);
			newLinks.clear();
		}
		Collections.sort(temp);

		if (globals.filtering != 0) {
			temp = filterLinks(globals.filtering, temp);
		}

		links = temp;
		updateColors();
	}

	public ArrayList<GeneralLink> filterLinks(long minDistance, ArrayList<GeneralLink> linkArray) {
		BitSet removeIndex = new BitSet(linkArray.size());
		for (int i = 0; i < linkArray.size(); ++i) {
			if (!isOk(linkArray, i, minDistance, removeIndex)) {
				removeIndex.set(i);
			}
		}
		ArrayList<GeneralLink> newLinks = new ArrayList<GeneralLink>(linkArray.size() - removeIndex.cardinality());
		for (int i = 0; i < linkArray.size(); ++i) {
			if (!removeIndex.get(i)) {
				newLinks.add(linkArray.get(i));
			}
		}
		return newLinks;
	}

	public void addToQueue(ViewChromosome aChromosome, ViewChromosome bChromosome, long aStart, long bStart) {
		synchronized (linkSyncLock) {
			GeneralLink a = new GeneralLink(aChromosome, bChromosome, aStart, bStart, true);
			GeneralLink b = new GeneralLink(aChromosome, bChromosome, aStart, bStart, false);
			newLinks.add(a);
			newLinks.add(b);
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

	public void updateNewLinkPositions() {
		for (GeneralLink link : newLinks) {
			link.calculatePositions(globals, overView.getGeneCircle());
		}
	}

	private boolean isOk(ArrayList<GeneralLink> linkArray, int i, long minDistance, BitSet removeIndex) {
		for (int j = i - 1; j > 0; --j) {
			if (removeIndex.get(j)) {
				continue;
			}
			if (startDistance(linkArray, i, j) > minDistance) {
				linkArray.get(j).addCounter(linkArray.get(i).getCounter());
				break;
			}
			if (endDistance(linkArray, i, j) < minDistance) {
				return false;
			}
		}
		for (int j = i + 1; j < linkArray.size(); ++j) {
			if (removeIndex.get(j)) {
				continue;
			}
			if (startDistance(linkArray, i, j) > minDistance) {
				linkArray.get(j).addCounter(linkArray.get(i).getCounter());
				break;
			}
			if (endDistance(linkArray, i, j) < minDistance) {
				return false;
			}
		}
		return true;
	}

	private long startDistance(ArrayList<GeneralLink> linkArray, int i, int j) {
		GeneralLink a = linkArray.get(i),
				b = linkArray.get(j);
		ViewChromosome aChr = a.getStartChromosome(), bChr = b.getStartChromosome();
		if (aChr != bChr) {
			return Integer.MAX_VALUE;
		}

		long aPos = a.getStartPosition(), bPos = b.getStartPosition();
		return Math.abs(aPos - bPos);
	}

	private long endDistance(ArrayList<GeneralLink> linkArray, int i, int j) {
		GeneralLink a = linkArray.get(i),
				b = linkArray.get(j);
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
		for (GeneralLink l : links) {
			avgCounter += l.getCounter();
		}
		avgCounter /= numLinks();

		for (GeneralLink l : links) {
			l.setColorByCounter(avgCounter);
		}
	}

	@Override
	public void run() {
		while (!overView.die) {
			try {
				Thread.sleep(GlobalVariables.linkSyncInterval);
			} catch (InterruptedException ex) {
				Logger.getLogger(LinkCollection.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (!newLinks.isEmpty()) {
				syncAdditions();
			}
		}
	}
}
