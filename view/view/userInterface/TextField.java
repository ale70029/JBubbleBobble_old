package view.userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import main.Game;

/**
 * Used to store and draw typed nickname in New Game Context.
 * Nicnkame can have up to MAX_CHARS characters and can only store letters and number for semplicity reasons.
 */
public class TextField {
////////////////////////////////////////////////////////////////////////////////////
	private static final int MAX_CHARS = 10;
	
	private String text;
	private Font font;

	private int x,y;
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	/**
	 * Create a new text field with given parameters.
	 * @param x width position
	 * @param y height position

	 */
	public TextField(int x,int y) {
		clear();
		this.x=Game.scale(x);
		this.y=Game.scale(y);
		this.font = new Font("Arcade Normal", Font.TRUETYPE_FONT, Game.scale(26));
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW 
	
	/**
	 * Draws text stored text.
	 * @param g
	 */
	public void draw(Graphics g) {
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y-Game.scale(5));
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Capture typed key to store text.
	 * Only characters from A to Z and numbers from 0 to 9 are accepted.
	 * If backspace is typed last character is removed.
	 * @param e key typed
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_BACK_SPACE) {
			if(!text.equals(""))
				setText(text.substring(0, text.length()-1));
		}
		else{
			if(!(text.length()>=MAX_CHARS))
				if((key>=65 && key<=90) || (key>=48 && key<=57))//From A to Z or from 0 to 9
					setText(text+=Character.toString((char)key));
		}
			
	}
	
//////////////////////////////////////////

	/**
	 * Resets text to "".
	 */
	public void clear() {text="";}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	
////////////////////////////////////////////////////////////////////////////////////

}
