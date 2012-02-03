package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.SpaceDivider;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractChromosome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneralLink;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GenoFPSCounter;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.GenoButton;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.ids.GenoTexID;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import gles.SoulGL2;
import gles.renderer.TextRenderer;
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
	private final GenoButton ConnectionsButton = new GenoButton(this, "CONNECTIONS_BUTTON", 1.0f, 1.0f, -0.055f, -0.075f, GenoTexID.CONNECTIONS_BUTTON);
	ConcurrentLinkedQueue<SessionViewCapsule> sessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	ConcurrentLinkedQueue<SessionViewCapsule> activeSessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
	ConcurrentLinkedQueue<GeneralLink> links = new ConcurrentLinkedQueue<GeneralLink>();
	RecentSessionManager recentSessions = new RecentSessionManager();
	OverViewState state = OverViewState.OVERVIEW_ACTIVE;

	public OverView() {
		super(null);
		geneCircle.setSize(0.485f);
		updateCircleSize();
		ConnectionsButton.setDimensions(0.1f, 0.1f);
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
				recentSessions.Add(capsule);
			}
		}
	}

	// TODO: might be a good idea to break this function apart
	private void disableActiveSession() {
		if (activeSessions.isEmpty()) {
			return;
		}

		for (SessionViewCapsule otherCapsule : sessions) {
			otherCapsule.show();
		}
		for(SessionViewRecentCapsule recentCapsule : recentSessions) recentCapsule.show();

		for (SessionViewCapsule capsule : activeSessions) {
			int chromosome = capsule.getSession().getSession().referenceSequence.chromosome;
			float position = capsule.getSession().getSession().position;
			float relativePosition = position / capsule.getSession().getSession().referenceSequence.sequence.length;
			relativePosition = Math.min(Math.max(relativePosition, 0.0f), 1.0f);
			capsule.updateGeneCirclePosition(geneCircle.getRelativePosition(chromosome, relativePosition));
			capsule.deactivate();
		}

		activeSessions.clear();
	}
	
	private void openSession(SessionViewCapsule capsule)
	{
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
				otherCapsule.hide();
			}
		}
		for (SessionViewRecentCapsule recentCapsule : recentSessions) {
			recentCapsule.hide();
		}
	}
	
	private void restoreCapsule(SessionViewRecentCapsule capsule)
	{
		SessionViewCapsule restorecapsule = new SessionViewCapsule(new SessionView(capsule.getSession(), this), capsule.getOldGeneCirclePosition(), geneCircle);
		restorecapsule.getSession().setDimensions(0.4f, 0.2f);
		Vector2 oldpos = capsule.getOldPosition();
		restorecapsule.getSession().setPosition(oldpos.x, oldpos.y);
		sessions.add(restorecapsule);
		recentSessions.Remove(capsule);
	}

	// TODO: This is becoming quite tedious. Consider writing separate input-handler classes.
	public boolean handle(MouseEvent event, float x, float y) {
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

		// allow capsules to update their states
		for (SessionViewCapsule capsule : sessions) {
			capsule.handle(event, x, y);
		}

		for (SessionViewRecentCapsule capsule : recentSessions) {
			capsule.handle(event, x, y);
		}

		ConnectionsButton.handle(event,x,y);

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
				for (SessionViewRecentCapsule capsule : recentSessions) {
					if (capsule.handle(event, x, y)) {
						restoreCapsule(capsule);
						return true;
					}
				}
				// respond to mouse click
				System.out.println("Adding capsule with " + x + " " + y);
				SessionViewCapsule capsule = new SessionViewCapsule(new SessionView(new Session(geneCircle.getChromosome().getReferenceSequence(), geneCircle.getChromosomePosition()), this), pointerGenePosition, geneCircle);
				capsule.getSession().setDimensions(0.4f, 0.2f);
				capsule.getSession().setPosition(x, y);
				sessions.add(capsule);
			}
			else if (event.getButton() == 3) {
				for (SessionViewCapsule capsule : sessions) {
					if (capsule.isDying()) {
						continue;
					}
					if (capsule.handle(event, x, y)) {
						capsule.die();
						capsule.deactivate();
						recentSessions.Add(capsule);
					}
				}
				return true;
			}
		}
		
		mousePosition.x = x;
		mousePosition.y = y;
		return false;
	}

	public boolean handle(KeyEvent event) {
		if (!activeSessions.isEmpty()) {
			for (SessionViewCapsule capsule : activeSessions) {
				if (capsule.inComponent(mousePosition.x, mousePosition.y)) {
					return capsule.getSession().handle(event);
				}
			}
		}
		if(event.VK_Z == event.getKeyCode())
		{
		  geneCircle.setSize(Math.max(0.0f, geneCircle.getSize()-0.01f));
		  updateCircleSize();
		}
		else if(event.VK_A == event.getKeyCode()) {
		    	geneCircle.setSize(geneCircle.getSize()+0.01f);
			updateCircleSize();
		}
		else if(event.VK_SPACE == event.getKeyCode())
		{
			Random r = new Random();
			AbstractChromosome begin=AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
			AbstractChromosome end=AbstractGenome.getChromosome(r.nextInt(AbstractGenome.getNumChromosomes()));
			GeneralLink newlink=new GeneralLink(begin,end,0,r.nextInt((int)begin.length()),0,r.nextInt((int)end.length()));
			newlink.calculatePositions(geneCircle);
			links.add(newlink);
		}
		return false;
	}

	public void draw(SoulGL2 gl) {
		Vector2 mypos = this.getPosition();
		Matrix4 geneCircleModelMatrix = new Matrix4();
		geneCircleModelMatrix.makeTranslationMatrix(mypos.x, mypos.y, 0);
		geneCircleModelMatrix.scale(geneCircle.getSize(), geneCircle.getSize(), geneCircle.getSize());
		for (GeneralLink link : links) {
		    link.draw(gl);
		}
		geneCircleGFX.draw(gl, geneCircleModelMatrix, this.mousePosition);

		for (SessionViewCapsule capsule : sessions) {
			capsule.draw(gl);
		}

		for (SessionViewRecentCapsule capsule : recentSessions) {
			capsule.draw(gl);
		}
		
		TextRenderer.getInstance().drawText(gl, "FPS: " + fpsCounter.getFps(), 0, 0.92f, 0.9f);
		TextRenderer.getInstance().drawText(gl, "Draw: " + fpsCounter.getMillis() + "ms", 0, 0.84f, 0.9f);

		if (state == OverViewState.OVERVIEW_ACTIVE) {
			ConnectionsButton.draw(gl);
			// Mouse hover information
			// TODO: Show the info of a session view, when hovering mouse over session view.
			long position; int chromosome;
			if (hoverCapsule == null) {
				position = this.geneCircle.getChromosomePosition();
				chromosome = this.geneCircle.getChromosome().getChromosomeNumber();
			} else {
				position = (long)this.hoverCapsule.getSession().getSession().position;
				chromosome = this.hoverCapsule.getSession().getSession().referenceSequence.chromosome;
			}
			TextureManager.bindTexture(gl, GenoTexID.FONT);
			TextRenderer.getInstance().drawText(gl, "Chromosome " + chromosome, 0, -0.86f, 0.8f);
			TextRenderer.getInstance().drawText(gl, "Position: " + position, 0, -0.95f, 0.8f);
		}
	}

	@Override
	public void userTick(float dt) {
		geneCircleGFX.tick(dt);
		fpsCounter.tick(dt);

		SessionViewCapsule killCapsule = null;
		for (SessionViewCapsule capsule : sessions) {
			if (!capsule.isAlive()) {
				killCapsule = capsule;
			}
			capsule.tick(dt);
			capsule.clearPositionAdjustment();
		}

		for (SessionViewRecentCapsule capsule : recentSessions) {
			capsule.tick(dt);
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
		
		ConnectionsButton.tick(dt);
	}

	public GenoFPSCounter getFpsCounter() {
		return fpsCounter;
	}

    private void updateCircleSize() {
	for(SessionViewCapsule capsule : sessions)
	    capsule.updateGeneCirclePosition();
	for(GeneralLink link : links)
	    link.calculatePositions(geneCircle);
    }
}
