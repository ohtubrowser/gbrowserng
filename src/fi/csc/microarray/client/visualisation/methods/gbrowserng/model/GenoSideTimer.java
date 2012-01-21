package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

public class GenoSideTimer {
	long prevTime = 0;

	public float getDT() {
		long timeCurrent = System.nanoTime();
		float dt = (timeCurrent - prevTime) / 1000000000.0f;
		prevTime = timeCurrent;
		return Math.min(0.1f, dt);
	}
}
