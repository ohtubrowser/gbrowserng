package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.CascadingComponent;

import java.util.ArrayList;

/**
 * Creates gaps between chromosomes.
 * 
 * @author Kristiina Paloheimo
 */
public class SpaceDivider {

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	// TODO: These should be decided within a component. Not static during one round of computation!
	private final float maximumSize;
	private final float minimumSize;
	private final int myDirection;
	private ArrayList<CascadingComponent> components;
	
	/**
	 * Returns all CascadingComponents added to the SpaceDivider object
	 * @return components
	 */
	public ArrayList<CascadingComponent> getComponents() {
		return components;
	}

	/**
	 * Returns maximum size of 
	 * @return maximun size
	 */
	public float getMaximumSize() {
		return maximumSize;
	}

	/**
	 * Return minimun size of
	 * @return minimum size
	 */
	public float getMinimumSize() {
		return minimumSize;
	}

	/**
	 * Returns the direction of
	 * @return direction
	 */
	public int getMyDirection() {
		return myDirection;
	}

	/**
	 * Creates a new instances of the Space Divider class
	 * 
	 * @param direction
	 * @param minimumSize
	 * @param maximumSize
	 */
	public SpaceDivider(int direction, float minimumSize, float maximumSize) {
		this.myDirection = direction;
		this.minimumSize = minimumSize;
		this.maximumSize = maximumSize;
		components = new ArrayList<CascadingComponent>();
	}

	/**
	 * Inserts a CascadingComponent into list of dividers of chromosomes
	 * @param component
	 */
	public void insertComponent(CascadingComponent component) {
		components.add(component);
	}

	/**
	 * What happens here is .. 
	 */
	public void calculate() {
		float componentSize = Math.max(this.minimumSize, Math.min(2.0f / components.size(), this.maximumSize));
		float pos = 0.5f * componentSize - 1.0f;

		for(CascadingComponent component : components) {
			if(myDirection == VERTICAL) {
				component.setPosition(0, pos);
				component.setDimensions(2, componentSize);
			} else {
				component.setPosition(pos, 0);
				component.setDimensions(componentSize, 2);
			}

			pos += componentSize;
		}
	}
}
