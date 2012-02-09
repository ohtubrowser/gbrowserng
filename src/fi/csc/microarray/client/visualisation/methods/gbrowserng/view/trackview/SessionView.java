package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.MouseTracker;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.GenoButton;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.GenoVisualBorder;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import gles.Color;
import gles.SoulGL2;
import gles.primitives.PrimitiveBuffers;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;
import java.nio.IntBuffer;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.media.opengl.GL2;
import managers.ShaderManager;
import managers.TextureManager;
import math.Matrix4;

public class SessionView extends GenosideComponent {

	private boolean active = false;
	private final GenoVisualBorder border = new GenoVisualBorder(this);
	private final GenoButton quitButton = new GenoButton(this, "QUIT_BUTTON", 1.0f, 1.0f, -0.04f, -0.04f, GenoTexID.QUIT_BUTTON);
	private final GenoButton shrinkButton = new GenoButton(this, "SHRINK_BUTTON", 1.0f, 1.0f, -0.08f, -0.04f, GenoTexID.SHRINK_BUTTON);
	private final GenoButton openReadFileButton = new GenoButton(this, "OPENREADFILE_BUTTON", 1.0f, 1.0f, -0.12f, -0.04f, GenoTexID.OPENFILE_BUTTON);
	private final GenoButton openAnotherSessionButton = new GenoButton(this, "ANOTHERSESSION_BUTTON", 1.0f, 1.0f, -0.16f, -0.04f, GenoTexID.OPENFILE_BUTTON);
	private final ConcurrentLinkedQueue<TrackView> trackViews = new ConcurrentLinkedQueue<TrackView>();
	private CoordinateRenderer coordinateView;
	private final Session session;
	private static IntBuffer frameBufferHandle = IntBuffer.allocate(1);
	private IntBuffer textureHandle = IntBuffer.allocate(1);
	private boolean textureCreated = false;
	private final MouseTracker mouseTracker = new MouseTracker();
	private boolean hide = false;
	private float alpha = 1.0f;

	// TODO : maybe split into sessionview and sessionviewGFX?
	public SessionView(Session session, GenosideComponent parent) {
		super(parent);
		this.session = session;

		TrackView trackView1 = new TrackView(this, this.session);
		TrackView trackView2 = new TrackView(this, this.session);
		this.addTrackView(trackView1);
		this.addTrackView(trackView2);

		this.getAnimatedValues().setAnimatedValue("ZOOM", session.targetZoomLevel);
		this.getAnimatedValues().setAnimatedValue("POSITION", session.position);

		this.coordinateView = new CoordinateRenderer(this);
		this.coordinateView.setPosition(0, -0.95f);
		this.coordinateView.setDimensions(2f, 0.1f);
	}

	public void addTrackView(TrackView view) {
		this.trackViews.add(view);
		this.recalculateTrackPositions();
	}

	public int getTrackID(String what) {
		int id = Integer.parseInt(what);
		return id;
	}

	// TODO: Think about what to do with inactive trackviews.
	/*
	 * public void recalculateTrackPositions() { int absents = 0; SpaceDivider
	 * divider = new SpaceDivider(SpaceDivider.VERTICAL, 0.4f, 2.0f);
	 *
	 * for (TrackView t : this.trackViews) { if (t.isActive()) {
	 * divider.insertComponent(t); } else { t.setPosition(-0.7f, 0.8f -
	 * absents++ * 0.20f); t.setDimensions(0.4f, 0.2f); } } divider.calculate();
	 * }
	 */
	public void recalculateTrackPositions() {
		int minimized, reads, heatMaps;
		minimized = reads = heatMaps = 0;

		for (TrackView t : this.trackViews) {
			if (t.getTrackViewMode() == TrackView.READ) {
				++reads;
			} else if (t.getTrackViewMode() == TrackView.HEATMAP) {
				++heatMaps;
			}
		}

		float total_y = 1.9f;
		float heatSize = 0.2f;
		float readSize = (total_y - heatMaps * heatSize) / reads;
		float y = 1.0f;

		for (TrackView t : this.trackViews) {
			if (t.getTrackViewMode() == TrackView.READ) {
				t.setPosition(0, y - readSize / 2);
				t.setDimensions(2, readSize);
				y -= readSize;
			} else if (t.getTrackViewMode() == TrackView.HEATMAP) {
				t.setPosition(0, y - heatSize / 2);
				t.setDimensions(2, heatSize);
				y -= heatSize;
			} else {
				t.setPosition(-0.7f, 0.8f - minimized++ * 0.20f);
				t.setDimensions(0.4f, 0.2f);
			}
		}
	}

	@Override
	public void childComponentCall(String who, String what) {

		if (who.equals("TRACKVIEW") && (what.equals("MINIMIZE"))) {
			recalculateTrackPositions();
		} else if (who.equals("MODESWITCH")) {
			recalculateTrackPositions();
		} else if (who.equals("DELETE")) {
			int id = this.getTrackID(what);
			Iterator<TrackView> it = this.trackViews.iterator();
			while (it.hasNext()) {
				TrackView t = it.next();
				if (t.getId() == id) {
					it.remove();
					break;
				}
			}

			recalculateTrackPositions();
		} else if (who.equals("SHRINK_BUTTON")) {
			if (what.equals("PRESSED")) {
				getParent().childComponentCall("SESSION", "SHRINK");
			}
		} else if (who.equals("QUIT_BUTTON")) {
			if (what.equals("PRESSED")) {
				getParent().childComponentCall("SESSION", "KILL");
			}
		} else if (who.equals("OPENREADFILE_BUTTON")) {
			if (what.equals("PRESSED")) {
				TrackView t = new TrackView(this, this.session);
				this.addTrackView(t);
			}
		} else if (who.equals("ANOTHERSESSION_BUTTON")) {
			if (what.equals("PRESSED")) {
				getParent().childComponentCall("SESSION", "OPEN_ANOTHER");
			}
		}

	}

	public boolean handle(KeyEvent event) {

		// does this view want to handle the input?
		if (event.getKeyChar() == 'b') {
			getParent().childComponentCall("SESSION", "SHRINK");
			return true;
		}


		if (KeyEvent.VK_LEFT == event.getKeyCode()) {
			this.session.position -= 0.05f / this.getAnimatedValues().getAnimatedValue("ZOOM");
			this.getAnimatedValues().setAnimatedValue("POSITION", this.session.position);
			return true;
		} else if (KeyEvent.VK_RIGHT == event.getKeyCode()) {
			this.session.position += 0.05f / this.getAnimatedValues().getAnimatedValue("ZOOM");
			this.getAnimatedValues().setAnimatedValue("POSITION", this.session.position);
			return true;
		} else if (KeyEvent.VK_UP == event.getKeyCode()) {
			zoom(0.9f / 1.0f);
			return true;
		} else if (KeyEvent.VK_DOWN == event.getKeyCode()) {
			zoom(1.0f / 0.9f);
			return true;
		}

		// child views want to handle this?
		boolean handled = false;
		for (TrackView t : trackViews) {
			if (t.handle(event)) {
				handled = true;
			}
		}

		return handled;
	}

	private void zoom(float v) {
		this.session.targetZoomLevel *= v;
		this.getAnimatedValues().setAnimatedValue("ZOOM", this.session.targetZoomLevel);
	}

	@Override
	public boolean handle(MouseEvent event, float screen_x, float screen_y) {

		if (quitButton.handle(event, screen_x, screen_y)) {
			return true;
		}
		if (shrinkButton.handle(event, screen_x, screen_y)) {
			return true;
		}
		if (openReadFileButton.handle(event, screen_x, screen_y)) {
			return true;
		}
		if (openAnotherSessionButton.handle(event, screen_x, screen_y)) {
			return true;
		}

		mouseTracker.handle(event, screen_x, screen_y);

		if (event.getEventType() == MouseEvent.EVENT_MOUSE_DRAGGED) {
			this.session.position -= mouseTracker.getDragging_dx() / this.getAnimatedValues().getAnimatedValue("ZOOM");
			this.session.halfSizeY *= 1.0f + mouseTracker.getDragging_dy();
			this.getAnimatedValues().setAnimatedValue("POSITION", session.position);
			return true;
		}

		// child views want to handle this?
		for (TrackView t : trackViews) {
			if (t.getTrackViewMode() == TrackView.MINIMIZED
					&& t.handle(event, screen_x, screen_y)) {
				return true;
			}
		}

		for (TrackView t : trackViews) {
			if (t.getTrackViewMode() != TrackView.MINIMIZED
					&& t.handle(event, screen_x, screen_y)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void draw(SoulGL2 gl) {
		if (!inScreen()) {
			return;
		}
		if (active) {
			drawActive(gl);
		} else {
			drawFromTexture(gl);
		}
		Color tempcolor = new Color(0, 0, 0, hide ? 0 : 255);
		gl.glEnable(SoulGL2.GL_BLEND);
		border.draw(gl, tempcolor);
		gl.glDisable(SoulGL2.GL_BLEND);
	}

	@Override
	public void userTick(float dt) {
		for (TrackView t : trackViews) {
			t.tick(dt);
		}

		quitButton.tick(dt);
		shrinkButton.tick(dt);
		openReadFileButton.tick(dt);
		openAnotherSessionButton.tick(dt);
		coordinateView.tick(dt);

		this.session.halfSizeX = this.getAnimatedValues().getAnimatedValue("ZOOM");
	}

	public Session getSession() {
		return session;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static void initFrameBuffer(GL2 gl) {
		gl.glGenFramebuffers(1, frameBufferHandle);
	}

	private void genTexture(SoulGL2 gl) {
		gl.glGenTextures(1, textureHandle);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle.get(0));
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGB, 160, 120, 0, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, null);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		updateTexture(gl);
	}

	public void updateTexture(SoulGL2 gl) {
		if (!textureCreated) {
			textureCreated = true;
			genTexture(gl);
		}
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle.get(0));

		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, frameBufferHandle.get(0));
		gl.glFramebufferTexture2D(SoulGL2.GL_FRAMEBUFFER, SoulGL2.GL_COLOR_ATTACHMENT0, SoulGL2.GL_TEXTURE_2D, textureHandle.get(0), 0);

		if (gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER) != GL2.GL_FRAMEBUFFER_COMPLETE) {
			System.out.println("FRAMEBUFFER ERROR --- ABANDON SHIP!\n");
		}

		IntBuffer oldViewPort = IntBuffer.allocate(4);

		gl.glGetIntegerv(SoulGL2.GL_VIEWPORT, oldViewPort);
		gl.glViewport(0, 0, 160, 120);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		// render
		drawActive(gl);
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
		gl.glViewport(oldViewPort.get(0), oldViewPort.get(1), oldViewPort.get(2), oldViewPort.get(3));
	}

	private void drawActive(SoulGL2 gl) {
		for (TrackView t : trackViews) {
			t.draw(gl);
		}
		quitButton.draw(gl);
		shrinkButton.draw(gl);
		openReadFileButton.draw(gl);
		openAnotherSessionButton.draw(gl);
		coordinateView.draw(gl);
	}

	private void drawFromTexture(SoulGL2 gl) {

		Matrix4 modelViewMatrix = new Matrix4();

		modelViewMatrix.makeTranslationMatrix(glx(0), gly(0), 0);
		modelViewMatrix.scale(getDimensions().x * 0.5f, getDimensions().y * 0.5f / GlobalVariables.aspectRatio, 1.0f);
		gl.glEnable(gl.GL_BLEND);
		alpha = hide ? 0.0f : 1.0f;
		Shader shader = ShaderManager.getProgram(GenoShaders.GenoShaderID.TEXRECTANGLE);
		shader.start(gl);
		ShaderMemory.setUniformMat4(gl, shader, "modelViewMatrix", modelViewMatrix);
		ShaderMemory.setUniformVec1(gl, shader, "alpha", alpha);

		gl.glBindTexture(SoulGL2.GL_TEXTURE_2D, textureHandle.get(0));

		PrimitiveBuffers.squareBuffer.rewind();
		PrimitiveBuffers.squareTextureBuffer.rewind();

		int vertexPositionHandle = shader.getAttribLocation(gl, "vertexCoord");
		int texPositionHandle = shader.getAttribLocation(gl, "texCoord");
		gl.glEnableVertexAttribArray(vertexPositionHandle);
		gl.glEnableVertexAttribArray(texPositionHandle);
		gl.glVertexAttribPointer(vertexPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, PrimitiveBuffers.squareBuffer);
		gl.glVertexAttribPointer(texPositionHandle, 2, SoulGL2.GL_FLOAT, false, 0, PrimitiveBuffers.squareTextureBuffer);
		gl.glDrawArrays(SoulGL2.GL_TRIANGLE_STRIP, 0, PrimitiveBuffers.squareBuffer.capacity() / 2);

		gl.glDisableVertexAttribArray(vertexPositionHandle);
		gl.glDisableVertexAttribArray(texPositionHandle);
		shader.stop(gl);
		gl.glDisable(gl.GL_BLEND);

		TextureManager.bindTexture(gl, GenoTexID.FONT); // TODO : this really shouldn't be necessary here
	}

	public void show() {
		hide = false;
	}

	public void hide() {
		hide = true;
	}
}
