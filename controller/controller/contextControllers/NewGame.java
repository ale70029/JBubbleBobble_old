package controller.contextControllers;


import static controller.filesManagers.FileManager.AVATAR_IMAGE_SIZE;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ConcurrentModificationException;
import java.util.List;

import controller.controllers.ProfilesController;
import model.game.Context;
import model.game.Profile;
import view.userInterface.Button;
import view.userInterface.HiddenButton;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;
import view.userInterface.TextField;
/**
 * 
Class to manage New Game Context, used to create profiles and start a new game.
 */
public class NewGame extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	//buttons
	private TextField textField;
	private Button[] avatarsButtons;
	private LoadSlot preview;
	private MenuButton startButton,backButton;
	private HiddenButton hiddenDeleteButton;
	//controll
	private List<Profile> profiles;
	private int selected=-1;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static NewGame instance;

	/**
	 * Singleton.
	 */
	public static NewGame getInstance() {
			if(instance==null)
				instance = new NewGame();
			return instance;
		}

	private NewGame() {
		createButtons();
		profiles= ProfilesController.getInstance().getProfiles();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INIT
	
	private void createButtons() {
		textField = new TextField(180,200);
		preview = new LoadSlot(-2,LoadSlot.CENTER,143,-2);
		
		avatarsButtons = new Button[6];
		int avatars_y = 400;
		//19, 139, 259, 379, 499, 619
		avatarsButtons[0] = new Button(0,scale(19),avatars_y,AVATAR_IMAGE_SIZE-35,AVATAR_IMAGE_SIZE-35);
		avatarsButtons[1] = new Button(1,scale(139),avatars_y,AVATAR_IMAGE_SIZE-35,AVATAR_IMAGE_SIZE-35);
		avatarsButtons[2] = new Button(2,scale(259),avatars_y,AVATAR_IMAGE_SIZE-35,AVATAR_IMAGE_SIZE-35);
		avatarsButtons[3] = new Button(3,scale(379),avatars_y,AVATAR_IMAGE_SIZE-35,AVATAR_IMAGE_SIZE-35);
		avatarsButtons[4] = new Button(4,scale(499),avatars_y,AVATAR_IMAGE_SIZE-35,AVATAR_IMAGE_SIZE-35);
		avatarsButtons[5] = new Button(5,scale(619),avatars_y,AVATAR_IMAGE_SIZE-35,AVATAR_IMAGE_SIZE-35);
		
		startButton = new MenuButton(6,MenuButton.CENTER,600,"start",Context.TUTORIAL);
		backButton = new MenuButton(7,MenuButton.CENTER,670,"back",Context.MENU);	
		
		buttons = new Button[avatarsButtons.length+2];
		for(int i=0;i<avatarsButtons.length;i++) 
			buttons[i] = avatarsButtons[i];
		buttons[avatarsButtons.length] = startButton;
		buttons[avatarsButtons.length+1] = backButton;
		
		hiddenDeleteButton = new HiddenButton(Context.DELETE);
	}

////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates buttons and profiles.
	 */
	public void update() {
		
		if(toUpdate) {
			profiles = ProfilesController.getInstance().updateProfiles();
			
			if(profiles.size() >= Profile.MAX_PROFILES) {
				hiddenDeleteButton.applyContext();
				return;
			}
			
			if(toUpdate&&checkProfile()) {
				index = startButton.getMenuIndex();
				selectButton(buttons,index);
				toUpdate = false;
			}
			else if(toUpdate&&!checkProfile()) {
				index = avatarsButtons[0].getMenuIndex();
				selectButton(buttons,index);
				toUpdate = false;
			}
		}
		
		for(Button avatar:avatarsButtons)
			avatar.update();
		
		if(checkProfile()) startButton.update();
		backButton.update();
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background, title, instructions, a preview of the builded profile, the textField with the nickname, alerts and buttons.
	 * If the profile builded is ok (checkProfile returns true) startButton is showed.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		drawTitle(g);
		drawInstructions(g);
		preview.draw(g);
		textField.draw(g);
		drawAvatars(g);
		drawAlert(g);
		if(checkProfile()) startButton.draw(g);
		backButton.draw(g);
	}
	
//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "new game";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(120);
		g.drawString(text, x, y);
	}
	
//////////////////////////////////////////
	
	private void drawInstructions(Graphics g) {
		//AVATAR
		g.setFont(FONT_30);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text2 = "avatar";
		int width2 = metrics.stringWidth(text2);
		int x2= (WINDOW_WIDTH - width2) / 2;
		int y2 = scale(320);
		g.drawString(text2, x2, y2);
		
		//nickname specifics
		g.setFont(FONT_15);
		g.setColor(Color.GRAY);
		metrics = g.getFontMetrics(g.getFont());
		String text3 = "TYPE only letters A-Z or numbers 1-9. ";
		int width3 = metrics.stringWidth(text3);
		int x3 = (WINDOW_WIDTH - width3) / 2;
		int y3 = scale(250);
		g.drawString(text3, x3, y3);
		
		//avatars instructions
		String text4 = "Mouse or arrows to navigate";
		int width4 = metrics.stringWidth(text4);
		int x4 = (WINDOW_WIDTH - width4) / 2;;
		int y4 = scale(350);
		g.drawString(text4, x4, y4);
		String text5 = "Click or enter to select";
		int width5 = metrics.stringWidth(text5);
		int x5 = (WINDOW_WIDTH - width5) / 2;;
		int y5 = scale(370);
		g.drawString(text5, x5, y5);
	}

//////////////////////////////////////////
	
	private void drawAvatars(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		//draw avatars
		for(int i=0; i<avatarsButtons.length;i++ )
			g2D.drawImage(AVATARS[i], scale((120*i)+19), scale(400), scale(AVATAR_IMAGE_SIZE-35), scale(AVATAR_IMAGE_SIZE-35), null);
		//draw selection
		for(Button avatar : avatarsButtons)
			if(avatar.isSelected()) {
				g2D.setColor(Color.WHITE);
				g2D.setStroke(new BasicStroke(scale(5)));
				g2D.drawRect(avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
			}
			else if(avatar.isPressed()) {
				g2D.setColor(Color.GREEN);
				g2D.setStroke(new BasicStroke(scale(5)));
				g2D.drawRect(avatar.getX(), avatar.getY(), avatar.getWidth(), avatar.getHeight());
			}
		//draw avatar in preview
		if(selected!=-1)
			g.drawImage(AVATARS[selected], preview.getX()+scale(4), preview.getY()+scale(4), scale(AVATAR_IMAGE_SIZE/2), scale(AVATAR_IMAGE_SIZE/2), null);
	}

//////////////////////////////////////////
	
	private void drawAlert(Graphics g) {
		try {
			//missing nickname
			if(textField.getText().equals("")) {
				g.setFont(FONT_24);
				g.setColor(Color.RED);
				String text = "TYPE NICKNAME";
				int x= scale(230);
				int y = scale(195);
				g.drawString(text, x, y);
			}
			//missing avatar
			if(selected==-1) {
				g.setFont(FONT_24);
				g.setColor(Color.RED);
				FontMetrics metrics = g.getFontMetrics(g.getFont());
				String text = "select avatar";
				int width = metrics.stringWidth(text);
				int x= (WINDOW_WIDTH - width) / 2;
				int y= scale(550);
				g.drawString(text, x, y);
			}
			//nickname taken
			for(Profile p : profiles)
				if(textField.getText().equals(p.getNickname())) {
					g.setFont(FONT_14);
					g.setColor(Color.RED);
					String text = "Nickname taken";
					int x= scale(445);
					int y= scale(190);
					g.drawString(text, x, y);
			}
		}catch(ConcurrentModificationException e) {}
		
		if(checkProfile()) {
			g.setFont(FONT_24);
			g.setColor(Color.green);
			String text= "READY";
			int x = scale(490);
			int y = scale(195);
			g.drawString(text, x, y);
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILS

	/**
	 * Check if builded profile is ok (has a unique nickname and a profile picture)
	 * @return true if profile is ok, else false-
	 */
	private boolean checkProfile(){
		if(textField.getText().equals("")) return false;
		if(selected==-1) return false;
		for(Profile p : profiles)
			if(textField.getText().equals(p.getNickname()))
		return false;
		
		// if ok
		toUpdate=true;
		return true;
	}

//////////////////////////////////////////

	private void reset() {
		selected = -1;
		for(int i=0;i<3;i++) 
			avatarsButtons[i].setPressed(false);
		textField.clear();
	}
	
//////////////////////////////////////////
	
	private void startProcedure() {
		ProfilesController.getInstance().addProfile(textField.getText(), selected);
		reset();
	}

////////////////////////////////////////////////////////////////////////////////////
//INPUTS
	

	public void mousePressed(MouseEvent e) {
		for(Button b : buttons)
			if(b.isIn(e)) {
				b.setPressed(true);
				return;
			}
	}

//////////////////////////////////////////

	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			reset();
			backButton.applyContext();
			return;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			textField.clear();
			return;
		}
		
		else if (e.getKeyCode()== KeyEvent.VK_ENTER) {
			if(startButton.isSelected()) {
				if(checkProfile()) {
					startProcedure();
					startButton.applyContext();
					return;
				}
			}
			else if (backButton.isSelected()) {
				reset();
				backButton.applyContext();
				return;
			}
			//reset unselected avatars buttons
			for(Button avatar: avatarsButtons) {
				if(avatar.isSelected()) {
					avatar.setPressed(true);
					selected = avatar.getMenuIndex();
				}
				else avatar.resetBools();
			}
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_UP) {
			if(index<avatarsButtons.length) index=avatarsButtons.length+1;
			else if(!checkProfile() && index==avatarsButtons.length+1) index = 0;
			else if(checkProfile() && index==avatarsButtons.length+1) index = avatarsButtons.length;
			else if(checkProfile() && index==avatarsButtons.length) index = 0;
			selectButton(buttons,index);
			return;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(index<avatarsButtons.length && checkProfile()) index=avatarsButtons.length;
			else if(index<=avatarsButtons.length) index=avatarsButtons.length+1;
			else index = 0;
			selectButton(buttons,index);
			return;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(index-1<0) index = avatarsButtons.length-1;
			else if(index<avatarsButtons.length) index--;
			selectButton(buttons,index);
			return;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(index==avatarsButtons.length-1) index = 0;
			else if(index<avatarsButtons.length-1) index++;
			selectButton(buttons,index);
			return;
		}
		
		else textField.keyReleased(e);
	}
	
//////////////////////////////////////////

	public void mouseReleased(MouseEvent e) {
		if(startButton.isIn(e)) {
			if(startButton.isPressed()) {
					if(checkProfile()) {
						startProcedure();
						startButton.applyContext();
						return;
					}
				}
		}
		else if(backButton.isIn(e)) {
			if(backButton.isPressed()) {
				reset();
				backButton.applyContext();
				return;
			}
		}
		
		for(Button avatar:avatarsButtons) {
			if(avatar.isIn(e)) {
				if(avatar.isPressed()) {
					selected = avatar.getMenuIndex();
				}
			}
			else avatar.setPressed(false);
		}
		
	}
	

//////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		for(Button b : buttons)
			b.setSelected(false);
		
		for(Button b : buttons) 
			if(b.isIn(e))
				b.setSelected(true);
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
