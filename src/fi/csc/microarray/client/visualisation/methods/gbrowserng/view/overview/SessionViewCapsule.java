package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import gles.Color;
import gles.SoulGL2;
import gles.renderer.PrimitiveRenderer;
import math.Vector2;

public class SessionViewCapsule extends GenosideComponent {

    private final SessionView sessionView;
    private final GeneCircle geneCircle;
    // TODO: SessionViewCapsuleData class could contain this.
    private boolean isActive = false;
    private boolean dying = false;
    private float death = 0;
    private float relativePos;
    private Color backGroundColor = new Color(0, 0, 0, 255);
    private Vector2 genecirclePosition = new Vector2(1, 0);
    private Vector2 positionAdjustment = new Vector2();
    private final LinkGFX link;

    public SessionViewCapsule(SessionView sessionView, float relativePos, GeneCircle geneCircle) {
	super(null); // should be ok
	this.sessionView = sessionView;
	this.geneCircle = geneCircle;
	this.relativePos = relativePos;
	this.getAnimatedValues().setAnimatedValue("ALPHA", 1.0f);
	setGeneCirclePosition(relativePos);
	link = new LinkGFX(sessionView, this);
    }

    public SessionViewCapsule(SessionView sessionView, Vector2 relativePosVector, GeneCircle geneCircle) {
	super(null); // should be ok
	this.sessionView = sessionView;
	this.geneCircle = geneCircle;
	this.getAnimatedValues().setAnimatedValue("ALPHA", 1.0f);
	this.genecirclePosition = relativePosVector;
	link = new LinkGFX(sessionView, this);
    }

    public Vector2 getGeneCirclePosition() {
	return genecirclePosition;
    }

    private void setGeneCirclePosition(float relativePos) {
	this.relativePos = relativePos;
	updateGeneCirclePosition();

    }

    public void updateGeneCirclePosition(float relativePos) {
	setGeneCirclePosition(relativePos);
    }

    public void updateGeneCirclePosition() {
	genecirclePosition.x = geneCircle.getSize();
	genecirclePosition.y = 0;
	genecirclePosition.rotate(2 * (float) Math.PI * relativePos);
    }

    public boolean isAlive() {
	return death < 1.0f;
    }

    public boolean isActive() {
	return isActive;
    }

    public void activate() {
	System.out.println("activating capsule");
	isActive = true;
	link.hide();
	// sessionView.setDimensions(2, 2);
	// sessionView.setPosition(0, 0);
	this.getAnimatedValues().setAnimatedValue("ALPHA", -0.02f);
    }

    public void deactivate() {
	System.out.println("deactivating capsule");
	isActive = false;
	if (!dying) {
	    link.show();
	    sessionView.setDimensions(0.4f, 0.2f);
	    this.show();
	} else {
	    link.hide();
	    sessionView.setDimensions(0.1f, 0.1f);
	}
	this.getAnimatedValues().setAnimatedValue("ALPHA", 1.0f);
    }

    @Override
    public void childComponentCall(String who, String what) {
    }

    @Override
    public boolean handle(MouseEvent event, float screen_x, float screen_y) {
	Vector2 dimensions = sessionView.getDimensions();
	Vector2 position = sessionView.getPosition();

	if (screen_x > position.x - dimensions.x * 0.5f && screen_x < position.x + dimensions.x * 0.5f) {
	    if (screen_y > position.y - dimensions.y * 0.5f && screen_y < position.y + dimensions.y * 0.5f) {
		this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 1);
		return true;
	    }
	}

	this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 0);
	return false;
    }

    @Override
    public boolean handle(KeyEvent event) {
	return false;
    }

    @Override
    public void draw(SoulGL2 gl) {

	link.draw(gl);

	// this is just for debug
	float v = 1.0f - this.getAnimatedValues().getAnimatedValue("MOUSEHOVER");
	float alpha = this.getAnimatedValues().getAnimatedValue("ALPHA");

	if (alpha > 0) {
	    gl.glEnable(SoulGL2.GL_BLEND);
	    this.backGroundColor.r = v;
	    this.backGroundColor.g = v;
	    this.backGroundColor.b = v;
	    this.backGroundColor.a = alpha * (1.0f - death);
	    PrimitiveRenderer.drawRectangle(sessionView.glx(0), sessionView.gly(0), sessionView.getDimensions().x * 0.5f, sessionView.getDimensions().y * 0.5f / GlobalVariables.aspectRatio, gl, backGroundColor);
	    gl.glDisable(SoulGL2.GL_BLEND);
	}

	sessionView.setActive(isActive);
	sessionView.draw(gl);
    }

    @Override
    public void userTick(float dt) {
	if (dying) {
	    death += dt;
	}
	sessionView.tick(dt);

	if (this.positionAdjustment.lengthSquared() > 0.00001f) {
	    this.getSession().modifyPosition(this.positionAdjustment.x * 4, this.positionAdjustment.y * 4);
	}
	this.setPosition(genecirclePosition.x, genecirclePosition.y);

	link.tick(dt);
    }

    public SessionView getSession() {
	return sessionView;
    }

    public void die() {
	dying = true;
	link.hide();
    }

    public boolean isDying() {
	return dying;
    }

    public void hide() {
	// todo
	link.hide();
	Vector2 myPos = this.sessionView.getPosition();
	myPos.normalize();
	myPos.scale(1.7f);
	this.sessionView.setPosition(myPos.x, myPos.y);
    }

    public void show() {
	// todo
	Vector2 myPos = this.sessionView.getPosition();

	link.show();
	if (myPos.length() < 0.00000001f) {
	    myPos.x = (float) Math.random() - 0.5f;
	    myPos.y = (float) Math.random() - 0.5f;
	}

	myPos.normalize();
	myPos.scale(0.7f);
	this.sessionView.setPosition(myPos.x, myPos.y);
    }

    public void incrementPositionAdjustment(float x, float y) {
	positionAdjustment.x += x;
	positionAdjustment.y += y;
    }

    public void clearPositionAdjustment() {
	this.positionAdjustment.x = 0;
	this.positionAdjustment.y = 0;
    }

    void drawToTexture(SoulGL2 gl) {
	sessionView.updateTexture(gl);
    }
}
