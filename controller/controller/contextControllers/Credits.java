package controller.contextControllers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import model.game.Context;
import view.userInterface.Button;
import view.userInterface.EggButton;
import view.userInterface.MenuButton;

/**
 * Class to manage Credits Context.
 */
public class Credits extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	private MenuButton backButton;
	private EggButton easterEgg;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Credits instance;
	
	/**
	 * Singleton.
	 */
	public static Credits getInstance() {
		if(instance==null)
		instance = new Credits();
	return instance;
	}
	
	private Credits() {
		easterEgg = new EggButton(480,680);
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
			easterEgg.reset();
		}
		easterEgg.update();
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
		drawTitle(g);
		drawCredits(g);
		backButton.draw(g);
		easterEgg.draw(g);
	}
	
//////////////////////////////////////////

	private void drawCredits(Graphics g) {
	
		//SECTIONS
		g.setFont(FONT_30);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		//music title
		g.setColor(Color.GREEN);
		String text = "MUSIC";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(170);
		g.drawString(text, x, y);
		//game title
		g.setColor(Color.BLUE);
		String text2 = "GAME";
		int width2 = metrics.stringWidth(text2);
		int x2 = (WINDOW_WIDTH - width2) / 2;
		int y2 = scale(320);
		g.drawString(text2, x2, y2);
		//testers title
		g.setColor(Color.ORANGE);
		String text3 = "BETA TESTERS";
		int width3 = metrics.stringWidth(text3);
		int x3 = (WINDOW_WIDTH - width3) / 2;
		int y3 = scale(470);
		g.drawString(text3, x3, y3);
		
		//music
		g.setColor(Color.WHITE);
		g.setFont(FONT_14);
		String music1 = "Covers by 8-bit universe:";
		g.drawString(music1, scale(10), y+scale(19));
		String music4 = "Covers by 8-bit Misfits:";
		g.drawString(music4, scale(10), y+scale(65));
		String music6 = "Sounds by Pixabays.";
		g.drawString(music6, scale(10), y+scale(110));
		
		g.setColor(Color.GRAY);
		g.setFont(FONT_12);
		String music2 = "What is love, Sweet Dreams, Psycho Killer, eye of the tiger,";
		g.drawString(music2, scale(10), y+scale(33));
		String music3 = "should i stay or should i go";
		g.drawString(music3, scale(10), y+scale(46));
		String music5 = "tainted love, you spin me round, master of puppets";
		g.drawString(music5, scale(10), y+scale(79));
		
		
		//Game
		g.setColor(Color.WHITE);
		g.setFont(FONT_14);
		
		String game1 = "Original game by taito.";
		FontMetrics metrics2 = g.getFontMetrics(g.getFont());
		int width4 = metrics2.stringWidth(game1);
		int x4 = (WINDOW_WIDTH - width4) / 2;
		g.drawString(game1, x4, y2+scale(20));
		String game2 = "Font:";
		g.drawString(game2, scale(10), y2+scale(40));
		g.setColor(Color.GRAY);
		g.setFont(FONT_12);
		String game3 = "Arcade by Yuji Adachi";
		g.drawString(game3, scale(10), y2+scale(56));
		
		//Testers
		g.setColor(Color.WHITE);
		g.setFont(FONT_14);
		String test1 = "Blackbarry, gabrenys, bianchina94, edoardo, skina ";
		g.drawString(test1, scale(10), y3+scale(25));
		
		
		//Author
		g.setFont(FONT_16);
		String author = "made by alessandro ciccarone";
		FontMetrics metrics3 = g.getFontMetrics(g.getFont());
		int width5 = metrics3.stringWidth(author);
		int x5 = (WINDOW_WIDTH - width5) / 2;
		g.drawString(author, x5, scale(610));
		
}

//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "credits";
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
				easterEgg.reset();
		}
	}
	
//////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		for(Button b : buttons)
			b.resetBools();
		easterEgg.resetBools();
		
		if (backButton.isIn(e)) 
			backButton.setSelected(true);
		
		if (easterEgg.isIn(e)) 
			easterEgg.setSelected(true);
	}
	
//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		if(backButton.isIn(e)) 
			if(backButton.isPressed()) {
				backButton.applyContext();
				easterEgg.reset();
			}
		
		if (easterEgg.isIn(e)) 
			if(easterEgg.isPressed()) {
				easterEgg.click();
			}
	}
	
//////////////////////////////////////////

	public void mousePressed(MouseEvent e) {
		if (backButton.isIn(e)) 
			backButton.setPressed(true);
		
		if (easterEgg.isIn(e)) 
			easterEgg.setPressed(true);
	}

////////////////////////////////////////////////////////////////////////////////////
}
