package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager.GBrowserPreview;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import java.awt.*;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

public class GenoWindow {


	public GLWindow window;
	public GBrowserPreview trackviewWindow;
	private Animator animator;
	private boolean isFullscreen = false;
	public NewtCanvasAWT newtCanvasAWT;
	Frame testFrame;
	Container testContainer;
	private boolean overviewVisible = true;

	public GenoWindow(int width, int height) {
		GLProfile.initSingleton();
		this.window = GLWindow.create(new GLCapabilities(GLProfile.getDefault()));
		this.window.setSize(width, height);
		this.window.setTitle("GenomeBrowserNG");


		newtCanvasAWT = new NewtCanvasAWT(window);
		testFrame = new Frame("GenomeBrowserNG");
		testFrame.setLayout(new BorderLayout());

		testContainer = new Container();
		testContainer.setLayout(new CardLayout());

		testContainer.add(newtCanvasAWT, "TESTING!");

		testFrame.add(testContainer, BorderLayout.CENTER);
		testFrame.setSize(width, height);
	}

	public void open() {
		this.window.setVisible(true);
		testFrame.setVisible(true);
		CardLayout cl = (CardLayout) testContainer.getLayout();
		cl.next(testContainer);
		this.animator = new Animator(this.window);
		this.animator.start();
	}

	public void close() {
		this.animator.stop();
		testFrame.setVisible(false);
		testFrame.dispose(); // This causes a sigsegv because of a jogl bug
		this.window.destroy();
	}

	public void toggleFullscreen() {
		if (isFullscreen = !isFullscreen) {
			this.window.setFullscreen(true);
			GlobalVariables.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			GlobalVariables.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		} else {
			this.window.setFullscreen(false);
			GlobalVariables.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.8);
			GlobalVariables.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.8);
		}
	}

	public void toggleVisible() {
		overviewVisible = !overviewVisible;
		CardLayout cl = (CardLayout) testContainer.getLayout();
		cl.next(testContainer);
	}

	public boolean isOverviewVisible() {
		return overviewVisible;
	}

	public void addContainer(Container swingContainer) {
		testContainer.add(swingContainer, "2");
		CardLayout cl = (CardLayout) testContainer.getLayout();
		cl.next(testContainer);
	}
}
