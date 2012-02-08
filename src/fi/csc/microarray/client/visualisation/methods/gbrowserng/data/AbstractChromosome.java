
package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

public class AbstractChromosome {

	private final long size;
	private final int id;
	private final ReferenceSequence sequence;

	public AbstractChromosome(int id, long size) {
		this.id = id;
		this.size = size;
		sequence = new ReferenceSequence(id, (int) 300);
	}

	public int getChromosomeNumber() {
		return id;
	}

	public long length() {
		return size;
	}

	ReferenceSequence getSequence() {
		return sequence;
	}

	public ReferenceSequence getReferenceSequence() {
		return sequence;
	}
}
