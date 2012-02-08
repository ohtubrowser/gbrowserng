package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;

public class GeneCircle {

	private float minimumChromosomeSlice;
	private float size;
	private AbstractChromosome chromosome = AbstractGenome.getChromosome(0);
	private long chromosomePosition = 0;
	private float[] chromosomeBoundaries;
	private Vector2[] chromosomeBoundariesPositions;

	public GeneCircle() {
		// 60% of the circle's circumference is divided evenly between chromosomes
		// Remaining 40% according to relative chromosome sizes
		minimumChromosomeSlice = 0.6f / AbstractGenome.getNumChromosomes();
		chromosomeBoundaries = new float[AbstractGenome.getNumChromosomes() + 1];
		chromosomeBoundariesPositions = new Vector2[AbstractGenome.getNumChromosomes()];
		float sliceSizeLeft = 1.0f - AbstractGenome.getNumChromosomes() * minimumChromosomeSlice;
		assert (sliceSizeLeft >= 0.0f);

		chromosomeBoundaries[0] = 1.0f; chromosomeBoundaries[AbstractGenome.getNumChromosomes()] = 0.0f;
		for(int i = 1; i < AbstractGenome.getNumChromosomes(); ++i) {
			chromosomeBoundaries[i] = chromosomeBoundaries[i-1] - (minimumChromosomeSlice + sliceSizeLeft * AbstractGenome.getChromosome(i - 1).length() / AbstractGenome.getTotalLength());
		}


	}

	public void updatePosition(float pointerGenePosition) {
		// TODO : clean
		pointerGenePosition -= 0.25f;
		if(pointerGenePosition < 0.0f)
			pointerGenePosition += 1.0f;
		for (int i = 1; i < chromosomeBoundaries.length; ++i) {
			if (chromosomeBoundaries[i] <= pointerGenePosition) {
				chromosome = AbstractGenome.getChromosome(i - 1);
				chromosomePosition = (long) (getChromosome().length() * (pointerGenePosition - chromosomeBoundaries[i - 1]) / (chromosomeBoundaries[i] - chromosomeBoundaries[i - 1]));
				break;
			}
		}
	}

	public AbstractChromosome getChromosome() {
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
		float ret = 0.25f + chromosomeBoundaries[chromosome] + (chromosomeBoundaries[chromosome + 1] - chromosomeBoundaries[chromosome]) * relativeChromosomePosition;
		return ret;
	}

	Vector2 getXYPosition(float relativeCirclePos) {
		Vector2 ret = new Vector2(size, 0.0f);
		ret.rotate((float)(Math.PI/2f) + 2 * (float) Math.PI * relativeCirclePos);
		return ret;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
		for (int i = 1; i <= AbstractGenome.getNumChromosomes(); ++i) {
			chromosomeBoundariesPositions[i - 1] = getXYPosition(chromosomeBoundaries[i - 1]);
		}

	}
}
