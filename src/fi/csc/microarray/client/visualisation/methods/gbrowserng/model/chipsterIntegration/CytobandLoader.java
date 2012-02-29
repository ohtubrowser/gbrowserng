package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowser.CytobandDataSource;
import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.AreaResultListener;
import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.Cytoband;
import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.CytobandHandlerThread;
import fi.csc.microarray.client.visualisation.methods.gbrowser.fileFormat.ColumnType;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CytobandLoader implements AreaResultListener {

	private String karyotypePath;
	private String seqPath;
	private Queue<AreaRequest> areaRequestQueue;
	private ConcurrentLinkedQueue<Chromosome> chrs;
	private CytobandHandlerThread dataThread;
	private AtomicInteger requestsReady;
	private AtomicInteger chromoId;
	private String[] chromosomes;

	public CytobandLoader(String k, String s, String[] chr) {
		this.karyotypePath = k;
		this.seqPath = s;
		this.areaRequestQueue = new ConcurrentLinkedQueue<AreaRequest>();
		this.requestsReady = new AtomicInteger(0);
		this.chromoId = new AtomicInteger(1);
		this.chromosomes = chr;
		this.chrs = new ConcurrentLinkedQueue<Chromosome>();
	}

	public ConcurrentLinkedQueue<Chromosome> getChromosomes() {
		CytobandDataSource file = null;
		try {
			file = new CytobandDataSource(new File(this.karyotypePath), new File(this.seqPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		this.dataThread = new CytobandHandlerThread(file, areaRequestQueue, this);
		this.dataThread.start();
		requestData(areaRequestQueue, chromosomes);

		while (requestsReady.get() != chromosomes.length) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return chrs;
	}
	

	private void requestData(Queue<AreaRequest> areaRequestQueue, String[] chromosomenames) {
		// TODO: Does chipster offer a more efficient way of doing this?
		for (String s : chromosomenames) {
			areaRequestQueue.add(new AreaRequest(
					new Region(0l, 270000000l, new fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome(s)),
					new HashSet<ColumnType>(Arrays.asList(new ColumnType[]{ColumnType.VALUE})),
					new FsfStatus()));
		}
		this.dataThread.notifyAreaRequestHandler();
	}

	@Override
	public void processAreaResult(AreaResult areaResult) {
		List<RegionContent> contents = areaResult.getContents();
		long chromosomeLength = 0;
		Long acenstart=null;
		Long acenend=null;

		String name=null;

		for (RegionContent r : contents) {
			Cytoband cband = (Cytoband) r.values.get(ColumnType.VALUE);
			if(name==null) name=new String(cband.getRegion().start.chr.toNormalisedString()); // Not sure about this
			if (cband.getStain() == Cytoband.Stain.ACEN) {
				if(acenstart==null) acenstart=r.region.start.bp;
				else if(acenend==null) acenend=r.region.end.bp;
			}
			chromosomeLength = r.region.end.bp;
		}
		// This id system is stupid.
		chrs.add(new Chromosome(chromoId.getAndAdd(1), name, chromosomeLength, (acenstart+acenend)/2));
		requestsReady.addAndGet(1);
	}

	
	public static void main(String[] args) {
		String karyotype = "karyotypeHuman.txt";
		String seq = "seq_regionHuman.txt";
		String[] chromosomes = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X"};
		CytobandLoader loader = new CytobandLoader(karyotype, seq, chromosomes);
		for(Chromosome c : loader.getChromosomes())
		{
			
			System.out.println(c.length());
		}
	}
	
}
