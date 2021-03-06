package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.soulaim.tech.math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkRangeIterator;
import java.util.Collections;

/**
 * Class represents all Genome data, as sections of an arc with dividers between all chromosomes.
 * Data about Chromosomes on Circle can be retrieved in various ways. 
 * Class also provides information about where a Chromosome can be found on the circle. 
 */
public class GeneCircle {

	private final float chromosomeSeperatorSize = 0.004f;
	private final float minimizedChromosomeSize = 0.007f;
	private float minimumChromosomeSlice;
	private float size;
	private ViewChromosome chromosome;
	private long chromosomePosition = 0;
	private float[] chromosomeBoundaries;
	private Vector2[] chromosomeBoundariesPositions;
	public final Object tickdrawLock = new Object();
	public boolean animating = true;
	public Genome Genome;

	/**
	 * Creates a ne GeneCircle, retrieves 1st chromosome in circle and turns animation on.
	 * @param globals GlobalVariabels used throughout program
	 */
	public GeneCircle(GlobalVariables globals) {
		Genome = globals.genome;
		chromosome = globals.genome.getChromosome(0);
		tick(0f);
		animating = true;
	}

	public void tick(float dt) { // TODO: Most of this logic should be pushed to individual AbstractChromosome instances.
		float[] chromosomeBoundaries = new float[Genome.getNumChromosomes() + 1];
		// 60% of the circle's circumference is divided evenly between chromosomes
		// Remaining 40% according to relative chromosome sizes
		minimumChromosomeSlice = 0.6f / Genome.getNumChromosomes();
		float sliceSizeLeft = 1.0f;
		for (int i = 1; i <= Genome.getNumChromosomes(); ++i) {
			ViewChromosome chromosome = Genome.getChromosome(i - 1);
			chromosome.tick(dt);
			sliceSizeLeft -= chromosome.isAnimating() ? minimumChromosomeSlice * chromosome.getAnimationProgress() + minimizedChromosomeSize * (1f - chromosome.getAnimationProgress())
					: chromosome.isMinimized() ? minimizedChromosomeSize : minimumChromosomeSlice;
		}
		assert (sliceSizeLeft >= 0.0f);

		chromosomeBoundaries[0] = 1.0f;
		chromosomeBoundaries[Genome.getNumChromosomes()] = 0.0f;
		animating = Genome.getChromosome(Genome.getNumChromosomes() - 1).isAnimating();
		for (int i = 1; i < Genome.getNumChromosomes(); ++i) {
			ViewChromosome chromosome = Genome.getChromosome(i - 1);
			if (chromosome.isAnimating()) {
				float chromosomesize = (minimumChromosomeSlice + sliceSizeLeft * chromosome.length() / Genome.getTotalLength()) * chromosome.getAnimationProgress();
				chromosomeBoundaries[i] = chromosomeBoundaries[i - 1] - minimizedChromosomeSize * (1f - chromosome.getAnimationProgress()) - chromosomesize;
				animating = true;
			} else if (chromosome.isMinimized()) {
				chromosomeBoundaries[i] = chromosomeBoundaries[i - 1] - minimizedChromosomeSize;
			} else {
				chromosomeBoundaries[i] = chromosomeBoundaries[i - 1] - (minimumChromosomeSlice + sliceSizeLeft * chromosome.length() / Genome.getTotalLength());
			}
		}

		Vector2[] chromosomeBoundariesPositions = new Vector2[Genome.getNumChromosomes()];
		for (int i = 1; i <= Genome.getNumChromosomes(); ++i) {
			chromosomeBoundariesPositions[i - 1] = getXYPosition(chromosomeBoundaries[i - 1]);
		}

		this.chromosomeBoundaries = chromosomeBoundaries;
		this.chromosomeBoundariesPositions = chromosomeBoundariesPositions;
	}

	public void updatePosition(float pointerGenePosition) {
		float relativePosition = pointerGenePosition - 0.25f; // Rotate 90 degrees counter-clockwise
		if (relativePosition < 0.0f) {
			relativePosition += 1.0f; // If negative add one full turn
		}
		chromosome = getChromosomeByRelativePosition(relativePosition);
		chromosomePosition = getPositionInChr(chromosome, relativePosition);
	}

	/**
	 * Returns the position on the chromosome at the specified location on the circle
	 * @param chr the chromosome desired
	 * @param relativePosition the relative position on the Circle
	 * @return position on chromosome
	 */
	public long getPositionInChr(ViewChromosome chr, float relativePosition) {
		long length = chr.length();
		float thisChromoBound = chromosomeBoundaries[chr.getChromosomeNumber()];
		float prevChromoBound = chromosomeBoundaries[chr.getChromosomeNumber() - 1];
		float relPosInChromosome = (prevChromoBound - relativePosition - chromosomeSeperatorSize);
		// Clamps to 1*10^-9 as minimum, because minimized chromosomes are shorter than 2 * chromosomeSeperatorSize
		float relChromoLength = Math.max(1e-9f, prevChromoBound - thisChromoBound - 2 * chromosomeSeperatorSize);
		// Clamps values between 0 and chromosome's length
		return Math.max(0l, Math.min(length, (long) (length * (relPosInChromosome / relChromoLength))));
	}

	/**
	 * Returns the chromosome found at the specified location on the circle.
	 * @param relativePosition on the circle
	 * @return desired chromosome
	 */
	public ViewChromosome getChromosomeByRelativePosition(float relativePosition) {
		for (int i = 1; i < chromosomeBoundaries.length; ++i) {
			if (chromosomeBoundaries[i] <= relativePosition) {
				return Genome.getChromosome(i - 1);
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

	public float getRelativePosition(int chromosome, long chrPosition) {
		float rel = ((float)chrPosition) / Genome.getChromosome(chromosome).length();
		return getRelativePosition(chromosome, rel);
	}

	public Vector2 getXYPosition(float relativeCirclePos) {
		Vector2 ret = new Vector2(size, 0.0f);
		ret.rotate((float) (Math.PI / 2f) + 2 * (float) Math.PI * relativeCirclePos);
		return ret;
	}

	public float getSize() {
		return size;
	}
	
	public float getMinimumChromosomeSlice() {
		return minimumChromosomeSlice;
	}

	/**
	 * Sets the size of the Circle, adjusts the chromsome boundaries accordingly.
	 * @param size
	 */
	public void setSize(float size) {
		this.size = size;
		Vector2[] chromosomeBoundariesPositions = new Vector2[Genome.getNumChromosomes()];
		for (int i = 1; i <= Genome.getNumChromosomes(); ++i) {
			chromosomeBoundariesPositions[i - 1] = getXYPosition(chromosomeBoundaries[i - 1]);
		}
		this.chromosomeBoundariesPositions = chromosomeBoundariesPositions;

	}

	public long getChromosomePosition(ViewChromosome startChr, float relativePos) {
		long ret = startChr.length();
		int chrNum = startChr.getChromosomeNumber() - 1;
		float chrRelLength = chromosomeBoundaries[chrNum] - chromosomeBoundaries[chrNum + 1];
		ret = (long) (ret * (chromosomeBoundaries[chrNum] - relativePos) / chrRelLength);

		return ret;
	}

	/**
	 * Creates a new LinkRangeIterator, based on information passed to method as parameters
	 * @param relativeStart point on circle
	 * @param relativeEnd point on circle
	 * @param links links to be included in iterator
	 * @return new LinkRangeIterator created with desired data
	 */
	public LinkRangeIterator getRangeIterator(float relativeStart, float relativeEnd, LinkCollection links) {
		ViewChromosome a = getChromosomeByRelativePosition(relativeStart);
		ViewChromosome b = getChromosomeByRelativePosition(relativeEnd);
		long posA = getPositionInChr(a, relativeStart);
		long posB = getPositionInChr(b, relativeEnd);

		GeneralLink tempA = GeneralLink.createComparisonObject(a, b, posA, posB, true);
		GeneralLink tempB = GeneralLink.createComparisonObject(a, b, posA, posB, false);

		int startIndex = Collections.binarySearch(links.getLinks(), tempA);
		if (startIndex < 0) {
			startIndex = -(startIndex + 1);
		}
		int endIndex = Collections.binarySearch(links.getLinks(), tempB);
		if (endIndex < 0) {
			endIndex = -(endIndex + 1);
		}

		startIndex = Math.min(links.numLinks() - 1, startIndex);
		endIndex = Math.min(links.numLinks(), endIndex);

		return new LinkRangeIterator(links, startIndex, endIndex);
	}
}
