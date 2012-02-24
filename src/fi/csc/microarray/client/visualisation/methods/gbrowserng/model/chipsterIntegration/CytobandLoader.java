package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import fi.csc.microarray.client.visualisation.methods.gbrowser.CytobandDataSource;
import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.AreaResultListener;
import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.Cytoband;
import fi.csc.microarray.client.visualisation.methods.gbrowser.dataFetcher.CytobandHandlerThread;
import fi.csc.microarray.client.visualisation.methods.gbrowser.fileFormat.ColumnType;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CytobandLoader implements AreaResultListener {

	private String karyotypePath;
	private String seqPath;
	private Queue<AreaRequest> areaRequestQueue;
	private ConcurrentLinkedQueue<Long> lengths;
	private ConcurrentLinkedQueue<long[]> chrosomeData;
	private CytobandHandlerThread dataThread;
	private AtomicInteger requestsReady;
	private String[] chromosomes;

	public CytobandLoader(String k, String s, String[] chr) {
		this.karyotypePath = k;
		this.seqPath = s;
		this.areaRequestQueue = new ConcurrentLinkedQueue<AreaRequest>();
		this.lengths = new ConcurrentLinkedQueue<Long>();
		this.requestsReady = new AtomicInteger(0);
		this.chromosomes = chr;
		this.chrosomeData = new ConcurrentLinkedQueue<long[]>();
	}

	public ConcurrentLinkedQueue<Long> getLengths() {
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
		return lengths;
	}
	
	public ConcurrentLinkedQueue<long[]> getData() {
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
		return chrosomeData;
	}

	private void requestData(Queue<AreaRequest> areaRequestQueue, String[] chromosomenames) {
		// TODO: Does chipster offer a more efficient way of doing this?
		for (String s : chromosomenames) {
			areaRequestQueue.add(new AreaRequest(
					new Region(0l, 270000000l, new Chromosome(s)),
					new HashSet<ColumnType>(Arrays.asList(new ColumnType[]{ColumnType.VALUE})),
					new FsfStatus()));
		}
		this.dataThread.notifyAreaRequestHandler();
	}

	@Override
	public void processAreaResult(AreaResult areaResult) {

		List<RegionContent> contents = areaResult.getContents();
		long chromosomeLength = 0;
		long[] centromereData = new long[5];
		int counter = 1;
		
		for (RegionContent r : contents) {
			Cytoband cband = (Cytoband) r.values.get(ColumnType.VALUE);
			Cytoband.Stain stain = cband.getStain();
			r.region.start.chr.toString();
			if (Cytoband.Stain.ACEN == stain) {
				long start = r.region.start.bp;
				centromereData[counter] = start;
				counter++;
				long end = r.region.end.bp;
				centromereData[counter] = end;
				counter++;
			}	
			chromosomeLength = r.region.end.bp;
		}
		centromereData[0] = chromosomeLength;
		lengths.add(chromosomeLength);
		chrosomeData.add(centromereData);
		requestsReady.addAndGet(1);
	}
	
}
