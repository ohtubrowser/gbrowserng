package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GenoSideTimer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import gles.SoulGL2;
import gles.primitives.PrimitiveBuffers;
import gles.renderer.PrimitiveRenderer;
import gles.renderer.TextRenderer;
import gles.shaders.DefaultShaders;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import managers.AssetManager;
import managers.TextureManager;
import soulaim.DesktopAssetManager;
import soulaim.DesktopGL2;
import soulaim.DesktopTextureManager;

public class GenoGLListener implements GLEventListener, Runnable {

	private GenoSideTimer timer = new GenoSideTimer();
	private OverView overView;

	public GenoGLListener(OverView overView) {
		this.overView = overView;
	}

	@Override
	public void run() {
		while (!overView.die) {
			float dt = timer.getDT();
			overView.tick(dt);
			try {
				Thread.sleep(0, 10);
			} catch (InterruptedException ex) {
				Logger.getLogger(GenoGLListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void display(GLAutoDrawable drawable) {
		timer.start();

		SoulGL2 gl = new DesktopGL2(drawable.getGL().getGL2());
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		overView.draw(gl);

		overView.getFpsCounter().addNano(timer.end());
	}

	public void dispose(GLAutoDrawable drawable) {
	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.setSwapInterval(1);
		TextRenderer.createInstance();
		PrimitiveBuffers.createBuffers();
		GeneralLink.initBezierPoints();
		SessionView.initFrameBuffer(gl);

		AssetManager.setInstance(new DesktopAssetManager());

		DesktopTextureManager textureManager = new DesktopTextureManager();
		textureManager.setGL2(drawable.getGL().getGL2());
		TextureManager.setInstance(textureManager);
		TextureManager.init(new DesktopGL2(gl));

		DefaultShaders.createDefaultShaders(new DesktopGL2(gl));
		GenoShaders.createShaders(new DesktopGL2(gl));
		GenoTexID.createTextures(new DesktopGL2(gl));

		Font font;
		float fontSize = 40f;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/drawable/Tiresias Signfont Bold.ttf")).deriveFont(fontSize);
		} catch (IOException e) {
			font = new Font("SansSerif", Font.BOLD, (int)fontSize);
		} catch (FontFormatException e) {
			font = new Font("SansSerif", Font.BOLD, (int)fontSize);
		}
		overView.textRenderer = new com.jogamp.opengl.util.awt.TextRenderer(font, true, true);

		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		SoulGL2 gl = new DesktopGL2(drawable.getGL().getGL2());
		gl.glViewport(0, 0, width, height);

		gl.glEnable(SoulGL2.GL_CULL_FACE);
		gl.glDisable(SoulGL2.GL_DEPTH_TEST);

		PrimitiveRenderer.onSurfaceChanged(width, height);
		TextRenderer.getInstance().onSurfaceChanged(width, height);

		GlobalVariables.width = width;
		GlobalVariables.height = height;
		GlobalVariables.aspectRatio = width * 1.0f / height;
	}

	public GenosideComponent getRoot() {
		return overView;
	}

	public void die() {
		overView.die = true;
	}
}
