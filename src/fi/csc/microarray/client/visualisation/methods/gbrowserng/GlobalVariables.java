package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import gles.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariables {
	public static float aspectRatio = 1.0f;
	public static final float animationConstant = 20f;
	public static final Map<Character, Color> genomeColors = initializeGenomeColors();
	
	private static Map<Character, Color>initializeGenomeColors() {
		Map<Character, Color> m = new HashMap<Character, Color>();
		m.put('A', Color.BLUE); m.put('G', Color.CYAN); m.put('C', Color.ORANGE); m.put('T', Color.MAGENTA);
		return Collections.unmodifiableMap(m);
	}
}
