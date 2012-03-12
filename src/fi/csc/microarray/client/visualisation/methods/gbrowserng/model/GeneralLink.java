package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import javax.media.opengl.GL2;

public class GeneralLink {

	private Chromosome aChromosome, bChromosome;
	private long aStart, bStart, aEnd, bEnd;
	//private float r = Math.max(0.5f, (float) Math.random()), g = (float) Math.random(), b = (float) Math.random();
	private float r = 1.0f, g = 0.0f, b = 0.0f;
	private final int drawMethod;
	float aCirclePos, bCirclePos;
	private Vector2 aXYPos, bXYPos;
	private float opacity;

    public GeneralLink(long readChr, long mateChr, long aStart, long aEnd, long bStart, long bEnd, long readChrLength, long mateChrLength) {
                this.aChromosome = new Chromosome((int) readChr, readChrLength);
                this.bChromosome = new Chromosome((int) mateChr, mateChrLength);
		this.aStart = aStart;
		this.bStart = bStart;
		this.aStart = aEnd;
		this.bStart = bEnd;
		this.opacity = 1.0f;
		drawMethod = GL2.GL_TRIANGLE_STRIP;
    }

	public void fadeIn(float fadespeed) {
		this.opacity += fadespeed;
		if (this.opacity > 1.0f) {
			this.opacity = 1.0f;
		}
	}

	public void fadeDim(float fadespeed) {
		this.opacity -= fadespeed;
		if (this.opacity < 0.2f) {
			this.opacity = 0.2f;
		}
	}

	public void fadeOut(float fadespeed) {
		this.opacity -= fadespeed;
		if (this.opacity < 0.0f) {
			this.opacity = 0.0f;
		}
	}

	public float getStartPos() {
		return aCirclePos;
	}

	public float getEndPos() {
		return bCirclePos;
	}

	public GeneralLink(Chromosome aChromosome, Chromosome bChromosome, long aStart, long aEnd, long bStart, long bEnd) {
		this.aChromosome = aChromosome;
		this.bChromosome = bChromosome;
		this.aStart = aStart;
		this.bStart = bStart;
		this.aStart = aEnd;
		this.bStart = bEnd;
		this.opacity = 1.0f;
		drawMethod = GL2.GL_TRIANGLE_STRIP;
	}

	public void calculatePositions(GeneCircle geneCircle) {
		aCirclePos = -0.25f + geneCircle.getRelativePosition(aChromosome.getChromosomeNumber() - 1, (float) aStart / aChromosome.length()); // Need -1 because of AbstractChromosome indexing
		bCirclePos = -0.25f + geneCircle.getRelativePosition(bChromosome.getChromosomeNumber() - 1, (float) bStart / bChromosome.length());
		aXYPos = geneCircle.getXYPosition(aCirclePos);
		// This magic constant is the same as in the circleseparators.
		aXYPos.x *= 0.9495;
		aXYPos.y *= 0.9495;
		bXYPos = geneCircle.getXYPosition(bCirclePos);
		bXYPos.x *= 0.9495;
		bXYPos.y *= 0.9495;
	}
	
	public static void beginDrawing(GL2 gl, float zoomLevel) {
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.BEZIER);

		shader.start(gl);

		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint2", 0.0f, 0.0f);
		ShaderMemory.setUniformVec1(gl, shader, "width", 0.005f * zoomLevel);
		ShaderMemory.setUniformVec1(gl, shader, "tstep", OpenGLBuffers.bezierStep);
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", identityMatrix);

		gl.glDisable(gl.GL_CULL_FACE); // TODO : maybe just change the vertex ordering so this isn't necessary
		gl.glEnable(GL2.GL_BLEND);
		
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.bezierID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, Float.SIZE/Byte.SIZE, 0);
	}
	
	public static void endDrawing(GL2 gl) {
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(0);

		gl.glEnable(gl.GL_CULL_FACE);
		gl.glDisable(GL2.GL_BLEND);
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.BEZIER);
		shader.stop(gl);
	}


	public void draw(GL2 gl, float f, float f0, float f1) {
		/*if (opacity <= 0.0f) {
			return; // No need to call shader on invisible links.
		}*/
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.BEZIER);

		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint1", aXYPos.x, aXYPos.y);
		ShaderMemory.setUniformVec1(gl, shader, "uniAlpha", opacity);
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint3", bXYPos.x, bXYPos.y);
		ShaderMemory.setUniformVec3(gl, shader, "color", f, f0, f1);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, OpenGLBuffers.numBezierPoints+1);

	}

	public boolean isMinimized() {
		return aChromosome.isMinimized() || bChromosome.isMinimized();
	}

	public Chromosome getAChromosome() {
		return aChromosome;
	}

	public Chromosome getBChromosome() {
		return bChromosome;
	}

	long getaStart() {
		return aStart;
	}

	long getbStart() {
		return bStart;
	}

}
