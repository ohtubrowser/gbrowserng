package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import java.util.ArrayList;

public class AbstractGenome {
    private static ArrayList<AbstractChromosome> abstractGenomeData = new ArrayList<AbstractChromosome>();
    private static String name = "empty";

    public static void setName(String name) {
        AbstractGenome.name = name;
    }

    public static String getName() {
        return name;
    }

    public static long getTotalLength() {
        long ans = 0;
        for(AbstractChromosome a : abstractGenomeData) {
            ans += a.length();
        }
        return ans;
    }

    public static long getEndPoint(int id) {
        long ans = 0;
        for(int i=0; i<=id; ++i) {
            ans += abstractGenomeData.get(i).length();
        }
        return ans;
    }

    public static void addChromosome(AbstractChromosome chromosome) {
        abstractGenomeData.add(chromosome);
    }

    public static int getNumChromosomes() {
        return abstractGenomeData.size();
    }

    public static AbstractChromosome getChromosome(int id) {
        return abstractGenomeData.get(id);
    }

    public static void clear() {
        abstractGenomeData.clear();
        name = "empty";
    }

}
