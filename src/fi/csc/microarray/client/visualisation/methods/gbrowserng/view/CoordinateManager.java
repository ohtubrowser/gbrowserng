package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import math.Matrix4;
import math.Vector2;

public class CoordinateManager {
	
	public static final float XASPECT=4.0f/3.5f;
	public static final float scale=0.5f;
	
	public static Matrix4 getCircleMatrix() {
		Matrix4 ret = new Matrix4();
		ret.makeScaleMatrix(scale*XASPECT, scale*GlobalVariables.aspectRatio, 1.0f);
		return ret;
	}
	public static Vector2 toCircleCoords(Vector2 v) {
		v.x *= scale * XASPECT;
		v.y *= scale * GlobalVariables.aspectRatio;
		return v;
	}
	public static float toCircleCoordsX(float x) {
		return (x * scale * XASPECT);
	}
	public static float toCircleCoordsY(float y) {
		return (y * scale * GlobalVariables.aspectRatio);
	}
	public static float fromCircleCoordsX(float x) {
		return (x / scale / XASPECT);
	}
	public static float fromCircleCoordsY(float y) {
		return (y / scale / GlobalVariables.aspectRatio);
	}
	/*
	public static int toCircleCoordsX(int x) {
		return x;
	}
	public static int toCircleCoordsY(int y) {
		return y;
	}
	*/
}
