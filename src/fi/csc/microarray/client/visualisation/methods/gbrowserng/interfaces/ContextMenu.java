/*
 * Context menu
 */
package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;
import com.soulaim.tech.math.Vector2;
import javax.media.opengl.GL2;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import com.jogamp.opengl.util.awt.TextRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.GlobalVariables;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.Genome;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.GenoWindow;
import com.soulaim.tech.gles.Color;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.PrimitiveRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ContextMenu {
	OverView overview;
	GenoWindow window;
	ViewChromosome chromosome;
	GeneCircle geneCircle;
	int width, selHeight, border, shadow;
	float x, y;
	Vector2 position, dimensions;
	TextRenderer textRenderer;
	ArrayList<Selection> selections;
	int selected;
	boolean close;

	private Color menuColor = new Color(0.7f, 0.7f, 0.7f, 0.9f);
	private Color selectColor = new Color(0.9f,0.9f,0.9f,0.95f);
	private Color borderColor = new Color(0.5f,0.6f,0.5f,0.9f);
	private Color shadowColor = new Color(0f,0f,0f,0.7f);

	public ContextMenu(ViewChromosome chromosome, GeneCircle geneCircle, float mx, float my, OverView overview) {
		this.overview = overview;
		this.window = overview.window;
		close = false;
		this.chromosome = chromosome;
		x = mx;
		y = my;
		this.geneCircle = geneCircle;

		width = 200;
		selHeight = 26;
		border = 4;
		shadow = 6;
		selections = new ArrayList<Selection>();

		if(!chromosome.isMinimized()) selections.add(new Selection("Minimize",0));
		else selections.add(new Selection("Restore",1));
		selections.add(new Selection("Maximize",2));
		selections.add(new Selection("Restore all",3));
//		if(window.isFullscreen()) selections.add(new Selection("Windowed mode","F",4));
//		else selections.add(new Selection("Fullscreen","F",4));
		
		selected = 0;
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
			restoreAll(chromosome);
			geneCircle.animating = true;
		} 
//		else if(selections.get(selected).action==4) {
//			window.toggleFullscreen();
//			overview.updateCircleSize();
//		}
		close = true;
	}

	public ViewChromosome getChromosome() {
		return chromosome;
	}

	private void minimizeAllButOne(ViewChromosome chromosome) {
		int chromosomes = overview.globals.genome.getNumChromosomes();
		for (int i = 0; i < chromosomes; ++i) {
			ViewChromosome c = overview.globals.genome.getChromosome(i);
			if (c != chromosome) {
				c.setMinimized(true);
			} else c.setMinimized(false);
		}
	}
	
	private void restoreAll(ViewChromosome chromosome) {
		int chromosomes = overview.globals.genome.getNumChromosomes();
		for (int i = 0; i < chromosomes; ++i) {
			ViewChromosome c = overview.globals.genome.getChromosome(i);
			c.setMinimized(false);
		}
	}

	public boolean handle(MouseEvent event, float mx, float my) {
		if(inComponent(mx,my)) {
			for(int i = 0; i<selections.size(); i++) {
				if(my > y - convertH(selHeight) * 0.5f - convertH(selHeight)*i && my < y + convertH(selHeight) * 0.5f - convertH(selHeight)*i) {
					selected = i;
					break;
				}
			}
			if (MouseEvent.MOUSE_CLICKED == event.getID()) {
				action(selections.get(selected).action);
			}
		}
		return true;
	}

	public boolean handle(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_UP && event.getID() == KeyEvent.KEY_PRESSED) {
			if(selected == 0) selected = selections.size()-1;
			else selected--;	
		} else if(event.getKeyCode()==KeyEvent.VK_DOWN && event.getID() == KeyEvent.KEY_PRESSED) {
			if(selected == selections.size()-1) selected = 0;
			else selected++;
		} else if(event.getKeyCode()==KeyEvent.VK_ENTER && event.getID() == KeyEvent.KEY_PRESSED) {
			action(selected);
			close=true;
		}
		return true;
	}

	public boolean close() {
		return close;
	}

	public void draw(GL2 gl) {
		gl.glEnable(GL2.GL_BLEND);

		if(shadow>0) {
			PrimitiveRenderer.drawRectangle(overview.globals, x + convertW(width + shadow) * .5f, y - convertH(selHeight * (selections.size() - 1) + shadow) * .5f, convertW(width + border) * .5f, convertH((selHeight * selections.size()) + border) * .5f / overview.globals.aspectRatio, gl, shadowColor);
		}

		if(border>0) {
			PrimitiveRenderer.drawRectangle(overview.globals, x+convertW(width)*.5f, y - convertH(selHeight*(selections.size()-1))*.5f, convertW(width+border)*.5f, convertH((selHeight*selections.size())+border)*.5f / overview.globals.aspectRatio, gl, borderColor);
		}

		for(int i = 0; i<selections.size(); i++) {
			Color color = menuColor;
			if(i==selected) color = selectColor;
			PrimitiveRenderer.drawRectangle(overview.globals, x+convertW(width)*.5f, y - i*convertH(selHeight), convertW(width)*.5f, convertH(selHeight)*.5f / overview.globals.aspectRatio, gl, color);
		}
		gl.glDisable(GL2.GL_BLEND);

		int swidth = overview.globals.width, sheight = overview.globals.height;
		textRenderer.beginRendering(swidth, sheight);
		int hw = swidth/2, hh = sheight/2;
		for(int i = 0; i<selections.size(); i++) {
			textRenderer.setColor(0f, 0f, 0f, 1f);
			textRenderer.draw(selections.get(i).name, convertX(x)+5, convertY(y)-i*selHeight-5);
			textRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
			textRenderer.draw(selections.get(i).shortcut, convertX(x)+width-20, convertY(y)-i*selHeight-5);
		}
		textRenderer.endRendering();
	}

	public boolean inComponent(float mx, float my) {
		if(mx < x || mx > x + convertW(width)) return false;
		if(my < y - convertH(selHeight) * 0.5f - convertH(selHeight)*(selections.size()-1) || my > y + convertH(selHeight) * 0.5f) return false;
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

	private float convertW(int c) {
		int hw = overview.globals.width/2;
		return ((float)c)/hw;
	}

	private float convertH(int c) {
		int hh = overview.globals.height/2;
		return ((float)c)/hh;
	}

	private int convertW(float c) {
		int hw = overview.globals.width/2;
		return (int)(hw*c);
	}

	private int convertH(float c) {
		int hh = overview.globals.height/2;
		return (int)(hh*c);
	}

	private int convertX(float c) {
		int hw = overview.globals.width/2;
		return (int)(hw+(hw*c));
	}

	private int convertY(float c) {
		int hh = overview.globals.height/2;
		return (int)(hh+(hh*c));
	}

	private float convertX(int c) {
		int hw = overview.globals.width/2;
		return ((float)c)/hw+1f;
	}

	private float convertY(int c) {
		int hh = overview.globals.height/2;
		return ((float)c)/hh+1f;
	}
}

class Selection {
	String name;
	String shortcut;
	int action;
	public Selection(String name, int action) {
		this.name = name;
		this.shortcut = "";
		this.action = action;
	}
	public Selection(String name, String shortcut, int action) {
		this.name = name;
		this.shortcut = shortcut;
		this.action = action;
	}
}
