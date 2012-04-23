package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChipsterInterface {

	public static ConcurrentLinkedQueue<ViewChromosome> getChromosomes(String karyotype, String seq, String coord, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, coord, chromosomes).getChromosomes();
	}
	public static LinkCollection getConnections(ConcurrentLinkedQueue<ViewChromosome> chromosomes, String bam, String bai) {
		return new ConnectionsLoader(bam, bai, chromosomes).getLinks();
	}	
}
