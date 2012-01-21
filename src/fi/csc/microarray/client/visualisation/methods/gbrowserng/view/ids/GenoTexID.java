package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids;

import gles.SoulGL2;
import gles.TextureID;
import managers.TextureManager;

public class GenoTexID {
	public static final TextureID QUIT_BUTTON = new TextureID();
	public static final TextureID SHRINK_BUTTON = new TextureID();
	public static final TextureID OPENFILE_BUTTON = new TextureID();
	public static final TextureID MAXIMIZE_BUTTON = new TextureID();
	public static final TextureID FONT = new TextureID();

	public static void createTextures(SoulGL2 gl) {
		TextureManager.loadTexture(gl, "quit.png", QUIT_BUTTON);
		TextureManager.loadTexture(gl, "shrink.png", SHRINK_BUTTON);
		TextureManager.loadTexture(gl, "openfile.png", OPENFILE_BUTTON);
		TextureManager.loadTexture(gl, "maximize.png", MAXIMIZE_BUTTON);
		TextureManager.loadTexture(gl, "font.png", FONT);
	}
}
