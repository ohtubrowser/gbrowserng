package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.util.awt.TextRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.SpaceDivider;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.LinkCollection;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.ContextMenu;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.CoordinateManager;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.media.opengl.GL2;
import math.Matrix4;
import math.Vector2;

public class OverView extends GenosideComponent {

	GeneCircle geneCircle = new GeneCircle();
	GeneCircleGFX geneCircleGFX = new GeneCircleGFX(geneCircle);
	GenoFPSCounter tickCounter = new GenoFPSCounter();
	GenoFPSCounter drawCounter = new GenoFPSCounter();
	private Vector2 mousePosition = new Vector2();
	private SessionViewCapsule hoverCapsule = null;
	ConcurrentLinkedQueue<SessionViewCapsule> sessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	ConcurrentLinkedQueue<SessionViewCapsule> activeSessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	LinkedList<SessionViewCapsule> textureUpdateList = new LinkedList<SessionViewCapsule>();
	ArrayList<ChromoName> chromoNames = new ArrayList<ChromoName>();
	private final Object textureUpdateListLock = new Object();
	public boolean die = false, arcHighlightLocked = false;
	OverViewState state = OverViewState.OVERVIEW_ACTIVE;
	public TextRenderer chromosomeNameRenderer;
	public TextRenderer textRenderer;
	private SimpleMouseEvent lastMouseClick;
	private LinkSelection linkSelection;
	public TrackviewManager trackviewManager;
	private LinkCollection linkCollection = new LinkCollection();
    private ContextMenu contextMenu;

	public OverView(GenoWindow window) {
		super(null);
		initTextRenderers();
		initChromoNames();
		trackviewManager = new TrackviewManager(window);
		geneCircle.setSize(0.485f);
		updateCircleSize();
		linkSelection = new LinkSelection(geneCircle);
	}

	private void initTextRenderers() {
		Font font;
		float fontSize = 40f;
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
		for (ViewChromosome chromosome : AbstractGenome.getChromosomes()) {
			chromoNames.add(new ChromoName(chromosome));
		}
	}

	@Override
	public void childComponentCall(String who, String what) {
		if (who.equals("SESSION")) {
			if (what.equals("SHRINK")) {
				disableActiveSession();
				state = OverViewState.OVERVIEW_ACTIVE;
			}
			if (what.equals("KILL")) {
				killActiveSession();
				disableActiveSession();
				state = OverViewState.OVERVIEW_ACTIVE;
			}
			if (what.equals("OPEN_ANOTHER")) {
				hideActiveSessions();
				state = OverViewState.OVERVIEW_ACTIVE;
			}
		}
	}

	private void showActiveSessions() {
		SpaceDivider divider = new SpaceDivider(SpaceDivider.HORIZONTAL, 1.0f, 2.0f);
		for (SessionViewCapsule capsule : activeSessions) {
			divider.insertComponent(capsule.getSession());
		}
		divider.calculate();
	}

	private void hideActiveSessions() {

		for (SessionViewCapsule otherCapsule : sessions) {
			otherCapsule.show();
		}

		for (SessionViewCapsule capsule : activeSessions) {
			capsule.getSession().setPosition(+2.5f, 0);
		}

	}

	private void killActiveSession() {
		if (activeSessions.isEmpty()) {
			return;
		}

		for (SessionViewCapsule capsule : activeSessions) {
			if (capsule.isActive()) {
				capsule.die();
			}
		}
	}

	// TODO: might be a good idea to break this function apart
	private void disableActiveSession() {
		if (activeSessions.isEmpty()) {
			return;
		}

		for (SessionViewCapsule otherCapsule : sessions) {
			otherCapsule.showBackround();
		}

		for (SessionViewCapsule capsule : activeSessions) {
			synchronized (textureUpdateListLock) {
				textureUpdateList.add(capsule);
				capsule.setNeedsTextureUpdate();
			}
			int chromosome = capsule.getSession().getSession().referenceSequence.chromosome;
			float position = capsule.getSession().getSession().position;
			float relativePosition = position / AbstractGenome.getChromosome(chromosome - 1).length();
			relativePosition = Math.min(Math.max(relativePosition, 0.0f), 1.0f);
			capsule.updateGeneCirclePosition(geneCircle.getRelativePosition(chromosome - 1, relativePosition));
			capsule.deactivate();
		}
		activeSessions.clear();
	}

	private void openSession(SessionViewCapsule capsule) {
		capsule.activate();
		activeSessions.add(capsule);

		showActiveSessions();
		state = OverViewState.SESSIONVIEW_ACTIVE;

		for (SessionViewCapsule otherCapsule : sessions) {
			boolean found = false;
			for (SessionViewCapsule activeCapsule : activeSessions) {
				if (otherCapsule.getId() == activeCapsule.getId()) {
					found = true;
				}
			}

			if (!found) {
				otherCapsule.hideBackground();
			}
		}
	}

	private void minimizeAllButOne(ViewChromosome chromosome) {
		int chromosomes = AbstractGenome.getNumChromosomes();
		for (int i = 0; i < chromosomes; ++i) {
			ViewChromosome c = AbstractGenome.getChromosome(i);
			if (c != chromosome) {
				c.setMinimized(true);
			}
		}
	}

	// TODO: This is becoming quite tedious. Consider writing separate input-handler classes.
	@Override
	public boolean handle(MouseEvent event, float x, float y) {
		float xx = x;
		float yy = y;
		x = CoordinateManager.toCircleCoordsY(x);
		y = CoordinateManager.toCircleCoordsX(y);

		if (lastMouseClick == null) {
			lastMouseClick = new SimpleMouseEvent(x, y, event.getWhen());
		}
		
		if(contextMenu!=null) {
			if(contextMenu.inComponent(xx,yy)) {
				contextMenu.handle(event, xx, yy);
				if(contextMenu.close()) {
					/*if(contextMenu.action()==0) contextMenu.getChromosome().setMinimized(true);
					if(contextMenu.action()==1) contextMenu.getChromosome().setMinimized(false);
					if(contextMenu.action()==2) minimizeAllButOne(contextMenu.getChromosome());*/
					contextMenu = null;
				}
				return true;
			}
		}
		
		this.hoverCapsule = null;
		for (SessionViewCapsule capsule : sessions) { // TODO : hoverCapsule is calculated many times in this function
			if (capsule.getSession().inComponent(xx, yy)) {
				this.hoverCapsule = capsule;
				break;
			}
		}
		// if there is an active session, let it handle input.
		if (!activeSessions.isEmpty() && hoverCapsule != null) {
			return hoverCapsule.getSession().handle(event, xx, yy);
		}

		// note, x axis is negated to make tracking begin from the mathematical zero angle.
		float pointerGenePosition = 1.0f - ((float) (Math.atan2(y, -x) / Math.PI) * 0.5f + 0.5f);
		for (ChromoName chromoName : chromoNames) {
			if (chromoName.isOver(xx, yy)) {
				int id = chromoName.getChromosome().getChromosomeNumber();
				float[] bounds = geneCircle.getChromosomeBoundaries();
				pointerGenePosition = bounds[id - 1] + (bounds[id] - bounds[id - 1]) / 2 + 0.25f;
			}
		}

		geneCircle.updatePosition(pointerGenePosition);
		if (!arcHighlightLocked) {
			if (pointOnCircle(x, y)) {
				linkSelection.update(pointerGenePosition);
			} else {
				linkSelection.reset();
			}
		}

		// allow capsules to update their states
		for (SessionViewCapsule capsule : sessions) {
			capsule.handle(event, xx, yy);
		}
		// then see if they actually want the event
		if (MouseEvent.EVENT_MOUSE_CLICKED == event.getEventType()) {
			if (event.getButton() == 1) {
				if(contextMenu!=null) {	    // context menu selection
				    if(contextMenu.handle(event, xx, yy)) {
					contextMenu = null;
					return true;
				    }
				    contextMenu = null;
				}
				for (SessionViewCapsule capsule : sessions) {
					if (capsule.isDying()) {
						continue;
					}
					if (capsule.handle(event, x, y)) {
						openSession(capsule);
						return true;
					}
				}
				if (pointOnCircle(x, y)) //				 respond to mouse click
				{
					arcHighlightLocked = true;
					linkSelection.update(pointerGenePosition);
					linkSelection.updateArea(linkCollection);
				} else if (arcHighlightLocked && pointInsideCircle(x, y)) {
					trackviewManager.clearContainer();
					trackviewManager.openLinkSession(linkSelection.getActiveLink());
					trackviewManager.toggleVisible();
				} else {
					if (arcHighlightLocked) {
						arcHighlightLocked = false;
						linkSelection.deactivate();
					} else {
						SessionViewCapsule capsule = new SessionViewCapsule(new SessionView(new Session(geneCircle.getChromosome().getReferenceSequence(), geneCircle.getChromosomePosition()), this), pointerGenePosition, geneCircle);
						capsule.getSession().setDimensions(0.4f, 0.2f);
						capsule.getSession().setPosition(x, y);
						sessions.add(capsule);
						//openSession(capsule);
						synchronized (textureUpdateListLock) {
							textureUpdateList.add(capsule);
							capsule.setNeedsTextureUpdate();
						}
					}
				}
			} else if (event.getButton() == 3) {
				for (SessionViewCapsule capsule : sessions) {
					if (capsule.isDying()) {
						continue;
					}
					if (capsule.handle(event, xx, yy)) {
						capsule.die();
						capsule.deactivate();
						return true;
					}
				}
				ViewChromosome chromosome = geneCircle.getChromosome();
				//chromosome.setMinimized(true);
				contextMenu = new ContextMenu(chromosome, geneCircle, xx, yy);
				/*if (chromosome.isMinimized()) {
					chromosome.setMinimized(false);
				} else {
					chromosome.setMinimized(true);
				}
				if ((chromosome.isAnimating() && !chromosome.isMinimized())
						&& lastMouseClick.getX() == x && lastMouseClick.getY() == y
						&& lastMouseClick.getWhen() + 250 > event.getWhen()) {
					minimizeAllButOne(chromosome);
				} else if ((chromosome.isAnimating() && chromosome.isMinimized())
						&& lastMouseClick.getX() == x && lastMouseClick.getY() == y
						&& lastMouseClick.getWhen() + 250 > event.getWhen()) {
					for (int i = 0; i < AbstractGenome.getNumChromosomes(); ++i) {
						AbstractGenome.getChromosome(i).setMinimized(false);
					}
				}*/
				lastMouseClick = new SimpleMouseEvent(x, y, event.getWhen());
				return true;
			}
		}

		// Wheel
		if (MouseEvent.EVENT_MOUSE_WHEEL_MOVED == event.getEventType()) {
			if (arcHighlightLocked) {
				linkSelection.updateArea(0.001f * event.getWheelRotation(), linkCollection);
			} else {
				geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() + event.getWheelRotation() * 0.05f));
				updateCircleSize();
			}
		}

		linkSelection.mouseMove(pointInsideCircle(x, y), xx, yy);
		mousePosition.x = x;
		mousePosition.y = y;
		return false;
	}

	@Override
	public boolean handle(KeyEvent event) {
		if(contextMenu!=null&&(event.getKeyCode()==KeyEvent.VK_DOWN||event.getKeyCode()==KeyEvent.VK_UP||event.getKeyCode()==KeyEvent.VK_ENTER)) {
			contextMenu.handle(event);
			if(contextMenu.close()) contextMenu = null;
			return true;
		}
		if (!activeSessions.isEmpty()) {
			for (SessionViewCapsule capsule : activeSessions) {
				if (capsule.inComponent(mousePosition.x, mousePosition.y)) {
					return capsule.getSession().handle(event);
				}
			}
		}
		if (arcHighlightLocked) {
			if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				trackviewManager.clearContainer();
				trackviewManager.openLinkSession(linkSelection.getActiveLink());
				trackviewManager.toggleVisible();
			}
			linkSelection.handle(event);
		}

		if (KeyEvent.VK_Z == event.getKeyCode()) {
			geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() - 0.01f));
			updateCircleSize();
		} else if (KeyEvent.VK_A == event.getKeyCode()) {
			geneCircle.setSize(geneCircle.getSize() + 0.01f);
			updateCircleSize();
		} else if (KeyEvent.VK_SPACE == event.getKeyCode()) {
			Random r = new Random();
			for (int i = 0; i < 1000; ++i) {
				ViewChromosome begin = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
				ViewChromosome end = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
				linkCollection.addToQueue(geneCircle, begin, end, r.nextInt((int) begin.length()), r.nextInt((int) end.length()));
			}
		}
		return false;
	}

	@Override
	public void draw(GL2 gl) {
		//trackviewManager.switchContainers();
		Vector2 mypos = this.getPosition();
		Matrix4 geneCircleModelMatrix = CoordinateManager.getCircleMatrix();
		geneCircleModelMatrix.translate(mypos.x, mypos.y, 0);
		geneCircleModelMatrix.scale(geneCircle.getSize(), geneCircle.getSize(), geneCircle.getSize());
		GeneralLink.beginDrawing(gl, geneCircle.getSize());
		if (arcHighlightLocked) {
			linkSelection.draw(gl);
		} else {
			synchronized (linkCollection.linkSyncLock) {
				for (GeneralLink link : linkCollection.getLinks()) {
					link.draw(gl, 1.0f, 0.0f, 0.0f);
				}
			}
		}
		GeneralLink.endDrawing(gl);
		geneCircleGFX.draw(gl, geneCircleModelMatrix, this.mousePosition);
		if (arcHighlightLocked) {
			linkSelection.draw(gl, geneCircle);
		}

		synchronized (textureUpdateListLock) {
			for (SessionViewCapsule capsule : textureUpdateList) {
				capsule.drawToTexture(gl);
			}
			textureUpdateList.clear();
		}

		for (SessionViewCapsule capsule : activeSessions) {
			synchronized (capsule.tickdrawLock) {
				capsule.drawToTexture(gl);
			}
		}

		for (SessionViewCapsule capsule : sessions) {
			synchronized (capsule.tickdrawLock) {
				capsule.draw(gl);
			}
		}
		int width = GlobalVariables.width, height = GlobalVariables.height;
		textRenderer.beginRendering(width, height);
		textRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
		String fps = "Tick: " + tickCounter.getMillis() + "ms";
		int stringHeight = (int) textRenderer.getBounds(fps).getHeight();
		textRenderer.draw(fps, 20, height - stringHeight - 7);
		String draw = "Draw: " + drawCounter.getMillis() + "ms";
		textRenderer.draw(draw, 20, (int) (height - stringHeight * 2.2));
		String arcs = "Arcs: " + linkCollection.getLinks().size();
		textRenderer.draw(arcs, 20, (int) (height - stringHeight * 3.3));

		if (state == OverViewState.OVERVIEW_ACTIVE) {
			// Mouse hover information
			long position;
			int chromosome;
			if (hoverCapsule == null) {
				position = this.geneCircle.getChromosomePosition();
				chromosome = this.geneCircle.getChromosome().getChromosomeNumber();
			} else {
				position = (long) this.hoverCapsule.getSession().getSession().position;
				chromosome = this.hoverCapsule.getSession().getSession().referenceSequence.chromosome;
			}

			String chrom = "Chromosome " + chromosome;
			String pos = "Position: " + position;
			textRenderer.draw(chrom, 20, stringHeight + 20);
			textRenderer.draw(pos, 20, 10);
		}
		textRenderer.endRendering();

		drawNumbers();
		
		if(contextMenu!=null) {
			contextMenu.draw(gl);
		}
	}

	private void drawNumbers() {
		int width = GlobalVariables.width, height = GlobalVariables.height;
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
			float angle = rotationv.relativeAngle(chromobounds[i % AbstractGenome.getNumChromosomes()]) / 2; // Rotate the numbers to the center of the chromosome.
			rotationv.rotate((angle < 0) ? angle : -((float) Math.PI - angle)); // Fix the >180 angle.

			// Convert to circlecoords using the rotated vector.
			Vector2 vv = new Vector2(CoordinateManager.toCircleCoords(rotationv));
			String chromoname = AbstractGenome.getChromosome(i - 1).getName();

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

	private void fadeLinks(float dt) {
		ViewChromosome thisChromo;
		thisChromo = geneCircle.getChromosome();
		synchronized (linkCollection.linkSyncLock) {
			for (GeneralLink link : linkCollection.getLinks()) {
				if (!link.isMinimized()) {
					if (linkSelection.inSelection(link)) {
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

	@Override
	public void userTick(float dt) {
		if (geneCircle.animating) {
			geneCircle.tick(dt);
			updateCircleSize();
		}
		linkSelection.tick(dt, linkCollection);
		linkCollection.tick(dt);
		geneCircleGFX.tick(dt);

		fadeLinks(dt);

		SessionViewCapsule killCapsule = null;
		for (SessionViewCapsule capsule : sessions) {
			if (!capsule.isAlive()) {
				killCapsule = capsule;
			}
			capsule.tick(dt);
			capsule.clearPositionAdjustment();
		}

		// if no active session, try to place session views in a good way.
		if (activeSessions.isEmpty()) {
			for (SessionViewCapsule capsule1 : sessions) {
				if (capsule1.isDying()) {
					continue;
				}

				// push away from the origin
				Vector2 position = new Vector2();
				position.copyFrom(capsule1.getSession().getPosition());
				position.normalize();
				position.scale(0.004f);
				capsule1.incrementPositionAdjustment(position.x, position.y);

				// pull towards target
				position.copyFrom(capsule1.getSession().getPosition());
				position.x -= capsule1.getPosition().x;
				position.y -= capsule1.getPosition().y;

				// to prevent overdoing things.
				float pow = Math.min(0.2f, position.lengthSquared());
				position.scale(0.1f * pow);

				capsule1.incrementPositionAdjustment(-position.x, -position.y);

				for (SessionViewCapsule capsule2 : sessions) {
					if (capsule1.getId() == capsule2.getId()) {
						continue;
					}
					if (capsule2.isDying()) {
						continue;
					}

					if (capsule1.getSession().getPosition().distance(capsule2.getSession().getPosition()) < 0.5f) {
						Vector2 v = capsule1.getSession().getPosition().minus(capsule2.getSession().getPosition());
						v.scale(0.0005f / (0.001f + v.lengthSquared()));
						capsule1.incrementPositionAdjustment(+v.x, +v.y);
						capsule2.incrementPositionAdjustment(-v.x, -v.y);
					}
				}
			}
		}

		if (killCapsule != null) {
			sessions.remove(killCapsule);
		}
	}

	public GenoFPSCounter getDrawCounter() {
		return drawCounter;
	}

	public GenoFPSCounter getTickCounter() {
		return tickCounter;
	}

	private void updateCircleSize() {
		for (SessionViewCapsule capsule : sessions) {
			capsule.updateGeneCirclePosition();
		}
		for (GeneralLink link : linkCollection.getLinks()) {
			link.calculatePositions(geneCircle);
		}
	}

	private boolean pointOnCircle(float x, float y) {

		float xx = CoordinateManager.fromCircleCoordsY(x);
		float yy = CoordinateManager.fromCircleCoordsX(y);

		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(size);
		float b = CoordinateManager.toCircleCoordsY(size);
		float s = Math.abs(xx * xx / (a * a) + yy * yy / (b * b));
		if (s < 1.0f && s > 0.8f) {
			return true;
		}
		return false;
	}

	private boolean pointInsideCircle(float x, float y) {

		float xx = CoordinateManager.fromCircleCoordsY(x);
		float yy = CoordinateManager.fromCircleCoordsX(y);

		float size = geneCircle.getSize();
		float a = CoordinateManager.toCircleCoordsX(size);
		float b = CoordinateManager.toCircleCoordsY(size);
		float s = Math.abs(xx*xx/(a*a) + yy*yy/(b*b));
		if (s < 0.9f)
		{
			return true;
		}
		return false;
	}
}

