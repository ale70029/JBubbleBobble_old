package controller.contextControllers;

import static controller.filesManagers.FileManager.AVATAR_IMAGE_SIZE;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import controller.controllers.LevelController;
import controller.controllers.ProfilesController;
import controller.filesManagers.FileManager;
import model.game.Context;
import model.game.Profile;
import model.player.Player;
import view.userInterface.Button;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;

/**
 * Class for GameOver context, applyed when player dies.
 */
public class GameOver extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	//Background
	private BufferedImage grave;
	//Buttons
	private MenuButton restartButton, menuButton;
	private LoadSlot preview;
	private boolean lostSaved;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static GameOver instance;
	
	/**
	 * Singleton.
	 */
	public static GameOver getInstance() {
		if(instance==null)
		instance = new GameOver();
		return instance;
	}
		
	private GameOver() {
		grave = FileManager.GRAVE;
		restartButton = new MenuButton(0,MenuButton.CENTER,350,"restart",Context.PLAYING);
		menuButton = new MenuButton(1,MenuButton.CENTER,422,"menu",Context.MENU);
		buttons= new Button[2];
		buttons[0] = restartButton;
		buttons[1] = menuButton;
		preview = new LoadSlot(-2, LoadSlot.CENTER, 250, -2);
	}

////////////////////////////////////////////////////////////////////////////////////
//UPDATE

	/**
	 * Updates player lost count and buttons.
	 */
	public void update() {
		if(!lostSaved) {
			ProfilesController.getInstance().addLost();
			lostSaved=true;
		}
		if(toUpdate) {
			index = restartButton.getMenuIndex();
			selectButton(buttons,index);
			toUpdate = false;
		}
		for(Button b : buttons)
			b.update();
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background, title, a grave with profile info and buttons.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {

		drawBackground(g);
		preview.draw(g);
		drawProfile(g);
		drawTitle(g);
		drawGrave(g);
		for(Button b : buttons)
			b.draw(g);
	}
	
//////////////////////////////////////////
	
	private void drawProfile(Graphics g) {
		Profile p = ProfilesController.getInstance().getCurrentProfile();
		g.drawImage(FileManager.AVATARS[p.getAvatar()],
				preview.getX()+scale(4), preview.getY()+scale(4), scale(AVATAR_IMAGE_SIZE/2), scale(AVATAR_IMAGE_SIZE/2), null);
		g.setFont(FONT_24);
		g.setColor(Color.WHITE);
		g.drawString(p.getNickname(), preview.getX()+scale(80), preview.getY()+scale(50));
		
		g.setFont(FONT_16);
		g.setColor(Color.YELLOW);
		g.drawString(""+p.getLastScore(), preview.getX()+scale(325), preview.getY()+scale(47));
		g.setColor(Color.BLUE);
		g.drawString("Lvl " + p.getLastLevel(), preview.getX()+scale(470), preview.getY()+scale(47));
		
	}
	
//////////////////////////////////////////

	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text1 = "GAME";
		String text2 = "OVER";
		int width1 = metrics.stringWidth(text1);
		int x1 = (WINDOW_WIDTH - width1) / 2;
		int y1 = scale(120);
		int width2 = metrics.stringWidth(text2);
		int x2 = (WINDOW_WIDTH - width2) / 2;
		int y2 = scale(220);
		g.drawString(text1, x1, y1);
		g.drawString(text2, x2, y2);
	}

//////////////////////////////////////////

	private void drawGrave(Graphics g) {
		//grave
		g.drawImage(grave, scale(270), WINDOW_HEIGHT-scale(260), scale(200), scale(260), null);
		//avatar
		Profile p = ProfilesController.getInstance().getCurrentProfile();
		g.drawImage(AVATARS[p.getAvatar()], scale(328), WINDOW_HEIGHT-scale(180), scale(80), scale(80), null);
		//name
		g.setFont(FONT_14);
		g.setColor(Color.BLACK);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int width = metrics.stringWidth(p.getNickname());
		int x1 = ((WINDOW_WIDTH - width) / 2);
		int y1 = WINDOW_HEIGHT-scale(70);
		g.drawString(p.getNickname(), x1, y1);
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	private void gameOverRestart() {
		int health=Player.MAX_HEALTH;
		
		if(Playing.getInstance().getDifficulty() == Playing.HARD) {
			LevelController.getInstance().restartGame();
		}
		else if(Playing.getInstance().getDifficulty() == Playing.NORMAL){
			LevelController.getInstance().restartLevel();
			health=Player.MAX_HEALTH/2;
		}
		else if(Playing.getInstance().getDifficulty() == Playing.EASY) {
			LevelController.getInstance().restartLevel();
		}
		
		Player.getInstance().setHealth(health);
		Player.getInstance().setLastHealth(health);
		ProfilesController.getInstance().getCurrentProfile().setLastHealth(health);
		ProfilesController.getInstance().saveCurrentProfile();
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INPUT
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if(index-1<0) index=buttons.length-1;
			else index--;
			selectButton(buttons,index);
			return;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(index+1>=buttons.length) index=0;
			else index++;
			selectButton(buttons,index);
			return;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			ProfilesController.getInstance().saveCurrentProfile();
			menuButton.applyContext();
			return;
		}
		
		else if (e.getKeyCode()== KeyEvent.VK_ENTER) {
			if(restartButton.isSelected()) {
				LevelController.getInstance().restartLevel();
				restartButtonAction();
				return;
			}
			else if (menuButton.isSelected()) {
				menuButtonAction();
				return;
			}
		}
	}
	
//////////////////////////////////////////
	
	public void mousePressed(MouseEvent e) {
		for(Button b : buttons)
			if(b.isIn(e)) {
				b.setPressed(true);
				return;
			}
	}

//////////////////////////////////////////

	public void mouseReleased(MouseEvent e) {
		if(restartButton.isIn(e)) {
			if(restartButton.isPressed()) {
				restartButtonAction();
				return;
			}
		}else if(menuButton.isIn(e)) {
			if(menuButton.isPressed()) {
				menuButtonAction();
				return;
			}
		}

	}

//////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		for(Button b : buttons)
			b.resetBools();
		
		for(Button b : buttons) {
			if(b.isIn(e)) {
				b.setSelected(true);
				return;
			}
		}
	}
	
//////////////////////////////////////////
	
	private void restartButtonAction() {
		gameOverRestart();
		restartButton.applyContext();
		lostSaved=false;
	}

//////////////////////////////////////////
	
	private void menuButtonAction() {
		gameOverRestart();
		menuButton.applyContext();
		lostSaved=false;
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
