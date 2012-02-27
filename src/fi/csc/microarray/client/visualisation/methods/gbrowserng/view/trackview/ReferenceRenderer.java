package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.CascadingComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.VisualComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import gles.Color;
import gles.SoulGL2;
import gles.renderer.PrimitiveRenderer;
import gles.renderer.TextRenderer;
import javax.media.opengl.GL2;
import managers.TextureManager;
import soulaim.DesktopGL2;

public class ReferenceRenderer extends CascadingComponent implements VisualComponent {

	private final Session session;
	private float height = -0.43f; // where the component renders itself
	private float mySizeY = 0.05f;

	public ReferenceRenderer(CascadingComponent parent, Session session) {
		super(parent);
		this.session = session;
		this.setDimensions(2, 2);
	}

	private void drawRefSeq(SoulGL2 gl, float y) {

		float smoothPosition = ((TrackView)getParent()).getGenePosition();
		int intPosition = (int) smoothPosition;
		float offsetPosition = smoothPosition - intPosition;

		float x = this.session.halfSizeX - offsetPosition * 2
				* this.session.halfSizeX;

		// positive direction -->
		for (int i = intPosition; i < session.referenceSequence.sequence.length
				&& x < 1f + glxSize(this.session.halfSizeX); ++i, x += 2 * this.session.halfSizeX) {
			if (i < 0)
				continue;
			char c = session.referenceSequence.sequence[i];
			PrimitiveRenderer.drawRectangle(glx(x), gly(y),
					glxSize(this.session.halfSizeX * session.payloadSize),
					glySize(mySizeY * session.payloadSize), gl, GlobalVariables.genomeColors.get(c));
			if (this.session.halfSizeX >= mySizeY) {
				TextureManager.bindTexture(gl, GenoTexID.FONT);
				TextRenderer.getInstance().drawText(gl, Character.toString(c),
						glx(x), gly(y), glySize(20 * this.session.halfSizeY));
			}

		}

		x = -this.session.halfSizeX - offsetPosition * 2
				* this.session.halfSizeX;
		// negative direction <--
		for (int i = intPosition - 1; i >= 0
				&& x > -1f - glxSize(this.session.halfSizeX); --i) {
			if (i < session.referenceSequence.sequence.length) {
				char c = session.referenceSequence.sequence[i];
				Color genomeColor = GlobalVariables.genomeColors.get(c);
				PrimitiveRenderer.drawRectangle(glx(x), gly(y),
						glxSize(this.session.halfSizeX * session.payloadSize),
						glySize(mySizeY * session.payloadSize), gl, genomeColor);
				if (this.session.halfSizeX >= mySizeY) {
					TextureManager.bindTexture(gl, GenoTexID.FONT);
					TextRenderer.getInstance().drawText(gl,
							Character.toString(c), glx(x), gly(y),
							glySize(20 * mySizeY));
				}
			}
			x -= 2 * this.session.halfSizeX;
		}
	}

	public void draw(GL2 gl) {
		SoulGL2 soulgl = new DesktopGL2(gl);
		this.drawRefSeq(soulgl, height);
	}

	public void tick(float dt) {
		cascadingTick(dt);
	}
}
