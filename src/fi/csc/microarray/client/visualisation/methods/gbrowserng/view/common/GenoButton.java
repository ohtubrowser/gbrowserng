package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.soulaim.tech.gles.Color;
import com.soulaim.tech.math.Vector2;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.PrimitiveRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;

import javax.media.opengl.GL2;

public class GenoButton extends GenosideComponent {

	private final GenoTexID.TextureID myTexture;
	private Color myColor = new Color(1, 1, 1, 1);

	private float xpos = 1;
	private float ypos = 1;

	private float x_offset;
	private float y_offset;

	private String buttonName;
	private float buttonLock = 0;

	public GenoButton(GenosideComponent parent, String name, float x, float y, float x_offset, float y_offset, GenoTexID.TextureID textureID) {
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

		Vector2 position=new Vector2(xpos+x_offset,ypos+y_offset);
		Vector2 dimensions=getTargetDimensions();
		
		if(screen_x > position.x - dimensions.x * 0.5f && screen_x < position.x + dimensions.x * 0.5f) {
			if(screen_y > position.y - dimensions.y - (y_offset/GlobalVariables.aspectRatio) * 0.5f && screen_y < position.y + dimensions.y + (y_offset/GlobalVariables.aspectRatio) * 0.5f) {
				if(event.getEventType() == MouseEvent.EVENT_MOUSE_CLICKED) {
					this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 0);
					this.getParent().childComponentCall(buttonName, "PRESSED");
					buttonLock = 0.5f;
					return true;
				}
				this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 1);
				return false;
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
	public void draw(GL2 gl) {
		Vector2 position=new Vector2(xpos+x_offset, ypos+y_offset);
		Vector2 dimensions=getTargetDimensions();
		gl.glEnable(GL2.GL_BLEND);
		myColor.g = 1f-this.getAnimatedValues().getAnimatedValue("MOUSEHOVER");
		myColor.b = 1f-this.getAnimatedValues().getAnimatedValue("MOUSEHOVER");
		float myScale = 0.02f + this.getAnimatedValues().getAnimatedValue("MOUSEHOVER") * 0.01f;
		//PrimitiveRenderer.drawTexturedSquare(position.x, position.y, dimensions.x * 0.5f, gl, myColor, myTexture);
		gl.glDisable(GL2.GL_BLEND);
	}

	@Override
	public void userTick(float dt) {
		buttonLock -= dt;
		buttonLock = (buttonLock < 0) ? 0 : buttonLock;
	}

}
