package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;

/**
 * Class represents a chromosome, which is part of the Genome.
 * Each ViewChromsome has a length, name, id and centromerePosition. This information is retrieved from data provided to the application. 
 * Information about class that changes during execution of program is animation-status and whether chromosome is open or closed in user window.
 */
public class ViewChromosome {

    private final long size;
    private final int id;
    private boolean minimized = false;
    private boolean animating = false;
    private float animationProgress = 1f;
    private String name;
    private float centromerePosition; // Relative position of centromere

	/**
	 * Creation of chromosme, with number and length data.
	 * @param id number of chromosome
	 * @param size length of chromosome
	 */
	public ViewChromosome(int id, long size) {
        this.id = id;
        this.size = size;
        this.centromerePosition = -1;
        this.name = "NULL";
    }

	/**
	 * Creation of chromosome, with number, name and length data.
	 * @param id number of chromosome
	 * @param name name of chromosome
	 * @param size length of chromosome
	 */
	public ViewChromosome(int id, String name, long size) {
        this.id = id;
        this.size = size;
        this.centromerePosition = 0.5f;
        this.name = name;
    }

	/**
	 * Creation of chromosome, with number, name, legnth and centromere position data.
	 * @param id number of chromosome
	 * @param name name of chromosome
	 * @param size length of chromosome
	 * @param centromereposition location of centromere on chromosome
	 */
	public ViewChromosome(int id, String name, long size, long centromereposition) {
        this.id = id;
        this.size = size;
        setCentromerePosition(centromereposition);
        this.name = name;
    }

	/**
	 * Sets the position of the centromere
	 * @param pos location on chromosome
	 */
	public void setCentromerePosition(long pos) {
        this.centromerePosition = (float) ((double) pos / size);
    }

    public String getName() {
        return this.name;
    }

    public float getCentromereRelativePosition() {
        return this.centromerePosition;
    }

    public int getChromosomeNumber() {
        return id;
    }

    public long length() {
        return size;
    }

    public boolean isMinimized() {
        return minimized;
    }

	/**
	 * Sets the chromosome to be shown or not shown in user view. 
	 * @param minimized true for not shown, false for shown
	 */
	public void setMinimized(boolean minimized) {
        this.minimized = minimized;
        animating = true;
    }

    public boolean isAnimating() {
        return animating;
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void tick(float dt) {
        if (minimized) {
            animationProgress = Math.max(0f, animationProgress - dt / GlobalVariables.chromosomeAnimationConstant);
            if (animationProgress - 0.01f < 0) {
                animating = false;
            }
        } else {
            animationProgress = Math.min(1f, animationProgress + dt / GlobalVariables.chromosomeAnimationConstant);
            if (animationProgress + 0.01f > 1) {
                animating = false;
            }
        }
    }
}
