package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.newt.event.KeyEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import gles.SoulGL2;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.media.opengl.GL2;
import managers.ShaderManager;
import math.Matrix4;
import soulaim.DesktopGL2;

public class LinkSelection {

	float begin, end, area;
	private boolean upKeyDown = false, downKeyDown = false;
	private ArrayList<GeneralLink> activeSelection = new ArrayList<GeneralLink>();
	public final Object linkSelectionLock = new Object();
	private int activeLinkIndex = 0;
	private float mouseX, mouseY;
	private boolean mouseInsideCircle = false;

	public LinkSelection() {
		begin = 0.0f;
		end = 1.0f;
		area = GlobalVariables.selectSize;
	}

	public void reset() {
		begin = 0.0f;
		end = 1.0f;
		activeLinkIndex = 0;
	}

	public void update(float pointerGenePosition) {
		begin = pointerGenePosition - 0.25f - area / 2;
		end = pointerGenePosition - 0.25f + area / 2;
		clamp();
	}

	public void updateArea(ConcurrentLinkedQueue<GeneralLink> links) {
		float oldArea = getCurrentArea();
		float change = (area - oldArea) / 2;
		begin -= change;
		end += change;
		clamp();
		updateActiveLinks(links);
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
		if (begin > end) {
			return 1.0f - begin + end;
		}
		return end - begin;
	}

	public void draw(GL2 gl, GeneCircle geneCircle) {
		// TODO : remove magic numbers
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.PLAINMVP);

		SoulGL2 soulgl = new DesktopGL2(gl);

		shader.start(soulgl);

		ShaderMemory.setUniformVec4(soulgl, shader, "color", 1.0f, 1.0f, 0.0f, 1.0f);

		Matrix4 modelMatrix = CoordinateManager.getCircleMatrix();
		float length = geneCircle.getSize() * 0.0505f;
		float width = geneCircle.getSize() * 0.015f;

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		float x, y;
		x = 0.9495f * geneCircle.getXYPosition(this.begin).x;
		y = 0.9495f * geneCircle.getXYPosition(this.begin).y;

		float angle = 180f * (float) Math.atan2(y, x) / (float) Math.PI;

		modelMatrix.translate(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(width, length, 0.2f);

		ShaderMemory.setUniformMat4(soulgl, shader, "MVP", modelMatrix);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		x = 0.9495f * geneCircle.getXYPosition(this.end).x;
		y = 0.9495f * geneCircle.getXYPosition(this.end).y;

		angle = 180f * (float) Math.atan2(y, x) / (float) Math.PI;

		modelMatrix = CoordinateManager.getCircleMatrix();
		modelMatrix.translate(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(width, length, 0.2f);

		ShaderMemory.setUniformMat4(soulgl, shader, "MVP", modelMatrix);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

		shader.stop(soulgl);
	}
	
	public GeneralLink getActiveLink() {
		if(activeLinkIndex >= activeSelection.size())
			return null;
		return activeSelection.get(activeLinkIndex);
	}

	public void handle(KeyEvent keyEvent) {
		synchronized (linkSelectionLock) {
			if (keyEvent.getEventType() == KeyEvent.EVENT_KEY_RELEASED) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
					activeLinkIndex++;
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
					activeLinkIndex--;
				}
			}
			if(keyEvent.getKeyCode() == KeyEvent.VK_UP) { 
			    upKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED); 
			} 
			if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) { 
			    downKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED); 
			}
		}
	}

	public void deactivate() {
		upKeyDown = downKeyDown = false;
		resetArea();
		reset();
	}

	public void tick(float dt, ConcurrentLinkedQueue<GeneralLink> links) {
	    if(upKeyDown) updateArea(-0.01f*dt, links); 
	    if(downKeyDown) updateArea(0.01f*dt, links);
	}

	public void move(float value, ConcurrentLinkedQueue<GeneralLink> links) {
		begin += value;
		end += value;
		clamp();
		updateActiveLinks(links);
	}

	public void resetArea() {
		area = GlobalVariables.selectSize;
	}

	public void updateArea(float f, ConcurrentLinkedQueue<GeneralLink> links) {
		area += f;
		area = Math.min(0.9f, Math.max(area, 0.001f));
		updateArea(links);
	}

	public boolean inSelection(GeneralLink link) {
		if (link.isMinimized()) {
			return false;
		}
		if (begin < end) {
			return (link.aCirclePos >= begin && link.aCirclePos <= end)
					|| (link.bCirclePos >= begin && link.bCirclePos <= end);
		} else {
			return (link.aCirclePos >= begin || link.aCirclePos <= end)
					|| (link.bCirclePos >= begin || link.bCirclePos <= end);
		}
	}

	public void mouseMove(boolean insideCircle, float x, float y) {
		synchronized (linkSelectionLock) {
			mouseInsideCircle = insideCircle; mouseX = x; mouseY = y;
		}
	}

	private void updateActiveLinks(ConcurrentLinkedQueue<GeneralLink> links) {
		synchronized (linkSelectionLock) {
			getActiveSelection().clear();
			for (GeneralLink link : links) {
				if (inSelection(link)) {
					getActiveSelection().add(link);
				}
			}
			activeLinkIndex = 0;
		}
	}

	public ArrayList<GeneralLink> getActiveSelection() {
		return activeSelection;
	}

	public void draw(GL2 gl) {
		synchronized (linkSelectionLock) {
			if (mouseInsideCircle) {
				boolean oneLinkSelected = false;
				for (int i = 0; i < activeSelection.size(); ++i) {
					if (oneLinkSelected) {
						activeSelection.get(i).draw(gl);
					}
					else {
						if (oneLinkSelected = activeSelection.get(i).draw(gl, mouseX, mouseY)) {
							activeLinkIndex = i;
						}
					}
				}
			} else {
				for (int i = 0; i < activeSelection.size(); ++i) {
					if (i != activeLinkIndex) {
						activeSelection.get(i).draw(gl);
					}
				}
				if (activeSelection.size() > activeLinkIndex) {
					activeSelection.get(activeLinkIndex).draw(gl, 0.0f, 0.0f, 1.0f);
				}
			}
		}
	}
}
