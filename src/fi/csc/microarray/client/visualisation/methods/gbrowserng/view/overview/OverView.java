package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.soulaim.tech.gles.SoulGL2;
import com.soulaim.tech.gles.renderer.TextRenderer;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ReferenceSequence;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GenoFPSCounter;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.sessionview.SessionView;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OverView extends GenosideComponent {

    GeneCircleGFX geneCircleGFX = new GeneCircleGFX();
    GenoFPSCounter fpsCounter = new GenoFPSCounter();

    private int mouseState = 0;
    private Vector2 mousePosition = new Vector2();

    ReferenceSequence referenceSequence = null;
    ConcurrentLinkedQueue<SessionViewCapsule> sessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
    SessionViewCapsule activeSession = null;

    public OverView() {
        super(null);
    }

    @Override
    public void childComponentCall(String who, String what) {
        if(who.equals("SESSION")) {
            if(what.equals("SHRINK")) {
                disableActiveSession();
            }
            if(what.equals("KILL")) {
                killActiveSession();
                disableActiveSession();
            }
        }
    }

    private void killActiveSession() {
        if(activeSession == null)
            return;
        if(activeSession.isActive()) {
            activeSession.die();
            activeSession.getSession().setPosition(-1.4f, 0.0f);
        }
    }

    private void disableActiveSession() {
        if(activeSession == null)
            return;

        for(SessionViewCapsule otherCapsule : sessions) {
            if(otherCapsule.getId() != activeSession.getId()) {
                otherCapsule.show();
            }
        }

        activeSession.deactivate();
        activeSession = null;
    }

    public boolean handle(MouseEvent event, float x, float y) {

        // if there is an active session, let it handle input.
        if(activeSession != null) {
            return activeSession.getSession().handle(event, x, y);
        }

        // allow capsules to update their states
        for(SessionViewCapsule capsule : sessions)
            capsule.handle(event, x, y);

        // then see if they actually want the event
        if(event.getButton() == 1 && mouseState == 0) {
            mouseState = 1;

            for(SessionViewCapsule capsule : sessions) {
                if(capsule.isDying()) {
                    continue;
                }
                if(capsule.handle(event, x, y)) {
                    capsule.activate();
                    activeSession = capsule;

                    for(SessionViewCapsule otherCapsule : sessions) {
                        if(otherCapsule.getId() != activeSession.getId()) {
                            otherCapsule.hide();
                        }
                    }

                    return true;
                }
            }

            // respond to mouse click
            float percentage = (float) (Math.atan2(y, x) / Math.PI);

            System.out.println("Adding capsule");
            SessionViewCapsule capsule = new SessionViewCapsule(new SessionView(new Session(), this));
            capsule.getSession().setDimensions(0.4f, 0.2f);
            capsule.getSession().setPosition(x, y);
            sessions.add(capsule);
        }
        else if(event.getButton() != 1) {
            mouseState = 0;
        }

        mousePosition.x = x;
        mousePosition.y = y;
        return false;
    }

    public boolean handle(KeyEvent event) {
        if (activeSession != null) {
            return activeSession.getSession().handle(event);
        }
        return false;
    }

    public void draw(SoulGL2 gl) {

        Vector2 mypos = this.getPosition();
        Matrix4 geneCircleModelMatrix = new Matrix4();
        geneCircleModelMatrix.makeTranslationMatrix(mypos.x, mypos.y, 0);
        geneCircleModelMatrix.scale(0.5f, 0.5f, 0.5f);
        geneCircleGFX.draw(gl, geneCircleModelMatrix, this.mousePosition);

        for(SessionViewCapsule capsule : sessions) {
            capsule.draw(gl);
        }

        TextRenderer.getInstance().drawText(gl, "FPS: " + fpsCounter.getFps(), 0, 0.92f, 1.0f);
    }

    @Override
    public void userTick(float dt) {
        geneCircleGFX.tick(dt);
        fpsCounter.tick(dt);


        SessionViewCapsule killCapsule = null;
        for(SessionViewCapsule capsule : sessions) {
            if(!capsule.isAlive())
                killCapsule = capsule;
            capsule.tick(dt);
        }

        if(killCapsule != null)
            sessions.remove(killCapsule);
    }

}

