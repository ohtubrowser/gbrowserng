package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.jogamp.opengl.util.Animator;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager.GBrowserPreview;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import java.awt.*;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class GenoWindow {


	public GLCanvas c;
	public GBrowserPreview trackviewWindow;
	private Animator animator;
	private boolean isFullscreen = false;
	public Frame frame;
	public Container container;
	private boolean overviewVisible = true;

	public GenoWindow(int width, int height) {
		
		GLProfile.initSingleton();
		c = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));

		frame = new Frame("GenomeBrowserNG");

		frame.setVisible(false);
		frame.setLayout(new BorderLayout());

		container = new Container();
		container.setLayout(new CardLayout());

		container.add(c, "1");
		c.setVisible(true);
		container.setVisible(true);

		frame.add(container, BorderLayout.CENTER);
		frame.setSize(width, height);
	}

	public void open() {
		this.c.setVisible(true);
		frame.setVisible(true);
		container.validate();
		c.requestFocusInWindow();
		this.animator = new Animator(c);
		this.animator.start();
	}

	public void close() {
		this.animator.stop();
		frame.setVisible(false);
		c.destroy();
		frame.dispose(); // This causes a sigsegv because of a jogl bug
	}

	public void toggleFullscreen() {
		if (isFullscreen = !isFullscreen) {
			GlobalVariables.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			GlobalVariables.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		} else {
			GlobalVariables.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.8);
			GlobalVariables.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.8);
		}
	}
	
	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void toggleVisible() {
		overviewVisible = !overviewVisible;
		System.out.println("Overview : " + overviewVisible);
		CardLayout cl = (CardLayout) container.getLayout();
		cl.next(container);
		container.validate();
		System.out.println("TOGGLEVISIBLE");
	}

	public boolean isOverviewVisible() {
		return overviewVisible;
	}

	public void addContainer(Container swingContainer) {
		container.add(swingContainer, "2");
		CardLayout cl = (CardLayout) container.getLayout();
		cl.next(container);
		container.validate();
	}
}
