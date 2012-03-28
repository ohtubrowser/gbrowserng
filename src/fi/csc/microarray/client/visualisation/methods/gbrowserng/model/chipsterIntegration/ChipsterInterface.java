package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChipsterInterface {

	public static ConcurrentLinkedQueue<ViewChromosome> getChromosomes(String karyotype, String seq, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, chromosomes).getChromosomes();
	}
	public static ConcurrentLinkedQueue<GeneralLink> getConnections(ConcurrentLinkedQueue<ViewChromosome> chromosomes) {
		return new ConnectionsLoader("ohtu-between-chrs.bam", "ohtu-between-chrs.bam.bai", chromosomes).getLinks();
	}
	
}
