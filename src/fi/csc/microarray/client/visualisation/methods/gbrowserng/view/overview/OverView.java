package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.util.awt.TextRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowser.message.Chromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.SpaceDivider;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GenoFPSCounter;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.SimpleMouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import gles.SoulGL2;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import managers.TextureManager;
import math.Matrix4;
import math.Vector2;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OverView extends GenosideComponent {

	GeneCircle geneCircle = new GeneCircle();
	GeneCircleGFX geneCircleGFX = new GeneCircleGFX(geneCircle);
	GenoFPSCounter fpsCounter = new GenoFPSCounter();
	private Vector2 mousePosition = new Vector2();
	private SessionViewCapsule hoverCapsule = null;
	ConcurrentLinkedQueue<SessionViewCapsule> sessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	ConcurrentLinkedQueue<SessionViewCapsule> activeSessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	ConcurrentLinkedQueue<GeneralLink> links = new ConcurrentLinkedQueue<GeneralLink>();
	LinkedList<SessionViewCapsule> textureUpdateList = new LinkedList<SessionViewCapsule>();
	private final Object textureUpdateListLock = new Object();
	public boolean die = false;
	OverViewState state = OverViewState.OVERVIEW_ACTIVE;
	public TextRenderer chromosomeNameRenderer;
	public TextRenderer textRenderer;
	private Vector2 showLinksInterval = new Vector2(0, 1);
	private SimpleMouseEvent lastMouseClick;

	public OverView() {
		super(null);
		initTextRenderers();
		geneCircle.setSize(0.485f);
		updateCircleSize();
	}

	private void initTextRenderers() {
		Font font;
		float fontSize = 40f;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/drawable/Tiresias Signfont Bold.ttf")).deriveFont(fontSize);
		} catch (IOException e) {
			font = new Font("SansSerif", Font.BOLD, (int)fontSize);
		} catch (FontFormatException e) {
			font = new Font("SansSerif", Font.BOLD, (int)fontSize);
		}
		this.textRenderer = new com.jogamp.opengl.util.awt.TextRenderer(font, true, true);

		Font smallFont;
		float smallfontSize = 10f;
		try {
			smallFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/drawable/Tiresias Signfont Bold.ttf")).deriveFont(smallfontSize);
		} catch (IOException e) {
			smallFont = new Font("SansSerif", Font.BOLD, (int)smallfontSize);
		} catch (FontFormatException e) {
			smallFont = new Font("SansSerif", Font.BOLD, (int)smallfontSize);
		}
		this.chromosomeNameRenderer = new com.jogamp.opengl.util.awt.TextRenderer(smallFont, true, true);
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
			float relativePosition = position / AbstractGenome.getChromosome(chromosome-1).length();
			relativePosition = Math.min(Math.max(relativePosition, 0.0f), 1.0f);
			capsule.updateGeneCirclePosition(geneCircle.getRelativePosition(chromosome-1, relativePosition));
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

	private void minimizeAllButOne(AbstractChromosome chromosome) {
		int chromosomes=AbstractGenome.getNumChromosomes();
		for(int i=0; i<chromosomes; ++i) {
			AbstractChromosome c = AbstractGenome.getChromosome(i);
			if(c!=chromosome) c.setMinimized(true);
		}
	}

	// TODO: This is becoming quite tedious. Consider writing separate input-handler classes.
	@Override
	public boolean handle(MouseEvent event, float x, float y) {
		if(lastMouseClick==null) lastMouseClick=new SimpleMouseEvent(x,y,event.getWhen());
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
		geneCircle.updatePosition(pointerGenePosition);
		if (Math.abs(mousePosition.lengthSquared() - (geneCircle.getSize() * geneCircle.getSize() * 0.95 * 0.95)) < 0.1f * geneCircle.getSize() * geneCircle.getSize()) { // TODO : magic numbers
			showLinksInterval.x = pointerGenePosition - 0.25f - 0.02f;
			showLinksInterval.y = pointerGenePosition - 0.25f + 0.02f;
			if(showLinksInterval.x < 0.0f)
				showLinksInterval.x += 1.0f;
			if(showLinksInterval.y < 0.0f)
				showLinksInterval.y += 1.0f;
			if(showLinksInterval.y > 1.0f)
					showLinksInterval.y -= 1.0f;
		} else {
			showLinksInterval.x = 0.0f;
			showLinksInterval.y = 1.0f;
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
//				 respond to mouse click
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
				AbstractChromosome chromosome = geneCircle.getChromosome();
				if (chromosome.isMinimized()) {
					chromosome.setMinimized(false);
				}
				else {
					chromosome.setMinimized(true);
				}
				if((chromosome.isAnimating() && !chromosome.isMinimized()) &&
						lastMouseClick.getX()==x && lastMouseClick.getY()==y &&
						lastMouseClick.getWhen()+250>event.getWhen()) {
					minimizeAllButOne(chromosome);
				}
				else if((chromosome.isAnimating() && chromosome.isMinimized()) &&
						lastMouseClick.getX()==x && lastMouseClick.getY()==y &&
						lastMouseClick.getWhen()+250>event.getWhen()) {
					for(int i=0; i<AbstractGenome.getNumChromosomes(); ++i) AbstractGenome.getChromosome(i).setMinimized(false);
				}
				geneCircle.animating = true;
				lastMouseClick = new SimpleMouseEvent(x,y,event.getWhen());
				return true;
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
		if (KeyEvent.VK_Z == event.getKeyCode()) {
			geneCircle.setSize(Math.max(0.0f, geneCircle.getSize() - 0.01f));
			updateCircleSize();
		} else if (KeyEvent.VK_A == event.getKeyCode()) {
			geneCircle.setSize(geneCircle.getSize() + 0.01f);
			updateCircleSize();
		} else if (KeyEvent.VK_SPACE == event.getKeyCode()) {
			Random r = new Random();
			AbstractChromosome begin = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
			AbstractChromosome end = AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
			GeneralLink newlink = new GeneralLink(begin, end, 0, r.nextInt((int) begin.length()), 0, r.nextInt((int) end.length()));
			newlink.calculatePositions(geneCircle);
			links.add(newlink);
		}
		return false;
	}

	@Override
	public void draw(SoulGL2 gl) {
		Vector2 mypos = this.getPosition();
		Matrix4 geneCircleModelMatrix = new Matrix4();
		geneCircleModelMatrix.makeTranslationMatrix(mypos.x, mypos.y, 0);
		geneCircleModelMatrix.scale(geneCircle.getSize(), geneCircle.getSize(), geneCircle.getSize());
		for (GeneralLink link : links) {
			link.draw(gl, geneCircle.getSize());
		}
		geneCircleGFX.draw(gl, geneCircleModelMatrix, this.mousePosition);

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
		String fps = "FPS: " + fpsCounter.getFps();
		int stringHeight = (int)textRenderer.getBounds(fps).getHeight();
		textRenderer.draw(fps, (int)(width/2-textRenderer.getBounds(fps).getWidth()/2), height-stringHeight-7);
		String draw = "Draw: " + fpsCounter.getMillis() + "ms";
		textRenderer.draw(draw, (int)(width/2-textRenderer.getBounds(draw).getWidth()/2), height-stringHeight*2-15);
		String arcs = "Arcs: " + links.size();
		textRenderer.draw(arcs, (int) (width / 2 - textRenderer.getBounds(arcs).getWidth() / 2), height - stringHeight * 2 - 55);

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
			textRenderer.draw(chrom, (int)(width/2-textRenderer.getBounds(chrom).getWidth()/2), stringHeight+20);
			textRenderer.draw(pos, (int)(width/2-textRenderer.getBounds(pos).getWidth()/2), 10);
		}
		textRenderer.endRendering();

		chromosomeNameRenderer.beginRendering(width, height);
		chromosomeNameRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
		int i=1;
		Vector2[] chromobounds=geneCircle.getChromosomeBoundariesPositions();
		for (Vector2 v : chromobounds)
		{
			Vector2 vv=new Vector2(v);
			float angle=v.relativeAngle(chromobounds[i % 23])/2; // Rotate the numbers to the center of the chromosome.
			vv.rotate((angle<0)?angle:(-((float)Math.PI-angle))); // Fix the >180 angle.
			String chromoname=String.valueOf(i);

			// Magic constant 1.90 is for positioning the numbers a little bit out from the circle.
			chromosomeNameRenderer.draw(
					chromoname,
					(width/2)+(int)((width*(vv.x)-chromosomeNameRenderer.getBounds(chromoname).getWidth())/1.90),
					(height/2)+(int)(((height*vv.y)-chromosomeNameRenderer.getBounds(chromoname).getHeight())/1.90)
			);
			++i;
		}
		chromosomeNameRenderer.endRendering();
	}

	private void fadeLinks(float dt) {
		// TODO: Hide links between two closed chromosomes.
		for(GeneralLink link : links) {
			int thischromonumber=geneCircle.getChromosome().getChromosomeNumber();
			float[] chromobounds=geneCircle.getChromosomeBoundaries();
			float thischromostart=chromobounds[thischromonumber-1];
			float thischromoend=chromobounds[(thischromonumber)%chromobounds.length];
			if(link.inInterval(showLinksInterval)) {
				link.fadeIn(dt*16);
			}
			else if(((link.getStartPos() <= thischromostart) && (link.getStartPos() > thischromoend)) ||
					((link.getEndPos() <= thischromostart) && (link.getEndPos() > thischromoend))) {
				link.fadeDim(dt*8);
			}
			else link.fadeOut(dt*8);
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
		geneCircleGFX.tick(dt);
		fpsCounter.tick(dt);

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

	public GenoFPSCounter getFpsCounter() {
		return fpsCounter;
	}

	private void updateCircleSize() {
		for (SessionViewCapsule capsule : sessions) {
			capsule.updateGeneCirclePosition();
		}
		for (GeneralLink link : links) {
			link.calculatePositions(geneCircle);
		}
	}
}