package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview;

import managers.TextureManager;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import gles.Color;
import gles.SoulGL2;
import gles.renderer.PrimitiveRenderer;
import gles.renderer.TextRenderer;

public class CoordinateRenderer extends GenosideComponent {

	public CoordinateRenderer(GenosideComponent parent) {
		super(parent);
	}

	@Override
	public void childComponentCall(String who, String what) {
	}

	@Override
	public void draw(SoulGL2 gl) {
		float position = this.getParent().getAnimatedValues().getAnimatedValue("POSITION");
		float halfWidth = this.getParent().getAnimatedValues().getAnimatedValue("ZOOM");
		float total = Math.min(100f, 1.0f / halfWidth);
		float offset = 2 * halfWidth * (position - (int) position);

		float delta_x = Math.max(2 * halfWidth, 0.02f);

		int p = (int) position - (int) total / 2;
		int delta = (int) total / 4;
		delta = (delta == 0) ? 1 : delta;
		
		for (float x = -1.0f + halfWidth - offset; x < 1.0f; x += delta_x, ++p) {
			if (p % delta == 0) {
				PrimitiveRenderer.drawRectangle(glx(x), gly(0.9f),
						glxSize(0.002f), glySize(0.1f), gl, Color.WHITE);
				TextureManager.bindTexture(gl, GenoTexID.FONT);
				TextRenderer.getInstance().drawText(gl, Integer.toString((int) p),
						glx(x), gly(0), glySize(10.0f));
			} else {
				PrimitiveRenderer.drawRectangle(glx(x), gly(0.7f),
						glxSize(0.002f), glySize(0.3f), gl, Color.WHITE);
			}
		}
	}

	@Override
	public boolean handle(MouseEvent event, float screenX, float screenY) {
		return false;
	}

	@Override
	public boolean handle(KeyEvent event) {
		return false;
	}

	@Override
	public void userTick(float dt) {
	}
}
