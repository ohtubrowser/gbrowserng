package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

public class GenoFPSCounter {

	int renderedFrames = 0;
	float fps = 0;
	float time = 0;

	public void tick(float dt) {
		++renderedFrames;
		time += dt;

		if(time > 2) {
			fps = renderedFrames / time;
			time = 0;
			renderedFrames = 0;
		}
	}

	public float getFps() {
		return fps;
	}

}
