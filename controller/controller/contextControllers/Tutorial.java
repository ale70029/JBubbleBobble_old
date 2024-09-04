package controller.contextControllers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import controller.filesManagers.FileManager;
import model.collectables.Item;
import model.collectables.Power;
import model.game.Context;
import view.userInterface.Button;
import view.userInterface.MenuButton;

/**
 * Class to manage Tutorial Context.
 * Shows how to play and collectables / enemy points.
 */
public class Tutorial extends GenericContext{
//////////////////////////////////////////
	private BufferedImage[] items,powers,keys,enemies;
	private BufferedImage[][]player;
	private MenuButton playButton;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Tutorial instance;
	
	/**
	 * Singleton.
	 */
	public static Tutorial getInstance() {
		if(instance==null)
			instance = new Tutorial();
		return instance;
	}
	
	private Tutorial() {
		items = FileManager.ITEMS;
		powers = FileManager.POWERS;
		keys = FileManager.KEYBOARD_KEYS;
		player = FileManager.PLAYER_ANIMATIONS;
		enemies = FileManager.ENEMIES;
		
		playButton= new MenuButton(0,MenuButton.CENTER,679,"PLAY",Context.PLAYING);
		buttons = new Button[1];
		buttons[0] = playButton;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates buttons.
	 */
	public void update() {
		if(toUpdate) {
			index = playButton.getMenuIndex();
			selectButton(buttons,index);
			toUpdate = false;
		}
		playButton.update();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background, title, point, keys, instructions and buttons.
	 * @param g
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		drawTitle(g);
		drawPoints(g);
		drawKeys(g);
		drawText(g);
		
		playButton.draw(g);
	}
	
//////////////////////////////////////////
	
	private void drawText(Graphics g) {
		g.setColor(Color.green);
		g.drawLine(scale(20), scale(385), scale(710), scale(385));
		g.drawLine(scale(20), scale(450), scale(710), scale(450));
		g.setFont(FONT_16);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text1 = "shoot bubbles at enemies and touch";
		String text2 = "trapped enemies to kill them";
		int width1 = metrics.stringWidth(text1);
		int x1= (WINDOW_WIDTH - width1) / 2;
		int y1 = scale(405);
		g.drawString(text1,x1,y1);
		int width2 = metrics.stringWidth(text2);
		int x2= (WINDOW_WIDTH - width2) / 2;
		int y2= scale(425);
		g.drawString(text2,x2 ,y2);
		g.setFont(FONT_12);
		metrics = g.getFontMetrics(g.getFont());
		String text3 = "kill all enemies and collect all items to complete level";
		int width3 = metrics.stringWidth(text3);
		int x3= (WINDOW_WIDTH - width3) / 2;
		int y3= scale(440);
		g.drawString(text3,x3 ,y3);
	}
	
//////////////////////////////////////////
	
	private void drawPoints(Graphics g) {
		//items
		for(int i=0; i<Item.MAX_ITEMS;i++) {
			g.drawImage(items[i], scale(30 + (i*70)), scale(150), scale(50), scale(50), null);
			g.setFont(FONT_16);
			g.setColor(Color.WHITE);
			String itemPoints = Integer.toString(i*100 + 100);
			g.drawString(itemPoints,scale(25 + (i*70)), scale(240));
		}
		//powers
		for(int i=0; i<Power.MAX_POWERS;i++) {
			g.drawImage(powers[i], scale(25 + (i*110)), scale(270), scale(50), scale(50), null);
			g.setFont(FONT_16);
			String power="";
			switch(i) {
				case 0:
					power = "SHIELD";
					break;
				case 1:
					power = " 1-up";
					break;
				case 2:
					power = " SPEED";
					break;
				}
			g.drawString(power,scale(15 + (i*100)), scale(355));
			
			g.setFont(FONT_12);
			g.setColor(Color.WHITE);
			String powerPoints="";
			switch(i) {
				case Power.SHIELD: powerPoints = "or 300";  break;
				case Power.LIFE:   powerPoints = "or 1000"; break;
				case Power.FAST:   powerPoints = " or 600"; break;
				}
			g.drawString(powerPoints,scale(20 + (i*100)), scale(375));
		}
		//enemies
		for(int i=0; i<enemies.length;i++) {
			g.drawImage(enemies[i], scale(430 + (i*110)), scale(270), scale(50), scale(50), null);
			g.setFont(FONT_16);
			g.setColor(Color.WHITE);
			g.drawString(""+(200*i + 200), scale(435 + (i*110)), scale(355));
		}	
	}
	
//////////////////////////////////////////
	
	private void drawKeys(Graphics g) {
		g.setFont(FONT_16);
		//left
		g.drawString("left", scale(40), scale(480));
		g.drawImage(player[1][0], scale(20), scale(500), scale(100), scale(100), null);
		g.drawImage(keys[1],scale(45),scale(615),scale(56),scale(56),null);
		//right
		g.drawString("right", scale(180), scale(480));
		g.drawImage(player[0][0], scale(170), scale(500), scale(100), scale(100), null);
		g.drawImage(keys[0],scale(192),scale(615),scale(56),scale(56),null);
		//jump
		g.drawString("jump", scale(330), scale(480));
		g.drawImage(player[4][0], scale(320), scale(500), scale(100), scale(100), null);
		g.drawImage(keys[2],scale(340),scale(615),scale(56),scale(56),null);
		//shoot
		g.drawString("Shoot", scale(478), scale(480));
		g.drawImage(player[6][0],scale(470), scale(500), scale(100), scale(100), null);
		g.drawImage(keys[4],scale(470),scale(615),scale(112),scale(56),null);	
		//pause
		g.drawString("pause", scale(625), scale(480));
		g.drawImage(player[7][1], scale(620), scale(500), scale(100), scale(100), null);
		g.drawImage(keys[3],scale(645),scale(615),scale(56),scale(56),null);
	}
	
//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "TUTORIAL";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(120);
		g.drawString(text, x, y);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INPUT
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE||e.getKeyCode() == KeyEvent.VK_ENTER) {
			playButton.applyContext();
			return;
		}
	}
	
//////////////////////////////////////////
	
	public void mouseMoved(MouseEvent e) {
		playButton.resetBools();
		if (playButton.isIn(e)) 
			playButton.setSelected(true);
	}
	
//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		if(playButton.isIn(e)) 
			if(playButton.isPressed()) 
				playButton.applyContext();
		playButton.resetBools();
	}
	
//////////////////////////////////////////
	
	public void mousePressed(MouseEvent e) {
		if (playButton.isIn(e)) 
			playButton.setPressed(true);
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
