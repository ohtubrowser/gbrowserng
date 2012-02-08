package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import gles.SoulGL2;
import gles.primitives.PrimitiveBuffers;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import managers.ShaderManager;
import math.Matrix4;
import math.MatrixUtils;

public class LinkGFX {

	GenosideComponent component1;
	GenosideComponent component2;
	Matrix4 modelMatrix = new Matrix4();
	Matrix4 identityMatrix = new Matrix4();

	float time = 0.0f;
	private float velocity = 5.0f;

	private float alpha = 1.0f;
	private float target_alpha = 1.0f;

	public LinkGFX(GenosideComponent a, GenosideComponent b) {
		component1 = a;
		component2 = b;
	}

	public void draw(SoulGL2 gl) {

		// if invisible, don't bother drawing
		if(alpha < 0)
			return;

		gl.glEnable(SoulGL2.GL_BLEND);

		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.TORRENT);
		shader.start(gl);

		ShaderMemory.setUniformVec1(gl, shader, "uniAlpha", alpha);
		ShaderMemory.setUniformVec1(gl, shader, "lifetime", time * velocity);
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);

		float x = (component1.getPosition().x + component2.getPosition().x) / 2.0f;
		float y = (component1.getPosition().y + component2.getPosition().y) / 2.0f;

		float dy = component1.getPosition().y - component2.getPosition().y;
		float dx = component1.getPosition().x - component2.getPosition().x;

		float angle = 180f * (float)Math.atan2(dy, dx) / (float)Math.PI;
		float length = component1.getPosition().distance(component2.getPosition()) * 0.5f;

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


	public void tick(float dt) {
		time -= dt;
		if(time < -1.3f / velocity)
			time += 2.6f / velocity;

		alpha += (target_alpha - alpha) * (1.0f - Math.pow(0.1f, dt));
	}

	public void hide() {
		// TODO : better value for this
		target_alpha = -0.1f;
	}

	public void show() {
		target_alpha = 1.0f;
	}

}
