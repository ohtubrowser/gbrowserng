package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

public class Session {

	public ReferenceSequence referenceSequence;
	public float position = 0;

	public float targetZoomLevel = 0.05f;
	public float halfSizeX = 0.05f;
	public float halfSizeY = 0.05f;

	public float startY = -0.7f;
	public float payloadSize = 0.85f;

	public Session(ReferenceSequence referenceSequence, long chromosomePosition) {
		this.referenceSequence = referenceSequence;
		this.position = chromosomePosition;
	}
}

