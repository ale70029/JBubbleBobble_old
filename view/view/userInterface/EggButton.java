package view.userInterface;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.contextControllers.EasterEgg;
import controller.filesManagers.FileManager;

import model.game.Context;
/**
 * Buttons for an easter egg. Keeps count of number of click and calls a method when this number reaches a Target number.
 */
public class EggButton extends Button {
////////////////////////////////////////////////////////////////////////////////////
	//draw
	private static BufferedImage EGG_IMAGE;
	private static Font FONT;
	//control
	private final int TARGET = 50;
	private int count;
////////////////////////////////////////////////////////////////////////////////////
//NEW
	
	public EggButton(int x, int y ) {
		super(-2, scale(x), y, 50, 50);
		EGG_IMAGE = FileManager.EGG;
		FONT = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(12));
	}

////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Calls found method when count is >= target.
	 */
	public void update() {
		if(count>=TARGET) found();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws the egg and the text "Daje" when clicked.
	 */
	public void draw(Graphics g) {
		g.drawImage(EGG_IMAGE, x, y, width, height, null);
		g.setFont(FONT);
		if(pressed) g.drawString("DAJE", x, y-scale(20));
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Sets button count to 0.
	 */
	public void reset() {count = 0;}
	
//////////////////////////////////////////
	
	/**
	 * Adds 1 to the count
	 */
	public void click() {count++;}

//////////////////////////////////////////
	
	/**
	 * Moves to surprise context.
	 */
	private void found() {
		Context.current = Context.EASTER_EGG;
		EasterEgg.getInstance().setToUpdate(true);
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	
}
