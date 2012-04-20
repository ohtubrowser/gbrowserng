package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.soulaim.tech.gles.Color;
import com.soulaim.tech.gles.primitives.PrimitiveBuffers;
import com.soulaim.tech.gles.shaders.Shader;
import com.soulaim.tech.gles.shaders.ShaderMemory;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.OpenGLBuffers;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.PrimitiveRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

public class SessionViewCapsule {

	private static IntBuffer frameBufferHandle = IntBuffer.allocate(1);
	private IntBuffer textureHandle = IntBuffer.allocate(1);
	private boolean needsTextureUpdate = true;
	private Texture texture;

	private final GeneralLink linkData;
	private final GeneCircle geneCircle;
	private final ViewChromosome chr;
	private final long chrPosition;
	
	private boolean dying = false;
	private float death = 0;
	private Color backGroundColor = new Color(0, 0, 0, 1.0f);

	private Vector2 capsulePosition = new Vector2(0.4f, 0.5f);
	private Vector2 circlePosition = new Vector2(1, 0);

	private float relativePosition;

	private float dimX = 0.2f;
	private float dimY = 0.1f;
	private boolean hover = false;
	private LinkGFX linkGFX;
	private boolean textureCreated;
	private float timeSinceTextureUpdate = 0f;

	public SessionViewCapsule(ViewChromosome chr, long chrPosition, GeneralLink linkData, GeneCircle geneCircle) {
		this.linkData = linkData;
		this.geneCircle = geneCircle;
		this.chr = chr;
		this.chrPosition = chrPosition;
		setRelativePosition(geneCircle);
		linkGFX = new LinkGFX(this, circlePosition);
	}

	public SessionViewCapsule(GeneralLink link, float relativePosition, GeneCircle geneCircle) {
		this.linkData = link;
		this.geneCircle = geneCircle;

		setRelativePosition(relativePosition);

		relativePosition -= 0.25f;
		if(relativePosition < 0)
			relativePosition+=1.0f;

		this.chr = geneCircle.getChromosomeByRelativePosition(relativePosition);
		this.chrPosition = geneCircle.getPositionInChr(chr, relativePosition);

		linkGFX = new LinkGFX(this, circlePosition);
	}

	public Vector2 getCapsulePosition() {
		return capsulePosition;
	}

	public void setRelativePosition(GeneCircle geneCircle) {
		float t = geneCircle.getRelativePosition(chr.getChromosomeNumber()-1, chrPosition);
		setRelativePosition(t);
	}

	public void setRelativePosition(float relativePosition) {
		this.relativePosition = relativePosition;
		updateGeneCirclePosition();
	}

	public Vector2 getGeneCirclePosition() {
		return circlePosition;
	}


	private void updateGeneCirclePosition() {
		circlePosition.x = geneCircle.getSize();
		circlePosition.y = 0;
		circlePosition.rotate(2 * (float) Math.PI * relativePosition);
		circlePosition.x = CoordinateManager.toCircleCoordsX(circlePosition.x);
		circlePosition.y = CoordinateManager.toCircleCoordsY(circlePosition.y);
	}

	public boolean isAlive() {
		return death < 1.0f;
	}

	public void setDimensions(float x, float y) {
		dimX = x;
		dimY = y;
	}

	public boolean inCapsule(float screen_x, float screen_y) {
		return (capsulePosition.x - CoordinateManager.toCircleCoordsX(dimX)/2 < screen_x
				&& capsulePosition.x + CoordinateManager.toCircleCoordsX(dimX)/2 > screen_x
				&& capsulePosition.y - CoordinateManager.toCircleCoordsY(dimY)/2 < screen_y
				&& capsulePosition.y + CoordinateManager.toCircleCoordsY(dimY)/2 > screen_y);
	}

	public boolean handle(MouseEvent event, float screen_x, float screen_y) {
		hover = inCapsule(screen_x, screen_y);
		return hover;
	}

	public boolean handle(KeyEvent event) {
		return false;
	}

	public void draw(GL2 gl, OverView overView) {
		linkGFX.draw(gl);

		gl.glEnable(GL2.GL_BLEND);
		PrimitiveRenderer.drawRectangle(capsulePosition.x, capsulePosition.y, dimX/2, dimY/2, gl, hover ? new Color(1.0f, 0.0f, 0.0f, 1.0f) : backGroundColor);
		gl.glDisable(GL2.GL_BLEND);
		if (needsTextureUpdate) {
			updateTexture(gl, overView);
			needsTextureUpdate = false;
		}
		drawFromTexture(gl);
	}

	public void setNeedsTextureUpdate() {
		needsTextureUpdate = true;
	}

	public void tick(float dt) {
		if (dying) {
			death += dt;
		}
		timeSinceTextureUpdate += dt;
		if (timeSinceTextureUpdate > 5f) {
			needsTextureUpdate = true;
			timeSinceTextureUpdate = 0f;
		}
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

	public LinkGFX getLinkGfX() {
		return linkGFX;
	}

	public ViewChromosome getChromosome() {
		return chr;
	}

	public long getChrPosition() {
		return chrPosition;
	}
	
	public Vector2 getDimensions() {
		return new Vector2(this.dimX, this.dimY);
	}

	public void setCapsulePosition(float x, float y) {
		capsulePosition.x = x;
		capsulePosition.y = y;
	}

	public static void initFrameBuffer(GL2 gl) {
		gl.glGenFramebuffers(1, frameBufferHandle);
	}

	public void updateTexture(GL2 gl, OverView overView) {
		BufferedImage image = overView.getTrackviewManager().getImage(chr, chrPosition, chrPosition + 10000l);

		texture = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);
	}

	private void drawFromTexture(GL2 gl) {

		Matrix4 modelViewMatrix = new Matrix4();

		modelViewMatrix.makeTranslationMatrix(capsulePosition.x, capsulePosition.y, 0);
		modelViewMatrix.scale(CoordinateManager.toCircleCoordsX(dimX * 0.45f),
							CoordinateManager.toCircleCoordsY(dimY * 0.45f), 1.0f);
		gl.glEnable(gl.GL_BLEND);
		Shader shader = GenoShaders.getProgram(GenoShaders.ShaderID.TEXRECTANGLE);
		shader.start(gl);
		ShaderMemory.setUniformMat4(gl, shader, "modelViewMatrix", modelViewMatrix);
		ShaderMemory.setUniformVec1(gl, shader, "alpha", 1.0f);

		texture.bind(gl);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.squareID);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 2, GL2.GL_FLOAT, false, 0, 0);
		
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, OpenGLBuffers.squareTexID);
		gl.glEnableVertexAttribArray(1);
		gl.glVertexAttribPointer(1, 2, GL2.GL_FLOAT, false, 0, 0);
		
		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
		
		gl.glDisableVertexAttribArray(0);
		gl.glDisableVertexAttribArray(1);
		shader.stop(gl);
		gl.glDisable(gl.GL_BLEND);

	}

}
