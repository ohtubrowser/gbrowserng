package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import java.nio.FloatBuffer;

public class OpenGLBuffers {

	public static FloatBuffer circleBuffer,
			squareBuffer,
			bezierBuffer,
			centromereBuffer;
	public static float bezierStep;
	public static int numBezierPoints = 50, circlePoints = 128;

	public static void initBuffers() {
		initCircleBuffer();
		initSquareBuffer();
		initBezierBuffer();
		initCentromereBuffer();
	}

	private static void initCircleBuffer() {
		circleBuffer = FloatBuffer.allocate((circlePoints + 2) * 2);

		circleBuffer.put(0);
		circleBuffer.put(0);
		for (int i = 0; i < circlePoints + 1; ++i) {
			float x = (float) Math.cos(2 * Math.PI * i / circlePoints);
			float y = (float) Math.sin(2 * Math.PI * i / circlePoints);

			circleBuffer.put(x);
			circleBuffer.put(y);
		}
	}

	private static void initSquareBuffer() {
		squareBuffer = FloatBuffer.allocate(4 * 2);
		squareBuffer.put(-1);
		squareBuffer.put(-1);
		squareBuffer.put(1);
		squareBuffer.put(-1);
		squareBuffer.put(-1);
		squareBuffer.put(1);
		squareBuffer.put(1);
		squareBuffer.put(1);
	}

	private static void initBezierBuffer() {
		final int points = numBezierPoints; // Setting this too low will cause problems on sharp curves
		final float step = 1.0f / points;
		bezierStep = step;
		bezierBuffer = FloatBuffer.allocate(points + 1);
		bezierBuffer.put(step);
		for (int i = 1; i < points; ++i) {
			bezierBuffer.put(((i % 2 == 0) ? i : -i) * step);
		}
		bezierBuffer.put(-bezierBuffer.get(points - 1));
	}

	private static void initCentromereBuffer() {
		centromereBuffer = FloatBuffer.allocate(6 * 2);
		centromereBuffer.put(-1);centromereBuffer.put(1);
		centromereBuffer.put(-1);centromereBuffer.put(-1);
		centromereBuffer.put(0);centromereBuffer.put(0);
		centromereBuffer.put(1);centromereBuffer.put(1);
		centromereBuffer.put(1);centromereBuffer.put(-1);
	}
}
