package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.soulaim.tech.gles.Color;
import com.soulaim.tech.gles.shaders.DefaultShaders;
import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.managers.ShaderManager;
import com.soulaim.tech.math.Matrix4;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;

import javax.media.opengl.GL2;

public class PrimitiveRenderer {
	private static Matrix4 modelMatrix = new Matrix4();
	private static Matrix4 modelviewProjectionMatrix = new Matrix4();
	private static Matrix4 projectionMatrix = new Matrix4();

	public static void drawTexturedSquare(float x, float y, float scale, GL2 gl, Color color, GenoTexID.TextureID textureID) {
		modelMatrix.makeTranslationMatrix(x,y,0);
		modelMatrix.scale(scale, scale * GlobalVariables.aspectRatio, 1.0f);
		modelviewProjectionMatrix.storeMultiplication(projectionMatrix, modelMatrix);

		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.TEXRECTANGLE);
		shader.start(gl);

		//ShaderMemory.setUniformVec2(gl, shader, "texture", );
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		
		shader.stop(gl);
	}
}
