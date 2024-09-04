package controller.contextControllers;

import static model.game.Profile.MAX_PROFILES;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import controller.controllers.ProfilesController;
import model.game.Context;
import model.game.Profile;
import view.userInterface.Button;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;
/**
 * Class to manage Delete Context, where you can delete profile.
 * Automatically opened when trying to create a new profile but save slots are full.
 */
public class Delete extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	//buttons
	private LoadSlot[] loadSlots;
	private MenuButton newProfileButton,menuButton;
	//controll
	List<Profile> profiles;
	private boolean check,full;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Delete instance;
	
	/**
	 * Singleton.
	 */
	
	public static Delete getInstance() {
		if(instance==null)
			instance = new Delete();
		return instance;
	}
	
	private Delete() {
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
		
		newProfileButton = new MenuButton(5,MenuButton.CENTER,625,"NEW",Context.NEW_GAME);
		menuButton = new MenuButton(6,MenuButton.CENTER,695,"menu",Context.MENU);
		
		buttons = new Button[MAX_PROFILES+2];
		for(int i=0;i<MAX_PROFILES;i++) 
			buttons[i] = loadSlots[i];
		buttons[5] = newProfileButton;
		buttons[6] = menuButton;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates buttons, profiles list and if at least one profile is deleteted updates newGameButton.
	 * If profiles is empty goes to New Game context controller.
	 */
	public void update() {

		if(profiles.size()>=MAX_PROFILES)
			full = true;
		else full = false;
		
		if(toUpdate) {
			profiles = ProfilesController.getInstance().updateProfiles();
			if(profiles.isEmpty()) newProfileButton.applyContext();
			selectButton(buttons,menuButton.getMenuIndex());
			toUpdate = false;
		}
		
		for(LoadSlot ls : loadSlots)
			ls.update();
		
		if(check) newProfileButton.update();
		menuButton.update();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background,title buttons,instructions,warnings,profiles and if at least one profile is deleteted updates newGameButton.
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		
		if(full)drawWarning(g);
		else drawTitle(g);
		
		drawInstructions(g);
		for(LoadSlot ls : loadSlots)
			ls.draw(g);

		drawProfiles(g);
		
		if(check) newProfileButton.draw(g);
		menuButton.draw(g);
	}
	
//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_65);
		g.setColor(Color.RED);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "DELETE";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(65);
		g.drawString(text, x, y);
	}
	
//////////////////////////////////////////

	private void drawInstructions(Graphics g) {
		g.setFont(FONT_16);
		g.setColor(Color.MAGENTA);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text1 = "Mouse or arrows to select";
		String text2 = "Click or enter to delete";
		int width1 = metrics.stringWidth(text1);
		int width2 = metrics.stringWidth(text2);
		int x1 = (WINDOW_WIDTH - width1) / 2;
		int x2 = (WINDOW_WIDTH - width2) / 2;
		g.drawString(text1, x1, scale(95));
		g.drawString(text2, x2, scale(115));
		g.setColor(Color.RED);
		String text3 = "irreversible operation";
		int width3 = metrics.stringWidth(text3);
		int x3 = (WINDOW_WIDTH - width3) / 2;
		g.drawString(text3, x3, scale(135));
	}

//////////////////////////////////////////
	
	private void drawWarning(Graphics g) {
		g.setFont(FONT_24);
		g.setColor(Color.ORANGE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text1 = "No free slot available";
		int width1 = metrics.stringWidth(text1);
		int x1 = (WINDOW_WIDTH - width1) / 2;
		int y1 = scale(30);
		g.drawString(text1, x1, y1);
		g.setFont(FONT_16);
		FontMetrics metrics2 = g.getFontMetrics(g.getFont());
		String text2 = "delete at least one in order to continue";
		int width2 = metrics2.stringWidth(text2);
		int x2 = (WINDOW_WIDTH - width2) / 2;
		int y2 = scale(50);
		g.drawString(text2, x2, y2);
	}
	
//////////////////////////////////////////
	
	private void drawProfiles(Graphics g) {
		for(int i=0; i<profiles.size();i++) {
			g.drawImage(AVATARS[profiles.get(i).getAvatar()],
					loadSlots[i].getX()+scale(4), loadSlots[i].getY()+scale(4), scale(70), scale(70), null);
			g.setColor(Color.RED);
			g.setFont(FONT_30);
			g.drawString(profiles.get(i).getNickname(),
					loadSlots[i].getX()+scale(80), loadSlots[i].getY()+scale(50));
			g.setFont(FONT_24);
			g.drawString("LVL "+profiles.get(i).getLastLevel(),
					loadSlots[i].getX()+scale(430),loadSlots[i].getY()+scale(47));
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILS
	/**
	 * Delete specifi profile file, triggers update and makes check true.
	 * @param index index of profile to delete in profiles
	 */
	public void deleteProfile(int index) {
		if(index>=profiles.size()) return;
		String nickname = profiles.get(index).getNickname();
		File file = new File("files/saveFiles/"+nickname+".txt");
		file.delete();
		toUpdate = true;
		check=true;
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
			if(check) 
				if(newProfileButton.isSelected()) {
					newProfileButton.applyContext();
					return;
				}
		
			if(menuButton.isSelected()) {
				menuButton.applyContext();
				return;
			}

			if(index<profiles.size()&&index!=-1) {
				deleteProfile(index);
				return;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			menuButton.applyContext();
			return;
		}	
	}
	
//////////////////////////////////////////


	public void mousePressed(MouseEvent e) {
		for (LoadSlot slot : loadSlots) {
			if (slot.isIn(e)) {
				slot.setPressed(true);
				return;
			}
		}
		
		if(check)
			if(newProfileButton.isIn(e)) {
				newProfileButton.setPressed(true);
				return;
			}
		
		if(menuButton.isIn(e)) {
			menuButton.setPressed(true);
			return;
		}
	}

//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		if(check)
			if(newProfileButton.isIn(e))
				if(newProfileButton.isPressed()) {
					check=false;
					newProfileButton.applyContext();
					return;
				}
		if(menuButton.isIn(e))
			if(menuButton.isPressed()) {
				menuButton.applyContext();
				return;
			}
		for (LoadSlot slot : loadSlots) {
			if (slot.isIn(e)) {
				if (slot.isPressed())
					if(slot.getProfileIndex()<profiles.size()) {
						index = slot.getProfileIndex();
						if(index<profiles.size()&&index!=-1) {
							deleteProfile(index); 
							return;
						}
					}
			}	
		}
	}

//////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		for(Button b : buttons)
			b.resetBools();
		
		for (LoadSlot slot : loadSlots) {
			if (slot.isIn(e)) {
				slot.setSelected(true);
				return;
			}
		}

		if(check)
			if(newProfileButton.isIn(e)) {
				newProfileButton.setSelected(true);
				return;
			}
		
		if(menuButton.isIn(e)) {
			menuButton.setSelected(true);
			return;
		}
	}

////////////////////////////////////////////////////////////////////////////////////
}
