package controller.contextControllers;



import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.filesManagers.FileManager;
import main.Game;
import model.game.Level;
import view.userInterface.Button;


/**
 * Class for a generic Context, includes background image, fonts, avatars pictures and window dimension ( width and height)
 */
public class GenericContext {
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Window dimension.
	 */
	public static final int WINDOW_WIDTH,WINDOW_HEIGHT;
	
	protected static BufferedImage background;
	protected static final BufferedImage[] AVATARS;
	public static final Font TITLE_80,TITLE_65,FONT_9,FONT_12,FONT_14,FONT_15,FONT_16,FONT_24,FONT_30;
	
	protected boolean toUpdate;
	protected Button[] buttons;
	protected int index;  //used to navigate in buttons
	public static Dimension size;
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	public Dimension getSize() {
		return size;
	}

	public static void setSize(Dimension siz) {
		size = siz;
	}

	static {
		WINDOW_WIDTH = scale(Level.TILE_SIZE * Level.LEVEL_COLS);
		WINDOW_HEIGHT = scale(Level.TILE_SIZE * Level.LEVEL_ROWS);
		//pictures
		background =FileManager.DEFAULT_BACKGROUND;
		AVATARS = FileManager.AVATARS;
		//fonts
		TITLE_80 = new Font("Arcade Interlaced", Font.TRUETYPE_FONT, scale(80));
		TITLE_65 = new Font("Arcade Interlaced", Font.TRUETYPE_FONT, scale(65));
		FONT_9 = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(9));
		FONT_12 = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(12));
		FONT_14 = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(14));
		FONT_16 = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(16)); 
		FONT_15 = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(15));
		FONT_24 = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(24));
		FONT_30 = new Font("Arcade Normal",Font.TRUETYPE_FONT, scale(30));
	}
		
//////////////////////////////////////////
	
	/**
	 * Create a new context controller with toUpdate setted on true.
	 */
	public GenericContext() {
		toUpdate=true;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws default background
	 * @param g Graphics engine
	 */
	public void drawBackground(Graphics g) {
		g.drawImage(background, 0, 0,WINDOW_WIDTH,WINDOW_HEIGHT, null);
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Sets buttons[index] button as selected and the other as not.
	 * @param buttons list of buttons
	 * @param index index of button tu select
	 */
	public void selectButton(Button[] buttons, int index) {
		for(Button b : buttons) {
			if(b.getMenuIndex()==index) b.setSelected(true);
			else b.setSelected(false);
		}
	}
	
	
	protected static int scale(int value) {
		return Game.scale(value);
	}
////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	
	public void setToUpdate(boolean toUpdate) {this.toUpdate=toUpdate;}
	
//////////////////////////////////////////
	
	public static BufferedImage getBackground(int bkg) {
		switch(bkg) {
			case 1 : return FileManager.BACKGROUND_1;
			case 2 : return FileManager.BACKGROUND_2;
			case 3 : return FileManager.BACKGROUND_3;
			default : return null;
		}
	}
	
//////////////////////////////////////////
	
	public static void setBackground(int bkg) {
		switch(bkg) {
			case 1 : background = FileManager.BACKGROUND_1; break;
			case 2 : background = FileManager.BACKGROUND_2; break;
			case 3 : background = FileManager.BACKGROUND_3; break;
			default : break;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
