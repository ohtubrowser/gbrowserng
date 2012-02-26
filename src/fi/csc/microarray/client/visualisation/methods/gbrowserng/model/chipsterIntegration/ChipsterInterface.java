package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChipsterInterface {
	public static ConcurrentLinkedQueue<Long> getLengths(String karyotype, String seq, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, chromosomes).getLengths();
	}
//      
//          Not working yet due to problem with files and not going into ProcessAreaResult for some reason! Use randomgenerated data below        
//         public static ConcurrentLinkedQueue<long[]> getConnections(String firstFile, String secondFile) {
//            return new ConnectionsLoader(firstFile, secondFile).getConnections();
//        }
         
              public static ConcurrentLinkedQueue<long[]> getGeneratedtConnections() {
            return new ConnectionsLoader("default", "default").getConnections();
        }
              
        /** Returns queue of data about chromosome. 
	 * [0] is length of chromosome, 
	 * [1] is start of p-arm centromere
	 * [2] is end of p-arm centromere
	 * [3] is start of q-arm centromere
	 * [4] is end of q-arm centromere
	 * 
	 * @param karyotype	textfile of karyotype
	 * @param seq		textfile of seq_region
	 * @param chromosomes	all names of chrosomes as numbers or characters, e.g. "1" or "X"
	 * @return 
	 */
	public static ConcurrentLinkedQueue<long[]> getData(String karyotype, String seq, String[] chromosomes) {
		return new CytobandLoader(karyotype, seq, chromosomes).getData();
	}
	
}
