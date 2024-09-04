package controller.contextControllers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import controller.filesManagers.FileManager;
import model.game.Context;
import view.userInterface.Button;
import view.userInterface.MenuButton;

/**
 * Class to manage EasterEgg Context.
 */
public class EasterEgg extends GenericContext{
	private MenuButton backButton;
	private BufferedImage SURPRISE; 
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static EasterEgg instance;
	
	/**
	 * Singleton.
	 */
	public static EasterEgg getInstance() {
		if(instance==null)
		instance = new EasterEgg();
	return instance;
	}
	
	private EasterEgg() {
		SURPRISE = FileManager.SURPRISE;
		backButton= new MenuButton(0,MenuButton.CENTER,679,"back",Context.MENU);
		buttons = new Button[1];
		buttons[0] = backButton;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Update buttons status and select back button when Context is applyed
	 */
	public void update() {
		if(toUpdate) {
			selectButton(buttons,backButton.getMenuIndex());
			toUpdate = false;
		}
		backButton.update();
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background, title, credits and buttons.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		drawSurprise(g);
		drawTitle(g);
		backButton.draw(g);

	}

//////////////////////////////////////////
	
	private void drawSurprise(Graphics g) {
		g.drawImage(SURPRISE, (WINDOW_WIDTH-scale(420))/2, scale(100), scale(420), scale(602), null);
		
	}

//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "surprise";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(120);
		g.drawString(text, x, y);
	}

////////////////////////////////////////////////////////////////////////////////////
//INPUT
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE||e.getKeyCode() == KeyEvent.VK_ENTER) {
				backButton.applyContext();
		}
	}
	
//////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		for(Button b : buttons)
			b.resetBools();
		
		if (backButton.isIn(e)) 
			backButton.setSelected(true);
	}
	
//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		if(backButton.isIn(e)) 
			if(backButton.isPressed()) {
				backButton.applyContext();
			}
	}
	
//////////////////////////////////////////

	public void mousePressed(MouseEvent e) {
		if (backButton.isIn(e)) 
			backButton.setPressed(true);
	}

////////////////////////////////////////////////////////////////////////////////////
}
