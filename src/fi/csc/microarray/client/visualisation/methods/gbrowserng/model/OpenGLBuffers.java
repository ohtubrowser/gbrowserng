package fi.csc.microarray.client.visualisation.methods.gbrowserng.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;

public class OpenGLBuffers {

	public static float bezierStep;
	public static int numBezierPoints = 50, circlePoints = 128;

	public static int circleID, squareID, bezierID, centromereID;

	public static void initBuffers(GL2 gl) {
		initCircleBuffer(gl);
		initSquareBuffer(gl);
		initBezierBuffer(gl);
		initCentromereBuffer(gl);
	}

	private static int generateVBO(GL2 gl, FloatBuffer buffer) {
		IntBuffer temp = IntBuffer.allocate(1);
		gl.glGenBuffers(1, temp);
		int id = temp.get(0);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, id);
		gl.glBufferData(gl.GL_ARRAY_BUFFER, buffer.capacity()*Float.SIZE/Byte.SIZE, buffer, gl.GL_STATIC_DRAW);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
		return id;
	}

	private static void initCircleBuffer(GL2 gl) {
		FloatBuffer circleBuffer = FloatBuffer.allocate((circlePoints + 2) * 2);

		circleBuffer.put(0);
		circleBuffer.put(0);
		for (int i = 0; i < circlePoints + 1; ++i) {
			float x = (float) Math.cos(2 * Math.PI * i / circlePoints);
			float y = (float) Math.sin(2 * Math.PI * i / circlePoints);

			circleBuffer.put(x);
			circleBuffer.put(y);
		}
		circleBuffer.rewind();
		circleID = generateVBO(gl, circleBuffer);
	}

	private static void initSquareBuffer(GL2 gl) {
		FloatBuffer squareBuffer = FloatBuffer.allocate(4 * 2);
		squareBuffer.put(-1);
		squareBuffer.put(-1);
		squareBuffer.put(1);
		squareBuffer.put(-1);
		squareBuffer.put(-1);
		squareBuffer.put(1);
		squareBuffer.put(1);
		squareBuffer.put(1);
		squareBuffer.rewind();
		
		squareID = generateVBO(gl, squareBuffer);
	}

	private static void initBezierBuffer(GL2 gl) {
		final int points = numBezierPoints; // Setting this too low will cause problems on sharp curves
		final float step = 1.0f / points;
		bezierStep = step;
		FloatBuffer bezierBuffer = FloatBuffer.allocate(points+2);
		bezierBuffer.put(step);
		for (int i = 1; i < points; ++i) {
			bezierBuffer.put(((i % 2 == 0) ? i : -i) * step);
		}
		bezierBuffer.put(-bezierBuffer.get(points - 1));
		bezierBuffer.put(-bezierBuffer.get(points - 1));

		bezierBuffer.rewind();
		bezierID = generateVBO(gl, bezierBuffer);
	}

	private static void initCentromereBuffer(GL2 gl) {
		FloatBuffer centromereBuffer = FloatBuffer.allocate(4 * 2);
		centromereBuffer.put(-1);centromereBuffer.put(1);
		centromereBuffer.put(1);centromereBuffer.put(-1);

		centromereBuffer.put(-1);centromereBuffer.put(-1);
		centromereBuffer.put(1);centromereBuffer.put(1);

		centromereBuffer.rewind();
		centromereID = generateVBO(gl, centromereBuffer);
	}
}
