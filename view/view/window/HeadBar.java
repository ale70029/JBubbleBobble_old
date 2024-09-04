package view.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import controller.contextControllers.Playing;
import controller.controllers.LevelController;
import controller.filesManagers.FileManager;
import main.Game;
import model.game.Level;
import model.player.Player;

@SuppressWarnings("deprecation")
/**
 * Class to manage Head Bar above playing.
 * Shows current level, difficulty, score, active powers and life points.
 * Observes Level to get level number, player to get score, active powers and life points, and Playing to get difficulty.
 */
public class HeadBar implements Observer{
////////////////////////////////////////////////////////////////////////////////////
	private BufferedImage[] powers;
	private Font font,font2;
	private int score,hp,level,mode;
	private boolean shield,fast;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static HeadBar instance;
	
	/**
	 * Singleton
	 */
	public static HeadBar getInstance() {
		if(instance==null)
			instance = new HeadBar();
		return instance;
	}

	private HeadBar() {
		powers = FileManager.POWERS;
		font = new Font("Arcade Normal", Font.TRUETYPE_FONT, Game.scale(20));
		font2 = new Font("Arcade Normal", Font.TRUETYPE_FONT, Game.scale(12));
		mode = Playing.NORMAL;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Observe Level to get level number.
	 * Observe Player to get score, active powers and life points.
	 * Observe Playing to get difficulty.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o.getClass()==LevelController.class) {
			Level lv = (Level)arg;
			level = lv.getLevelNumber();
		}
		
		else if(o.getClass()==Player.class) {
			Player pl =(Player)arg;
			score = pl.getScore();
			hp= pl.getHealth();
			shield = pl.isShield();
			fast = pl.isFast();
		}
		
		else if(o.getClass()==Playing.class) {
			mode = Playing.getInstance().getDifficulty();
		}
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws headbar informations.
	 * @param g Graphics engine.
	 */
	public void draw(Graphics g) {
		drawLevel(g);
		drawScore(g);
		drawPowers(g);
		drawHP(g);
	}
	
//////////////////////////////////////////
	
	private void drawLevel(Graphics g) {
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Level", Game.scale(10), Game.scale(40));
		
		g.setColor(Color.BLUE);
		g.drawString(Integer.toString(level), Game.scale(110), Game.scale(40));
		
		g.setFont(font2);
		String text="";
		switch(mode) {
			case Playing.EASY : g.setColor(Color.GREEN); text= "easy"; break;
			case Playing.NORMAL : g.setColor(Color.BLUE); text= "normal"; break;
			case Playing.HARD : g.setColor(Color.RED); text= "hard"; break;
			default : break;
		};
		g.drawString(text,Game.scale(12),Game.scale(58));	
	}
	
//////////////////////////////////////////

	private void drawScore(Graphics g) {
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Score", Game.scale(180), Game.scale(40));
		g.setColor(Color.YELLOW);
		g.drawString(Integer.toString(score),Game.scale(285), Game.scale(40));
	}
	
//////////////////////////////////////////
	
	private void drawPowers(Graphics g) {
		if(shield) 
			g.drawImage(powers[0],Game.scale(480),Game.scale(20),Game.scale(25),Game.scale(25),null);
		
		if(fast)
			g.drawImage(powers[2],Game.scale(450),Game.scale(20),Game.scale(25),Game.scale(25),null);
	}
	
//////////////////////////////////////////

	private void drawHP(Graphics g) {
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("HP", Game.scale(535), Game.scale(40));
		for(int i=0;i<hp;i++) {
			g.drawImage(powers[1],Game.scale(580+i*30),Game.scale(18),Game.scale(25),Game.scale(25),null);
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
