package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

/**
 *
 * @author Paloheimo
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.AreaResultListener;
import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.SAMHandlerThread;
import fi.csc.microarray.client.visualisation.methods.gbrowser.fileFormat.ColumnType;
import fi.csc.microarray.client.visualisation.methods.gbrowser.SAMDataSource;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.AreaRequest;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.AreaResult;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.BpCoord;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.FsfStatus;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Region;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.RegionContent;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsLoader implements AreaResultListener {

    public SAMHandlerThread dataThread;
    public Queue<AreaRequest> areaRequestQueue = new ConcurrentLinkedQueue<AreaRequest>();
    private SortedSet<RegionContent> reads = new TreeSet<RegionContent>();
    private static final int WIN_WIDTH = 1280;
    private ConcurrentLinkedQueue<long[]> queue; 
    private Long[] chrLengths;
    private AtomicInteger requestsReady;
    
    public ConnectionsLoader(String firstFile, String secondFile) {

        queue = new ConcurrentLinkedQueue<long[]>();
        //Init Chipster data layer
        SAMDataSource file = null;
        this.requestsReady = new AtomicInteger(0);
        
//        try {
//
//             Adjust these paths to point to the demo data   
//            File bam = new File(firstFile);
//            File bai = new File(secondFile);
////                
////          File bam = new File("ohtu-between-chrs.bam");
////          File bai = new File("ohtu-between-chrs.bam.bai");
//            
//            System.out.println("Bam: " + bam.getAbsolutePath());
//            System.out.println("Bai: " + bai.getAbsolutePath());
//            bam = new File(bam.getAbsolutePath());
//            bai = new File(bam.getAbsolutePath());
//                     
//            file = new SAMDataSource(bam, bai);
//      
//        } catch (FileNotFoundException e) {
//            System.out.println(e.toString());
//            e.printStackTrace();
//        } catch (RuntimeException re) {
//            System.out.println(re.toString());
//        } catch (Exception ex) {
//            System.out.println(ex.toString());
//        }
//
//        this.dataThread = new SAMHandlerThread(file, areaRequestQueue, this);
//  
//        this.dataThread.start();
//
//        requestData();
//        while(requestsReady.get()!=21) {
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

      generateRandomContent();
    }

    public void requestData() {

        /*
         * This is the best place for limiting the amount of data. If all
         * connections make this too slow, request only data of smaller areas.
         */

        areaRequestQueue.add(new AreaRequest(
                new Region(0l, 270000000l, new Chromosome("1")),
                new HashSet<ColumnType>(Arrays.asList(new ColumnType[]{
                    ColumnType.ID, ColumnType.STRAND, ColumnType.MATE_POSITION})),
                new FsfStatus()));

        areaRequestQueue.add(new AreaRequest(
                new Region(0l, 270000000l, new Chromosome("2")),
                new HashSet<ColumnType>(Arrays.asList(new ColumnType[]{
                    ColumnType.ID, ColumnType.STRAND, ColumnType.MATE_POSITION})),
                new FsfStatus()));

        this.dataThread.notifyAreaRequestHandler();
    }

    public int bpToDisplay(long bp) {
        return (int) (bp / 270000000f * WIN_WIDTH);
    }

    public static void main(String arg[]) {

//        String firstFile = "C:/Users/Mammutti/Desktop/ohtu-between-chrs.bam";
//        String secondFile = "C:/Users/Mammutti/Desktop/ohtu-between-chrs.bam.bai";
    
                String firstFile = "C:/Users/Mammutti/Desktop/ohtu-within-chr.bam";
        String secondFile = "C:/Users/Mammutti/Desktop/ohtu-within-chr.bam.bai";
//        
        ConnectionsLoader loader = new ConnectionsLoader(
                firstFile, secondFile);
        Queue queue = loader.getConnections();
        
    }
    
    
    
    public ConcurrentLinkedQueue<long[]> getConnections() { 
        
        return queue;
        
    }
    
    
    @Override
    public void processAreaResult(AreaResult areaResult) {
        Chromosome readChr = null;
        Chromosome mateChr = null;

        Chromosome upperChr = new Chromosome("1");
        Chromosome lowerChr = new Chromosome("20");

        long[] values = new long[4];

        for (RegionContent read : areaResult.getContents()) {
            
            readChr = read.region.start.chr;
            values[0] = Long.parseLong(readChr.toString());
            
            String readStart = read.region.start.toString();
            
            values[1] = Long.parseLong(readStart);
            
            if (!upperChr.equals(readChr) && !lowerChr.equals(readChr)) {
                //Make sure that there aren't any data from other chromosomes
                continue;
            }

            mateChr = ((BpCoord) read.values.get(ColumnType.MATE_POSITION)).chr;
            values[2] = Long.parseLong(mateChr.toString());
            String mateStart = ((BpCoord) read.values.get(ColumnType.MATE_POSITION)).toString();
            values[3] = Long.parseLong(mateStart.toString());
            
            if (readChr.equals(mateChr)) {

                /*
                 * Make data smaller by filtering out pairs that are relatively
                 * close to each other. Probably this shouldn't be done in a
                 * real study, at least not this heavily. However, this
                 * filtering might be useful in early phases of visualisation
                 * development to limit the size of the data.
                 */
                if (Math.abs(
                        read.region.start.bp - ((BpCoord) read.values.get(ColumnType.MATE_POSITION)).bp) < 100000) {
                    continue;

                }
            }



            /*
             * The connections of read pairs aren't stored in the data
             * separately, but both ends know the location of other pair:
             *
             * (BpCoord)read.values.get(ColumnType.MATE_POSITION)
             *
             * If there are more than one read in that location, the correct one
             * can be found by comparing the identifiers of the reads, pairs
             * have identical identifiers:
             *
             * (String) read.values.get(ColumnType.ID)
             *
             */

            queue.add(values);
        }
        System.out.println(values[0] + values[1] + values[2] + values[3]);
        
    }


    
    public void generateRandomContent() {
        ConcurrentLinkedQueue lengths = ChipsterInterface.getLengths("karyotype.txt", "seq_region.txt", new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
            
            "13", "14", "15", "16", "17", "18", "19", "20", "X"});
      
        int i = 1;
        chrLengths = new Long[22];
        for (Object contents : lengths) {          
            Long length = (Long) contents;
            this.chrLengths[i] = length;
            i++;
        }
        
        for (int j = 0; j < 1; j++) {
            int readChr = (int)  (Math.random() * 21) + 1;
            int mateChr = (int)  (Math.random() * 21) + 1;
            
           
            int beginning = getLocation(readChr);
            int end = getLocation(mateChr);
            
            long[] table = {readChr, mateChr, beginning, end, this.chrLengths[readChr], this.chrLengths[mateChr]};
            queue.add(table);
//            System.out.println(readChr + " " + mateChr + " " + beginning + " " + end);
        }
        
    }
    
    private int getLocation(int chr) {
        int location = 0;
        Double length = (double) this.chrLengths[chr];
        location = (int) (Math.random() * length);
        
        return location;
    }
}
