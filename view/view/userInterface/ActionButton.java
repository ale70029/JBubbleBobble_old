package view.userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.contextControllers.Playing;
import controller.filesManagers.AudioManager;
import controller.filesManagers.FileManager;


import static controller.filesManagers.FileManager.MENU_BUTTON_HEIGHT;
import static controller.filesManagers.FileManager.MENU_BUTTON_WIDTH;

/**
 * A kind of button that is meant for action, like any kind of action that has an ON/OFF state (like music, fun mode..)
 * Button picture is green when ON, red when OFF, blue when SELECTED.
 */
public class ActionButton extends Button{
////////////////////////////////////////////////////////////////////////////////////
	public static enum Action{ MUSIC,SOUND,FUN,NEXT,PREVIOUS,DIFFICULTY;}
	/**
	 * Type index
	 */
//	public static final int MUSIC = 0,SOUND = 1,FUN = 2, NEXT = 3, PREVIOUS = 4,DIFFICULTY= 5;
	/**
	 * Status index for color
	 */
	public static final int OFF = 0, ON = 2;   //Overrides Button UNSELECTED with OFF and PRESSED with ON
	
	private static final BufferedImage[] SOUND_IMAGES;

	private static final Font FONT;
	
	private Action type;
	private boolean chosen,muted;

	static {
		SOUND_IMAGES = FileManager.SOUND_BUTTON;
		FONT = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(12));
	}
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	/**
	 * Creates a new ActionButton
	 * @param menuIndex index of button in buttons array where will be putted
	 * @param x width position
	 * @param y height position
	 * @param type action that will be performed
	 */
	public ActionButton(int menuIndex,int x, int y, Action type) {
		super(menuIndex,x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT-20);
		this.type = type;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates button statusIndex needed for graphic visualitation.
	 */
	@Override
	public void update() {
		if(type==Action.MUSIC || type==Action.SOUND) {
			if (!chosen) statusIndex = muted? OFF:ON;
			if (chosen||selected) statusIndex = SELECTED;
		}
		else if(type==Action.FUN) {
			if(!chosen) statusIndex = AudioManager.getInstance().isFunOn() ? ON:OFF;
			if (chosen||selected) statusIndex = SELECTED;
		}
		else {
			if(AudioManager.getInstance().isFunOn()) {
				if (chosen||selected) statusIndex = SELECTED;
				else statusIndex = ON;
			}
			else statusIndex=OFF;
		}
		
		if(type == Action.DIFFICULTY) {
			switch(Playing.getInstance().getDifficulty()) {
				case Playing.EASY : statusIndex = ON; break;
				case Playing.NORMAL : statusIndex = ON; break;
				case Playing.HARD : statusIndex = OFF; break;
				default:break;
			}
			if(selected) statusIndex = SELECTED;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	@Override
	/**
	 * Draws button based on type and draws button text
	 */
	public void draw(Graphics g) {
		
		
		g.drawImage(SOUND_IMAGES[statusIndex], x, y, width, height, null);
		g.setFont(FONT);
		g.setColor(Color.WHITE);
		String text = "";
	
		if(type==Action.MUSIC) {
			if(muted) text = "Music OFF";
			else text ="Music ON";
		}
		
		else if(type==Action.SOUND) {
			if(muted) text = "Sound OFF";
			else text ="Sound ON";
		}
		
		else if(type==Action.FUN) {
			if(AudioManager.getInstance().isFunOn()) text = "FUN ON";
			else text ="FUN OFF";
		}
		
		else if(type==Action.NEXT) text = "NEXT";
		
		else if(type==Action.PREVIOUS) text = "PREV";
		
		else if(type==Action.DIFFICULTY) {
			text = switch(Playing.getInstance().getDifficulty()) {
				case Playing.EASY -> "easy";
				case Playing.NORMAL -> "normal";
				case Playing.HARD -> "hard";
				default -> "";
			};
		}
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int textW = metrics.stringWidth(text);
		int textH = metrics.getHeight();
		int textX = x + (width - textW) / 2;
        int textY = y + (height - textH) ;
		g.drawString(text, textX, textY);
	}

////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Switches audio effect on and off (music or sound depending on button type)
	 */
	public void switchSound() {
		muted = !muted;
		if(type==Action.MUSIC) AudioManager.getInstance().switchMusic();
		else if (type==Action.SOUND) AudioManager.getInstance().switchSound();
	}
	
//////////////////////////////////////////
	
	/**
	 * Switches fun mode on and off
	 */
	public void switchMode() {
		if(type==Action.FUN) AudioManager.getInstance().switchMode();
	}
	
//////////////////////////////////////////
	
	/**
	 * Plays next song in list
	 */
	public void nextSong() {
		if(type==Action.NEXT) AudioManager.getInstance().nextSong();
	}
	
//////////////////////////////////////////
	
	/**
	 * Plays previous song in list
	 */
	public void previouSong() {
		if(type==Action.PREVIOUS) AudioManager.getInstance().previousSong();
	}
	
//////////////////////////////////////////
	
	/**
	 * Changes game difficulty
	 */
	public void changeDifficulty(){
		if(type==Action.DIFFICULTY) Playing.getInstance().setDifficulty();
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
