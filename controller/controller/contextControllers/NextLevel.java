package controller.contextControllers;
import static controller.filesManagers.FileManager.AVATAR_IMAGE_SIZE;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import controller.controllers.ProfilesController;
import controller.filesManagers.FileManager;
import model.game.Context;
import model.game.Profile;
import model.player.Player;
import view.userInterface.ActionButton;
import view.userInterface.Button;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;
import view.userInterface.MusicPlayer;

/**
 * Class to manage Next Level Context.
 */
public class NextLevel extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	//background
	private BufferedImage trophy;
	//buttons
	private MenuButton continueButton, menuButton;
	private LoadSlot preview;
	private ActionButton prevButton,nextButton,musicButton,sfxButton;
	private MusicPlayer musicPlayer;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static NextLevel instance;

	/**
	 * Singleton.
	 */
	public static NextLevel getInstance() {
			if(instance==null)
				instance = new NextLevel();
			return instance;
		}

	private NextLevel() {
		trophy = FileManager.TROPHY;
		musicPlayer = new MusicPlayer(510,null);
		ActionButton[] musicButtons = musicPlayer.getButtons();
		prevButton = musicButtons[0];
		nextButton = musicButtons[1];
		musicButton = musicButtons[2];
		sfxButton = musicButtons[3];
		
		continueButton = new MenuButton(4,MenuButton.CENTER,340,"continue",Context.PLAYING);
		menuButton = new MenuButton(5,MenuButton.CENTER,412,"menu",Context.MENU);
		
		
		buttons= new Button[6];
		buttons[0] = prevButton;
		buttons[1] = nextButton;
		buttons[2] = musicButton;
		buttons[3] = sfxButton;
		buttons[4] = continueButton;
		buttons[5] = menuButton;
		
		preview = new LoadSlot(-2, LoadSlot.CENTER, 250, -2);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates buttons.
	 */
	public void update() {
		if(toUpdate) {
			index = continueButton.getMenuIndex();
			selectButton(buttons,index);
			toUpdate = false;
		}
		for(Button b : buttons)
			b.update();

	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background, title, a trophy and buttons.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		drawTitle(g);
		preview.draw(g);
		drawProfile(g);
		g.drawImage(trophy, WINDOW_WIDTH/2-scale(385), WINDOW_WIDTH-scale(200), scale(250), scale(250), null);
		g.drawImage(trophy, WINDOW_WIDTH/2+scale(135), WINDOW_WIDTH-scale(200), scale(250), scale(250), null);
		for(Button b : buttons)
			b.draw(g);
		musicPlayer.draw(g);
	}
	
//////////////////////////////////////////
		
	private void drawProfile(Graphics g) {
		Profile p = ProfilesController.getInstance().getCurrentProfile();
		g.drawImage(FileManager.AVATARS[p.getAvatar()],
				preview.getX()+scale(4), preview.getY()+scale(4), scale(AVATAR_IMAGE_SIZE/2), scale(AVATAR_IMAGE_SIZE/2), null);
		g.setFont(FONT_24);
		g.setColor(Color.WHITE);
		g.drawString(p.getNickname(), preview.getX()+scale(80), preview.getY()+scale(50));
		g.setColor(Color.YELLOW);
		g.drawString("Level " + p.getLastLevel(), preview.getX()+scale(380), preview.getY()+scale(50));
		
	}

//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text1 = "NEXT";
		String text2 = "LEVEL";
		int width1 = metrics.stringWidth(text1);
		int x1 = (WINDOW_WIDTH - width1) / 2;
		int y1 = scale(120);
		int width2 = metrics.stringWidth(text2);
		int x2 = (WINDOW_WIDTH - width2) / 2;
		int y2 = scale(220);
		g.drawString(text1, x1, y1);
		g.drawString(text2, x2, y2);
	}

////////////////////////////////////////////////////////////////////////////////////
//INPUT

	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			switch(index) {
				case 0 :
				case 1 : 
					index=buttons.length-1; selectButton(buttons,index);return;
				case 2 :
				case 3 : 
					index =0; selectButton(buttons,index);return;
				case 4: 
					index = 2; selectButton(buttons,index);return;
				default:index --; selectButton(buttons,index); return;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			switch(index) {
				case 0 :
				case 1 : 
					index=2; selectButton(buttons,index);return;
				case 2 :
				case 3 : 
					index =4; selectButton(buttons,index);return;
				default:break;
			}
			if(index+1>=buttons.length) index=0;
			else index++;
			selectButton(buttons,index);
			return;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			switch(index) {
				case 0 : index =1; selectButton(buttons,index);return;
				case 1 : index =0; selectButton(buttons,index);return;
				case 2 : index =3; selectButton(buttons,index);return;
				case 3 : index =2; selectButton(buttons,index);return;
				default:return;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			menuButtonAction();
			return;
		}
		else if (e.getKeyCode()== KeyEvent.VK_ENTER) {
			if(index==nextButton.getMenuIndex()) {
				nextButton.nextSong();
				return;
			}
			else if(index==prevButton.getMenuIndex()) {
				prevButton.previouSong();
				return;
			}

			else if(index==musicButton.getMenuIndex()) {
				musicButton.switchSound();
				return;
			}
			else if(index==sfxButton.getMenuIndex()) {
				sfxButton.switchSound();
				return;
			}
			else if(menuButton.isSelected()) {
				menuButtonAction();
				return;
			}
			else if (continueButton.isSelected()) {
				continueButtonAction();
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
		if(prevButton.isIn(e)) {
			if(prevButton.isPressed()) {
				prevButton.previouSong();
				return;
			}
		}
		else if(nextButton.isIn(e)) {
			if(nextButton.isPressed()) {
				nextButton.nextSong();
				return;
			}
		}
		else if(musicButton.isIn(e)) {
			if(musicButton.isPressed()) {
				musicButton.switchSound();
				return;
			}
			
		}
		else if(sfxButton.isIn(e)) {
			if(sfxButton.isPressed()) {
				sfxButton.switchSound();
				return;
			}
		}
		
		else if(continueButton.isIn(e)) {
			if(continueButton.isPressed()) {
				continueButtonAction();
				return;
			}
		}
		else if(menuButton.isIn(e)) {
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
		
		for(Button b : buttons)
			if(b.isIn(e)) {
				b.setSelected(true);
				return;
			}
	}
	
//////////////////////////////////////////
	
	private void continueButtonAction() {
		ProfilesController.getInstance().getCurrentProfile().setLastHealth(Player.getInstance().getHealth());
		ProfilesController.getInstance().saveCurrentProfile();
		continueButton.applyContext();
		
	}

//////////////////////////////////////////
	
	private void menuButtonAction() {
		ProfilesController.getInstance().getCurrentProfile().setLastHealth(Player.getInstance().getHealth());
		ProfilesController.getInstance().saveCurrentProfile();
		menuButton.applyContext();
	}
	
////////////////////////////////////////////////////////////////////////////////////
}


