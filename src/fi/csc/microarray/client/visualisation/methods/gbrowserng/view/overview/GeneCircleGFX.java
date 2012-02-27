package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import gles.SoulGL2;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;
import managers.ShaderManager;
import math.Matrix4;
import math.Vector2;
import soulaim.DesktopGL2;

public class GeneCircleGFX {

	public float time = 0;
	private float hilight = 0;
	private float hilightTarget = 0;
	private Vector2 mousePos = new Vector2();
	private GeneCircle geneCircle;

	public GeneCircleGFX(GeneCircle geneCircle) {
		this.geneCircle = geneCircle;
	}

	public void draw(GL2 gl, Matrix4 modelMatrix, Vector2 mousePosition) {
		gl.glEnable(GL2.GL_BLEND);
		SoulGL2 soulgl = new DesktopGL2(gl);
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.GENE_CIRCLE);
		shader.start(soulgl);

		mousePos.copyFrom(mousePosition);
		mousePos.normalize();

		ShaderMemory.setUniformVec1(soulgl, shader, "thickness", 0.1f);
		ShaderMemory.setUniformVec2(soulgl, shader, "mouse", mousePos.x, mousePos.y);
		ShaderMemory.setUniformMat4(soulgl, shader, "modelMatrix", modelMatrix);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.circleID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		gl.glDrawArrays(GL2.GL_TRIANGLE_FAN, 0, OpenGLBuffers.circlePoints + 2);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(0);
		shader.stop(soulgl);

		drawChromosomeSeparators(gl);
		drawCentromeres(gl);
	}

	public void drawChromosomeSeparators(GL2 gl) {
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.CIRCLESEPARATOR);

		SoulGL2 soulgl = new DesktopGL2(gl);

		shader.start(soulgl);
		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformMat4(soulgl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(soulgl, shader, "projectionMatrix", identityMatrix);
		Matrix4 modelMatrix = new Matrix4();
		float length = geneCircle.getSize() * 0.0505f;
		float width = geneCircle.getSize() * 0.015f;

		int len;
		Vector2[] chromopositions;
		synchronized (geneCircle.tickdrawLock) {
			chromopositions = geneCircle.getChromosomeBoundariesPositions();
			len = chromopositions.length;
		}

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		for (int i = 0; i < len; ++i) {
			float x, y;
			synchronized (geneCircle.tickdrawLock) {
				x = 0.9495f * chromopositions[i].x;
				y = 0.9495f * chromopositions[i].y;

				//float dy = 0.9495f*vec.y;
				//float dx = 0.9495f*vec.x;
			}

			float angle = 180f * (float) Math.atan2(/*
					 * dy
					 */y, /*
					 * dx
					 */ x) / (float) Math.PI;

			modelMatrix.makeTranslationMatrix(x, y, 0);
			modelMatrix.rotate(angle + 90f, 0, 0, 1);
			modelMatrix.scale(width, length, 0.2f);

			ShaderMemory.setUniformMat4(soulgl, shader, "modelMatrix", modelMatrix);

			gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);
		}
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

		shader.stop(soulgl);
	}

	public void drawCentromeres(GL2 gl) {
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.CENTROMERE);

		SoulGL2 soulgl = new DesktopGL2(gl);

		shader.start(soulgl);
		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformMat4(soulgl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(soulgl, shader, "projectionMatrix", identityMatrix);
		Matrix4 modelMatrix = new Matrix4();
		gl.glLineWidth(2.0f);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.centromereID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);

		for (Chromosome c : AbstractGenome.getChromosomes()) {
			if (!c.isMinimized()) {
				modelMatrix.makeRotationMatrix(360.f * geneCircle.getRelativePosition(c.getChromosomeNumber() - 1, c.getCentromereRelativePosition()), 0, 0, 1);
				modelMatrix.translate(geneCircle.getSize() * 0.95f, 0.0f, 0);
				modelMatrix.scale(geneCircle.getSize() * 0.05f, geneCircle.getSize() * 0.02f, 1.0f);

				ShaderMemory.setUniformMat4(soulgl, shader, "modelMatrix", modelMatrix);
				gl.glDrawArrays(GL2.GL_LINES, 0, 4);
			}
		}
		shader.stop(soulgl);
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
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
