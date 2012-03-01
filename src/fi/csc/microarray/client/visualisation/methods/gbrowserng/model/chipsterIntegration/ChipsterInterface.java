package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChipsterInterface {

	public static ConcurrentLinkedQueue<Chromosome> getChromosomes(String karyotype, String seq, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, chromosomes).getChromosomes();
	}
//      
//          Not working yet due to problem with files and not going into ProcessAreaResult for some reason! Use randomgenerated data below        
//         public static ConcurrentLinkedQueue<long[]> getConnections(String firstFile, String secondFile) {
//            return new ConnectionsLoader(firstFile, secondFile).getConnections();
//        }

	public static ConcurrentLinkedQueue<GeneralLink> getConnections(ConcurrentLinkedQueue<Chromosome> chromosomes) {
		return new ConnectionsLoader("ohtu-between-chrs.bam", "ohtu-between-chrs.bam.bai", chromosomes).getLinks();
	}
	
}
