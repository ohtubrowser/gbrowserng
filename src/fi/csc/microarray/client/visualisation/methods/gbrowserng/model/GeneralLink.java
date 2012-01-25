package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import gles.Color;
import gles.SoulGL2;
import gles.renderer.PrimitiveRenderer;
import math.Vector2;

public class GeneralLink {

    private AbstractChromosome aChromosome, bChromosome;
    private long aStart, bStart, aEnd, bEnd;

    private float aCirclePos, bCirclePos;
    private Vector2 aXYPos, bXYPos;

    public GeneralLink(AbstractChromosome aChromosome, AbstractChromosome bChromosome, long aStart, long aEnd, long bStart, long bEnd) {
	this.aChromosome = aChromosome;	this.bChromosome = bChromosome;
	this.aStart = aStart; this.bStart = bStart;
	this.aStart = aEnd; this.bStart = bEnd;
    }

    public void calculatePositions(GeneCircle geneCircle) {
	aCirclePos = geneCircle.getRelativePosition(aChromosome.getChromosomeNumber(), (float)aStart/aChromosome.length());
	bCirclePos = geneCircle.getRelativePosition(bChromosome.getChromosomeNumber(), (float)bStart/bChromosome.length());
	aXYPos = geneCircle.getXYPosition(aCirclePos); bXYPos = geneCircle.getXYPosition(bCirclePos);
    }

    public void draw(SoulGL2 gl) {
	PrimitiveRenderer.drawLine(aXYPos.x, aXYPos.y, bXYPos.x, bXYPos.y, gl, Color.MAGENTA);
    }

}
