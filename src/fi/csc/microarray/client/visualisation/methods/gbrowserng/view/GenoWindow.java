package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import java.awt.Toolkit;

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
			GlobalVariables.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			GlobalVariables.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		else {
			this.window.setFullscreen(false);
			GlobalVariables.width = (int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.8);
			GlobalVariables.height = (int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.8);
		}
	}
}
