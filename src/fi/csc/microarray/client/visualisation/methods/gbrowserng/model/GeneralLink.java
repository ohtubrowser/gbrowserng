package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import gles.SoulGL2;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import javax.media.opengl.GL2;
import managers.ShaderManager;
import math.Matrix4;
import math.Vector2;
import soulaim.DesktopGL2;

public class GeneralLink implements Comparable<GeneralLink> {

	private ViewChromosome aChromosome, bChromosome;
	private long aStart, bStart;
	public final boolean aOcc; // Used for sorting, describes whether this is the a or b-occurrence of this link (since all are present twice)
	//private float r = Math.max(0.5f, (float) Math.random()), g = (float) Math.random(), b = (float) Math.random();
	private float r = 1.0f, g = 0.0f, b = 0.0f;
	float aCirclePos, bCirclePos;
	private float aX, aY, bX, bY;
	private float opacity;

	private GeneralLink(float aCirclePos, float bCirclePos, boolean aOcc) { // Private constructor for creating temporary comparison objects
		this.aOcc = aOcc;
		this.aCirclePos = aCirclePos;
		this.bCirclePos = bCirclePos;
	}
	
	public static GeneralLink createComparisonObject(float aCirclePos, float bCirclePos, boolean aOcc) {
		return new GeneralLink(aCirclePos, bCirclePos, aOcc);
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

	public GeneralLink(ViewChromosome aChromosome, ViewChromosome bChromosome, long aStart, long bStart, boolean aOcc) {
		this.aChromosome = aChromosome;
		this.bChromosome = bChromosome;
		this.aStart = aStart;
		this.bStart = bStart;
		this.opacity = 1.0f;
		this.aOcc = aOcc;
	}

	public void calculatePositions(GeneCircle geneCircle) {
		aCirclePos = -0.25f + geneCircle.getRelativePosition(aChromosome.getChromosomeNumber() - 1, (float) aStart / aChromosome.length()); // Need -1 because of AbstractChromosome indexing
		bCirclePos = -0.25f + geneCircle.getRelativePosition(bChromosome.getChromosomeNumber() - 1, (float) bStart / bChromosome.length());
		Vector2 aXYPos = geneCircle.getXYPosition(aCirclePos);
		aX = aXYPos.x; aY = aXYPos.y;
		// This magic constant is the same as in the circleseparators.
		aX *= 0.9495;
		aY *= 0.9495;
		Vector2 bXYPos = geneCircle.getXYPosition(bCirclePos);
		bX = bXYPos.x; bY = bXYPos.y;
		bX *= 0.9495;
		bY *= 0.9495;
		
		aX = CoordinateManager.toCircleCoordsX(aX);
		aY = CoordinateManager.toCircleCoordsY(aY);
		bX = CoordinateManager.toCircleCoordsX(bX);
		bY = CoordinateManager.toCircleCoordsY(bY);
	}

	public static void beginDrawing(GL2 gl, float zoomLevel) {
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.BEZIER);

		SoulGL2 soulgl = new DesktopGL2(gl);
		shader.start(soulgl);

		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformVec2(soulgl, shader, "ControlPoint2", 0.0f, 0.0f);
		ShaderMemory.setUniformVec1(soulgl, shader, "width", 0.005f * zoomLevel);
		ShaderMemory.setUniformVec1(soulgl, shader, "tstep", OpenGLBuffers.bezierStep);
		ShaderMemory.setUniformMat4(soulgl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(soulgl, shader, "projectionMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(soulgl, shader, "modelMatrix", identityMatrix);

		gl.glDisable(GL2.GL_CULL_FACE); // TODO : maybe just change the vertex ordering so this isn't necessary
		gl.glEnable(SoulGL2.GL_BLEND);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, OpenGLBuffers.bezierID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, Float.SIZE/Byte.SIZE, 0);
	}

	public static void endDrawing(GL2 gl) {
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(0);

		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glDisable(SoulGL2.GL_BLEND);
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.BEZIER);
		SoulGL2 soulgl = new DesktopGL2(gl);
		shader.stop(soulgl);
	}


	public void draw(GL2 gl, float f, float f0, float f1) {
		if (opacity <= 0.0f) {
			return; // No need to call shader on invisible links.
		}
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.BEZIER);

		SoulGL2 soulgl = new DesktopGL2(gl);

		ShaderMemory.setUniformVec2(soulgl, shader, "ControlPoint1", aX, aY);
		ShaderMemory.setUniformVec1(soulgl, shader, "uniAlpha", opacity);
		ShaderMemory.setUniformVec2(soulgl, shader, "ControlPoint3", bX, bY);
		ShaderMemory.setUniformVec3(soulgl, shader, "color", f, f0, f1);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, OpenGLBuffers.numBezierPoints+1);

	}

	public boolean isMinimized() {
		return aChromosome.isMinimized() || bChromosome.isMinimized();
	}

	public ViewChromosome getAChromosome() {
		return aChromosome;
	}

	public ViewChromosome getBChromosome() {
		return bChromosome;
	}

	public long getaStart() {
		return aStart;
	}

	public long getbStart() {
		return bStart;
	}

	@Override
	public int compareTo(GeneralLink o) {
		float thisPos = aOcc ? aCirclePos : bCirclePos,
				oPos = o.aOcc ? o.aCirclePos : o.bCirclePos;
		
		float diff = thisPos - oPos;
		
		// For type safety, maybe overkill
		int ret = 0;
		if(diff > 0)
			ret = 1;
		if(diff < 0)
			ret = -1;
		
		return ret;
	}

}
