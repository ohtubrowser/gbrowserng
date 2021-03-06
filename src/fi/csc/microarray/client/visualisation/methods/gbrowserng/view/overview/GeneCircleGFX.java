package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.soulaim.tech.gles.SoulGL2;
import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;

import javax.media.opengl.GL2;

public class GeneCircleGFX {

	public float time = 0;
	private float hilight = 0;
	private float hilightTarget = 0;
	private Vector2 mousePos = new Vector2();
	private GeneCircle geneCircle;
	public GlobalVariables globals;

	public GeneCircleGFX(GlobalVariables globals, GeneCircle geneCircle) {
		this.globals = globals;
		this.geneCircle = geneCircle;
	}

	public void draw(GL2 gl, Matrix4 modelMatrix, Vector2 mousePosition) {
		gl.glEnable(GL2.GL_BLEND);
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.GENE_CIRCLE);
		shader.start(gl);

		mousePos.copyFrom(mousePosition);
		mousePos.normalize();

		ShaderMemory.setUniformVec1(gl, shader, "thickness", 0.1f);
		ShaderMemory.setUniformVec2(gl, shader, "mouse", mousePos.x, mousePos.y);
		ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.circleID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		gl.glDrawArrays(GL2.GL_TRIANGLE_FAN, 0, OpenGLBuffers.circlePoints + 2);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(0);
		shader.stop(gl);

		drawChromosomeSeparators(gl);
		drawCentromeres(gl);
	}

	public void drawChromosomeSeparators(GL2 gl) {
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.CIRCLESEPARATOR);

		shader.start(gl);
		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);
		Matrix4 modelMatrix = new Matrix4();
		float length = geneCircle.getSize() * 0.0505f;
		float width = geneCircle.getSize() * 0.015f;

		int len;
		Vector2[] chromopositions;
		chromopositions = geneCircle.getChromosomeBoundariesPositions();
		len = chromopositions.length;

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		for (int i = 0; i < len; ++i) {
			float x, y;
			x = 0.9495f * chromopositions[i].x;
			y = 0.9495f * chromopositions[i].y;

			float angle = 180f * (float) Math.atan2(y, x) / (float) Math.PI;
			modelMatrix=CoordinateManager.getCircleMatrix(globals);
			modelMatrix.translate(x, y, 0);
			modelMatrix.rotate(angle + 90f, 0, 0, 1);
			modelMatrix.scale(width, length, 0.2f);

			ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

			gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);
		}
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

		shader.stop(gl);
	}

	public void drawCentromeres(GL2 gl) {
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.CENTROMERE);

		shader.start(gl);
		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);
		gl.glLineWidth(2.0f);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.centromereID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);

		for (ViewChromosome c : globals.genome.getChromosomes()) {
			if (!c.isMinimized()) {
				Matrix4 modelMatrix = CoordinateManager.getCircleMatrix(globals);
				modelMatrix.rotate(360.f * geneCircle.getRelativePosition(c.getChromosomeNumber() - 1, c.getCentromereRelativePosition()), 0, 0, 1);
				modelMatrix.translate(geneCircle.getSize() * 0.95f, 0.0f, 0);
				modelMatrix.scale(geneCircle.getSize() * 0.05f, geneCircle.getSize() * 0.02f, 1.0f);

				ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);
				gl.glDrawArrays(GL2.GL_LINES, 0, 4);
			}
		}
		shader.stop(gl);
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
		gl.glDisable(SoulGL2.GL_BLEND);
	}

	public void tick(float dt) {
		time += dt;
		hilight += (1.0f - Math.pow(0.1f, dt)) * (hilightTarget - hilight);
	}
}
