package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;

public abstract class CascadingComponent {

	CascadingComponent parent;

	public CascadingComponent(CascadingComponent parent) {
		this.parent = parent;
	}

	private final Vector2 currentPosition = new Vector2();
	private final Vector2 targetPosition = new Vector2();
	private final Vector2 currentDimensions = new Vector2();
	private final Vector2 targetDimensions = new Vector2();

	private final Vector2 relativePosition = new Vector2();
	private final Vector2 relativeDimensions = new Vector2();

	public final boolean inComponent(float x, float y) {
		if(x < this.getPosition().x - this.getDimensions().x * 0.5f || x > this.getPosition().x + this.getDimensions().x * 0.5f) return false;
		if(y < this.getPosition().y - this.getDimensions().y * 0.5f || y > this.getPosition().y + this.getDimensions().y * 0.5f) return false;
		return true;
	}

	public final boolean inScreen() {
		if(getPosition().x + getDimensions().x * 0.5f < -1) return false;
		if(getPosition().x - getDimensions().x * 0.5f > +1) return false;
		if(getPosition().y + getDimensions().y * 0.5f < -1) return false;
		if(getPosition().y - getDimensions().y * 0.5f > +1) return false;
		return true;
	}

	public CascadingComponent getParent() {
		return parent;
	}

	public final void cascadingTick(float dt) {

		currentPosition.x += (targetPosition.x - currentPosition.x) * (Math.min(1.0f, GlobalVariables.animationConstant*dt));
		currentPosition.y += (targetPosition.y - currentPosition.y) * (Math.min(1.0f, GlobalVariables.animationConstant*dt));

		currentDimensions.x += (targetDimensions.x - currentDimensions.x) * (Math.min(1.0f, GlobalVariables.animationConstant*dt));
		currentDimensions.y += (targetDimensions.y - currentDimensions.y) * (Math.min(1.0f, GlobalVariables.animationConstant*dt));

		updateDimensions();
		updatePosition();
	}

	public final Vector2 getPosition() {
		return relativePosition;
	}

	public final Vector2 getDimensions() {
		return relativeDimensions;
	}
	
	public final Vector2 getTargetDimensions()
	{
		return targetDimensions;
	}

	private final void updatePosition() {
		if(parent == null)
			relativePosition.copyFrom(currentPosition);
		else
			relativePosition.copyFrom( parent.getPosition().plus(new Vector2(parent.getDimensions().x * 0.5f * currentPosition.x, parent.getDimensions().y * 0.5f * currentPosition.y)) );
	}

	private final void updateDimensions() {
		if(parent == null) {
			relativeDimensions.x = 2;
			relativeDimensions.y = 2;
		}
		else
			relativeDimensions.copyFrom( parent.getDimensions().scaled(0.5f).scale(new Vector2(currentDimensions.x * 0.5f, currentDimensions.y * 0.5f)).scale(2.0f) );
	}

	// transforms a float from -1, 1 range to the correct position within this component
	public final float glx(float x) {
		return getPosition().x + getDimensions().x * 0.5f * x;
	}

	// transforms a float from -1, 1 range to the correct position within this component
	public final float gly(float y) {
		return getPosition().y + getDimensions().y * 0.5f * y;
	}

	public final float glySize(float y) {
		return this.getDimensions().y/2 * y;
	}

	public final float glxSize(float x) {
		return this.getDimensions().x/2 * x;
	}

	public final void setPosition(float x, float y) {
		targetPosition.x = x;
		targetPosition.y = y;
	}

	public final void setDimensions(float w, float h) {
		targetDimensions.x = w;
		targetDimensions.y = h;
	}

	public final void modifyPosition(float x, float y) {
		targetPosition.x += x;
		targetPosition.y += y;
	}

	public final void modifyDimensions(float x, float y) {
		targetDimensions.x += x;
		targetDimensions.y += y;
	}


}
