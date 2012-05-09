package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import java.util.ArrayList;

/**
 * Genome contains all chromosomes.
 * The Genome has a list of chromosomes, known as ViewChromosomes. The Genome's length is the total length of all chromosomes, length are of type Long. 
 * Chromosomes can be added and retrieved, but not deleted.
 */
public class Genome {

	private ArrayList<ViewChromosome> abstractGenomeData = new ArrayList<ViewChromosome>();

	/**
	 * Calculation of length of Genome. All chromosomes that are currently being animated, are added up.
	 * @return total length of Genome
	 */
	public long getTotalLength() {
		long ans = 0;
		for (ViewChromosome a : abstractGenomeData) {
			ans += a.isAnimating() ? a.length() * a.getAnimationProgress() :
					a.isMinimized() ? 0 : a.length();
		}
		return ans;
	}

	public long getStartPoint(int id) {
		if (id == 0) {
			return 0;
		} else {
			return getEndPoint(id - 1);
		}
	}

	public long getEndPoint(int id) {
		long ans = 0;
		for (int i = 0; i <= id; ++i) {
			ans += abstractGenomeData.get(i).length();
		}
		return ans;
	}

	public void addChromosome(ViewChromosome chromosome) {
		abstractGenomeData.add(chromosome);
	}

	public int getNumChromosomes() {
		return abstractGenomeData.size();
	}

	/**
	 * Returns a chromosome of the Genome, based on a point on the circle.
	 * @param position point on Genome
	 * @return the chromosome at the specified position
	 */
	public ViewChromosome getChromosomeByPosition(long position) {
		for (int i = 0; i < getNumChromosomes(); ++i) {
			if (getEndPoint(i) >= position) {
				return abstractGenomeData.get(i);
			}
		}
		return abstractGenomeData.get(0);
	}

	public ViewChromosome getChromosome(int id) {
		return abstractGenomeData.get(id);
	}

	public void clear() {
		abstractGenomeData.clear();
	}

	public ArrayList<ViewChromosome> getChromosomes() {
		return abstractGenomeData;
	}
}
