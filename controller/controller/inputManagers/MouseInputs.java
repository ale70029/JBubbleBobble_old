package controller.inputManagers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import controller.contextControllers.*;
import model.game.Context;

/**
 * Custom mouse listener, propagates MouseEvents to Context controllers.
 */
public class MouseInputs implements MouseListener, MouseMotionListener {
////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (Context.current) {
			case MENU:       Menu.getInstance().mouseMoved(e);       break;
			case LOAD:       Load.getInstance().mouseMoved(e);       break;
			case DELETE:     Delete.getInstance().mouseMoved(e);     break;
			case NEW_GAME:   NewGame.getInstance().mouseMoved(e);    break;
			case GAME_OVER:  GameOver.getInstance().mouseMoved(e);   break;
			case STATISTICS: Statistics.getInstance().mouseMoved(e); break;
			case NEXT_LEVEL: NextLevel.getInstance().mouseMoved(e);  break;
			case PAUSE:      Pause.getInstance().mouseMoved(e);      break;
			case TUTORIAL:   Tutorial.getInstance().mouseMoved(e);   break;
			case WIN:        Win.getInstance().mouseMoved(e);        break;
			case CREDITS:    Credits.getInstance().mouseMoved(e);    break;
			case OPTIONS:    Options.getInstance().mouseMoved(e);    break;
			case EASTER_EGG: EasterEgg.getInstance().mouseMoved(e);  break;
			case PLAYING:
			case QUIT:
			default:break;
		}
	}

////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void mousePressed(MouseEvent e) {
		switch (Context.current) {
			case MENU:       Menu.getInstance().mousePressed(e);       break;
			case LOAD:       Load.getInstance().mousePressed(e);       break;
			case DELETE:     Delete.getInstance().mousePressed(e);     break;
			case NEW_GAME:   NewGame.getInstance().mousePressed(e);    break;
			case GAME_OVER:  GameOver.getInstance().mousePressed(e);   break;
			case STATISTICS: Statistics.getInstance().mousePressed(e); break;
			case NEXT_LEVEL: NextLevel.getInstance().mousePressed(e);  break;
			case PAUSE:      Pause.getInstance().mousePressed(e);      break;
			case TUTORIAL:   Tutorial.getInstance().mousePressed(e);   break;
			case WIN:        Win.getInstance().mousePressed(e);        break;
			case CREDITS:    Credits.getInstance().mousePressed(e);    break;
			case OPTIONS:    Options.getInstance().mousePressed(e);    break;
			case EASTER_EGG: EasterEgg.getInstance().mousePressed(e);  break;
			case PLAYING:
			case QUIT:
			default:break;
		}
	}

////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void mouseReleased(MouseEvent e) {
		switch (Context.current) {
			case MENU:       Menu.getInstance().mouseReleased(e);       break;
			case LOAD:       Load.getInstance().mouseReleased(e);       break;
			case DELETE:     Delete.getInstance().mouseReleased(e);     break;
			case NEW_GAME:   NewGame.getInstance().mouseReleased(e);    break;
			case GAME_OVER:  GameOver.getInstance().mouseReleased(e);   break;
			case STATISTICS: Statistics.getInstance().mouseReleased(e); break;
			case NEXT_LEVEL: NextLevel.getInstance().mouseReleased(e);  break;
			case PAUSE:      Pause.getInstance().mouseReleased(e);      break;
			case TUTORIAL:   Tutorial.getInstance().mouseReleased(e);   break;
			case WIN:        Win.getInstance().mouseReleased(e);        break;
			case CREDITS:    Credits.getInstance().mouseReleased(e);    break;
			case OPTIONS:    Options.getInstance().mouseReleased(e);    break;
			case EASTER_EGG: EasterEgg.getInstance().mouseReleased(e);  break;
			case PLAYING:
			case QUIT:
			default:break;
		}
	}

////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void mouseEntered(MouseEvent e) {} //NOT USED
	@Override
	public void mouseExited(MouseEvent e) {}  //NOT USED
	@Override
	public void mouseDragged(MouseEvent e) {} //NOT USED
	@Override
	public void mouseClicked(MouseEvent e) {} //NOT USED
	
////////////////////////////////////////////////////////////////////////////////////
}