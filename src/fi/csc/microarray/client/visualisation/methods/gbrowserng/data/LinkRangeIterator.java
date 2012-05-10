package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;

/**
 * Class to iterate over a range from a LinkCollection.
 */
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

	/**
	 * Rewind the iterator back to its startIndex.
	 */
	public void rewind() {
		currentIndex = startIndex;
	}
	
	/**
	 * 
	 * @return The value at the iterator's current position.
	 */
	public GeneralLink value() {
		if(currentIndex == endIndex)
			return null;
		return collection.valueAt(currentIndex);
	}
	
	/**
	 * Advance the iterator forward. Loops around at the end.
	 */
	public void increment() {
		currentIndex = clampIndex(currentIndex+1);
	}

	/**
	 * Advance the iterator backward. Loops around at the start.
	 */	
	public void decrement() {
		currentIndex = clampIndex(currentIndex-1);
	}

	/**
	 * 
	 * @param i
	 * @return The value at index i from the associated LinkCollection.
	 */
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

	/**
	 * 
	 * @param link
	 * @return True if the link is in the range this iterator represents.
	 */
	public boolean inRange(GeneralLink link) {
		int a = link.compareTo(collection.valueAt(startIndex));
		int b = link.compareTo(collection.valueAt(clampIndex(endIndex-1)));
		if(startIndex >= endIndex) {
			return a <= 0 || b > 0;
		}
		return a >= 0 && b < 0;
	}

	/**
	 * 
	 * @param index
	 * @return True if the index is in the range this iterator represents.
	 */
	public boolean inRange(int index) {
		if(startIndex >= endIndex)
			return index >= startIndex || index < endIndex;
		return index >= startIndex && index < endIndex;
	}
}
