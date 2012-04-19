package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.opengl.util.awt.TextRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.CapsuleManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.ContextMenu;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.media.opengl.GL2;

public class OverView extends GenosideComponent {

	public GeneCircle geneCircle = new GeneCircle();
	private GeneCircleGFX geneCircleGFX = new GeneCircleGFX(geneCircle);
	private GenoFPSCounter tickCounter = new GenoFPSCounter();
	private GenoFPSCounter drawCounter = new GenoFPSCounter();
	private Vector2 mousePosition = new Vector2();
	private SessionViewCapsule hoverCapsule = null;
	public GenoWindow window;
	private ConcurrentHashMap<Integer, SessionViewCapsule> sessions = CapsuleManager.getSessions();//new ConcurrentLinkedQueue<SessionViewCapsule>();
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

	// initialize object and neede parts
	public OverView(GenoWindow window, LinkCollection linkCollection) {
		drawArcs = false;
		this.window = window;
		initTextRenderers();
		initChromoNames();
		trackviewManager = new TrackviewManager(window);
		this.linkCollection = linkCollection;
		geneCircle.setSize(0.485f);
		updateCircleSize();
		linkSelection = new LinkSelection(geneCircle);
		mouseEventHandler = new MouseEventHandler(this);
		keyEventHandler = new KeyEventHandler(this);
	}

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

	private void initChromoNames() {
		for (ViewChromosome chromosome : Genome.getChromosomes()) {
			chromoNames.add(new ChromoName(chromosome));
		}
	}

	public void updateCircleSize() {
		for (SessionViewCapsule capsule : sessions.values()) {
			capsule.setRelativePosition(geneCircle);
		}
		for (GeneralLink link : linkCollection.getLinks()) {
			link.calculatePositions(geneCircle);
		}
	}

	// handle events
	@Override
	public boolean handle(KeyEvent event) {
		return this.keyEventHandler.handle(event);
	}

	@Override
	public boolean handle(MouseEvent event, float screen_x, float screen_y) {
		return mouseEventHandler.handle(event, screen_x, screen_y, this.geneCircle);
	}

	// handle sessions
	public void openSession(SessionViewCapsule capsule) {
		if (capsule.isLinkSession()) {
			trackviewManager.openLinkSession(capsule.getLink());
		} else {
			trackviewManager.openAreaSession(capsule.getChromosome(), capsule.getChrPosition(), capsule.getChrPosition()+1000);
		}
		trackviewManager.toggleVisible();
	}

	// drawing things + fading things
	@Override
	public void draw(GL2 gl) {
		if (linkCollection.loading) {
			drawArcs = false;
			window.c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			drawArcs = true;
			window.c.setCursor(Cursor.getDefaultCursor());
		}
		Matrix4 geneCircleModelMatrix = CoordinateManager.getCircleMatrix();
		geneCircleModelMatrix.scale(geneCircle.getSize(), geneCircle.getSize(), geneCircle.getSize());

		if (drawArcs) {
			GeneralLink.beginDrawing(gl, geneCircle.getSize());
			if (arcHighlightLocked) {
				linkSelection.draw(gl);
			} else {
				synchronized (linkCollection.linkSyncLock) {
					for (GeneralLink link : linkCollection.getLinks()) {
						link.draw(gl);
					}
				}
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
		renderText(GlobalVariables.width, GlobalVariables.height);
		drawNumbers(GlobalVariables.width, GlobalVariables.height);

		if (contextMenu != null) {
			contextMenu.draw(gl);
		}
	}

	private void drawCapsules(GL2 gl) {
		synchronized (textureUpdateListLock) {
			for (SessionViewCapsule capsule : textureUpdateList) {
				capsule.drawToTexture(gl);
			}
			textureUpdateList.clear();
		}

		/*
		for (SessionViewCapsule capsule : activeSessions) {
			capsule.drawToTexture(gl);
		}
		*/

		for (SessionViewCapsule capsule : sessions.values()) {
			capsule.draw(gl);
		}
	}

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
			float angle = rotationv.relativeAngle(chromobounds[i % Genome.getNumChromosomes()]) / 2; // Rotate the numbers to the center of the chromosome.
			rotationv.rotate((angle < 0) ? angle : -((float) Math.PI - angle)); // Fix the >180 angle.

			// Convert to circlecoords using the rotated vector.
			Vector2 vv = new Vector2(CoordinateManager.toCircleCoords(rotationv));
			String chromoname = Genome.getChromosome(i - 1).getName();

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

	private void renderText(int width, int height) {
		textRenderer.beginRendering(width, height);
		textRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
		int stringHeight = (int) textRenderer.getBounds("TEXT").getHeight();
		if(GlobalVariables.debug == true) {
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
		if (arcHighlightLocked && linkSelection.mouseInCircle()) {
			GeneralLink link = linkSelection.getActiveLink();
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

	private void fadeLinks(float dt) {
		ViewChromosome thisChromo;
		thisChromo = geneCircle.getChromosome();
		synchronized (linkCollection.linkSyncLock) {
			for (int i = 0; i < linkCollection.numLinks(); ++i) {
				GeneralLink link = linkCollection.valueAt(i);
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
	}

	// tick - used for ?
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
		linkCollection.tick(dt, geneCircle);
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
			sessions.remove(killCapsule);
		}
	}

	// getters and setters 
	public GenoFPSCounter getDrawCounter() {
		return drawCounter;
	}

	public GenoFPSCounter getTickCounter() {
		return tickCounter;
	}

	public void setHoverCapsule(SessionViewCapsule capsule) {
		this.hoverCapsule = capsule;
	}

	/*
	public ConcurrentLinkedQueue<SessionViewCapsule> getActiveSessions() {
		return this.activeSessions;
	}
	*/

	public SessionViewCapsule getHoverCapsule() {
		return this.hoverCapsule;
	}

	public ConcurrentHashMap<Integer, SessionViewCapsule> getSessions() {
		return this.sessions;
	}

	public ArrayList<ChromoName> getChromoNames() {
		return this.chromoNames;
	}

	public GeneCircle getGeneCircle() {
		return this.geneCircle;
	}

	public boolean isArcHighlightLocked() {
		return this.arcHighlightLocked;
	}

	public LinkSelection getLinkSelection() {
		return this.linkSelection;
	}

	public void setArcHighlightLocked(boolean b) {
		this.arcHighlightLocked = b;
	}

	public void setMousePositionX(float x, float y) {
		this.mousePosition.x = x;
		this.mousePosition.y = y;
	}

	public LinkedList<SessionViewCapsule> getTextureUpdateList() {
		return this.textureUpdateList;
	}

	public void addCapsuleToTextureUpdateList(SessionViewCapsule capsule) {
		this.textureUpdateList.add(capsule);
	}

	public LinkCollection getLinkCollection() {
		return linkCollection;
	}

	public TrackviewManager getTrackviewManager() {
		return trackviewManager;
	}

	public void openContextMenu(float x, float y) {
		contextMenu = new ContextMenu(geneCircle.getChromosome(), geneCircle, x, y, this);
	}

	public ContextMenu getContextMenu() {
		return this.contextMenu;
	}

	protected Vector2 getMousePosition() {
		return this.mousePosition;
	}

	public void closeContextMenu() {
		this.contextMenu = null;
	}

	public void setDrawArcs() {
		this.drawArcs = !this.drawArcs;
	}

	void setCircleUpdate(boolean b) {
		this.circleNeedsUpdate = b;
	}
}
