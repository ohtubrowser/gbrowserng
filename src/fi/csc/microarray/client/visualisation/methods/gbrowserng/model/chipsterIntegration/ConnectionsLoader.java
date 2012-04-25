package fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
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
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsLoader implements AreaResultListener {

	public SAMHandlerThread dataThread;
	public Queue<AreaRequest> areaRequestQueue = new ConcurrentLinkedQueue<AreaRequest>();
	private ConcurrentLinkedQueue<ViewChromosome> chromosomes;
	private LinkCollection links;
	public GlobalVariables globals;

	public ConnectionsLoader(GlobalVariables globals, String bam, String bai, ConcurrentLinkedQueue<ViewChromosome> chromosomes) {
		links = new LinkCollection(globals);
		this.globals = globals;
		this.chromosomes = chromosomes;

		SAMDataSource file = null;
		try {
			File bamfile = new File(bam), baifile = new File(bai);
			if (!bamfile.canRead()) {
				System.err.println("Cannot read BAM file " + bam + " !");
				System.exit(2);
			}
			if (!baifile.canRead()) {
				System.err.println("Cannot read BAI file " + bai + " !");
				System.exit(2);
			}
			file = new SAMDataSource(bamfile.toURI().toURL(), baifile.toURI().toURL());
		} catch (Exception ex) {
			System.out.println(ex);
		}

		this.dataThread = new SAMHandlerThread(file, areaRequestQueue, this);
		this.dataThread.start();

		requestData();
	}

	public LinkCollection getLinks() {
		return links;
	}

	public void requestData() {
		for (ViewChromosome c : this.chromosomes) {
			if(globals.debug && c.getName().compareTo("15") != 0) continue;
			areaRequestQueue.add(new AreaRequest(
					new Region(0l, 270000000l, new fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome(c.getName())),
					new HashSet<ColumnType>(Arrays.asList(new ColumnType[]{
						ColumnType.ID, ColumnType.STRAND, ColumnType.MATE_POSITION})),
					new FsfStatus()));
		}
		this.dataThread.notifyAreaRequestHandler();
	}

	@Override
	public void processAreaResult(AreaResult areaResult) {

		Chromosome readChr, mateChr;

		for (RegionContent read : areaResult.getContents()) {

			readChr = read.region.start.chr;
			mateChr = ((BpCoord) read.values.get(ColumnType.MATE_POSITION)).chr;

			long matebp = ((BpCoord) read.values.get(ColumnType.MATE_POSITION)).bp;
			long readbp = read.region.start.bp;

			if (readChr.equals(mateChr)) {
				if (Math.abs(readbp - matebp) < 100000) {
					continue;
				}
			}
			ViewChromosome begin = null;
			ViewChromosome end = null;
			for (ViewChromosome c : chromosomes) {
				if (c.getName().equals(readChr.toNormalisedString())) {
					begin = c;
				} else if (c.getName().equals(mateChr.toNormalisedString())) {
					end = c;
				}
				if (begin != null && end != null) {
					break;
				}
			}
			this.links.addToQueue(begin, end, readbp, matebp);
		}
	}
}
