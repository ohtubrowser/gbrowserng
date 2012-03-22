/*
 * Context menu
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import java.util.ArrayList;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import math.Vector2;
import javax.media.opengl.GL2;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import com.jogamp.opengl.util.awt.TextRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.AbstractGenome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import gles.Color;
import gles.SoulGL2;
import gles.renderer.PrimitiveRenderer;
import soulaim.DesktopGL2;

public class ContextMenu implements InteractiveComponent, VisualComponent {
	GenoWindow window;
	ViewChromosome chromosome;
	GeneCircle geneCircle;
	float x, y, sizex, sizey, selectionHeight;
	Vector2 position, dimensions;
	TextRenderer textRenderer;
	ArrayList<Selection> selections;
	int selected;
	boolean close;

	private Color menuColor = new Color(0.7f, 0.7f, 0.7f, 255);
	private Color selectColor = new Color(0.9f,0.9f,0.9f,255);

	
	public ContextMenu(ViewChromosome chromosome, GeneCircle geneCircle, float mx, float my, GenoWindow window) {
		this.window = window;
		close = false;
		this.chromosome = chromosome;
		x = mx;
		y = my;
		this.geneCircle = geneCircle;
		sizex = 0.2f;
		selectionHeight = 0.06f; // todo: säädä sopiva koko
		selections = new ArrayList<Selection>();
		
		if(!chromosome.isMinimized()) selections.add(new Selection("Minimize",0));
		else selections.add(new Selection("Restore",1));
		selections.add(new Selection("Maximize",2));
		if(window.isFullscreen()) selections.add(new Selection("Windowed mode",3));
		else selections.add(new Selection("Fullscreen",3));
		
		selected = 0;
		sizey = selectionHeight * selections.size();
		initTextRenderers();
        }
	
	private void action(int selection) {
		if(selections.get(selected).action==0) {
			chromosome.setMinimized(true);
			geneCircle.animating = true;
		} else if(selections.get(selected).action==1) {
			chromosome.setMinimized(false);
			geneCircle.animating = true;
		} else if(selections.get(selected).action==2) {
			minimizeAllButOne(chromosome);
			geneCircle.animating = true;
		} else if(selections.get(selected).action==3) {
			window.toggleFullscreen();
		}
		close = true;
	}
	
	public ViewChromosome getChromosome() {
		return chromosome;
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
	
	@Override
	public boolean handle(MouseEvent event, float mx, float my) {
		if(inComponent(mx,my)) {
			for(int i = 0; i<selections.size(); i++) {
				if(my > y - selectionHeight * 0.5f - selectionHeight*i && my < y + selectionHeight * 0.5f - selectionHeight*i) {
					selected = i;
					break;
				}
			}
			if (MouseEvent.EVENT_MOUSE_CLICKED == event.getEventType()) {
				action(selections.get(selected).action);
			}
		}
		return true;
	}
	
	@Override
	public boolean handle(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_UP) {
			if(selected == 0) selected = selections.size()-1;
			else selected--;
		} else if(event.getKeyCode()==KeyEvent.VK_DOWN) {
			if(selected == selections.size()-1) selected = 0;
			else selected++;
		} else if(event.getKeyCode()==KeyEvent.VK_ENTER) {
			action(selected);
			close=true;
		}
		return true;
	}
	
	public boolean close() {
		return close;
	}
	
	@Override
	public void draw(GL2 gl) {
		gl.glEnable(SoulGL2.GL_BLEND);

		SoulGL2 soulgl = new DesktopGL2(gl);
                
		for(int i = 0; i<selections.size(); i++) {
			Color color = menuColor;
			if(i==selected) color = selectColor;
			PrimitiveRenderer.drawRectangle(x, y - i*selectionHeight, sizex * 0.5f, selectionHeight * 0.5f / GlobalVariables.aspectRatio, soulgl, color);
                }
                gl.glDisable(SoulGL2.GL_BLEND);

		int width = GlobalVariables.width, height = GlobalVariables.height;
		textRenderer.beginRendering(width, height);
		int hw = width/2, hh = height/2;
		for(int i = 0; i<selections.size(); i++) {
			textRenderer.setColor(0f, 0f, 0f, 1f);
			textRenderer.draw(selections.get(i).name, (int)(hw+(hw*(x-sizex/2))), (int)(hh+(hh*(y-selectionHeight/2+0.01f-i*selectionHeight))));
		}
		textRenderer.endRendering();
	}
	
	@Override
	public void tick(float dt) {
	    
	}
	
	public boolean inComponent(float mx, float my) {
		if(mx < x - sizex * 0.5f || mx > x + sizex * 0.5f) return false;
		if(my < y - selectionHeight * 0.5f - selectionHeight*(selections.size()-1) || my > y + selectionHeight * 0.5f) return false;
		return true;
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
	}

}

class Selection {
	String name;
	int action;
	public Selection(String name, int action) {
		this.name = name;
		this.action = action;
	}
}
