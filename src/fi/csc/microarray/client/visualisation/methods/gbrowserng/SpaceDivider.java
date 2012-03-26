package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.CascadingComponent;

import java.util.ArrayList;

/**
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
	
	public ArrayList<CascadingComponent> getComponents() {
		return components;
	}

	public float getMaximumSize() {
		return maximumSize;
	}

	public float getMinimumSize() {
		return minimumSize;
	}

	public int getMyDirection() {
		return myDirection;
	}

	public SpaceDivider(int direction, float minimumSize, float maximumSize) {
		this.myDirection = direction;
		this.minimumSize = minimumSize;
		this.maximumSize = maximumSize;
		components = new ArrayList<CascadingComponent>();
	}

	public void insertComponent(CascadingComponent component) {
		components.add(component);
	}

	public void calculate() {
		float componentSize = Math.max(this.minimumSize, Math.min(2.0f / components.size(), this.maximumSize));
		float pos = 0.5f * componentSize - 1.0f;

		for(CascadingComponent component : components) {
			if(myDirection == VERTICAL) {
				component.setPosition(0, pos);
				System.out.println("Vertical");
				component.setDimensions(2, componentSize);
			} else {
				System.out.println("Horizontal");
				component.setPosition(pos, 0);
				component.setDimensions(componentSize, 2);
			}

			pos += componentSize;
		}
	}
}
