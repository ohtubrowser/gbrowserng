package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;

public class CoordinateManager {
	public static Matrix4 getCircleMatrix() {
		Matrix4 ret = new Matrix4();
		ret.makeScaleMatrix(1.0f, GlobalVariables.aspectRatio, 1.0f);
		return ret;
	}
	public static Vector2 toCircleCoords(Vector2 v) {
		Vector2 ret=new Vector2(v);
		ret.y = ret.y * GlobalVariables.aspectRatio;
		return ret;
	}
	public static float toCircleCoordsX(float x) {
		return x;
	}
	public static float toCircleCoordsY(float y) {
		return (y * GlobalVariables.aspectRatio);
	}
	public static float fromCircleCoordsX(float x) {
		return x;
	}
	public static float fromCircleCoordsY(float y) {
		return y / GlobalVariables.aspectRatio;
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
