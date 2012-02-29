package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import gles.Color;
import gles.SoulGL2;
import gles.renderer.PrimitiveRenderer;
import javax.media.opengl.GL2;
import soulaim.DesktopGL2;

public class GenoVisualBorder extends GenosideComponent {

	public GenoVisualBorder(GenosideComponent parent) {
		super(parent);
	}

	@Override
	public void childComponentCall(String who, String what) {
	}

	@Override
	public boolean handle(MouseEvent event, float screen_x, float screen_y) {
		return false;
	}

	@Override
	public boolean handle(KeyEvent event) {
		return false;
	}

	@Override
	public void draw(GL2 gl) {
		draw(gl, Color.BLACK);
	}

	@Override
	public void userTick(float dt) {
	}

	public void draw(GL2 gl, Color tempcolor) {
                SoulGL2 soulgl = new DesktopGL2(gl);
		GenosideComponent parent = this.getParent();
		PrimitiveRenderer.drawRectangle(parent.glx(0), parent.gly(-1), parent.glxSize(1.0f), 0.003f, soulgl, tempcolor);
		PrimitiveRenderer.drawRectangle(parent.glx(0), parent.gly(+1), parent.glxSize(1.0f), 0.003f, soulgl, tempcolor);
		PrimitiveRenderer.drawRectangle(parent.glx(+1), parent.gly(0), 0.003f, parent.glySize(1.0f / GlobalVariables.aspectRatio), soulgl, tempcolor);
		PrimitiveRenderer.drawRectangle(parent.glx(-1), parent.gly(0), 0.003f, parent.glySize(1.0f / GlobalVariables.aspectRatio), soulgl, tempcolor);
	}
}
