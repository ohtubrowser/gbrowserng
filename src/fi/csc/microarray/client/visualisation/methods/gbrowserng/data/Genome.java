package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import java.util.ArrayList;

public class Genome {

	private ArrayList<ViewChromosome> abstractGenomeData = new ArrayList<ViewChromosome>();

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
