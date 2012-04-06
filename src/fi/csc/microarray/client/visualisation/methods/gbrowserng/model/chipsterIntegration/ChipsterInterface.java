package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChipsterInterface {

	public static ConcurrentLinkedQueue<ViewChromosome> getChromosomes(String karyotype, String seq, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, chromosomes).getChromosomes();
	}
	public static LinkCollection getConnections(ConcurrentLinkedQueue<ViewChromosome> chromosomes) {
		return new ConnectionsLoader("ohtu-between-chrs.bam", "ohtu-between-chrs.bam.bai", chromosomes).getLinks();
	}
	public static LinkCollection getConnections(ConcurrentLinkedQueue<ViewChromosome> chromosomes, String bam, String bai) {
		return new ConnectionsLoader(bam, bai, chromosomes).getLinks();
	}	
}
