package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkRangeIterator;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import java.awt.event.KeyEvent;
import javax.media.opengl.GL2;
/**
 * Represents a selection of links inside a LinkCollection.
 */

public class LinkSelection {

	private LinkRangeIterator currentSelection;
	private float begin, end, area;
	private boolean upKeyDown = false, downKeyDown = false;
	public final Object linkSelectionLock = new Object();
	private float mouseX, mouseY;
	private boolean mouseInsideCircle = false;
	private GeneCircle geneCircle;
	public GlobalVariables globals;

	public LinkSelection(GlobalVariables globals, GeneCircle geneCircle) {
		begin = 0.0f;
		end = 1.0f;
		area = GlobalVariables.selectSize;
		this.globals = globals;
		this.geneCircle = geneCircle;
	}

	/**
	 * Reset the selection so that all links belong in it.
	 */
	public void reset() {
		begin = 0.0f;
		end = 1.0f;
		currentSelection = null;
		area = GlobalVariables.selectSize;
	}

	/**
	 * Center the selection on a given position on the genecircle.
	 * @param pointerGenePosition The position on the genecircle.
	 * @param linkCollection
	 */
	public void update(float pointerGenePosition, LinkCollection linkCollection) {
		begin = pointerGenePosition - 0.25f - area / 2;
		end = pointerGenePosition - 0.25f + area / 2;
		clamp();
		updateActiveLinks(linkCollection);
	}

	/**
	 * Update the selection when the selection size has been altered.
	 * @param linkCollection 
	 */
	public void updateArea(LinkCollection linkCollection) {
		float oldArea = getCurrentArea();
		float change = (area - oldArea) / 2;
		begin -= change;
		end += change;
		clamp();
		updateActiveLinks(linkCollection);
	}

	/**
	 * Ensure begin and end are within [0,1]
	 */
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

	/**
	 * 
	 * @return The current area selected. 1.0f == Entire circle
	 */
	private float getCurrentArea() {
		if (begin > end) {
			return 1.0f - begin + end;
		}
		return end - begin;
	}

	/**
	 * Draw the yellow clamps indicating the selected region.
	 * @param gl 
	 */
	public void drawClamps(GL2 gl) {
		// TODO : remove magic numbers
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.PLAINMVP);

		shader.start(gl);

		ShaderMemory.setUniformVec4(gl, shader, "color", 1.0f, 1.0f, 0.0f, 1.0f);

		Matrix4 modelMatrix = CoordinateManager.getCircleMatrix(globals);
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

		modelMatrix = CoordinateManager.getCircleMatrix(globals);
		modelMatrix.translate(x, y, 0);
		modelMatrix.rotate(angle + 90f, 0, 0, 1);
		modelMatrix.scale(width, length, 0.2f);

		ShaderMemory.setUniformMat4(gl, shader, "MVP", modelMatrix);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableVertexAttribArray(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

		shader.stop(gl);
	}

	/**
	 * 
	 * @return The selected link.
	 */
	public GeneralLink getActiveLink() {
		return currentSelection == null ? null : currentSelection.value();
	}

	public void handle(KeyEvent keyEvent) {
		synchronized (linkSelectionLock) {
			if (keyEvent.getID() == KeyEvent.KEY_RELEASED) {
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
				upKeyDown = (keyEvent.getID() == KeyEvent.KEY_PRESSED);
			}
			if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
				downKeyDown = (keyEvent.getID() == KeyEvent.KEY_PRESSED);
			}
		}
	}

	/**
	 * Resets the selection (called when user clicks outside circle)
	 */
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

	/**
	 * Move the selection on the genecircle.
	 * @param value Amount to move.
	 * @param linkCollection 
	 */
	public void move(float value, LinkCollection linkCollection) {
		begin += value;
		end += value;
		clamp();
		updateActiveLinks(linkCollection);
	}

	/**
	 * Reset the selection area to the default.
	 */
	public void resetArea() {
		area = GlobalVariables.selectSize;
	}

	/**
	 * Changes the area by f and updates the changes.
	 * @param f
	 * @param linkCollection 
	 */
	public void updateArea(float f, LinkCollection linkCollection) {
		area += f;
		area = Math.min(0.9f, Math.max(area, 0.001f));
		updateArea(linkCollection);
	}

	/**
	 * 
	 * @param link
	 * @return True if link is inside the current selection.
	 */
	public boolean inSelection(GeneralLink link) {
		if (link.isMinimized()) {
			return false;
		}
		if (currentSelection == null) {
			return true;
		}
		return currentSelection.inRange(link);
	}

	/**
	 * Method checks whether link can be located in range of previously selected link (in overview window).
	 * @param index
	 * @return True if the link at index is inside the current selection.
	 */
	public boolean inSelection(int index) {
		if (currentSelection == null) {
			return true;
		}
		return currentSelection.inRange(index);
	}
/**
 * Refreshes the underlying iterator to reflect the current begin and end coordinates.
 * @param linkCollection 
 */
	private void updateActiveLinks(LinkCollection linkCollection) {
		synchronized (linkSelectionLock) {
			currentSelection = geneCircle.getRangeIterator(end, begin, linkCollection);
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

	public boolean mouseInCircle() {
		return mouseInsideCircle;
	}
}
