package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChipsterInterface {
	public static ConcurrentLinkedQueue<Long> getLengths(String karyotype, String seq, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, chromosomes).getLengths();
	}
}
