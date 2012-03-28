package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;

public class LinkRangeIterator {
	public final int startIndex, endIndex;
	public int currentIndex;
	private final LinkCollection collection;
	
	public LinkRangeIterator(LinkRangeIterator linkRangeIterator) {
		this.currentIndex = linkRangeIterator.currentIndex;
		this.startIndex = linkRangeIterator.startIndex;
		this.endIndex = linkRangeIterator.endIndex;
		this.collection = linkRangeIterator.collection;
	}
	
	public LinkRangeIterator(LinkCollection collection, int startIndex, int endIndex) {
		currentIndex = startIndex;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.collection = collection;
	}

	public void rewind() {
		currentIndex = startIndex;
	}
	
	public GeneralLink value() {
		if(currentIndex == endIndex)
			return null;
		return collection.valueAt(currentIndex);
	}
	
	public void increment() {
		currentIndex = clampIndex(currentIndex+1);
	}
	
	public void decrement() {
		currentIndex = clampIndex(currentIndex-1);
	}

	public GeneralLink value(int i) {
		return collection.valueAt(i);
	}
	
	private int clampIndex(int i) {
		int n = collection.numLinks();
		if(i >= n && endIndex != n)
			i -= n;
		if(i < 0)
			i += n;
		return i;
	}

	public boolean inRange(GeneralLink link) {
		int a = link.compareTo(collection.valueAt(startIndex));
		int b = link.compareTo(collection.valueAt(clampIndex(endIndex-1)));
		if(startIndex >= endIndex) {
			return a <= 0 || b > 0;
		}
		return a >= 0 && b < 0;
	}

	public boolean inRange(int index) {
		if(startIndex >= endIndex)
			return index >= startIndex || index < endIndex;
		return index >= startIndex && index < endIndex;
	}
}
