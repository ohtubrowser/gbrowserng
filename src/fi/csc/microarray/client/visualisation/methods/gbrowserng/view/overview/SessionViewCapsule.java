package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.soulaim.tech.gles.Color;
import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.PrimitiveRenderer;
import javax.media.opengl.GL2;

public class SessionViewCapsule extends GenosideComponent {

	private boolean needsTextureUpdate = true;
	private final GeneralLink linkData;
	private final GeneCircle geneCircle;
	private final ViewChromosome chr;
	private final long chrPosition;
	
	// TODO: SessionViewCapsuleData class could contain this.
	private boolean isActive = false;
	private boolean dying = false;
	private float death = 0;
	private Color backGroundColor = new Color(0, 0, 0, 255);

	private Vector2 capsulePosition = new Vector2(0.4f, 0.5f);
	private Vector2 circlePosition = new Vector2(1, 0);

	private float relativePosition;

	private float dimX = 0.1f;
	private float dimY = 0.1f;
	private boolean hover = false;

	public SessionViewCapsule(ViewChromosome chr, long chrPosition, GeneralLink linkData, GeneCircle geneCircle) {
		super(null); // should be ok
		this.linkData = linkData;
		this.geneCircle = geneCircle;
		this.chr = chr;
		this.chrPosition = chrPosition;
		setRelativePosition(geneCircle.getRelativePosition(chr.getChromosomeNumber()-1, chrPosition));
	}

	SessionViewCapsule(GeneralLink link, float relativePosition, GeneCircle geneCircle) {
		super(null);
		this.linkData = link;
		this.geneCircle = geneCircle;
		setRelativePosition(relativePosition);

		this.chr = geneCircle.getChromosomeByRelativePosition(relativePosition);
		this.chrPosition = geneCircle.getChromosomePosition(chr, relativePosition);
	}

	public Vector2 getCapsulePosition() {
		return capsulePosition;
	}

	public void setRelativePosition(float relativePosition) {
		this.relativePosition = relativePosition;
		updateGeneCirclePosition();
	}

	public Vector2 getGeneCirclePosition() {
		return circlePosition;
	}

	public void updateGeneCirclePosition() {
		circlePosition.x = geneCircle.getSize();
		circlePosition.y = 0;
		circlePosition.rotate(2 * (float) Math.PI * relativePosition);
		circlePosition = CoordinateManager.toCircleCoords(circlePosition);
	}

	public boolean isAlive() {
		return death < 1.0f;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setDimensions(float x, float y) {
		dimX = x;
		dimY = y;
	}

	public boolean inCapsule(float screen_x, float screen_y) {
		return (capsulePosition.x - dimX/2 < screen_x
				&& capsulePosition.x + dimX/2 > screen_x
				&& capsulePosition.y - dimY/(2*GlobalVariables.aspectRatio) < screen_y
				&& capsulePosition.y + dimY/(2*GlobalVariables.aspectRatio) > screen_y);
	}

	@Override
	public boolean handle(MouseEvent event, float screen_x, float screen_y) {
		hover = true;
		return inCapsule(screen_x, screen_y);
	}

	@Override
	public boolean handle(KeyEvent event) {
		return false;
	}

	@Override
	public void draw(GL2 gl) {
		//link.draw(gl);

		gl.glEnable(GL2.GL_BLEND);
		PrimitiveRenderer.drawRectangle(capsulePosition.x, capsulePosition.y, dimX, dimY / GlobalVariables.aspectRatio, gl, backGroundColor);
		gl.glDisable(GL2.GL_BLEND);
	}

	public void setNeedsTextureUpdate() {
		needsTextureUpdate = true;
	}

	@Override
	public void userTick(float dt) {
		if (needsTextureUpdate) {
			return;
		}
		if (dying) {
			death += dt;
		}
		
		setCirclePosition(circlePosition.x, circlePosition.y);
//		link.tick(dt);
	}

	public void die() {
		dying = true;
//		link.hide();
	}

	public boolean isDying() {
		return dying;
	}

	void drawToTexture(GL2 gl) {
		/*
		sessionView.updateTexture(gl);
		needsTextureUpdate = false;*/
	}
	
	public boolean isLinkSession() {
		return linkData != null;
	}

	public GeneralLink getLink() {
		return linkData;
	}

	public ViewChromosome getChromosome() {
		return chr;
	}

	public long getChrPosition() {
		return chrPosition;
	}

	private void setCirclePosition(float x, float y) {
		circlePosition.x = x;
		circlePosition.y = y;
	}


	/*public static void initFrameBuffer(GL2 gl) {
		gl.glGenFramebuffers(1, frameBufferHandle);
	}

	private void genTexture(GL2 gl) {
		gl.glGenTextures(1, textureHandle);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle.get(0));
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGB, 160, 120, 0, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, null);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
		updateTexture(gl);
	}

	public void updateTexture(GL2 gl) {
		if (!textureCreated) {
			textureCreated = true;
			genTexture(gl);
		}

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle.get(0));

		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, frameBufferHandle.get(0));
		gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, textureHandle.get(0), 0);

		if (gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER) != GL2.GL_FRAMEBUFFER_COMPLETE) {
			System.out.println("FRAMEBUFFER ERROR --- ABANDON SHIP!\n");
		}

		IntBuffer oldViewPort = IntBuffer.allocate(4);

		gl.glGetIntegerv(GL2.GL_VIEWPORT, oldViewPort);
		gl.glViewport(0, 0, 160, 120);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		// render
		drawActive(gl);
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
		gl.glViewport(oldViewPort.get(0), oldViewPort.get(1), oldViewPort.get(2), oldViewPort.get(3));
	}

	private void drawFromTexture(GL2 gl) {

		Matrix4 modelViewMatrix = new Matrix4();

		modelViewMatrix.makeTranslationMatrix(glx(0), gly(0), 0);
		modelViewMatrix.scale(getDimensions().x * 0.5f, getDimensions().y * 0.5f / GlobalVariables.aspectRatio, 1.0f);
		gl.glEnable(gl.GL_BLEND);
		alpha = hide ? 0.0f : 1.0f;
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.TEXRECTANGLE);
		shader.start(gl);
		ShaderMemory.setUniformMat4(gl, shader, "modelViewMatrix", modelViewMatrix);
		ShaderMemory.setUniformVec1(gl, shader, "alpha", alpha);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle.get(0));

		PrimitiveBuffers.squareBuffer.rewind();
		PrimitiveBuffers.squareTextureBuffer.rewind();

		int vertexPositionHandle = shader.getAttribLocation(gl, "vertexCoord");
		int texPositionHandle = shader.getAttribLocation(gl, "texCoord");
		gl.glEnableVertexAttribArray(vertexPositionHandle);
		gl.glEnableVertexAttribArray(texPositionHandle);
		gl.glVertexAttribPointer(vertexPositionHandle, 2, GL2.GL_FLOAT, false, 0, PrimitiveBuffers.squareBuffer);
		gl.glVertexAttribPointer(texPositionHandle, 2, GL2.GL_FLOAT, false, 0, PrimitiveBuffers.squareTextureBuffer);
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, PrimitiveBuffers.squareBuffer.capacity() / 2);

		gl.glDisableVertexAttribArray(vertexPositionHandle);
		gl.glDisableVertexAttribArray(texPositionHandle);
		shader.stop(gl);
		gl.glDisable(gl.GL_BLEND);

	}

	public void show() {
		hide = false;
	}

	public void hide() {
		hide = true;
	}*/

}
