package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import gles.Color;
import gles.SoulGL2;
import gles.TextureID;
import gles.renderer.PrimitiveRenderer;

public class GenoButton extends GenosideComponent {

	private final TextureID myTexture;
	private Color myColor = new Color(1, 1, 1, 1);

	private float xpos = 1;
	private float ypos = 1;

	private float x_offset;
	private float y_offset;

	private String buttonName;
	private float buttonLock = 0;

	public GenoButton(GenosideComponent parent, String name, float x, float y, float x_offset, float y_offset, TextureID textureID) {
		super(parent);
		setDimensions(0.05f, 0.05f);

		this.myTexture = textureID;
		this.xpos = x;
		this.ypos = y;
		this.x_offset = x_offset;
		this.y_offset = y_offset;
		this.buttonName = name;
	}

	@Override
	public void childComponentCall(String who, String what) {
	}

	@Override
	public boolean handle(MouseEvent event, float screen_x, float screen_y) {

		if(buttonLock > 0.01f)
			return false;

		float dx = screen_x - this.getParent().glx(xpos) - x_offset + 0.01f;
		float dy = screen_y - this.getParent().gly(ypos) - y_offset + 0.01f;

		if(dx * dx + dy * dy < 0.02f * 0.02f) {
			if(event.getEventType() == MouseEvent.EVENT_MOUSE_CLICKED) {
				this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 0);
				this.getParent().childComponentCall(buttonName, "PRESSED");
				buttonLock = 0.5f;
				return true;
			}

			this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 1);
			return false;
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
		gl.glEnable(SoulGL2.GL_BLEND);
		myColor.g = 1f-this.getAnimatedValues().getAnimatedValue("MOUSEHOVER");
		myColor.b = 1f-this.getAnimatedValues().getAnimatedValue("MOUSEHOVER");
		float myScale = 0.02f + this.getAnimatedValues().getAnimatedValue("MOUSEHOVER") * 0.01f;
		PrimitiveRenderer.drawTexturedSquare(this.getParent().glx(xpos) - myScale * 0.5f + x_offset, this.getParent().gly(ypos) - myScale * 0.5f + y_offset, myScale, gl, myColor, myTexture);
		gl.glDisable(SoulGL2.GL_BLEND);
	}

	@Override
	public void userTick(float dt) {
		buttonLock -= dt;
		buttonLock = (buttonLock < 0) ? 0 : buttonLock;
	}

}
