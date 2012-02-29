package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.newt.event.KeyEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import gles.SoulGL2;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import javax.media.opengl.GL2;
import managers.ShaderManager;
import math.Matrix4;
import soulaim.DesktopGL2;

public class LinkSelection {
	float begin, end, area;
	private boolean leftKeyDown = false, rightKeyDown = false, upKeyDown = false, downKeyDown = false;
	public LinkSelection() {
		begin = 0.0f;
		end = 1.0f;
		area = GlobalVariables.selectSize;
	}

	public void reset() {
		begin = 0.0f;
		end = 1.0f;
	}

	public void update(float pointerGenePosition) {
		begin = pointerGenePosition - 0.25f - area/2;
		end = pointerGenePosition - 0.25f + area/2;
		clamp();
	}

	public void updateArea() {
		float oldArea = getCurrentArea();
		float change = (area - oldArea)/2;
		begin -= change;
		end += change;
		clamp();
	}

	private void clamp() {
		if (begin < 0.0f) {
			begin += 1.0f;
		}
		if (end < 0.0f) {
			end += 1.0f;
		}
		if (end > 1.0f) {
			end -= 1.0f;
		}
		if (begin > 1.0f) {
			begin -= 1.0f;
		}
	}

	private float getCurrentArea() {
		if(begin > end)
			return 1.0f-begin + end;
		return end - begin;
	}

	public void draw(GL2 gl, GeneCircle geneCircle) {
		// TODO : remove magic numbers
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.PLAINMVP);

		SoulGL2 soulgl = new DesktopGL2(gl);

		shader.start(soulgl);

		ShaderMemory.setUniformVec4(soulgl, shader, "color", 1.0f, 1.0f, 0.0f, 1.0f);

		Matrix4 modelMatrix = new Matrix4();
		float length = geneCircle.getSize() * 0.0505f;
		float width = geneCircle.getSize() * 0.015f;

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		float x, y;
		synchronized (geneCircle.tickdrawLock) {
			x = 0.9495f * geneCircle.getXYPosition(this.begin).x;
			y = 0.9495f * geneCircle.getXYPosition(this.begin).y;
		}

		float angle = 180f * (float) Math.atan2(y, x) / (float) Math.PI;

		modelMatrix.makeTranslationMatrix(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(width, length, 0.2f);

		ShaderMemory.setUniformMat4(soulgl, shader, "MVP", modelMatrix);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		x = 0.9495f * geneCircle.getXYPosition(this.end).x;
		y = 0.9495f * geneCircle.getXYPosition(this.end).y;

		angle = 180f * (float) Math.atan2(y, x) / (float) Math.PI;

		modelMatrix.makeTranslationMatrix(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(width, length, 0.2f);

		ShaderMemory.setUniformMat4(soulgl, shader, "MVP", modelMatrix);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

		shader.stop(soulgl);
	}
	
	public void handle(KeyEvent keyEvent) {
		if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_UP) {
			upKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
			downKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED);
		}
	}
	
	public void deactivate() {
		leftKeyDown = rightKeyDown = upKeyDown = downKeyDown = false;
		resetArea();
		reset();
	}
	
	public void tick(float dt) {
		// TODO : concurrency
		if(leftKeyDown)
			move(-0.1f*dt);
		if(rightKeyDown)
			move(0.1f*dt);
		if(upKeyDown)
			updateArea(-0.01f*dt);
		if(downKeyDown)
			updateArea(0.01f*dt);
	}

	public void move(float value) {
		begin += value; end += value;
		clamp();
	}

	public void resetArea() {
		area = GlobalVariables.selectSize;
	}

	public void updateArea(float f) {
		area += f;
		area = Math.min(0.9f, Math.max(area, 0.001f));
		updateArea();
	}

	public boolean inSelection(GeneralLink link) {
		if (begin < end) {
			return (link.aCirclePos >= begin && link.aCirclePos <= end)
					|| (link.bCirclePos >= begin && link.bCirclePos <= end);
		} else {
			return (link.aCirclePos >= begin || link.aCirclePos <= end)
					|| (link.bCirclePos >= begin || link.bCirclePos <= end);
		}
	}
}
