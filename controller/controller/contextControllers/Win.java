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
import view.userInterface.Button;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;

/**
 * Class to manage Win Context.
 */
public class Win extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	private BufferedImage trophy;
	private MenuButton newGameButton,menuButton;
	private LoadSlot preview;
	

////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Win instance;

	/**
	 * Singleton.
	 */
	public static Win getInstance() {
			if(instance==null)
				instance = new Win();
			return instance;
		}
	
	private Win() {
		
		trophy = FileManager.TROPHY;
		newGameButton = new MenuButton(0,MenuButton.CENTER,490,"new Play",Context.PLAYING);
		menuButton = new MenuButton(1,MenuButton.CENTER,700,"menu",Context.MENU);

		buttons = new Button[2];
		buttons[0] = newGameButton;
		buttons[1] = menuButton;
		preview = new LoadSlot(-2, LoadSlot.CENTER, 250, -2);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates buttons and Profile win count.
	 */
	public void update() {
		if(toUpdate) {
			index = newGameButton.getMenuIndex();
			selectButton(buttons,index);
			toUpdate = false;
		}
		
		for(Button b : buttons)
			b.update();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background, title and buttons.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		drawTitle(g);
		drawText(g);
		drawProfile(g);
		drawDecoration(g);
		for(Button b : buttons)
			b.draw(g);
	}
	
//////////////////////////////////////////
	
	private void drawDecoration(Graphics g) {
		g.drawImage(trophy, WINDOW_WIDTH/2-scale(350), WINDOW_WIDTH-scale(200), scale(250), scale(250), null);
		g.drawImage(trophy, WINDOW_WIDTH/2+scale(100), WINDOW_WIDTH-scale(200), scale(250), scale(250), null);
	}

//////////////////////////////////////////
	
	private void drawText(Graphics g) {
		g.setFont(FONT_30);
		g.setColor(Color.YELLOW);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "congratulations";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(400);
		g.drawString(text, x, y);
		
		g.setColor(Color.WHITE);
		g.setFont(FONT_24);
		metrics = g.getFontMetrics(g.getFont());
		String text2 = "you can play a new game";
		int width2 = metrics.stringWidth(text2);
		int x2 = (WINDOW_WIDTH - width2) / 2;
		int y2 = scale(440);
		g.drawString(text2, x2, y2);
		
		String text3 = "keeping your score";
		int width3 = metrics.stringWidth(text3);
		int x3 = (WINDOW_WIDTH - width3) / 2;
		int y3 = scale(470);
		g.drawString(text3, x3, y3);
		
		
	}

//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "YOU WIN";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(120);
		g.drawString(text, x, y);
	}
	
//////////////////////////////////////////
	
	private void drawProfile(Graphics g) {
		preview.draw(g);
		Profile p = ProfilesController.getInstance().getCurrentProfile();
		//score
		g.setColor(Color.YELLOW);
		g.setFont(FONT_30);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text =""+ p.getLastScore();
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = preview.getY()-scale(30);
		g.drawString(""+p.getLastScore(), x,y);
		//profile
		g.setColor(Color.WHITE);
		g.drawImage(FileManager.AVATARS[p.getAvatar()],
				preview.getX()+scale(4), preview.getY()+scale(4), scale(AVATAR_IMAGE_SIZE/2), scale(AVATAR_IMAGE_SIZE/2), null);
		g.setFont(FONT_24);
		g.setColor(Color.WHITE);
		g.drawString(p.getNickname(), preview.getX()+scale(80), preview.getY()+scale(50));
		g.setColor(Color.YELLOW);
		g.drawString("Level " + p.getLastLevel(), preview.getX()+scale(380), preview.getY()+scale(50));
		
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
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			ProfilesController.getInstance().addWin();
			menuButton.applyContext();
			return;
		}
		if (e.getKeyCode()== KeyEvent.VK_ENTER) {
			if(menuButton.isSelected()) {
				ProfilesController.getInstance().addWin();
				LevelController.getInstance().restartGame();
				menuButton.applyContext();
				return;
			}
			else if (newGameButton.isSelected()) {
				ProfilesController.getInstance().addWin();
				LevelController.getInstance().restartGame();
				newGameButton.applyContext();
				
			}
		}
	}
	
//////////////////////////////////////////
	
	public void mouseMoved(MouseEvent e) {
		for (Button b : buttons)
			b.resetBools();
		
		for (Button b : buttons)
			if (b.isIn(e)) {
				b.setSelected(true);
				return;
			}
	}
	
//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		if(newGameButton.isIn(e)) {
			if(newGameButton.isPressed()) {
				ProfilesController.getInstance().addWin();
				LevelController.getInstance().restartGame();
				newGameButton.applyContext();
				
				return;
			}
		}
		else if(menuButton.isIn(e)) {
			if(menuButton.isPressed()) {
				ProfilesController.getInstance().addWin();
				LevelController.getInstance().restartGame();
				menuButton.applyContext();
				return;
			}
		}
	}
	
//////////////////////////////////////////
	
	public void mousePressed(MouseEvent e) {

		for (Button b : buttons)
			if(b.isIn(e)) {
				b.setPressed(true);
				return;
			}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
