package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import java.util.ArrayList;

public class AbstractGenome {

	private static final ArrayList<Chromosome> abstractGenomeData = new ArrayList<Chromosome>();
	private static String name = "empty";

	public static void setName(String name) {
		AbstractGenome.name = name;
	}

	public static String getName() {
		return name;
	}

	public static long getTotalLength() {
		long ans = 0;
		for (Chromosome a : abstractGenomeData) {
			ans += a.isAnimating() ? a.length() * a.getAnimationProgress() :
					a.isMinimized() ? 0 : a.length();
		}
		return ans;
	}

	public static long getStartPoint(int id) {
		if (id == 0) {
			return 0;
		} else {
			return getEndPoint(id - 1);
		}
	}

	public static long getEndPoint(int id) {
		long ans = 0;
		for (int i = 0; i <= id; ++i) {
			ans += abstractGenomeData.get(i).length();
		}
		return ans;
	}

	public static void addChromosome(Chromosome chromosome) {
		abstractGenomeData.add(chromosome);
	}

	public static int getNumChromosomes() {
		return abstractGenomeData.size();
	}

	public static Chromosome getChromosomeByPosition(long position) {
		for (int i = 0; i < getNumChromosomes(); ++i) {
			if (getEndPoint(i) >= position) {
				return abstractGenomeData.get(i);
			}
		}
		return abstractGenomeData.get(0);
	}

	public static Chromosome getChromosome(int id) {
		return abstractGenomeData.get(id);
	}

	public static void clear() {
		abstractGenomeData.clear();
		name = "empty";
	}

	public static ArrayList<Chromosome> getChromosomes() {
		return abstractGenomeData;
	}
}
