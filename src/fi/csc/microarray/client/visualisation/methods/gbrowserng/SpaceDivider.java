package fi.csc.microarray.client.visualisation.methods.gbrowserng;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.CascadingComponent;

import java.util.ArrayList;

public class SpaceDivider {

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	// TODO: These should be decided within a component. Not static during one round of computation!
	private final float maximumSize;
	private final float minimumSize;
	private final int myDirection;

	ArrayList<CascadingComponent> components = new ArrayList<CascadingComponent>();

	public SpaceDivider(int direction, float minimumSize, float maximumSize) {
		this.myDirection = direction;
		this.minimumSize = minimumSize;
		this.maximumSize = maximumSize;
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
				component.setDimensions(2, componentSize);
			} else {
				component.setPosition(pos, 0);
				component.setDimensions(componentSize, 2);
			}

			pos += componentSize;
		}
	}
}
