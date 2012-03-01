package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.util.awt.TextRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.SpaceDivider;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.*;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.chipsterIntegration.ChipsterInterface;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoShaders;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import gles.SoulGL2;
import gles.shaders.Shader;
import gles.shaders.ShaderMemory;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.LinkedList;
import math.Matrix4;
import math.Vector2;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.media.opengl.GL2;
import managers.ShaderManager;
import soulaim.DesktopGL2;

public class OverView extends GenosideComponent {

	GeneCircle geneCircle = new GeneCircle();
	GeneCircleGFX geneCircleGFX = new GeneCircleGFX(geneCircle);
	GenoFPSCounter tickCounter = new GenoFPSCounter();
	GenoFPSCounter drawCounter = new GenoFPSCounter();
	private Vector2 mousePosition = new Vector2();
	private SessionViewCapsule hoverCapsule = null;
	ConcurrentLinkedQueue<SessionViewCapsule> sessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	ConcurrentLinkedQueue<SessionViewCapsule> activeSessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	ConcurrentLinkedQueue<GeneralLink> links = new ConcurrentLinkedQueue<GeneralLink>();
	LinkedList<SessionViewCapsule> textureUpdateList = new LinkedList<SessionViewCapsule>();
	ArrayList<ChromoName> chromoNames = new ArrayList<ChromoName>();
	private final Object textureUpdateListLock = new Object();
	public boolean die = false, arcHighlightLocked = false;
	OverViewState state = OverViewState.OVERVIEW_ACTIVE;
	public TextRenderer chromosomeNameRenderer;
	public TextRenderer textRenderer;
	private SimpleMouseEvent lastMouseClick;
	private LinkSelection linkSelection = new LinkSelection();

	public OverView(ConcurrentLinkedQueue<GeneralLink> links) {
		super(null);
		this.links=links;
		for(GeneralLink l : links) {
			l.calculatePositions(geneCircle);
		}
		initTextRenderers();
		initChromoNames();
		geneCircle.setSize(0.485f);
		updateCircleSize();
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
		for (Chromosome chromosome : AbstractGenome.getChromosomes()) {
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

	private void minimizeAllButOne(Chromosome chromosome) {
		int chromosomes = AbstractGenome.getNumChromosomes();
		for (int i = 0; i < chromosomes; ++i) {
			Chromosome c = AbstractGenome.getChromosome(i);
			if (c != chromosome) {
				c.setMinimized(true);
			}
		}
	}

	// TODO: This is becoming quite tedious. Consider writing separate input-handler classes.
	@Override
	public boolean handle(MouseEvent event, float x, float y) {
		if (lastMouseClick == null) {
			lastMouseClick = new SimpleMouseEvent(x, y, event.getWhen());
		}
		this.hoverCapsule = null;
		for (SessionViewCapsule capsule : sessions) { // TODO : hoverCapsule is calculated many times in this function
			if (capsule.getSession().inComponent(x, y)) {
				this.hoverCapsule = capsule;
				break;
			}
		}
		// if there is an active session, let it handle input.
		if (!activeSessions.isEmpty() && hoverCapsule != null) {
			return hoverCapsule.getSession().handle(event, x, y);
		}

		// note, x axis is negated to make tracking begin from the mathematical zero angle.
		float pointerGenePosition = 1.0f - ((float) (Math.atan2(y, -x) / Math.PI) * 0.5f + 0.5f);
		for (ChromoName chromoName : chromoNames) {
			if (chromoName.isOver(x, y)) {
				synchronized (geneCircle.tickdrawLock) {
					int id = chromoName.getChromosome().getChromosomeNumber();
					float[] bounds = geneCircle.getChromosomeBoundaries();
					pointerGenePosition = bounds[id - 1] + (bounds[id] - bounds[id - 1]) / 2 + 0.25f;
				}
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
			capsule.handle(event, x, y);
		}
		// then see if they actually want the event
		if (MouseEvent.EVENT_MOUSE_CLICKED == event.getEventType()) {
			if (event.getButton() == 1) {
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
					linkSelection.updateArea(links);
				} else {
					if (arcHighlightLocked) {
						arcHighlightLocked = false;
						linkSelection.deactivate();
					} else {
						System.out.println("Adding capsule with " + x + " " + y);

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
					if (capsule.handle(event, x, y)) {
						capsule.die();
						capsule.deactivate();
						return true;
					}
				}
				Chromosome chromosome = geneCircle.getChromosome();
				if (chromosome.isMinimized()) {
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
				}
				geneCircle.animating = true;
				lastMouseClick = new SimpleMouseEvent(x, y, event.getWhen());
				return true;
			}
		}

		// Wheel
		if (MouseEvent.EVENT_MOUSE_WHEEL_MOVED == event.getEventType()) {
			if (arcHighlightLocked) {
				linkSelection.updateArea(0.001f * event.getWheelRotation(), links);
			} else {
				geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() + event.getWheelRotation() * 0.05f));
				updateCircleSize();
			}
		}

		mousePosition.x = x;
		mousePosition.y = y;
		return false;
	}

	@Override
	public boolean handle(KeyEvent event) {
		if (!activeSessions.isEmpty()) {
			for (SessionViewCapsule capsule : activeSessions) {
				if (capsule.inComponent(mousePosition.x, mousePosition.y)) {
					return capsule.getSession().handle(event);
				}
			}
		}
		if (arcHighlightLocked) {
			linkSelection.handle(event);
		}

		if (KeyEvent.VK_Z == event.getKeyCode()) {
			geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() - 0.01f));
			updateCircleSize();
		} else if (KeyEvent.VK_A == event.getKeyCode()) {
			geneCircle.setSize(geneCircle.getSize() + 0.01f);
			updateCircleSize();
		} else if (KeyEvent.VK_SPACE == event.getKeyCode()) {
			
/*			
			ConcurrentLinkedQueue<long[]> queue = ChipsterInterface.getConnectionsBetweenChrs();


			GeneralLink newlink = new GeneralLink(table[0], table[1], 0, table[2], 0, table[3], table[4], table[5]);
			newlink.calculatePositions(geneCircle);
			links.add(newlink);

			}

			for (long[] table : queue) {
				GeneralLink newlink = new GeneralLink(table[0], table[1], 0, table[2], 0, table[3], table[4], table[5]);
				newlink.calculatePositions(geneCircle);
				links.add(newlink);

			}
			System.out.println(links.size());
			System.out.println("finished");
			 */
			Random r = new Random();
			for (int i = 0; i < 1000; ++i) {
				Chromosome begin = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
				Chromosome end = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
				GeneralLink newlink = new GeneralLink(begin, end, 0, r.nextInt((int) begin.length()), 0, r.nextInt((int) end.length()));
				newlink.calculatePositions(geneCircle);
				links.add(newlink);
			}
		}
		return false;
	}

	@Override
	public void draw(GL2 gl) {
		Vector2 mypos = this.getPosition();
		Matrix4 geneCircleModelMatrix = new Matrix4();
		geneCircleModelMatrix.makeTranslationMatrix(mypos.x, mypos.y, 0);
		geneCircleModelMatrix.scale(geneCircle.getSize(), geneCircle.getSize(), geneCircle.getSize());
		GeneralLink.beginDrawing(gl, geneCircle.getSize());
		if (arcHighlightLocked) {
			linkSelection.draw(gl);
		} else {
			for (GeneralLink link : links) {
				link.draw(gl, 1.0f, 0.0f, 0.0f);
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
		String arcs = "Arcs: " + links.size();
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

		chromosomeNameRenderer.beginRendering(width, height);
		chromosomeNameRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
		int i = 1;
		float halfHeight = height / 2f;
		float halfWidth = width / 2f;
		synchronized (geneCircle.tickdrawLock) {
			Vector2[] chromobounds = geneCircle.getChromosomeBoundariesPositions();
			float lastBound = 0f;
			float overlap = 1.04f;
			boolean first = true;
			for (Vector2 v : chromobounds) {
				Vector2 vv = new Vector2(v);
				float angle = v.relativeAngle(chromobounds[i % AbstractGenome.getNumChromosomes()]) / 2; // Rotate the numbers to the center of the chromosome.
				vv.rotate((angle < 0) ? angle : -((float) Math.PI - angle)); // Fix the >180 angle.
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
		}
		chromosomeNameRenderer.endRendering();
	}

	private void fadeLinks(float dt) {
		Chromosome thisChromo;
		synchronized (geneCircle.tickdrawLock) {
			thisChromo = geneCircle.getChromosome();
		}
		for (GeneralLink link : links) {
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

	@Override
	public void userTick(float dt) {
		synchronized (geneCircle.tickdrawLock) {
			if (geneCircle.animating) {
				geneCircle.tick(dt);
				updateCircleSize();
			}
		}
		linkSelection.tick(dt, links);
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
		for (GeneralLink link : links) {
			link.calculatePositions(geneCircle);
		}
	}

	private boolean pointOnCircle(float x, float y) {
		if (Math.abs(x * x + y * y - (geneCircle.getSize() * geneCircle.getSize() * 0.95 * 0.95)) < 0.1f * geneCircle.getSize() * geneCircle.getSize()) // TODO : magic numbers
		{
			return true;
		}
		return false;
	}
}
