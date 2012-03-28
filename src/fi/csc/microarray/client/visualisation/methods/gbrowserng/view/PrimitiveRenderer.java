package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.soulaim.tech.gles.Color;
import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;

import javax.media.opengl.GL2;

// TODO: This is NOT ready
public class PrimitiveRenderer {
	public static void drawRectangle(float x, float y, float width, float height, GL2 gl, Color color) {
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.PLAINMVP);

		shader.start(gl);
		ShaderMemory.setUniformVec4(gl, shader, "color", color.r, color.g, color.b, color.a);

		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		
		Matrix4 modelMatrix = new Matrix4();
		modelMatrix.makeTranslationMatrix(x, y, 0);
		modelMatrix.scale(width, height*GlobalVariables.aspectRatio, 0.0f);

		ShaderMemory.setUniformMat4(gl, shader, "MVP", modelMatrix);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);

		shader.stop(gl);
	}
}
