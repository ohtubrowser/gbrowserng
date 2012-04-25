package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChipsterInterface {

	public static ConcurrentLinkedQueue<ViewChromosome> getChromosomes(String karyotype, String seq, String coord, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, coord, chromosomes).getChromosomes();
	}
	public static LinkCollection getConnections(GlobalVariables globals, ConcurrentLinkedQueue<ViewChromosome> chromosomes, String bam, String bai) {
		return new ConnectionsLoader(globals, bam, bai, chromosomes).getLinks();
	}	
}
