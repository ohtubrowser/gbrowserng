package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.soulaim.tech.gles.SoulGL2;
import com.soulaim.tech.math.Matrix4;
import com.soulaim.tech.math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ReferenceSequence;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.sessionview.SessionView;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OverView extends GenosideComponent {

    GeneCircleGFX geneCircleGFX = new GeneCircleGFX();

    private int mouseState = 0;
    private Vector2 mousePosition = new Vector2();
    private float totalTime = 0;

    private float percentage = 0;

    ReferenceSequence referenceSequence = null;
    ConcurrentLinkedQueue<SessionViewCapsule> sessions = new ConcurrentLinkedQueue<SessionViewCapsule>();
    SessionView activeSession = null;

    public OverView() {
        super(null);
    }

    public boolean handle(MouseEvent event, float x, float y) {

        // if there is an active session, let it handle input.
        if(activeSession != null) {
            return activeSession.handle(event, x, y);
        }

        // if no session is active, handle input myself.
        if(event.getButton() == 3 && mouseState == 0) {
            mouseState = 3;

            for(SessionViewCapsule capsule : sessions) {
                if(capsule.handle(event, x, y)) {
                    capsule.die();
                    return true;
                }
            }
        }
        if(event.getButton() == 1 && mouseState == 0) {
            mouseState = 1;

            for(SessionViewCapsule capsule : sessions) {
                if(capsule.handle(event, x, y)) {
                    capsule.activate();
                    activeSession = capsule.getSession();
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

        if (event.getKeyChar() == 'b') {
            for(SessionViewCapsule capsule : sessions) {
                if(capsule.isActive()) {
                    capsule.deactivate();
                    activeSession = null;
                    return true;
                }
            }
        }

        if (activeSession != null) {
            return activeSession.handle(event);
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
    }

    @Override
    protected void userTick(float dt) {
        geneCircleGFX.tick(dt);
        for(SessionViewCapsule capsule : sessions) {
            capsule.userTick(dt);
        }
    }

}
