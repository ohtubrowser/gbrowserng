package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;

/**
 * CoordinateManager class converts coordinates from/to circle coordinates.
 * Circle coordinates are affected by the aspect ratio of the window.
 */

public class CoordinateManager {
	/**
	 * Returns 4x4 matrix, where the y-axis is transformed by
	 * the aspect ratio.
	 * @param globals GlobalVariables object to retreive aspect ratio from.
	 * @return 4x4 matrix, where the y-axis is transformed by the aspect ratio.
	 */
	public static Matrix4 getCircleMatrix(GlobalVariables globals) {
		Matrix4 ret = new Matrix4();
		ret.makeScaleMatrix(1.0f, globals.aspectRatio, 1.0f);
		return ret;
	}

	/**
	 * Converts Vector2 to circle coordinates.
	 * @param globals GlobalVariables object to retreive aspect ratio from.
	 * @param v Vector2 to transform.
	 * @return Transformed Vector2
	 */
	public static Vector2 toCircleCoords(GlobalVariables globals, Vector2 v) {
		Vector2 ret=new Vector2(v);
		ret.y = ret.y * globals.aspectRatio;
		return ret;
	}

	/**
	 * Converts float to circle's x-coordinate.
	 * @param globals GlobalVariables object to retreive aspect ratio from.
	 * @param x Float to transform.
	 * @return Transformed float.
	 */
	public static float toCircleCoordsX(GlobalVariables globals, float x) {
		return x;
	}
	/**
	 * Converts float to circle's y-coordinate.
	 * @param globals GlobalVariables object to retreive aspect ratio from.
	 * @param y Float to transform.
	 * @return Transformed float.
	 */
	public static float toCircleCoordsY(GlobalVariables globals, float y) {
		return (y * globals.aspectRatio);
	}
	/**
	 * Converts float from circle's y-coordinate.
	 * @param globals GlobalVariables object to retreive aspect ratio from.
	 * @param x Float to transform.
	 * @return Transformed float.
	 */
	public static float fromCircleCoordsX(GlobalVariables globals, float x) {
		return x;
	}
	/**
	 * Converts float from circle's y-coordinate.
	 * @param globals GlobalVariables object to retreive aspect ratio from.
	 * @param y Float to transform.
	 * @return Transformed float.
	 */
	public static float fromCircleCoordsY(GlobalVariables globals, float y) {
		return y / globals.aspectRatio;
	}
}
