package controller.contextControllers;

import static model.game.Profile.MAX_PROFILES;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import controller.controllers.LevelController;
import controller.controllers.ProfilesController;
import model.game.Context;
import model.game.Profile;
import model.player.Player;
import view.userInterface.Button;
import view.userInterface.HiddenButton;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;

/**
 * Class to manage Load Context, where you can load a profile.
 * If there are no profiles goes to New Game context controller.
 */
public class Load extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	//buttons
	private LoadSlot[] loadSlots;
	private MenuButton deleteButton,menuButton;
	private HiddenButton hiddenNewGameButton,hiddenWinButton,hiddenPlayButton;
	//controll
	List<Profile> profiles;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Load instance;
	
	/**
	 * Singleton.
	 */
	public static Load getInstance() {
		if(instance==null)
			instance = new Load();
		return instance;
	}
	
	private Load() {
		createButtons();
		profiles = ProfilesController.getInstance().getProfiles();
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INIT
	
	private void createButtons() {
		loadSlots = new LoadSlot[MAX_PROFILES];
		loadSlots[0] = new LoadSlot(0,LoadSlot.CENTER ,143,0);
		loadSlots[1] = new LoadSlot(1,LoadSlot.CENTER ,240,1);
		loadSlots[2] = new LoadSlot(2,LoadSlot.CENTER ,338,2);
		loadSlots[3] = new LoadSlot(3,LoadSlot.CENTER ,436,3);
		loadSlots[4] = new LoadSlot(4,LoadSlot.CENTER ,534,4);
		
		deleteButton = new MenuButton(5,MenuButton.CENTER,625,"delete",Context.DELETE);
		menuButton = new MenuButton(6,MenuButton.CENTER,695,"back",Context.MENU);
		
		buttons = new Button[MAX_PROFILES+2];
		for(int i=0;i<MAX_PROFILES;i++) 
			buttons[i] = loadSlots[i];
		buttons[5] = deleteButton;
		buttons[6] = menuButton;
		
		hiddenNewGameButton = new HiddenButton(Context.NEW_GAME);
		hiddenWinButton = new HiddenButton(Context.WIN);
		hiddenPlayButton = new HiddenButton(Context.PLAYING);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates buttons, profiles and goes to New Game context controller if profiles is empty.
	 */
	public void update() {
		if(toUpdate) {
			profiles = ProfilesController.getInstance().updateProfiles();
			if(profiles.isEmpty()) {
				hiddenNewGameButton.applyContext();
				return;
			}
			index = menuButton.getMenuIndex();
			selectButton(buttons,index);
			toUpdate = false;
		}
		
		for(Button b : buttons)
			b.update();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws backgrouns, title, instruciton, buttons and profiles.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {
		
		drawBackground(g);
		drawTitle(g);
		drawInstructions(g);
		for(Button b : buttons)
			b.draw(g);
		drawProfiles(g);
	}
	
//////////////////////////////////////////
	
	private void drawInstructions(Graphics g) {
		g.setFont(FONT_16);
		g.setColor(Color.MAGENTA);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text1 = "Mouse or arrows to select";
		String text2 = "Click or enter to load";
		int width1 = metrics.stringWidth(text1);
		int width2 = metrics.stringWidth(text2);
		int x1 = (WINDOW_WIDTH - width1) / 2;
		int x2 = (WINDOW_WIDTH - width2) / 2;
		g.drawString(text1, x1, scale(115));
		g.drawString(text2, x2, scale(130));
	}

//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "load";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(80);
		g.drawString(text, x, y);
	}
	
//////////////////////////////////////////
	
	private void drawProfiles(Graphics g) {
		for(int i=0; i<loadSlots.length;i++) {
			if(i<profiles.size()) {
				g.drawImage(AVATARS[profiles.get(i).getAvatar()],
						loadSlots[i].getX()+scale(4), loadSlots[i].getY()+scale(4), scale(70), scale(70), null);
				g.setColor(Color.WHITE);
				g.setFont(FONT_30);
				g.drawString(profiles.get(i).getNickname(),
						loadSlots[i].getX()+scale(80), loadSlots[i].getY()+scale(50));
				g.setFont(FONT_24);
				g.drawString("LVL "+profiles.get(i).getLastLevel(),
						loadSlots[i].getX()+scale(430),loadSlots[i].getY()+scale(47));
			}
			else {
				g.setFont(FONT_24);
				g.setColor(Color.GREEN);
				g.drawString("NEW PROFILE", scale(250), loadSlots[i].getY()+scale(50));
			}
		}
	}

	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Load a profile, if profile last level is higher than last game level, goes to Win context controller,
	 * else loads last level and goes to Playing context controller. 
	 * @param slot with profile to load
	 */
	private void loadProfile(LoadSlot slot) {
		Profile p = profiles.get(slot.getProfileIndex());
		
		if(p.getLastLevel()>=LevelController.getInstance().getLevels().length) {
			hiddenWinButton.applyContext();
			return;
		}
		else {
			ProfilesController.getInstance().setCurrentProfile(p);
			Player.getInstance().setHealth(p.getLastHealth());
			Player.getInstance().setLastHealth(p.getLastHealth());
			LevelController.getInstance().applyLevel(p.getLastLevel());
			hiddenPlayButton.applyContext();
			return;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INPUTS
	
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
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(index<profiles.size()&&index!=-1) loadProfile(loadSlots[index]);
			else if(index==deleteButton.getMenuIndex()) {
				deleteButton.applyContext();
				return;
			}
			else if(index==menuButton.getMenuIndex()) {
				menuButton.applyContext();
				return;
			}
			else if (index>=profiles.size()) hiddenNewGameButton.applyContext();
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			menuButton.applyContext();
			return;
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
		for (LoadSlot slot : loadSlots) {
			if (slot.isIn(e)) {
				if (slot.isPressed())
					if(slot.getProfileIndex()<profiles.size()) {
						loadProfile(slot);
						return;
					}
					else {
						hiddenNewGameButton.applyContext(); //if empty slot create new profile
						return;
					}
			}
		}
		
		if(deleteButton.isIn(e))
			if(deleteButton.isPressed()) {
				deleteButton.applyContext();
				return;
			}
		
		if(menuButton.isIn(e))
			if(menuButton.isPressed()) {
				menuButton.applyContext();
				return;
			}
	}

//////////////////////////////////////////
	
	public void mouseMoved(MouseEvent e) {
		//reset
		for(Button b : buttons)
			b.resetBools();
		
		for(Button b : buttons)
			if(b.isIn(e)) {
				b.setSelected(true);
				return;
			}
	}

////////////////////////////////////////////////////////////////////////////////////
}
