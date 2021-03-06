package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.soulaim.desktop.DesktopAssetManager;
import com.soulaim.tech.gles.primitives.PrimitiveBuffers;
import com.soulaim.tech.gles.renderer.PrimitiveRenderer;
import com.soulaim.tech.gles.renderer.TextRenderer;
import com.soulaim.tech.managers.AssetManager;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GenoSideTimer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class GenoGLListener implements GLEventListener, Runnable {

	private GenoSideTimer drawTimer = new GenoSideTimer();
	private GenoSideTimer tickTimer = new GenoSideTimer();
	private OverView overView;

	public GenoGLListener(OverView overView) {
		this.overView = overView;
	}

	@Override
	public void run() {
		while (!overView.die) {
			tickTimer.start();

			float dt = tickTimer.getDT();
			overView.tick(dt);

			overView.getTickCounter().addNano(tickTimer.end());

			try {
				Thread.sleep(0, 10);
			} catch (InterruptedException ex) {
				Logger.getLogger(GenoGLListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void display(GLAutoDrawable drawable) {
		drawTimer.start();

		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		overView.draw(gl);

		overView.getDrawCounter().addNano(drawTimer.end());
	}

	public void dispose(GLAutoDrawable drawable) {
	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		System.out.println("OpenGL version :" + gl.glGetString(GL2.GL_VERSION));

		gl.setSwapInterval(1);
		TextRenderer.createInstance();
		PrimitiveBuffers.createBuffers();
		OpenGLBuffers.initBuffers(gl);
		//SessionViewCapsule.initFrameBuffer(gl);
		
		AssetManager.setInstance(new DesktopAssetManager());

		GenoShaders.createShaders(gl);

		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);

		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glDisable(GL2.GL_DEPTH_TEST);

		PrimitiveRenderer.onSurfaceChanged(width, height);
		TextRenderer.getInstance().onSurfaceChanged(width, height);

		overView.globals.width = width;
		overView.globals.height = height;
		overView.globals.aspectRatio = (float)width / (float)height;
		
		overView.updateCircleSize();
	}

	public GenosideComponent getRoot() {
		return overView;
	}

	public void die() {
		overView.die = true;
	}
}
