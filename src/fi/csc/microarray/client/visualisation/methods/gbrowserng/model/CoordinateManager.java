package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;

public class CoordinateManager {
	public static Matrix4 getCircleMatrix(GlobalVariables globals) {
		Matrix4 ret = new Matrix4();
		ret.makeScaleMatrix(1.0f, globals.aspectRatio, 1.0f);
		return ret;
	}
	public static Vector2 toCircleCoords(GlobalVariables globals, Vector2 v) {
		Vector2 ret=new Vector2(v);
		ret.y = ret.y * globals.aspectRatio;
		return ret;
	}
	public static float toCircleCoordsX(GlobalVariables globals, float x) {
		return x;
	}
	public static float toCircleCoordsY(GlobalVariables globals, float y) {
		return (y * globals.aspectRatio);
	}
	public static float fromCircleCoordsX(GlobalVariables globals, float x) {
		return x;
	}
	public static float fromCircleCoordsY(GlobalVariables globals, float y) {
		return y / globals.aspectRatio;
	}
}
