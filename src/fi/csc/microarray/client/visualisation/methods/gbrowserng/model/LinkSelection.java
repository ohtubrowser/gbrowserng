package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.newt.event.KeyEvent;
import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.media.opengl.GL2;

public class LinkSelection {

	float begin, end, area;
	private boolean upKeyDown = false, downKeyDown = false;
	private ArrayList<GeneralLink> activeSelection = new ArrayList<GeneralLink>();
	public final Object linkSelectionLock = new Object();
	private int activeLinkIndex = 0;

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
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.PLAINMVP);

		shader.start(gl);

		ShaderMemory.setUniformVec4(gl, shader, "color", 1.0f, 1.0f, 0.0f, 1.0f);

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

		ShaderMemory.setUniformMat4(gl, shader, "MVP", modelMatrix);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		x = 0.9495f * geneCircle.getXYPosition(this.end).x;
		y = 0.9495f * geneCircle.getXYPosition(this.end).y;

		angle = 180f * (float) Math.atan2(y, x) / (float) Math.PI;

		modelMatrix.makeTranslationMatrix(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(width, length, 0.2f);

		ShaderMemory.setUniformMat4(gl, shader, "MVP", modelMatrix);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

		shader.stop(gl);
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
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					GeneralLink l = activeSelection.get(activeLinkIndex);
					Chromosome a = l.getAChromosome();
					Chromosome b = l.getBChromosome();
					System.out.println("USER WANTS TO OPEN CONNECTION WITH");
					System.out.println("Start:\n Chromosome: " + a.getName() + " Position: " + l.getaStart());
					System.out.println("End:\n Chromosome: " + b.getName() + " Position: " + l.getbStart());
				}
				if (activeLinkIndex >= activeSelection.size()) {
					activeLinkIndex -= activeSelection.size();
				}
				if (activeLinkIndex < 0) {
					activeLinkIndex += activeSelection.size();
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
			for (int i = 0; i < activeSelection.size(); ++i) {
				if (i != activeLinkIndex) {
					activeSelection.get(i).draw(gl, 1.0f, 0.0f, 0.0f);
				}
			}
			if (activeSelection.size() > activeLinkIndex) {
				activeSelection.get(activeLinkIndex).draw(gl, 0.0f, 0.0f, 1.0f);
			}
		}
	}
}
