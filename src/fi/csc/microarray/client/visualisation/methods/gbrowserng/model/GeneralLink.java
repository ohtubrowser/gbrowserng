package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import gles.SoulGL2;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import java.nio.FloatBuffer;
import managers.ShaderManager;
import math.Matrix4;
import math.Vector2;

import javax.media.opengl.GL2;

public class GeneralLink {

	private AbstractChromosome aChromosome, bChromosome;
	private long aStart, bStart, aEnd, bEnd;
	private float r = Math.max(0.5f, (float) Math.random()), g = (float) Math.random(), b = (float) Math.random();
	private final int drawMethod;
	private static FloatBuffer bezierPoints;
	private static float tstep;
	private float aCirclePos, bCirclePos;
	private Vector2 aXYPos, bXYPos;

	public static void initBezierPoints() {
		final int points = 50; // Setting this too low will cause problems on sharp curves
		final float step = 1.0f / points;
		tstep = step;
		bezierPoints = FloatBuffer.allocate(points+1);
		bezierPoints.put(step);
		for (int i = 1; i < points; ++i) {
			bezierPoints.put( ((i % 2 == 0) ? i : -i)*step);
		}
		bezierPoints.put(-bezierPoints.get(points-1));
	}

	public GeneralLink(AbstractChromosome aChromosome, AbstractChromosome bChromosome, long aStart, long aEnd, long bStart, long bEnd) {
		this.aChromosome = aChromosome;
		this.bChromosome = bChromosome;
		this.aStart = aStart;
		this.bStart = bStart;
		this.aStart = aEnd;
		this.bStart = bEnd;
		drawMethod = SoulGL2.GL_TRIANGLE_STRIP;
	}

	public void calculatePositions(GeneCircle geneCircle) {
		aCirclePos = -0.25f + geneCircle.getRelativePosition(aChromosome.getChromosomeNumber() - 1, (float) aStart / aChromosome.length()); // Need -1 because of AbstractChromosome indexing
		bCirclePos = -0.25f + geneCircle.getRelativePosition(bChromosome.getChromosomeNumber() - 1, (float) bStart / bChromosome.length());
		aXYPos = geneCircle.getXYPosition(aCirclePos);
		bXYPos = geneCircle.getXYPosition(bCirclePos);
	}

	public void draw(SoulGL2 gl, float zoomLevel, Vector2 showLinksInterval) {
		float nonActive = 0.0f;
		if(!inInterval(showLinksInterval))
			nonActive = 0.9f;
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.BEZIER);
		shader.start(gl);

		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint1", aXYPos.x, aXYPos.y);
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint2", 0.0f, 0.0f);
		ShaderMemory.setUniformVec1(gl, shader, "width", 0.005f * zoomLevel);
		ShaderMemory.setUniformVec1(gl, shader, "uniAlpha", 1.0f - nonActive);
		ShaderMemory.setUniformVec1(gl, shader, "tstep", tstep);
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint3", bXYPos.x, bXYPos.y);
		ShaderMemory.setUniformVec3(gl, shader, "color", r, g, b);
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);

		float x = (aXYPos.x + bXYPos.x) / 2.0f;
		float y = (aXYPos.y + bXYPos.y) / 2.0f;

		float dx = x - bXYPos.x;
		float dy = y - bXYPos.y;

		float angle = 180f * (float) Math.atan2(dy, dx) / (float) Math.PI;
		float length = aXYPos.distance(bXYPos) * 0.5f;

		Matrix4 modelMatrix = new Matrix4();
		modelMatrix.makeTranslationMatrix(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(0.01f, length, 0.2f);

		ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", identityMatrix);

		int vertexPositionHandle = shader.getAttribLocation(gl, "t");
		bezierPoints.rewind();

		gl.glLineWidth(3.0f);
		gl.glDisable(gl.GL_CULL_FACE); // TODO : maybe just change the vertex ordering so this isn't necessary
		gl.glEnable(SoulGL2.GL_BLEND);

		gl.glEnableVertexAttribArray(vertexPositionHandle);
		gl.glVertexAttribPointer(vertexPositionHandle, 1, SoulGL2.GL_FLOAT, false, 0, bezierPoints);
		gl.glDrawArrays(drawMethod, 0, bezierPoints.capacity());
		gl.glDisableVertexAttribArray(vertexPositionHandle);

		gl.glEnable(gl.GL_CULL_FACE);

		shader.stop(gl);

		gl.glDisable(SoulGL2.GL_BLEND);
	}

	private boolean inInterval(Vector2 showLinksInterval) {
		if(showLinksInterval.x < showLinksInterval.y) {
			return (aCirclePos >= showLinksInterval.x && aCirclePos <= showLinksInterval.y)
					|| (bCirclePos >= showLinksInterval.x && bCirclePos <= showLinksInterval.y);
		}
		else {
			return (aCirclePos >= showLinksInterval.x || aCirclePos <= showLinksInterval.y)
					|| (bCirclePos >= showLinksInterval.x || bCirclePos <= showLinksInterval.y);
		}

	}
}
