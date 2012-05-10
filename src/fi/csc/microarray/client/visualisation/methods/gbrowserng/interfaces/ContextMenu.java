package fi.csc.microarray.client.visualisation.methods.gbrowserng.interfaces;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.soulaim.tech.gles.Color;

import javax.media.opengl.GL2;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fi.csc.microarray.client.visualisation.methods.gbrowserng.model.GeneCircle;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.PrimitiveRenderer;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.view.overview.OverView;
import fi.csc.microarray.client.visualisation.methods.gbrowserng.data.ViewChromosome;



/**
 * Handles the drawing and functionality of a context menu opened by right clicking.
 * @author Elias Laitala
 */

public class ContextMenu {
	private OverView overview;
	private ViewChromosome chromosome;
	private GeneCircle geneCircle;
	private int width, selHeight, border, shadow;
	private float x, y;
	private TextRenderer textRenderer;
	private ArrayList<Selection> selections;
	private int selected;
	private boolean close;

	private Color menuColor = new Color(0.7f, 0.7f, 0.7f, 0.9f);
	private Color selectColor = new Color(0.9f,0.9f,0.9f,0.95f);
	private Color borderColor = new Color(0.5f,0.6f,0.5f,0.9f);
	private Color shadowColor = new Color(0f,0f,0f,0.7f);

	/**
	 * The constructor.
	 * @param chromosome	the chromosome selected by the mouse position
	 * @param geneCircle	the GeneCircle object
	 * @param mx			mouse x position in the window as a floating point Gl coordinate
	 * @param my			mouse y position
	 * @param overview		the OverView object
	 */
	public ContextMenu(ViewChromosome chromosome, GeneCircle geneCircle, float x, float y, OverView overview) {
		this.overview = overview;
		close = false;
		this.chromosome = chromosome;
		this.x = x;
		this.y = y;
		this.geneCircle = geneCircle;

		width = 200;
		selHeight = 26;
		border = 4;
		shadow = 6;

		initializeMenu(overview);
		initTextRenderers();
	}

	private void initializeMenu(OverView overview) {
		selections = new ArrayList<Selection>();
		
		int chromosomes = overview.globals.genome.getNumChromosomes();
		int minimized=0;
		for (int i = 0; i < chromosomes; ++i) {
			ViewChromosome c = overview.globals.genome.getChromosome(i);
			if(c.isMinimized()) minimized++;
		}
		boolean oneMaximized = minimized==overview.globals.genome.getNumChromosomes()-1;

		if (oneMaximized) {
			
			if(this.chromosome.isMinimized()) {
				selections.add(new Selection("Open",1));
				selections.add(new Selection("Maximize",2));
			}
			selections.add(new Selection("Open all",3));
		}
		else {
			if(this.chromosome.isMinimized()) {
				selections.add(new Selection("Open",1));
				selections.add(new Selection("Maximize",2));
			}
			else {
				selections.add(new Selection("Close",0));
				selections.add(new Selection("Maximize",2));
			}
			if(minimized>0) selections.add(new Selection("Open all",3));
		}

		selected = 0;
	}

	/**
	 * Performs an action and closes the menu.
	 * @param selection		the number of the action to be performed
	 */
	private void action() {
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
			restoreAll();
			geneCircle.animating = true;
		} 
		close = true;
	}

	/**
	 * Maximizes the selected chromosome.
	 * @param chromosome	the chromosome to be maximized
	 */
	private void minimizeAllButOne(ViewChromosome chromosome) {
		int chromosomes = overview.globals.genome.getNumChromosomes();
		for (int i = 0; i < chromosomes; ++i) {
			ViewChromosome c = overview.globals.genome.getChromosome(i);
			if (c != chromosome) {
				c.setMinimized(true);
			} else c.setMinimized(false);
		}
	}
	
	/**
	 * Restores all minimized chromosomes.
	 */
	private void restoreAll() {
		int chromosomes = overview.globals.genome.getNumChromosomes();
		for (int i = 0; i < chromosomes; ++i) {
			ViewChromosome c = overview.globals.genome.getChromosome(i);
			c.setMinimized(false);
		}
	}

	/**
	 * Handles mouse movement and clicking within the menu.
	 * @param event		the mouse event
	 * @param mx		mouse x
	 * @param my		mouse y
	 * @return 
	 */
	public boolean handle(MouseEvent event, float mx, float my) {
		if(inComponent(mx,my)) {
			for(int i = 0; i <selections.size(); i++) {
				if(my > y - convertH(selHeight) * 0.5f - convertH(selHeight)*i && my < y + convertH(selHeight) * 0.5f - convertH(selHeight)*i) {
					selected = i;
					break;
				}
			}
			if (MouseEvent.MOUSE_CLICKED == event.getID()) {
				action();
			}
		}
		return true;
	}

	/**
	 * Handles keyboard use of the menu. The selected action can be changed with up and down keys and selected with enter.
	 * @param event		key event
	 * @return 
	 */
	public boolean handle(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_UP && event.getID() == KeyEvent.KEY_PRESSED) {
			if(selected == 0) selected = selections.size()-1;
			else selected--;	
		} else if(event.getKeyCode()==KeyEvent.VK_DOWN && event.getID() == KeyEvent.KEY_PRESSED) {
			if(selected == selections.size()-1) selected = 0;
			else selected++;
		} else if(event.getKeyCode()==KeyEvent.VK_ENTER && event.getID() == KeyEvent.KEY_PRESSED) {
			action();
			close=true;
		}
		return true;
	}

	/**
	 * Returns a boolean value about is the menu to be closed.
	 * @return is the menu to be closed
	 */
	public boolean close() {
		return close;
	}

	/**
	 * Draws the menu on the screen.
	 * @param gl gl interface
	 */
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
		for(int i = 0; i<selections.size(); i++) {
			textRenderer.setColor(0f, 0f, 0f, 1f);
			textRenderer.draw(selections.get(i).name, convertX(x)+5, convertY(y)-i*selHeight-5);
			textRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
			textRenderer.draw(selections.get(i).shortcut, convertX(x)+width-20, convertY(y)-i*selHeight-5);
		}
		textRenderer.endRendering();
	}

	/**
	 * Checks if the mouse cursor is inside the menu.
	 * @param mx	mouse x position
	 * @param my	mouse y position
	 * @return 
	 */
	public boolean inComponent(float mx, float my) {
		if(mx < x || mx > x + convertW(width)) return false;
		if(my < y - convertH(selHeight) * 0.5f - convertH(selHeight)*(selections.size()-1) || my > y + convertH(selHeight) * 0.5f) return false;
		return true;
	}

	/**
	 * Initializes a text renderer for writing text in the menu.
	 */
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

	/**
	 * Converts a width value from integer to float.
	 * @param c	value to be converted
	 * @return 
	 */
	private float convertW(int c) {
		int hw = overview.globals.width/2;
		return ((float)c)/hw;
	}

	/**
	 * Converts a height value from integer to float.
	 * @param c value to be converted
	 * @return 
	 */
	private float convertH(int c) {
		int hh = overview.globals.height/2;
		return ((float)c)/hh;
	}

	/**
	 * Converts a screen x coordinate value to integer.
	 * @param c
	 * @return 
	 */
	private int convertX(float c) {
		int hw = overview.globals.width/2;
		return (int)(hw+(hw*c));
	}

	/**
	 * Converts a screen x coordinate value to integer.
	 * @param c
	 * @return 
	 */
	private int convertY(float c) {
		int hh = overview.globals.height/2;
		return (int)(hh+(hh*c));
	}

}
/**
 * A data structure for menu options.
 * @author Elias Laitala
 */
class Selection {
	String name;
	String shortcut;
	int action;
	
	/**
	 * A constructor that creates an action without a shortcut key.
	 * @param name		name of the action
	 * @param action	number of the action
	 */
	public Selection(String name, int action) {
		this.name = name;
		this.shortcut = "";
		this.action = action;
	}
	
	/**
	 * A constructor that creates an action with a shortcut key.
	 * @param name		name of the action
	 * @param shortcut	shortcut key
	 * @param action	number of the action
	 */
	public Selection(String name, String shortcut, int action) {
		this.name = name;
		this.shortcut = shortcut;
		this.action = action;
	}
}
