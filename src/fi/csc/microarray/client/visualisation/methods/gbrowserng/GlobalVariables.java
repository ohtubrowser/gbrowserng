package fi.csc.microarray.client.visualisation.methods.gbrowserng;

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
	public static final float selectSize = 0.01f;
	public static int width;
	public static int height;
	public static final float linkSyncInterval = 3.0f;
	public static boolean devmode = true;

}
