package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

public class GenoWindow {

	public GLWindow window;
	private Animator animator;
	private boolean isFullscreen = false;

	public GenoWindow(int width, int height)
	{
		GLProfile.initSingleton();
		this.window = GLWindow.create(new GLCapabilities(GLProfile.getDefault()));
		this.window.setSize(width, height);
		this.window.setTitle("GenomeBrowserNG");
	}

	public void open()
	{
		this.window.setVisible(true);
		this.animator = new Animator(this.window);
		this.animator.start();
	}

	public void close()
	{
		this.animator.stop();
		this.window.destroy();
	}

	public void toggleFullscreen() {
		if (isFullscreen = !isFullscreen) {
			this.window.setFullscreen(true);
		}
		else {
			this.window.setFullscreen(false);
		}
	}
}
