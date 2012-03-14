package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import java.text.DecimalFormat;

public class GenoFPSCounter {

	int renderedFrames = 0;
	float fps = 0;
	float time = 0;
	float millisAvg = 0;
	float millisTemp = 0;
	long nanoPrev = 0;
	DecimalFormat format = new DecimalFormat("###0.###");

	public void tick(float dt) {
		++renderedFrames;
		time += dt;

		if(time > 2) {
			fps = renderedFrames / time;
			time = 0;
			renderedFrames = 0;
		}
	}

	public String getFps() {
		return format.format(fps);
	}

	public void addNano(long nano) {
		long now = System.nanoTime();
		if (now - nanoPrev >= 1e9) {
			this.millisAvg = this.millisTemp;
			this.millisTemp = nano/1e6f;
			nanoPrev = now;
		}
		else {
			this.millisTemp = ((nano/1e6f)+this.millisTemp)/2;
		}
	}

	public String getMillis() {
		return format.format(millisAvg);
	}

}
