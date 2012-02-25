package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import gles.SoulGL2;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import managers.ShaderManager;
import math.Matrix4;
import math.Vector2;

public class GeneCircleGFX {

	public float time = 0;
	private float hilight = 0;
	private float hilightTarget = 0;
	private Vector2 mousePos = new Vector2();
	private GeneCircle geneCircle;

	public GeneCircleGFX(GeneCircle geneCircle) {
		this.geneCircle = geneCircle;
	}

	public void draw(SoulGL2 gl, Matrix4 modelMatrix, Vector2 mousePosition) {
		gl.glEnable(SoulGL2.GL_BLEND);
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.GENE_CIRCLE);
		shader.start(gl);

		mousePos.copyFrom(mousePosition);
		mousePos.normalize();

		ShaderMemory.setUniformVec1(gl, shader, "thickness", 0.1f);
		ShaderMemory.setUniformVec2(gl, shader, "mouse", mousePos.x, mousePos.y);
		ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

		int vertexPositionHandle = shader.getAttribLocation(gl, "vertexPosition");
		OpenGLBuffers.circleBuffer.rewind();
		gl.glEnableVertexAttribArray(vertexPositionHandle);
		gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, OpenGLBuffers.circleBuffer);
		gl.glDrawArrays(SoulGL2.GL_TRIANGLE_FAN, 0, OpenGLBuffers.circleBuffer.capacity() / 2);
		gl.glDisableVertexAttribArray(vertexPositionHandle);
		shader.stop(gl);

		drawChromosomeSeparators(gl);
		drawCentromeres(gl);
	}

	public void drawChromosomeSeparators(SoulGL2 gl) {
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.CIRCLESEPARATOR);
		shader.start(gl);
		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);
		Matrix4 modelMatrix = new Matrix4();
		float length = geneCircle.getSize()*0.0505f;
		float width = geneCircle.getSize()*0.015f;

		int len;
		Vector2[] chromopositions;
		synchronized (geneCircle.tickdrawLock) {
			chromopositions = geneCircle.getChromosomeBoundariesPositions();
			len = chromopositions.length;
		}
		for(int i=0; i<len; ++i) {
			float x,y;
			synchronized (geneCircle.tickdrawLock) {
				x = 0.9495f*chromopositions[i].x;
				y = 0.9495f*chromopositions[i].y;

				//float dy = 0.9495f*vec.y;
				//float dx = 0.9495f*vec.x;
			}

			float angle = 180f * (float)Math.atan2(/*dy*/y, /*dx*/x) / (float)Math.PI;

			modelMatrix=CoordinateManager.getCircleMatrix();
			modelMatrix.translate(x, y, 0);
			modelMatrix.rotate(angle + 90f, 0, 0, 1);
			modelMatrix.scale(width, length, 0.2f);

			ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

			int vertexPositionHandle = shader.getAttribLocation(gl, "vertexPosition");
			OpenGLBuffers.squareBuffer.rewind();
			gl.glEnableVertexAttribArray(vertexPositionHandle);
			gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, OpenGLBuffers.squareBuffer);
			gl.glDrawArrays(SoulGL2.GL_TRIANGLE_STRIP, 0, OpenGLBuffers.squareBuffer.capacity() / 2);
			gl.glDisableVertexAttribArray(vertexPositionHandle);

		}
		shader.stop(gl);
	}

	public void drawCentromeres(SoulGL2 gl) {
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.CENTROMERE);
		shader.start(gl);
		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);
		Matrix4 modelMatrix = new Matrix4();
		for (AbstractChromosome c : AbstractGenome.getChromosomes()) {
			if(!c.isMinimized()) {
				modelMatrix=CoordinateManager.getCircleMatrix();
				modelMatrix.rotate(360.f*geneCircle.getRelativePosition(c.getChromosomeNumber()-1, c.centromerePosition), 0, 0, 1);
				modelMatrix.translate(geneCircle.getSize()*0.95f, 0.0f, 0);
				modelMatrix.scale(geneCircle.getSize()*0.05f, geneCircle.getSize()*0.02f, 1.0f);

				ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

				gl.glLineWidth(2.0f);
				int vertexPositionHandle = shader.getAttribLocation(gl, "vertexPosition");
				OpenGLBuffers.centromereBuffer.rewind();
				gl.glEnableVertexAttribArray(vertexPositionHandle);
				gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, OpenGLBuffers.centromereBuffer);
				gl.glDrawArrays(SoulGL2.GL_LINES, 0, OpenGLBuffers.centromereBuffer.capacity() / 2);
				gl.glDisableVertexAttribArray(vertexPositionHandle);
			}
		}
		shader.stop(gl);
		gl.glDisable(SoulGL2.GL_BLEND);
	}

	public void tick(float dt) {
		time += dt;
		hilight += (1.0f - Math.pow(0.1f, dt)) * (hilightTarget - hilight);
	}

	public void setHilight(float value) {
		hilightTarget = value;
	}
}
