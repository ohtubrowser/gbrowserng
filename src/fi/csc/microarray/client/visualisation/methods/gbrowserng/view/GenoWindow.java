package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import java.awt.Container;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Toolkit;

public class GenoWindow {

	public GLWindow window;
	private Animator animator;
	private boolean isFullscreen = false;
	public NewtCanvasAWT newtCanvasAWT;
	Frame testFrame;
	Container testContainer;

	public GenoWindow(int width, int height)
	{
		GLProfile.initSingleton();
		this.window = GLWindow.create(new GLCapabilities(GLProfile.getDefault()));
		newtCanvasAWT = new NewtCanvasAWT(window);
		testFrame = new Frame("HERP - DERP");
		testFrame.setLayout(new BorderLayout());

		testContainer = new Container();
		testContainer.setLayout(new CardLayout());
		testContainer.add(new Button("ASD"), "TESTING");//BorderLayout.NORTH);
		testContainer.add(newtCanvasAWT, "TESTING!");//BorderLayout.CENTER);

		testFrame.add(testContainer, BorderLayout.CENTER);
		testFrame.setSize(width, height);

		this.window.setSize(width, height);
		this.window.setTitle("GenomeBrowserNG");
	}

	public void open()
	{
		this.window.setVisible(true);
		testFrame.setVisible(true);
		CardLayout cl = (CardLayout) testContainer.getLayout();
		cl.next(testContainer);
		this.animator = new Animator(this.window);
		this.animator.start();
	}

	public void close()
	{
		this.animator.stop();
		testFrame.setVisible(false);
		testFrame.dispose(); // This causes a sigsegv because of a jogl bug
		this.window.destroy();
	}

	public void toggleFullscreen() {
		/*if (isFullscreen = !isFullscreen) {
			this.window.setFullscreen(true);
			GlobalVariables.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			GlobalVariables.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		else {
			this.window.setFullscreen(false);
			GlobalVariables.width = (int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.8);
			GlobalVariables.height = (int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.8);
		}*/
		toggleVisible();
	}

	public void toggleVisible() {
		CardLayout cl = (CardLayout) testContainer.getLayout();
		cl.next(testContainer);
	}
}
