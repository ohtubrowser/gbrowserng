package fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import gles.Color;
import gles.SoulGL2;
import gles.renderer.PrimitiveRenderer;
import math.Vector2;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Session;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces.GenosideComponent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.common.GenoVisualBorder;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.trackview.SessionView;
import javax.media.opengl.GL2;
import soulaim.DesktopGL2;

public class SessionViewRecentCapsule extends GenosideComponent {

	private int	id;
	private Session	session;
	private SessionView	sessionView;
	private Vector2	oldPosition;
	private Vector2	oldGeneCirclePosition;
	private GenoVisualBorder border;
	private float posy;
	private boolean isVisible;

	public SessionViewRecentCapsule(int id, Vector2 oldposition, Vector2 oldgenecirclepos, SessionView sessionview, Session session) {
		super(null);
		this.id=id;
		this.session=session;
		this.sessionView = sessionview;
		this.oldPosition=oldposition;
		this.oldGeneCirclePosition=oldgenecirclepos;
		this.border=new GenoVisualBorder(this.sessionView);
		this.posy=1.25f;
		this.isVisible=false;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public Vector2 getOldPosition()
	{
		return this.oldPosition;
	}
	public Vector2 getOldGeneCirclePosition()
	{
		return this.oldGeneCirclePosition;
	}
	public SessionView getSessionView()
	{
		return this.sessionView;
	}
	public Session getSession()
	{
		return this.session;
	}
	@Override
	public void childComponentCall(String who, String what) {}

	@Override
	public boolean handle(MouseEvent event, float screen_x, float screen_y) {
		Vector2 dimensions = sessionView.getDimensions();
		Vector2 position = sessionView.getPosition();

		if(screen_x > position.x - dimensions.x * 0.5f && screen_x < position.x + dimensions.x * 0.5f)
		{
			if(screen_y > position.y - dimensions.y * 0.5f && screen_y < position.y + dimensions.y * 0.5f) {
					this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 1);
				return true;
			}
		}
		this.getAnimatedValues().setAnimatedValue("MOUSEHOVER", 0);
		return false;
	}

	@Override
	public boolean handle(KeyEvent event) {
		return false;
	}

	public void hide()
	{
		if(isVisible) this.posy+=0.30f;
		isVisible=false;
		System.out.println("hide");
	}
	public void show()
	{
		if(!isVisible) this.posy-=0.30f;
		isVisible=true;
		System.out.println("show");
	}

	@Override
	public void draw(GL2 gl) {
		float r=this.getAnimatedValues().getAnimatedValue("MOUSEHOVER");
		Color c=new Color(r,r,r);
		gl.glEnable(SoulGL2.GL_BLEND);
		SoulGL2 soulgl = new DesktopGL2(gl);
		PrimitiveRenderer.drawRectangle(this.sessionView.getPosition().x, this.sessionView.getPosition().y, 0.05f, 0.05f/GlobalVariables.aspectRatio, soulgl, c);
		border.draw(gl);
		gl.glDisable(SoulGL2.GL_BLEND);
	}

	@Override
	public void userTick(float dt) {
		Vector2 dimensions=this.sessionView.getDimensions();
		this.sessionView.setPosition(-1.0f+(dimensions.x * 0.5f)+(this.id*0.125f), posy);
		sessionView.tick(dt);
	}

}
