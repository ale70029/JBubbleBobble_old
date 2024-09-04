package view.userInterface;

import java.awt.Graphics;

import model.game.Context;
/**
 * Class for hidden buttons that are not meant to be updated or drawed.
 * They are just used to apply Context when a relative MenuButton is not present.
 */
public class HiddenButton extends MenuButton{
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	public HiddenButton(Context state) {
		super(-1, 0, 0, "", state);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//ALERTS
	
	/**
	 * Prints a warning if called.
	 */
	@Override
	public void update() {System.out.println("Hidden button can't be updated");}
	
//////////////////////////////////////////
	
	/**
	 * Prints a warning if called.
	 */
	@Override 
	public void draw(Graphics g) {System.out.println("Hidden button can't be drawed");}
	
////////////////////////////////////////////////////////////////////////////////////
}
