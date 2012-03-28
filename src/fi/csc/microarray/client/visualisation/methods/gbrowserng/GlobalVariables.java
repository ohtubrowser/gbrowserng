package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import com.soulaim.tech.gles.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Determines variables used throughout program.
 * Genome colors are initialized here in addition to static variables determined in code. 
 * Width and height are determined by screen size and set in GenomeBrowserNG upon running program
 * @author Kristiina Paloheimo
 */
public class GlobalVariables {
	public static float aspectRatio = 1.0f;
	public static final float animationConstant = 20f;
	public static final float chromosomeAnimationConstant = 0.4f;
	public static final Map<Character, Color> genomeColors = initializeGenomeColors();
	public static final float selectSize = 0.02f;
	public static int width;
	public static int height;
	public static final float linkSyncInterval = 3.0f;

	/**
	 * Initializes the color used for each of the four DNA nucleotides.
	 * A is Blue, G is Cyan, C is Orange and T Magenta
	 * @return color-character pairs for the four nucleotides A, C, T and G
	 */
	private static Map<Character, Color>initializeGenomeColors() {
		Map<Character, Color> m = new HashMap<Character, Color>();
		m.put('A', Color.BLUE); m.put('G', Color.CYAN); m.put('C', Color.ORANGE); m.put('T', Color.MAGENTA);
		return Collections.unmodifiableMap(m);
	}
		/**
	 * Returns colors used for nucleotides in trackview window.
	 * @return color-character pairs for the four nucleotides A, C, T and G
	 */	
	public Map<Character, Color> getGenomeColors() {
		return genomeColors;
	}
	
}
