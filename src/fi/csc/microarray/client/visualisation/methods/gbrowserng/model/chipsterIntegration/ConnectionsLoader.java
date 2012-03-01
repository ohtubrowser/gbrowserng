package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import java.io.File;
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
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.FsfStatus;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Region;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.RegionContent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsLoader implements AreaResultListener {

	public SAMHandlerThread dataThread;
	public Queue<AreaRequest> areaRequestQueue = new ConcurrentLinkedQueue<AreaRequest>();
	private AtomicInteger requestsReady;
	private ConcurrentLinkedQueue<Chromosome> chromosomes;
	private ConcurrentLinkedQueue<GeneralLink> links;

	public ConnectionsLoader(String bam, String bai, ConcurrentLinkedQueue<Chromosome> chromosomes) {
		this.chromosomes=chromosomes;
		this.links=new ConcurrentLinkedQueue<GeneralLink>();

		SAMDataSource file = null;
		this.requestsReady = new AtomicInteger(0);
		try {
			File bamfile = new File(bam), baifile = new File(bai);
			if(!bamfile.canRead()) {
				System.err.println("Cannot read BAM file " + bam + " !");
				System.exit(2);
			}
			if(!baifile.canRead()) {
				System.err.println("Cannot read BAI file " + bai + " !");
				System.exit(2);
			}
			file = new SAMDataSource(bamfile, baifile);
		} catch (Exception ex) {
			System.out.println(ex);
		}

		this.dataThread = new SAMHandlerThread(file, areaRequestQueue, this);
		this.dataThread.start();
		
		requestData();
		while (requestsReady.get() < 1052) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ConcurrentLinkedQueue<GeneralLink> getLinks() {
		return links;
	}

	public void requestData() {
		for (Chromosome c : this.chromosomes) {
			if(c.getName().equals("1")) {
				System.out.println("request");
			areaRequestQueue.add(new AreaRequest(
					new Region(0l, 270000000l, new fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome(c.getName())),
					new HashSet<ColumnType>(Arrays.asList(new ColumnType[]{
						ColumnType.ID, ColumnType.STRAND, ColumnType.MATE_POSITION})),
					new FsfStatus()));
			}
		}
		this.dataThread.notifyAreaRequestHandler();
	}

	@Override
	public void processAreaResult(AreaResult areaResult) {

		fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome readChr;
		fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome mateChr;

		for (RegionContent read : areaResult.getContents()) {
			
			readChr = read.region.start.chr;
			mateChr = ((BpCoord)read.values.get(ColumnType.MATE_POSITION)).chr;
			
			long matebp = ((BpCoord) read.values.get(ColumnType.MATE_POSITION)).bp;
			long readbp = read.region.start.bp;

			if (readChr.equals(mateChr)) {
				if (Math.abs(readbp - matebp) < 100000) {
					continue;
				}
			}
			Chromosome begin=null;
			Chromosome end=null;
			for(Chromosome c : chromosomes) {
				if(c.getName().equals(readChr.toNormalisedString())) {
					begin=c;
				}
				else if(c.getName().equals(mateChr.toNormalisedString())) {
					end=c;
				}
				if(begin!=null && end!=null) break;
			}
			//System.out.println("From: " + begin.getName() + " " + readbp + " - To: " + end.getName() + " " + matebp);
			this.links.add(new GeneralLink(begin, end, 0, readbp, 0, matebp));
		}
		requestsReady.addAndGet(1);
	}
}
