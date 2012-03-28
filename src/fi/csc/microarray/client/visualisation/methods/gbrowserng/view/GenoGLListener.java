package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.soulaim.desktop.DesktopAssetManager;
import com.soulaim.desktop.DesktopGL2;
import com.soulaim.tech.gles.primitives.PrimitiveBuffers;
import com.soulaim.tech.gles.renderer.PrimitiveRenderer;
import com.soulaim.tech.gles.renderer.TextRenderer;
import com.soulaim.tech.gles.shaders.DefaultShaders;
import com.soulaim.tech.managers.AssetManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GenoSideTimer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import com.soulaim.tech.managers.TextureManager;
import com.soulaim.desktop.DesktopTextureManager;

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
		SessionView.initFrameBuffer(gl);

		AssetManager.setInstance(new DesktopAssetManager());

		/*
		DesktopTextureManager textureManager = new DesktopTextureManager();
		TextureManager.setInstance(textureManager);
		TextureManager.init(gl);
		*/

		/*
		 * TODO: Soulgl
		DefaultShaders.createDefaultShaders(gl);
		*/
		GenoShaders.createShaders(gl);
		GenoTexID.createTextures(gl);

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

		GlobalVariables.width = width;
		GlobalVariables.height = height;
		GlobalVariables.aspectRatio = (float)width / (float)height;
		
		overView.updateCircleSize();
	}

	public GenosideComponent getRoot() {
		return overView;
	}

	public void die() {
		overView.die = true;
	}
}
