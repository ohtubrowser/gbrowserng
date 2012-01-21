package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;

public class GeneCircle {
    
    private float minimumChromosomeSlice = 0.08f; // TODO should check that this is not too big
    
    private AbstractChromosome chromosome = AbstractGenome.getChromosome(0);
    private long chromosomePosition = 0;
    private float[] chromosomeBoundaries;
    private Vector2[] chromosomeBoundariesPositions;
    
    public GeneCircle(){
        chromosomeBoundaries = new float[AbstractGenome.getNumChromosomes()+1];
        chromosomeBoundariesPositions = new Vector2[AbstractGenome.getNumChromosomes()];
        float sliceSizeLeft = 1.0f - AbstractGenome.getNumChromosomes() * minimumChromosomeSlice;
        assert(sliceSizeLeft >= 0);
        
        chromosomeBoundaries[0] = 0;
        for(int i = 1; i <= AbstractGenome.getNumChromosomes(); ++i) {
            chromosomeBoundaries[i] = chromosomeBoundaries[i-1] + minimumChromosomeSlice + sliceSizeLeft * AbstractGenome.getChromosome(i-1).length()/AbstractGenome.getTotalLength();
        	chromosomeBoundariesPositions[i-1] = new Vector2((float)Math.cos(2*Math.PI*chromosomeBoundaries[i-1]), (float)Math.sin(2*Math.PI*chromosomeBoundaries[i-1]));
        }
        }
        
    public void updatePosition(float pointerGenePosition) {
        // TODO : fix this for something better than linear?
        for(int i=1; i<chromosomeBoundaries.length; ++i)
            if(chromosomeBoundaries[i] >= pointerGenePosition) {
                chromosome = AbstractGenome.getChromosome(i-1);
                chromosomePosition = (long) (getChromosome().length() * (pointerGenePosition - chromosomeBoundaries[i-1]) / (chromosomeBoundaries[i] - chromosomeBoundaries[i-1]));
                break;
            }        
    }

    public AbstractChromosome getChromosome() {
        return chromosome;
    }

    public long getChromosomePosition() {
        return chromosomePosition;
    }
    
    public float[] getChromosomeBoundaries() {
        return chromosomeBoundaries;
    }
    
    public Vector2[] getChromosomeBoundariesPositions() {
        return chromosomeBoundariesPositions;
    }

    public float getRelativePosition(int chromosome, float relativeChromosomePosition) {
        return chromosomeBoundaries[chromosome] + (chromosomeBoundaries[chromosome+1] - chromosomeBoundaries[chromosome]) * relativeChromosomePosition;
    }
 }
