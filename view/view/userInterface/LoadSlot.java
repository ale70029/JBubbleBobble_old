package view.userInterface;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.filesManagers.FileManager;
import main.Game;

import static controller.contextControllers.GenericContext.WINDOW_WIDTH;
import static controller.filesManagers.FileManager.LOAD_SLOT_HEIGHT;
import static controller.filesManagers.FileManager.LOAD_SLOT_WIDTH;

/**
 * Buttons used for load slots.
 * They are wide and linked to a specific profile with profileIndex.
 * Also used as graphic display for profiles with no interaction.
 */
public class LoadSlot extends Button{
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Index for putting the button in the middle of the panel.
	 */
	public static final int CENTER;
	
	private static BufferedImage[] loadImages;
	private int profileIndex;

	
	static {
		CENTER = (WINDOW_WIDTH-Game.scale(LOAD_SLOT_WIDTH))/2;
		loadImages = FileManager.LOAD_BUTTON;
	}
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	/**
	 * Create a new Load Slot with given position, menu index and profile index.
	 * @param menuIndex index in buttons array where it will be put
	 * @param x width position
	 * @param y height positon
	 * @param profileIndex used to link a button with the relative profile.
	 */
	public LoadSlot(int menuIndex,int x, int y,int profileIndex) {
		super(menuIndex, x, y, LOAD_SLOT_WIDTH, LOAD_SLOT_HEIGHT);
		this.profileIndex=profileIndex;
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws load slot based on status index.
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(loadImages[statusIndex], x, y, width, height, null);
	}

////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	public int getProfileIndex() {return profileIndex;}
	
////////////////////////////////////////////////////////////////////////////////////
}
