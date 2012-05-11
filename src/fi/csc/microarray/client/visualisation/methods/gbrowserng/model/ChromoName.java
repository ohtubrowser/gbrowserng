package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;

/**
 * Class represents the name of each chromome as displayed on screen outside circle.
 */
public class ChromoName {
	private ViewChromosome chromosome;
	private Vector2 position = new Vector2();
	private Vector2 size = new Vector2();
	private boolean active = false;
	
	/**
	 * Creates a new ChromoName, linked to a cerain ViewChromosome
	 * @param chromo The chromosome
	 */
	public ChromoName(ViewChromosome chromo) {
		this.chromosome = chromo;
	}
	
	/**
	 * Returns knowledge of whether mouse cursor is hovering over the ChromoName
	 * @param x vertical location of mouse
	 * @param y horizontal location of mouse
	 * @return whether mouse cursor is hovering over the ChromoName
	 */
	public boolean isOver(float x, float y) {
		return active = (position.x - size.x/2 < x
				&& position.x + size.x/2 > x
				&& position.y - size.y/2 < y
				&& position.y + size.y/2 > y);
	}
	
	public ViewChromosome getChromosome() {
		return chromosome;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public Vector2 getPosition() {
		return new Vector2(position);
	}
	
	public Vector2 getSize() {
		return new Vector2(size);
	}

	public void setSize(float w, float h) {
		size.x = w;
		size.y = h;
	}

	public boolean isActive() {
		return active;
	}
}
