package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;

public class LinkRangeIterator {
	public final int startIndex, endIndex;
	public int currentIndex;
	private final LinkCollection collection;
	
	LinkRangeIterator(LinkCollection collection, int startIndex, int endIndex) {
		currentIndex = startIndex;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.collection = collection;
	}
	
	public GeneralLink value() {
		if(currentIndex == endIndex)
			return null;
		return collection.valueAt(currentIndex);
	}
	
	public void increment() {
		++currentIndex;
		currentIndex = Math.min(endIndex-1, currentIndex); // Maybe not necessary if we trust users of this iterator to be safe
	}
	
	public void decrement() {
		--currentIndex;
		currentIndex = Math.max(startIndex, currentIndex); // Maybe not necessary if we trust users of this iterator to be safe
	}

	public GeneralLink value(int i) {
		return collection.valueAt(i);
	}
}
