package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import java.util.ArrayList;

public class TrackSession {

	private final ReferenceSequence referenceSequence;
	private final HeatMap heatMap;
	private ArrayList<Read> reads = new ArrayList<Read>();

	public TrackSession(ReferenceSequence sequence) {
		this.referenceSequence = sequence;
		for(int i=0; i<10; ++i) reads.add(GenomeGenerator.generateRead(this.referenceSequence));
		this.heatMap = new HeatMap(this.referenceSequence, this.reads);
	}

	public HeatMap getHeatMap() {
		return heatMap;
	}

	public ArrayList<Read> getReads() {
		return reads;
	}
}
