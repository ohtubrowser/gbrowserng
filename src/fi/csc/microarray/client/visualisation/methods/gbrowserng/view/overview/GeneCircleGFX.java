package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
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
        Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.GENE_CIRCLE);
        shader.start(gl);

        mousePos.copyFrom(mousePosition);
        mousePos.normalize();

        /*
        mousePos.scale(1.2f);
        if(mousePos.length() > 0.6f) {
            mousePos.scale( Math.max(0.1f, 1.6f - mousePos.length()) );
        }
        */

        ShaderMemory.setUniformVec1(gl, shader, "time", time);
        ShaderMemory.setUniformVec1(gl, shader, "hilight", hilight);
        ShaderMemory.setUniformVec2(gl, shader, "mouse", mousePos.x, mousePos.y);
        ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

        int vertexPositionHandle = shader.getAttribLocation(gl, "vertexPosition");
        PrimitiveBuffers.circleBuffer.rewind();
        gl.glEnableVertexAttribArray(vertexPositionHandle);
        gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, PrimitiveBuffers.circleBuffer);
        gl.glDrawArrays(SoulGL2.GL_TRIANGLE_FAN, 0, PrimitiveBuffers.circleBuffer.capacity() / 2);
        gl.glDisableVertexAttribArray(vertexPositionHandle);
        shader.stop(gl);
        
        gl.glEnable(SoulGL2.GL_BLEND);
		shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.CIRCLESEPARATOR);
        shader.start(gl);
        Matrix4 identityMatrix = new Matrix4();
        ShaderMemory.setUniformMat4(gl, shader, "viewMatrix", identityMatrix);
        ShaderMemory.setUniformMat4(gl, shader, "projectionMatrix", identityMatrix);
        modelMatrix = new Matrix4();
		float length = 0.10f;
        for(Vector2 vec : geneCircle.getChromosomeBoundariesPositions()) {

            float x = 0.49f*vec.x;
            float y = 0.49f*vec.y;

            float dy = 0.55f*vec.y-y;
			float dx = 0.55f*vec.x-x;
			
            float angle = 180f * (float)Math.atan2(dy, dx) / (float)Math.PI;
            
            modelMatrix.makeTranslationMatrix(x, y, 0);
            modelMatrix.rotate(angle + 90f, 0, 0, 1);
            modelMatrix.scale(0.01f, length, 0.2f);

            ShaderMemory.setUniformMat4(gl, shader, "modelMatrix", modelMatrix);

            vertexPositionHandle = shader.getAttribLocation(gl, "vertexPosition");
            PrimitiveBuffers.squareBuffer.rewind();
            gl.glEnableVertexAttribArray(vertexPositionHandle);
            gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, PrimitiveBuffers.squareBuffer);
            gl.glDrawArrays(SoulGL2.GL_TRIANGLE_STRIP, 0, PrimitiveBuffers.squareBuffer.capacity() / 2);
            gl.glDisableVertexAttribArray(vertexPositionHandle);
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
