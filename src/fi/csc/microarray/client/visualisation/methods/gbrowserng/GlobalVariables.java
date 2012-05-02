package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;

/**
 * Determines variables used throughout program.
 * Genome colors are initialized here in addition to static variables determined in code. 
 * Width and height are determined by screen size and set in GenomeBrowserNG upon running program
 * @author Kristiina Paloheimo
 */
public class GlobalVariables {
	public static final float animationConstant = 20f;
	public static final float chromosomeAnimationConstant = 0.4f;
	public static final float selectSize = 0.01f;
	public static final long linkSyncInterval = 100;

	public final int SLOTS_PER_QUAD = 4;
	public final float SLOT_HEIGHT = 1.0f/SLOTS_PER_QUAD;

	public long filtering;
	public boolean debug;
	public float aspectRatio = 1.0f;
	public int width;
	public int height;
	public Genome genome;
}
