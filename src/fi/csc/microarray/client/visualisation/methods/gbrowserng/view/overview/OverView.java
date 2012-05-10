package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;
import com.jogamp.opengl.util.awt.TextRenderer;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.CapsuleManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.ContextMenu;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.media.opengl.GL2;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


import java.util.concurrent.ConcurrentHashMap;

/**
 * Overview class. Handles the main window and functionality of the program.
 * @author 
 */
public class OverView extends GenosideComponent {

	public GeneCircle geneCircle;
	private GeneCircleGFX geneCircleGFX;
	private GenoFPSCounter tickCounter = new GenoFPSCounter();
	private GenoFPSCounter drawCounter = new GenoFPSCounter();
	private Vector2 mousePosition = new Vector2();
	private SessionViewCapsule hoverCapsule = null;
	public GenoWindow window;
	private ConcurrentHashMap<Integer, SessionViewCapsule> sessions;
	private LinkedList<SessionViewCapsule> textureUpdateList = new LinkedList<SessionViewCapsule>();
	private ArrayList<ChromoName> chromoNames = new ArrayList<ChromoName>();
	final Object textureUpdateListLock = new Object();
	public boolean die = false, arcHighlightLocked = false;
	public TextRenderer chromosomeNameRenderer, textRenderer;
	private LinkSelection linkSelection;
	public TrackviewManager trackviewManager;
	private LinkCollection linkCollection;
	private ContextMenu contextMenu;
	private MouseEventHandler mouseEventHandler;
	private KeyEventHandler keyEventHandler;
	private boolean drawArcs;
	private boolean circleNeedsUpdate = false;
	public GlobalVariables globals;
	public CapsuleManager CapsuleManager;
	public GeneralLink activeLink;
	private Thread linkSyncThread;

	/**
	 * Constructor
	 * @param globals Data structure with variables used throughout the program
	 * @param window Window object
	 * @param linkCollection Data loading object
	 */
	public OverView(GlobalVariables globals, GenoWindow window, LinkCollection linkCollection) {
		this.globals = globals;
		geneCircle = new GeneCircle(globals);
		geneCircleGFX = new GeneCircleGFX(globals, geneCircle);
		CapsuleManager = new CapsuleManager(globals);
		sessions = CapsuleManager.getSessions();
		drawArcs = false;
		this.window = window;
		window.overView = this;
		initTextRenderers();
		initChromoNames();
		trackviewManager = new TrackviewManager(window);
		this.linkCollection = linkCollection;
		geneCircle.setSize(0.485f);
		updateCircleSize();
		this.linkCollection.setOverview(this);

		linkSyncThread = new Thread(linkCollection);
		linkSyncThread.start();

		linkSelection = new LinkSelection(globals, geneCircle);
		mouseEventHandler = new MouseEventHandler(this);
		keyEventHandler = new KeyEventHandler(this);
	}

	/**
	 * Initializes fonts for text rendering.
	 */
	private void initTextRenderers() {
		Font font;
		float fontSize = 16f;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/drawable/Tiresias Signfont Bold.ttf")).deriveFont(fontSize);
		} catch (IOException e) {
			font = new Font("SansSerif", Font.BOLD, (int) fontSize);
		} catch (FontFormatException e) {
			font = new Font("SansSerif", Font.BOLD, (int) fontSize);
		}
		this.textRenderer = new com.jogamp.opengl.util.awt.TextRenderer(font, true, true);

		Font smallFont;
		float smallfontSize = 12f;
		try {
			smallFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/drawable/Tiresias Signfont Bold.ttf")).deriveFont(smallfontSize);
		} catch (IOException e) {
			smallFont = new Font("SansSerif", Font.BOLD, (int) smallfontSize);
		} catch (FontFormatException e) {
			smallFont = new Font("SansSerif", Font.BOLD, (int) smallfontSize);
		}
		this.chromosomeNameRenderer = new com.jogamp.opengl.util.awt.TextRenderer(smallFont, true, true);
	}

	/**
	 * Initializes chromosome names.
	 */
	private void initChromoNames() {
		for (ViewChromosome chromosome : globals.genome.getChromosomes()) {
			chromoNames.add(new ChromoName(chromosome));
		}
	}

	/**
	 * Updates the genome circle size if resized.
	 */
	public void updateCircleSize() {
		for (SessionViewCapsule capsule : sessions.values()) {
			capsule.setRelativePosition(geneCircle);
		}
		for (GeneralLink link : linkCollection.getLinks()) {
			link.calculatePositions(globals, geneCircle);
		}
	}

	/**
	 * Handles keyboard events with KeyEventHandler class and returns a boolean value about was the action successful.
	 * @param event event to be handled
	 * @return 
	 */
	@Override
	public boolean handle(KeyEvent event) {
		return this.keyEventHandler.handle(event);
	}

	/**
	 * Handles mouse events with MouseEventHandler class and returns a boolean value about was the action successful.
	 * @param event mouse event
	 * @param screen_x mouse x-coordinate
	 * @param screen_y mouse y-coordinate
	 * @return 
	 */
	@Override
	public boolean handle(MouseEvent event, float screen_x, float screen_y) {
		return mouseEventHandler.handle(event, screen_x, screen_y, this.geneCircle);
	}

	/**
	 * Opens a trackview session based on a SessionViewCapsule object.
	 * @param capsule capsule object
	 */
	public void openSession(SessionViewCapsule capsule) {
		if (capsule.isLinkSession()) {
			trackviewManager.openLinkSession(capsule.getLink());
		} else {
			trackviewManager.openAreaSession(capsule.getChromosome(), capsule.getChrPosition(), capsule.getChrPosition()+1000);
		}
		trackviewManager.toggleVisible();
	}

	/**
	 * Draws the content of the window.
	 * @param gl Gl interface
	 */
	@Override
	public void draw(GL2 gl) {
		if (linkCollection.loading) {
			drawArcs = false;
			window.c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			drawArcs = true;
			window.c.setCursor(Cursor.getDefaultCursor());
		}
		Matrix4 geneCircleModelMatrix = CoordinateManager.getCircleMatrix(globals);
		geneCircleModelMatrix.scale(geneCircle.getSize(), geneCircle.getSize(), geneCircle.getSize());

		if (drawArcs) {
			GeneralLink.beginDrawing(gl, geneCircle.getSize());
			if (arcHighlightLocked) {
				linkSelection.draw(gl);
			} else {
				int i = 0;
				GeneralLink activeLink = null;
				for (GeneralLink link : linkCollection.getLinks()) {
					if (activeLink == null && i < 1000
							&& link.isHit(CoordinateManager.fromCircleCoordsY(globals, mousePosition.x),
							CoordinateManager.fromCircleCoordsX(globals, mousePosition.y))) {
						activeLink = link;
						i++;
					} else {
						link.draw(gl);
					}
				}
				if (activeLink != null) {
					activeLink.draw(gl, 0f, 0f, 1f);
				}
				this.activeLink = activeLink;
			}
			GeneralLink.endDrawing(gl);
		}
		geneCircleGFX.draw(gl, geneCircleModelMatrix, this.mousePosition);
		if (drawArcs) {
			if (arcHighlightLocked) {
				linkSelection.drawClamps(gl);
			}
		}

		drawCapsules(gl);
		renderText(globals.width, globals.height);
		drawNumbers(globals.width, globals.height);

		if (contextMenu != null) {
			contextMenu.draw(gl);
		}
	}

	/**
	 * Draws opened trackview session capsules.
	 * @param gl 
	 */
	private void drawCapsules(GL2 gl) {
		synchronized (textureUpdateListLock) {
			for (SessionViewCapsule capsule : textureUpdateList) {
				capsule.drawToTexture(gl);
			}
			textureUpdateList.clear();
		}

		for (SessionViewCapsule capsule : sessions.values()) {
			capsule.draw(gl, this);
		}
	}

	/**
	 * Draws chromosome names.
	 * @param width screen width
	 * @param height screen height
	 */
	private void drawNumbers(int width, int height) {
		chromosomeNameRenderer.beginRendering(width, height);
		chromosomeNameRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
		int i = 1;
		float halfHeight = height / 2f;
		float halfWidth = width / 2f;
		Vector2[] chromobounds = geneCircle.getChromosomeBoundariesPositions();
		float lastBound = 0f;
		float overlap = 1.04f;
		boolean first = true;
		for (Vector2 v : chromobounds) {
			// Rotation needs to be done first because of coordinate modification.
			Vector2 rotationv = new Vector2(v);
			float angle = rotationv.relativeAngle(chromobounds[i % globals.genome.getNumChromosomes()]) / 2; // Rotate the numbers to the center of the chromosome.
			rotationv.rotate((angle < 0) ? angle : -((float) Math.PI - angle)); // Fix the >180 angle.

			// Convert to circlecoords using the rotated vector.
			Vector2 vv = new Vector2(CoordinateManager.toCircleCoords(globals, rotationv));
			String chromoname = globals.genome.getChromosome(i - 1).getName();

			float bound = vv.relativeAngle(new Vector2(0f, 1f));
			bound = bound > 0 ? bound : (float) Math.PI * 2 + bound;
			if (first) {
				first = false;
				lastBound = bound;
			} else {
				if (lastBound - bound > -0.1f) {
					overlap += 0.04f;
				} else {
					overlap = 1.04f;
					lastBound = bound;
				}
			}

			Rectangle2D rect = chromosomeNameRenderer.getBounds(chromoname);

			ChromoName chromoBox = chromoNames.get(i - 1);
			chromoBox.setPosition(vv.x * overlap, vv.y * overlap);
			chromoBox.setSize((float) (rect.getWidth() * 1.5f / halfWidth), (float) (rect.getHeight() * 1.5f / halfHeight));
			if (chromoBox.isActive()) {
				chromosomeNameRenderer.setColor(1.0f, 0.1f, 0.1f, 0.8f);
			}
			chromosomeNameRenderer.draw(
					chromoname,
					(int) (halfWidth + (halfWidth * vv.x * overlap) - rect.getWidth() / 2f),
					(int) (halfHeight + (halfHeight * vv.y * overlap) - rect.getHeight() / 2f));

			if (chromoBox.isActive()) {
				chromosomeNameRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
			}
			++i;
		}
		chromosomeNameRenderer.endRendering();
	}

	/**
	 * Draws information about the mouse position.
	 * @param width
	 * @param height 
	 */
	private void renderText(int width, int height) {
		textRenderer.beginRendering(width, height);
		textRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
		int stringHeight = (int) textRenderer.getBounds("TEXT").getHeight();
		if(globals.debug == true) {
			String fps = "Tick: " + tickCounter.getMillis() + "ms";
			textRenderer.draw(fps, 20, height - stringHeight);
			String draw = "Draw: " + drawCounter.getMillis() + "ms";
			textRenderer.draw(draw, 20, (int) (height - stringHeight * 2.2));
			String arcs = "Arcs: " + linkCollection.numLinks();
			textRenderer.draw(arcs, 20, (int) (height - stringHeight * 3.3));
		}
		// Mouse hover information
		long position = 0;
		int chromosome = 0;
		if ((arcHighlightLocked || activeLink != null) && linkSelection.mouseInCircle()) {
			GeneralLink link = !arcHighlightLocked ? activeLink : linkSelection.getActiveLink();
			if (link != null) {
				String counter = "Links " + link.getCounter();
				textRenderer.draw(counter, 20, 2 * stringHeight + 30);
				position = link.getEndPosition();
				chromosome = link.getEndChromosome().getChromosomeNumber();
			}
		} else {
			if (hoverCapsule == null) {
				position = this.geneCircle.getChromosomePosition();
				chromosome = this.geneCircle.getChromosome().getChromosomeNumber();
			} else {
				position = this.hoverCapsule.getChrPosition();
				chromosome = this.hoverCapsule.getChromosome().getChromosomeNumber();
			}
		}
		
		// calculate MB size by dividing by 1 million
		double mBposition = position*1.0 / 1000000;
		// calculate what result is with only 1 decimal point
		int ix = (int)(mBposition * 10.0); // scale it 
		mBposition = ((double)ix)/10.0;
		
		String chrom = "Chromosome " + chromosome;
		String pos = "Position: " + mBposition + "Mbp";
		textRenderer.draw(chrom, 20, stringHeight + 20);
		textRenderer.draw(pos, 20, 10);
		textRenderer.endRendering();
	}

	/**
	 * Fades out links connected to minimized chromosomes.
	 * @param dt 
	 */
	private void fadeLinks(float dt) {
		ViewChromosome thisChromo;
		thisChromo = geneCircle.getChromosome();
		ArrayList<GeneralLink> tempLinks = linkCollection.getLinks();
		for (int i = 0; i < tempLinks.size(); ++i) {
			GeneralLink link = tempLinks.get(i);
			if (!link.isMinimized()) {
				if (linkSelection.inSelection(i)) {
					link.fadeIn(dt * 16);
				} else if (!arcHighlightLocked
						&& ((link.getAChromosome() == thisChromo) || (link.getBChromosome() == thisChromo))) {
					link.fadeDim(dt * 8);
				} else {
					link.fadeOut(dt * 8);
				}
			} else {
				link.fadeOut(dt * 8);
			}
		}
	}

	/**
	 * Handles animations.
	 * @param dt 
	 */
	@Override
	public void tick(float dt) {
		if (geneCircle.animating) {
			geneCircle.tick(dt);
			updateCircleSize();
		} else if (circleNeedsUpdate) {
			updateCircleSize();
			circleNeedsUpdate = false;
		}

		linkSelection.tick(dt, linkCollection);
		geneCircleGFX.tick(dt);

		fadeLinks(dt);

		SessionViewCapsule killCapsule = null;
		for (SessionViewCapsule capsule : sessions.values()) {
			if (!capsule.isAlive()) {
				killCapsule = capsule;
			}
			capsule.tick(dt);
		}

		if (killCapsule != null) {
			CapsuleManager.removeCapsule(killCapsule);
		}
	}

	/**
	 * Returns drawCounter object.
	 * @return draw counter
	 */
	public GenoFPSCounter getDrawCounter() {
		return drawCounter;
	}

	/**
	 * returns tickCounter object.
	 * @return tick counter
	 */
	public GenoFPSCounter getTickCounter() {
		return tickCounter;
	}

	/**
	 * Sets the active capsule (the mouse cursor is over).
	 * @param capsule 
	 */
	public void setHoverCapsule(SessionViewCapsule capsule) {
		this.hoverCapsule = capsule;
	}

	/**
	 * Returns the active capsule.
	 * @return the capsule under the mouse cursor
	 */
	public SessionViewCapsule getHoverCapsule() {
		return this.hoverCapsule;
	}

	/**
	 * Returns open trackview sessions.
	 * @return open sessions
	 */
	public ConcurrentHashMap<Integer, SessionViewCapsule> getSessions() {
		return this.sessions;
	}

	/**
	 * Returns the chromosome names.
	 * @return chromosome names
	 */
	public ArrayList<ChromoName> getChromoNames() {
		return this.chromoNames;
	}

	/**
	 * Returns the GeneCircle-object.
	 * @return gene circle object
	 */
	public GeneCircle getGeneCircle() {
		return this.geneCircle;
	}

	/**
	 * Returns a boolean value about is arc hilighting locked.
	 * @return is the arc highlight locked
	 */
	public boolean isArcHighlightLocked() {
		return this.arcHighlightLocked;
	}

	/**
	 * Returns the links selected to be shown with the hilight selector.
	 * @return selected links
	 */
	public LinkSelection getLinkSelection() {
		return this.linkSelection;
	}

	/**
	 * Sets the boolean value about is the arc hilighting in use.
	 * @param b value to be set
	 */
	public void setArcHighlightLocked(boolean b) {
		this.arcHighlightLocked = b;
	}

	/**
	 * Sets the variables for mouse position.
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setMousePositionX(float x, float y) {
		this.mousePosition.x = x;
		this.mousePosition.y = y;
	}

	/**
	 * Returns list of capsules whose texture is to be updated.
	 * @return texture update list
	 */
	public LinkedList<SessionViewCapsule> getTextureUpdateList() {
		return this.textureUpdateList;
	}

	/**
	 * Adds a capsule to the texture update list.
	 * @param capsule the capsule to be added
	 */
	public void addCapsuleToTextureUpdateList(SessionViewCapsule capsule) {
		this.textureUpdateList.add(capsule);
	}

	/**
	 * Gets the link collection.
	 * @return the link collection
	 */
	public LinkCollection getLinkCollection() {
		return linkCollection;
	}

	/**
	 * Gets the track view manager.
	 * @return the track view manager
	 */
	public TrackviewManager getTrackviewManager() {
		return trackviewManager;
	}

	/**
	 * Opens the context menu to the given position.
	 * @param x mouse x coordinate
	 * @param y mouse y coordinate
	 */
	public void openContextMenu(float x, float y) {
		contextMenu = new ContextMenu(geneCircle.getChromosome(), geneCircle, x, y, this);
	}

	/**
	 * Returns the context menu object. Null if no context menu open.
	 * @return context menu object
	 */
	public ContextMenu getContextMenu() {
		return this.contextMenu;
	}

	/**
	 * Returns the mouse position as a vector object.
	 * @return 
	 */
	protected Vector2 getMousePosition() {
		return this.mousePosition;
	}

	/**
	 * Closes the context menu.
	 */
	public void closeContextMenu() {
		this.contextMenu = null;
	}

	/**
	 * Toggles the drawing of the arcs either on or off.
	 */
	public void setDrawArcs() {
		this.drawArcs = !this.drawArcs;
	}

	/**
	 * Sets the boolean value about if the circle needs updating.
	 * @param b should the circle be updated
	 */
	void setCircleUpdate(boolean b) {
		this.circleNeedsUpdate = b;
	}
}
