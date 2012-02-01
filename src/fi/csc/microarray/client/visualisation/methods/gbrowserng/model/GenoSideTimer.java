package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

public class GenoSideTimer {
	long prevTime = 0;
	long startTime = 0;

	public float getDT() {
		long timeCurrent = System.nanoTime();
		float dt = (timeCurrent - prevTime) / 1e9f;
		prevTime = timeCurrent;
		return dt;
	}

	public void start() {
		this.startTime = System.nanoTime();
	}
	public long end() {
		return System.nanoTime() - this.startTime;
	}
}
