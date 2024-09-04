package controller.contextControllers;

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
import view.userInterface.ActionButton;
import view.userInterface.Button;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;
import view.userInterface.MusicPlayer;



/**
 * Class to manage Pause context.
 * Pause looks like is drawed over Playing.
 * There is a music player that lets you choose songs to listen if Fun Mode is enabled in options, else is disabled.
 * There are music/sound switch, and buttons to restart level, continue playing, see tutorial, open options and go back to menu.
 * 
 * NOTE THAT GOING INTO OPTIONS WILL RESTART THE LEVEL TO PREVENT CHEATING
 *(like changing difficulty when enemies are already dead to get more points from items)
 */
public class Pause extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	//Background
	private BufferedImage pausePanel;
	//Buttons
	private ActionButton prevButton,nextButton,musicButton,sfxButton;
	private MenuButton optionsButton,menuButton,continueButton,restartButton,tutorialButton;
	private LoadSlot currentProfile;
	//Music
	private MusicPlayer musicPlayer;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Pause instance;
	
	/**
	 * Singleton.
	 */
	public static Pause getInstance() {
		if(instance==null)
			instance = new Pause();
		return instance;
	}
	
	private Pause() {
		pausePanel = FileManager.PAUSE_PANEL;
		musicPlayer = new MusicPlayer(200,Color.BLACK);
		createButtons();
		

	}

////////////////////////////////////////////////////////////////////////////////////
//INIT
	
	private void createButtons() {

		ActionButton[] musicButtons = musicPlayer.getButtons();
		prevButton = musicButtons[0];
		nextButton = musicButtons[1];
		musicButton = musicButtons[2];
		sfxButton = musicButtons[3];
		optionsButton = new MenuButton(4,MenuButton.CENTER,342,"options",Context.OPTIONS);
		continueButton = new MenuButton(5,MenuButton.CENTER,404,"continue",Context.PLAYING);
		restartButton = new MenuButton(6,MenuButton.CENTER,466,"restart",Context.PLAYING);
		tutorialButton = new MenuButton(7,MenuButton.CENTER,528,"tutorial",Context.TUTORIAL);
		menuButton = new MenuButton(8,MenuButton.CENTER,590,"menu",Context.MENU);
		
		buttons = new Button[9];
		
		buttons[0] = prevButton;
		buttons[1] = nextButton;
		buttons[2] = musicButton;
		buttons[3] = sfxButton;
		
		buttons[4] = optionsButton;
		buttons[5] = continueButton;
		buttons[6] = restartButton;
		buttons[7] = tutorialButton;
		buttons[8] = menuButton;
		
		currentProfile = new LoadSlot(-2,(WINDOW_WIDTH-scale(340))/2,690,-2);
		currentProfile.setWidth(340);
		
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
		for(Button bu: buttons)
			bu.update();
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws playing as background, the semiTransparent pause panel overlay, title, song name, current profile and buttons.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {
		//draws playing in background
		Playing.getInstance().draw(g);
		//pause panel
		g.drawImage(pausePanel, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
		drawButtonsPanel(g);
		//text
		drawTitle(g);
		//song player
		musicPlayer.draw(g);
		//buttons
		for(Button b:buttons)
			b.draw(g);
		//profile
		drawProfile(g);
	}
	
//////////////////////////////////////////

	private void drawTitle(Graphics g) {
		g.setFont(TITLE_65);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "PAUSE";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(140);
		g.drawString(text, x, y);
	}
	
//////////////////////////////////////////
	
	private void drawButtonsPanel(Graphics g) {
		int yUp = optionsButton.getY()-scale(10);
		int yDown = menuButton.getY()+menuButton.getHeight()+scale(10);
		int xLeft = optionsButton.getX()-scale(10);
		int xRight = optionsButton.getX()+optionsButton.getWidth()+scale(10);
		int panelWidth = xRight-xLeft;
		int panelHeight = yDown-yUp;
		g.setColor(Color.BLACK);
		g.fillRect(xLeft, yUp, panelWidth, panelHeight);
	}
	
//////////////////////////////////////////

	private void drawProfile(Graphics g) {
		currentProfile.draw(g);
		Profile p = ProfilesController.getInstance().getCurrentProfile();
		g.drawImage(AVATARS[p.getAvatar()], currentProfile.getX()+scale(2), currentProfile.getY()+scale(4), scale(70), scale(70), null);
		g.setFont(FONT_24);
        g.setColor(Color.WHITE);
        int x = currentProfile.getX()+scale(85);
        int y = currentProfile.getY()+scale(50);
        g.drawString(p.getNickname(), x, y);
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

		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			//music
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
				//menu
				else if(index==optionsButton.getMenuIndex()) {
					optionsButton.applyContext();
					return;
				}
				else if (index == continueButton.getMenuIndex()) {
					continueButton.applyContext();
					return;
				}
				else if (index==restartButton.getMenuIndex()) {
					LevelController.getInstance().restartLevel();
					restartButton.applyContext();
					return;
				}
				else if (index == tutorialButton.getMenuIndex()) {
					tutorialButton.applyContext();
					return;
				}
				else if(index==menuButton.getMenuIndex()) {
					LevelController.getInstance().restartLevel();
					ProfilesController.getInstance().saveCurrentProfile();
					menuButton.applyContext();
					return;
				}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			continueButton.applyContext();
			return;
		}
	}

//////////////////////////////////////////

	public void mouseReleased(MouseEvent e) {
		//music
		
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
		//menu
		else if(optionsButton.isIn(e)) {
			if(optionsButton.isPressed()) { 
				optionsButton.applyContext();	
				return;
			}
		}
		else if(continueButton.isIn(e)) {
			if(continueButton.isPressed()) {
				continueButton.applyContext();
				return;
			}
		}
		else if(restartButton.isIn(e)) {
			if(restartButton.isPressed()) {
				LevelController.getInstance().restartLevel();
				restartButton.applyContext();
				return;
			}
		}
		else if(tutorialButton.isIn(e)) {
			if(tutorialButton.isPressed()) {
				tutorialButton.applyContext();
				return;
			}
		}
		else if(menuButton.isIn(e)) {
			if(menuButton.isPressed()) {
				LevelController.getInstance().restartLevel();
				ProfilesController.getInstance().saveCurrentProfile();
				menuButton.applyContext();
				return;
			}
		}
	}

//////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		for (Button b : buttons)
			b.resetBools();
		
		for (Button b : buttons) {
			if (b.isIn(e)) 
				b.setSelected(true);
		}
	}
	
//////////////////////////////////////////
	
	public void mousePressed(MouseEvent e) {
		for(Button b : buttons)
			if(b.isIn(e))
				b.setPressed(true);
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
