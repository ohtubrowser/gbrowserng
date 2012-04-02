package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import javax.media.opengl.GL2;

public class GeneralLink implements Comparable<GeneralLink> {

	private ViewChromosome aChromosome, bChromosome;
	private long aStart, bStart;
	private final boolean aOcc; // Used for sorting, describes whether this is the a or b-occurrence of this link (since all are present twice)
	public float r, g, b;
	private float aCirclePos, bCirclePos;
	public float aX, aY, bX, bY;
	private float opacity;
	private long counter = 1;

	public GeneralLink(ViewChromosome aChromosome, ViewChromosome bChromosome, long aStart, long bStart, boolean aOcc) {
		this.aChromosome = aChromosome;
		this.bChromosome = bChromosome;
		this.aStart = aStart;
		this.bStart = bStart;
		this.opacity = 1.0f;
		this.aOcc = aOcc;
		initLinkColor();
	}

	/**
	 * Returns coordinates of ending point of link.
	 * Every link occurs twice, so tbis method checks whether this is 1st or 2nd occurence and determines the terminating point accordingly.
	 * If 1st occurence, method returns b-chromsome end position.
	 * If 2nd occurence, method returns a-chromosome end position.
	 * @return
	 */
	public float[] getLinkEndPositions() {
		if (aOcc) {
			return new float[]{bX, bY};
		} else {
			return new float[]{aX, aY};
		}
	}

	public static GeneralLink createComparisonObject(ViewChromosome aC, ViewChromosome bC, long aS, long bS, boolean aOcc) {
		return new GeneralLink(aC, bC, aS, bS, aOcc);
	}

	private void initLinkColor() {
		this.r = ((float) Math.random() * 0.3f + 0.7f);
		this.g = this.b = 0.3f;
	}

	public void fadeIn(float fadespeed) {
		this.opacity += fadespeed;
		if (this.opacity > 1.0f) {
			this.opacity = 1.0f;
		}
	}

	public void fadeDim(float fadespeed) {
		this.opacity -= fadespeed;
		if (this.opacity < 0.05f) {
			this.opacity = 0.05f;
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

	public void calculatePositions(GeneCircle geneCircle) {
		aCirclePos = -0.25f + geneCircle.getRelativePosition(aChromosome.getChromosomeNumber() - 1, (float) aStart / aChromosome.length()); // Need -1 because of AbstractChromosome indexing
		bCirclePos = -0.25f + geneCircle.getRelativePosition(bChromosome.getChromosomeNumber() - 1, (float) bStart / bChromosome.length());
		Vector2 aXYPos = geneCircle.getXYPosition(aCirclePos);
		aX = aXYPos.x;
		aY = aXYPos.y;
		// This magic constant is the same as in the circleseparators.
		aX *= 0.9495;
		aY *= 0.9495;
		Vector2 bXYPos = geneCircle.getXYPosition(bCirclePos);
		bX = bXYPos.x;
		bY = bXYPos.y;
		bX *= 0.9495;
		bY *= 0.9495;

		aX = CoordinateManager.toCircleCoordsX(aX);
		aY = CoordinateManager.toCircleCoordsY(aY);
		bX = CoordinateManager.toCircleCoordsX(bX);
		bY = CoordinateManager.toCircleCoordsY(bY);
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
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, Float.SIZE / Byte.SIZE, 0);
	}

	public static void endDrawing(GL2 gl) {
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(0);

		gl.glEnable(gl.GL_CULL_FACE);
		gl.glDisable(GL2.GL_BLEND);
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.BEZIER);
		shader.stop(gl);
	}

	public float bezier(float t, float p1, float p2, float p3) {
		return (1.0f - t) * ((1.0f - t) * p1 + t * p2) + t * ((1.0f - t) * p2 + t * p3);
	}

	public float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2f) + Math.pow(y1 - y2, 2f));
	}

	public boolean isHit(float x, float y) {
		boolean hit = false;
		for (float i = 1; i <= 100; i++) {
			float bezierX = bezier(i / 100, aX, 0, bX);
			float bezierY = bezier(i / 100, aY, 0, bY);
			float dist = distance(x, y, bezierX, bezierY);
			if (dist < 0.01f) {
				hit = true;
				break;
			}
		}
		return hit;
	}

	public boolean draw(GL2 gl, float x, float y) {
		boolean hit = isHit(x, y);
		if (hit) {
			draw(gl, 0f, 0f, 1f);
		} else {
			draw(gl, r, g, b);
		}
		return hit;
	}

	public void draw(GL2 gl, float f, float f0, float f1) {
		if (opacity <= 0.0f) {
			return; // No need to call shader on invisible links.
		}
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.BEZIER);

		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint1", aX, aY);
		ShaderMemory.setUniformVec1(gl, shader, "uniAlpha", opacity);
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint3", bX, bY);
		ShaderMemory.setUniformVec3(gl, shader, "color", f, f0, f1);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, OpenGLBuffers.numBezierPoints + 1);
	}

	public void draw(GL2 gl) {
		this.draw(gl, r, g, b);
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
		long thisPos = aOcc ? aChromosome.getChromosomeNumber() : bChromosome.getChromosomeNumber(),
				oPos = o.aOcc ? o.aChromosome.getChromosomeNumber() : o.bChromosome.getChromosomeNumber();

		long diff = thisPos - oPos;

		if (diff == 0) {
			thisPos = aOcc ? aStart : bStart;
			oPos = o.aOcc ? o.aStart : o.bStart;
			diff = thisPos - oPos;
		}

		// For type safety, maybe overkill
		int ret = 0;
		if (diff > 0) {
			ret = 1;
		}
		if (diff < 0) {
			ret = -1;
		}

		return ret;
	}

	public boolean isaocc() {
		return aOcc;
	}

	public ViewChromosome getStartChromosome() {
		return aOcc ? aChromosome : bChromosome;
	}

	public ViewChromosome getEndChromosome() {
		return aOcc ? bChromosome : aChromosome;
	}

	public long getStartPosition() {
		return aOcc ? aStart : bStart;
	}

	public long getEndPosition() {
		return aOcc ? bStart : aStart;
	}

	/**
	 * @return the counter
	 */
	public long getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(long counter) {
		this.counter = counter;
	}
	
	public void addCounter(long value) {
		counter += value;
	}

	public void setColorByCounter(long avgCounter) {
		float r = Math.min(1.0f, 0.5f*counter / avgCounter);
		this.r = (r * 0.5f + 0.5f);
		this.g = this.b = 0.3f;
	}
}
