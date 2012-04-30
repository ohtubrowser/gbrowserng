package fi.csc.microarray.client.visualisation.methods.gbrowserng.view;

import com.jogamp.opengl.util.Animator;
import fi.csc.microarray.client.visualisation.methods.gbrowser.PreviewManager.GBrowserPreview;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;

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
	public GlobalVariables globals;
	public OverView overView;

	public GenoWindow(GlobalVariables globals, int width, int height) {
		this.globals = globals;
		globals.width = width;
		globals.height = height;
		globals.aspectRatio = (float)width / (float)height;

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
		frame.dispose();
	}

	public void toggleFullscreen() {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (isFullscreen = !isFullscreen) {
			if(device.isFullScreenSupported())
				device.setFullScreenWindow(frame);
			globals.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			globals.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		} else {
			device.setFullScreenWindow(null);
			globals.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.8);
			globals.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.8);
		}
	}
	
	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void toggleVisible() {
		overviewVisible = !overviewVisible;
		CardLayout cl = (CardLayout) container.getLayout();
		cl.next(container);
		container.validate();
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
