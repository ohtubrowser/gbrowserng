package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import gles.Color;
import gles.SoulGL2;
import gles.primitives.PrimitiveBuffers;
import gles.renderer.PrimitiveRenderer;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import managers.ShaderManager;
import math.Matrix4;
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
		//PrimitiveRenderer.drawLine(aXYPos.x, aXYPos.y, bXYPos.x, bXYPos.y, gl, Color.MAGENTA);
		gl.glEnable(SoulGL2.GL_BLEND);

		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.TORRENT);
		shader.start(gl);

		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformVec1(gl, shader, "uniAlpha", 1.0f);
		ShaderMemory.setUniformVec1(gl, shader, "lifetime", 5.0f);
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);

		float x = (aXYPos.x + bXYPos.x) / 2.0f;
		float y = (aXYPos.y + bXYPos.y) / 2.0f;

		float dx = x-bXYPos.x;
		float dy = y-bXYPos.y;

		float angle = 180f * (float)Math.atan2(dy, dx) / (float)Math.PI;
		float length = aXYPos.distance(bXYPos) * 0.5f;

		Matrix4 modelMatrix = new Matrix4();
		modelMatrix.makeTranslationMatrix(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(0.01f, length, 0.2f);

		ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

		int vertexPositionHandle = shader.getAttribLocation(gl, "vertexPosition");
		PrimitiveBuffers.squareBuffer.rewind();
		gl.glEnableVertexAttribArray(vertexPositionHandle);
		gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, PrimitiveBuffers.squareBuffer);
		gl.glDrawArrays(SoulGL2.GL_TRIANGLE_STRIP, 0, PrimitiveBuffers.squareBuffer.capacity() / 2);
		gl.glDisableVertexAttribArray(vertexPositionHandle);

		shader.stop(gl);

		gl.glDisable(SoulGL2.GL_BLEND);
    }

}
