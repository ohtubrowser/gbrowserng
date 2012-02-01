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
	private float r = Math.max(0.5f, (float)Math.random()),g = (float) Math.random(), b=(float)Math.random();

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


		FloatBuffer smoothings = FloatBuffer.allocate(101);
		for(float i = 1.0f; i <= 100.0f; i+=1.0f)
		    smoothings.put(i/100.0f);
		smoothings.rewind();

		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.BEZIER);
		shader.start(gl);

		Matrix4 identityMatrix = new Matrix4();
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint1", aXYPos.x, aXYPos.y);
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint2", 0.0f, 0.0f);
		ShaderMemory.setUniformVec2(gl, shader, "ControlPoint3", bXYPos.x, bXYPos.y);
		ShaderMemory.setUniformVec3(gl, shader, "color", r, g, b);
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

		ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", identityMatrix);

		int vertexPositionHandle = shader.getAttribLocation(gl, "t");
		smoothings.rewind();

		gl.glLineWidth(3.0f);
		gl.glEnable(GL2.GL_LINE_SMOOTH);
		gl.glEnableVertexAttribArray(vertexPositionHandle);
		gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, smoothings);
		gl.glDrawArrays(SoulGL2.GL_LINE_STRIP, 0, smoothings.capacity() / 2);
		gl.glDisableVertexAttribArray(vertexPositionHandle);
		gl.glDisable(GL2.GL_LINE_SMOOTH);

		gl.glLineWidth(1.0f);

		shader.stop(gl);

		gl.glDisable(SoulGL2.GL_BLEND);
    }

}
