package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import java.util.concurrent.ConcurrentHashMap;
import math.Vector2;

public class AnimatedValues {

	final ConcurrentHashMap<String, Vector2> animatedValues = new ConcurrentHashMap<String, Vector2>();

	public float getAnimatedValue(String name) {
		if(animatedValues.containsKey(name))
			return animatedValues.get(name).x;
		return 0;
	}

	public void setAnimatedValue(String name, float value) {
		if(animatedValues.containsKey(name)) {
			animatedValues.get(name).y = value;
		}
		else {
			animatedValues.put(name, new Vector2(value, value));
		}
	}

	public void tick(float dt) {
		for(Vector2 v : animatedValues.values()) {
			v.x += (1.0f - Math.pow(0.001f, dt)) * (v.y - v.x);
		}
	}
}

