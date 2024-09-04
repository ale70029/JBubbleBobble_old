/**
 * Package with classes that controls keyboard and mouse inputs.
 */
package controller.inputManagers;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.contextControllers.*;
import model.game.Context;

/**
 * Custom KeyListener, propagates KeyEvents to Context Controllers.
 */
public class KeyboardInputs implements KeyListener {
////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch (Context.current) {
			case MENU:       Menu.getInstance().keyReleased(e);       break;
			case DELETE:     Delete.getInstance().keyReleased(e);     break;
			case GAME_OVER:  GameOver.getInstance().keyReleased(e);   break;
			case STATISTICS: Statistics.getInstance().keyReleased(e); break;
			case PLAYING:    Playing.getInstance().keyReleased(e);    break;
			case NEW_GAME:   NewGame.getInstance().keyReleased(e);    break;
			case CREDITS:    Credits.getInstance().keyReleased(e);    break;
			case LOAD:       Load.getInstance().keyReleased(e);       break;
			case NEXT_LEVEL: NextLevel.getInstance().keyReleased(e);  break;
			case OPTIONS:    Options.getInstance().keyReleased(e);    break;
			case PAUSE:      Pause.getInstance().keyReleased(e);      break;
			case TUTORIAL:   Tutorial.getInstance().keyReleased(e);   break;
			case WIN:        Win.getInstance().keyReleased(e);        break;
			case EASTER_EGG: EasterEgg.getInstance().keyReleased(e);  break;
			default: break;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (Context.current) {
			case PLAYING :   Playing.getInstance().keyPressed(e);     break;
			default: break;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void keyTyped(KeyEvent e) {}  //NOT USED
	
////////////////////////////////////////////////////////////////////////////////////
}