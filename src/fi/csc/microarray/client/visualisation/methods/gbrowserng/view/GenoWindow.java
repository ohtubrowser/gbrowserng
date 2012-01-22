package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

public class GenoWindow {

	public GLWindow window;
	private FPSAnimator animator;

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
		this.animator = new FPSAnimator(this.window, 60);
		this.animator.start();
	}

	public void close()
	{
		this.animator.stop();
		this.window.destroy();
	}
}
