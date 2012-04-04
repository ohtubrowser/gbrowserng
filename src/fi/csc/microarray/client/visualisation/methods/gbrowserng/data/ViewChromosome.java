package fi.csc.microarray.client.visualisation.methods.gbrowserng.data;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;

public class ViewChromosome {

    private float relativeSize = 1f;
    private final long size;
    private final int id;
    private boolean minimized = false;
    private boolean animating = false;
    private float animationProgress = 1f;
    private String name;
    private float centromerePosition; // Relative position of centromere

    public ViewChromosome(int id, long size) {
        this.id = id;
        this.size = size;
        this.centromerePosition = -1;
        this.name = "NULL";
    }

    public ViewChromosome(int id, String name, long size) {
        this.id = id;
        this.size = size;
        this.centromerePosition = 0.5f;
        this.name = name;
    }

    public ViewChromosome(int id, String name, long size, long centromereposition) {
        this.id = id;
        this.size = size;
        setCentromerePosition(centromereposition);
        this.name = name;
    }

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

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
        animating = true;
        //animationProgress = minimized ? 1f : 0f;
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
