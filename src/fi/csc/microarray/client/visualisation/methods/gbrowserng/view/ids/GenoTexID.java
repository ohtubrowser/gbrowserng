package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids;


import com.soulaim.tech.gles.TextureID;
import com.soulaim.desktop.DesktopTextureManager;

import javax.media.opengl.GL2;

public class GenoTexID {
	public enum TextureID {
		QUIT_BUTTON,
		SHRINK_BUTTON,
		OPENFILE_BUTTON,
		MAXIMIZE_BUTTON,
		CONNECTIONS_BUTTON,
		FONT
	}

	public static void createTextures(GL2 gl) {
		loadTexture(gl, "quit.png", TextureID.QUIT_BUTTON);
		loadTexture(gl, "shrink.png", TextureID.SHRINK_BUTTON);
		loadTexture(gl, "openfile.png", TextureID.OPENFILE_BUTTON);
		loadTexture(gl, "maximize.png", TextureID.MAXIMIZE_BUTTON);
		loadTexture(gl, "connections.png", TextureID.CONNECTIONS_BUTTON);
		loadTexture(gl, "font.png", TextureID.FONT);
	}
	
	private static void loadTexture(GL2 gl, String path, TextureID id) {

	}
}
