package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.jogamp.newt.event.KeyEvent;
import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkRangeIterator;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.media.opengl.GL2;

public class LinkSelection {

	LinkRangeIterator currentSelection;
	float begin, end, area;
	private boolean upKeyDown = false, downKeyDown = false;
	public final Object linkSelectionLock = new Object();
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
		currentSelection = null;
	}

	public void update(float pointerGenePosition, LinkCollection linkCollection) {
		begin = pointerGenePosition - 0.25f - area / 2;
		end = pointerGenePosition - 0.25f + area / 2;
		clamp();
		updateActiveLinks(linkCollection);
	}

	public void updateArea(LinkCollection linkCollection) {
		float oldArea = getCurrentArea();
		float change = (area - oldArea) / 2;
		begin -= change;
		end += change;
		clamp();
		updateActiveLinks(linkCollection);
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

		ShaderMemory.setUniformMat4(gl, shader, "MVP", modelMatrix);

		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		x = 0.9495f * geneCircle.getXYPosition(this.end).x;
		y = 0.9495f * geneCircle.getXYPosition(this.end).y;

		angle = 180f * (float) Math.atan2(y, x) / (float) Math.PI;

		modelMatrix = CoordinateManager.getCircleMatrix();
		modelMatrix.translate(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(width, length, 0.2f);

		ShaderMemory.setUniformMat4(gl, shader, "MVP", modelMatrix);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

		shader.stop(gl);
	}

	public GeneralLink getActiveLink() {
		return currentSelection == null ? null : currentSelection.value();
	}

	public void handle(KeyEvent keyEvent) {
		synchronized (linkSelectionLock) {
			if (keyEvent.getEventType() == KeyEvent.EVENT_KEY_RELEASED) {
				int oldIndex = currentSelection.currentIndex;
				if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
					do {
						currentSelection.increment();
						if(currentSelection.currentIndex == currentSelection.endIndex)
							currentSelection.currentIndex = currentSelection.startIndex;
					} while (currentSelection.value().isMinimized() && currentSelection.currentIndex != oldIndex);
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
					do {
						if(currentSelection.currentIndex == currentSelection.startIndex)
							currentSelection.currentIndex = currentSelection.endIndex;
						currentSelection.decrement();
					} while (currentSelection.value().isMinimized() && currentSelection.currentIndex != oldIndex);
				}
			}
			if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
				upKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED);
			}
			if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
				downKeyDown = (keyEvent.getEventType() == KeyEvent.EVENT_KEY_PRESSED);
			}
		}
	}

	public void deactivate() {
		upKeyDown = downKeyDown = false;
		resetArea();
		reset();
	}

	public void tick(float dt, LinkCollection linkCollection) {
		if (upKeyDown) {
			updateArea(-0.01f * dt, linkCollection);
		}
		if (downKeyDown) {
			updateArea(0.01f * dt, linkCollection);
		}
	}

	public void move(float value, LinkCollection linkCollection) {
		begin += value;
		end += value;
		clamp();
		updateActiveLinks(linkCollection);
	}

	public void resetArea() {
		area = GlobalVariables.selectSize;
	}

	public void updateArea(float f, LinkCollection linkCollection) {
		area += f;
		area = Math.min(0.9f, Math.max(area, 0.001f));
		updateArea(linkCollection);
	}

	public boolean inSelection(GeneralLink link) {
		if (link.isMinimized()) {
			return false;
		}
		if (currentSelection == null) {
			return true;
		}
		return currentSelection.inRange(link);
	}

	public boolean inSelection(int index) {
		if (currentSelection == null) {
			return true;
		}
		return currentSelection.inRange(index);
	}

	private void updateActiveLinks(LinkCollection linkCollection) {
		synchronized (linkSelectionLock) {
			currentSelection = linkCollection.getRangeIterator(end, begin);
		}
	}

	public void mouseMove(boolean insideCircle, float x, float y) {
		synchronized (linkSelectionLock) {
			mouseInsideCircle = insideCircle;
			mouseX = x;
			mouseY = y;
		}
	}

	public void draw(GL2 gl) {
		synchronized (linkSelectionLock) {
			if (currentSelection == null) {
				return;
			}
			if (mouseInsideCircle) {
				boolean oneLinkSelected = false;
				LinkRangeIterator rangeIterator = new LinkRangeIterator(currentSelection);
				rangeIterator.rewind();
				while (rangeIterator.currentIndex != rangeIterator.endIndex) {
					if (oneLinkSelected) {
						rangeIterator.value().draw(gl);
					} else {
						if (!rangeIterator.value().isMinimized() && (oneLinkSelected = rangeIterator.value().draw(gl, mouseX, mouseY))) {
							currentSelection.currentIndex = rangeIterator.currentIndex;
						}
					}
					rangeIterator.increment();

				}
			} else {
				LinkRangeIterator rangeIterator = new LinkRangeIterator(currentSelection);
				rangeIterator.rewind();
				while (rangeIterator.currentIndex != rangeIterator.endIndex) {
					if (rangeIterator.currentIndex != currentSelection.currentIndex) {
						rangeIterator.value().draw(gl);
					}
					rangeIterator.increment();
				}
				if (getActiveLink() != null) {
					getActiveLink().draw(gl, 0.0f, 0.0f, 1.0f);
				}
			}
		}
	}
}
