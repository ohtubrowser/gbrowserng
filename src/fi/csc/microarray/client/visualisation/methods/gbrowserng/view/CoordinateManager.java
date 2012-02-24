package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import math.Matrix4;
import math.Vector2;

public class CoordinateManager {
	
	public static final float XASPECT=4.0f/3.5f;
	
	public static Matrix4 getCircleMatrix() {
		Matrix4 ret = new Matrix4();
		ret.makeScaleMatrix(0.5f*XASPECT, 0.5f*GlobalVariables.aspectRatio, 1.0f);
		return ret;
	}
	public static Vector2 toCircleCoords(Vector2 v) {
		v.x *= 0.5;
		v.y *= 0.5 * GlobalVariables.aspectRatio;
		return v;
	}
	public static float toCircleCoordsX(float x) {
		return x*= 0.5 * XASPECT;
	}
	public static float toCircleCoordsY(float y) {
		return y*= 0.5 * GlobalVariables.aspectRatio;
	}
	public static int toCircleCoordsX(int x) {
		return x;
	}
	public static int toCircleCoordsY(int y) {
		return y;
	}
	public static Vector2 fromCircleCoords(Vector2 v) {
		v.x *= 2;
		v.y /= GlobalVariables.aspectRatio * 2;
		return v;
	}
}
