package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import math.Matrix4;
import math.Vector2;

public class CoordinateManager {

	public static final float XASPECT=4.0f/4.0f;
	public static final float scale=1.0f;

	public static Matrix4 getCircleMatrix() {
		Matrix4 ret = new Matrix4();
		ret.makeScaleMatrix(scale*XASPECT, scale*GlobalVariables.aspectRatio, 1.0f);
		return ret;
	}
	public static Vector2 toCircleCoords(Vector2 v) {
		Vector2 ret=new Vector2(v);
		ret.x = ret.x * scale * XASPECT;
		ret.y = ret.y * scale * GlobalVariables.aspectRatio;
		return ret;
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
	
	public float convertW(int c) {
		int hw = GlobalVariables.width/2;
		return ((float)c)/hw;
	}
	
	public float convertH(int c) {
		int hh = GlobalVariables.height/2;
		return ((float)c)/hh;
	}
	
	public int convertW(float c) {
		int hw = GlobalVariables.width/2;
		return (int)(hw*c);
	}
	
	public int convertH(float c) {
		int hh = GlobalVariables.height/2;
		return (int)(hh*c);
	}
	
	public int convertX(float c) {
		int hw = GlobalVariables.width/2;
		return (int)(hw+(hw*c));
	}
	
	public int convertY(float c) {
		int hh = GlobalVariables.height/2;
		return (int)(hh+(hh*c));
	}
	
	public float convertX(int c) {
		int hw = GlobalVariables.width/2;
		return ((float)c)/hw+1f;
	}
	
	public float convertY(int c) {
		int hh = GlobalVariables.height/2;
		return ((float)c)/hh+1f;
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
