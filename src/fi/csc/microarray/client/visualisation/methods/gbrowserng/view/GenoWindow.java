package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

public class GenoWindow {
	
    public GLWindow window;
    private Animator animator;
	
	public GenoWindow(int width, int height)
	{
        GLProfile.initSingleton(true);
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
}