package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;

public class GeneCircle {

	private final float chromosomeSeperatorSize = 0.004f;
	private final float minimizedChromosomeSize = 0.007f;
	private float minimumChromosomeSlice;
	private float size;
	private ViewChromosome chromosome = AbstractGenome.getChromosome(0);
	private long chromosomePosition = 0;
	private float[] chromosomeBoundaries;
	private Vector2[] chromosomeBoundariesPositions;
	public final Object tickdrawLock = new Object();
	public boolean animating = true;

	public GeneCircle() {
		tick(0f);
		animating = true;
	}

	public void tick(float dt) { // TODO: Most of this logic should be pushed to individual AbstractChromosome instances.
		float[] chromosomeBoundaries = new float[AbstractGenome.getNumChromosomes() + 1];
		// 60% of the circle's circumference is divided evenly between chromosomes
		// Remaining 40% according to relative chromosome sizes
		minimumChromosomeSlice = 0.6f / AbstractGenome.getNumChromosomes();
		float sliceSizeLeft = 1.0f;
		for (int i = 1; i <= AbstractGenome.getNumChromosomes(); ++i) {
			ViewChromosome chromosome = AbstractGenome.getChromosome(i - 1);
			chromosome.tick(dt);
			sliceSizeLeft -= chromosome.isAnimating() ? minimumChromosomeSlice*chromosome.getAnimationProgress() + minimizedChromosomeSize*(1f-chromosome.getAnimationProgress()) :
					chromosome.isMinimized() ? minimizedChromosomeSize : minimumChromosomeSlice;
		}
		assert (sliceSizeLeft >= 0.0f);

		chromosomeBoundaries[0] = 1.0f;
		chromosomeBoundaries[AbstractGenome.getNumChromosomes()] = 0.0f;
		animating = AbstractGenome.getChromosome(AbstractGenome.getNumChromosomes()-1).isAnimating();
		for(int i = 1; i < AbstractGenome.getNumChromosomes(); ++i) {
			ViewChromosome chromosome = AbstractGenome.getChromosome(i - 1);
			if (chromosome.isAnimating()) {
				float chromosomesize = (minimumChromosomeSlice + sliceSizeLeft * chromosome.length() / AbstractGenome.getTotalLength())*chromosome.getAnimationProgress();
				chromosomeBoundaries[i] = chromosomeBoundaries[i-1] - minimizedChromosomeSize*(1f-chromosome.getAnimationProgress()) - chromosomesize;
				animating = true;
			}
			else if (chromosome.isMinimized()) chromosomeBoundaries[i] = chromosomeBoundaries[i-1] - minimizedChromosomeSize;
			else chromosomeBoundaries[i] = chromosomeBoundaries[i-1] - (minimumChromosomeSlice + sliceSizeLeft * chromosome.length() / AbstractGenome.getTotalLength());
		}

		Vector2[] chromosomeBoundariesPositions = new Vector2[AbstractGenome.getNumChromosomes()];
		for (int i = 1; i <= AbstractGenome.getNumChromosomes(); ++i) {
			chromosomeBoundariesPositions[i - 1] = getXYPosition(chromosomeBoundaries[i - 1]);
		}

		this.chromosomeBoundaries = chromosomeBoundaries;
		this.chromosomeBoundariesPositions = chromosomeBoundariesPositions;
	}

	public void updatePosition(float pointerGenePosition) {
		// TODO : clean
		float relativePosition = pointerGenePosition-0.25f;
		if(relativePosition < 0.0f)
			relativePosition += 1.0f;
		chromosome = getChromosomeByRelativePosition(relativePosition);
		chromosomePosition = (long) (getChromosome().length() * (relativePosition - chromosomeBoundaries[chromosome.getChromosomeNumber() - 1])
				/ (chromosomeBoundaries[chromosome.getChromosomeNumber()] - chromosomeBoundaries[chromosome.getChromosomeNumber() - 1]));
	}

	public ViewChromosome getChromosomeByRelativePosition(float relativePosition) {
		for (int i = 1; i < chromosomeBoundaries.length; ++i) {
			if (chromosomeBoundaries[i] <= relativePosition) {
				return AbstractGenome.getChromosome(i - 1);
			}
		}
		return null;
	}

	public ViewChromosome getChromosome() {
		return chromosome;
	}

	public long getChromosomePosition() {
		return chromosomePosition;
	}

	public float[] getChromosomeBoundaries() {
		return chromosomeBoundaries;
	}

	public Vector2[] getChromosomeBoundariesPositions() {
		return chromosomeBoundariesPositions;
	}

	public float getRelativePosition(int chromosome, float relativeChromosomePosition) {
		float ret = 0.25f + chromosomeBoundaries[chromosome] - chromosomeSeperatorSize
				+ (chromosomeBoundaries[chromosome + 1] - chromosomeBoundaries[chromosome] + chromosomeSeperatorSize * 2) * relativeChromosomePosition;
		return ret;
	}

	public Vector2 getXYPosition(float relativeCirclePos) {
		Vector2 ret = new Vector2(size, 0.0f);
		ret.rotate((float)(Math.PI/2f) + 2 * (float) Math.PI * relativeCirclePos);
		return ret;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
		Vector2[] chromosomeBoundariesPositions = new Vector2[AbstractGenome.getNumChromosomes()];
		for (int i = 1; i <= AbstractGenome.getNumChromosomes(); ++i) {
			chromosomeBoundariesPositions[i - 1] = getXYPosition(chromosomeBoundaries[i - 1]);
		}
		this.chromosomeBoundariesPositions = chromosomeBoundariesPositions;

	}


}
