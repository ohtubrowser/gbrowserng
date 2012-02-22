package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import math.Vector2;

public class CoordinateManager {
	/*
	 * The idea of CircleCoords is that the circle would take a square-sized space
	 * from the center of the screen. That square is mapped as -1, 1, -1, 1 coordinates.
	 */
	public static Vector2 toCircleCoords(Vector2 v) {
		v.x *= 0.5;
		v.y *= 0.5 * GlobalVariables.aspectRatio;
		return v;
	}
	public static float toCircleCoordsX(float x) {
		return x*= 0.5 * 4/3;
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
