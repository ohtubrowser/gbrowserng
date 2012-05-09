package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;

import javax.media.opengl.GL2;

public class LinkGFX {

	SessionViewCapsule capsule;
	Vector2 circlePos;

	Matrix4 modelMatrix = new Matrix4();
	Matrix4 identityMatrix = new Matrix4();

	float time = 0.0f;
	private float velocity = 5.0f;

	private float alpha = 1.0f;

	public LinkGFX(SessionViewCapsule c, Vector2 circlePos) {
		this.capsule = c;
		this.circlePos = circlePos;
	}
	
	public Vector2 getXYPosition() {
		return this.circlePos;
	}

	public void draw(GL2 gl) {

		// if invisible, don't bother drawing
		if(alpha < 0)
			return;

		gl.glEnable(GL2.GL_BLEND);

		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.TORRENT);
		shader.start(gl);

		ShaderMemory.setUniformVec1(gl, shader, "uniAlpha", alpha);
		ShaderMemory.setUniformVec1(gl, shader, "lifetime", time * velocity);
		ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
		ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);

		float x = (capsule.getCapsulePosition().x + circlePos.x) / 2.0f;
		float y = (capsule.getCapsulePosition().y + circlePos.y) / 2.0f;

		float dx = capsule.getCapsulePosition().x - circlePos.x;
		float dy = capsule.getCapsulePosition().y - circlePos.y;

		float angle = 180f * (float)Math.atan2(dy, dx) / (float)Math.PI;
		float length = capsule.getCapsulePosition().distance(circlePos) * 0.5f;

		modelMatrix.makeTranslationMatrix(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(0.01f, length, 0.2f);

		ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
		gl.glDisableVertexAttribArray(0);
		
		shader.stop(gl);

		gl.glDisable(GL2.GL_BLEND);
	}


	public void tick(float dt) {
		time -= dt;
		if(time < -1.3f / velocity)
			time += 2.6f / velocity;

		alpha += (1.0f - alpha) * (Math.min(1.0f, GlobalVariables.animationConstant*dt));
	}
}
