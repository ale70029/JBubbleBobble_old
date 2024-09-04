package view.userInterface;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import controller.filesManagers.AudioManager;
import main.Game;

import static controller.contextControllers.GenericContext.FONT_24;
import static controller.contextControllers.GenericContext.WINDOW_WIDTH;

/**
 * A music player that shows current track name, buttons to listen to previous or next song, and sound controls.
 */
public class MusicPlayer {
////////////////////////////////////////////////////////////////////////////////////
	private ActionButton nextButton,prevButton,musicButton,sfxButton;
	private ActionButton[] buttons;
	private Color color;
////////////////////////////////////////////////////////////////////////////////////
//NEW
	
	/**
	 * Creates a new Music player in y height with color background color. The player will automatically be centered.
	 * @param y height position
	 * @param color background
	 */
	public MusicPlayer (int y, Color color) {
		this.color = color;
		
		prevButton = new ActionButton(0,MenuButton.CENTER-Game.scale(2),y+30,ActionButton.Action.PREVIOUS);
		prevButton.setWidth(Game.unScale(prevButton.getWidth())/2);
		
		nextButton = new ActionButton(1,Game.scale(370),y+30,ActionButton.Action.NEXT);
		nextButton.setWidth(Game.unScale(nextButton.getWidth())/2);
		
		musicButton = new ActionButton(2,MenuButton.CENTER-Game.scale(47),y+80,ActionButton.Action.MUSIC);
		musicButton.setWidth(Game.unScale(musicButton.getWidth())-40);
		
		sfxButton = new ActionButton(3,MenuButton.CENTER+Game.scale(86),y+80,ActionButton.Action.SOUND);
		sfxButton.setWidth(Game.unScale(sfxButton.getWidth())-40);
		
		buttons = new ActionButton[4];
		buttons[0] = prevButton;
		buttons[1] = nextButton;
		buttons[2] = musicButton;
		buttons[3] = sfxButton;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws the music player.
	 * @param g Graphic engine
	 */
	public void draw(Graphics g) {
		
		g.setFont(FONT_24);
		String text = AudioManager.getInstance().getSongName();
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = prevButton.getY()-Game.scale(10);
		
		//panel
		if(color!=null) {
			int yUp = y-Game.scale(30);
			int yDown = sfxButton.getY()+sfxButton.getHeight()+Game.scale(10);
			int xLeft = x-Game.scale(10);
			if(xLeft>prevButton.getX()-Game.scale(8)) xLeft=prevButton.getX()-Game.scale(8);
			int xRight = x+width+Game.scale(10);
			int musicWidth = xRight-xLeft;
			if(musicWidth<musicButton.getWidth()) musicWidth = musicButton.getWidth();
			int panelHeight = yDown-yUp;
		
			g.setColor(color);
			g.fillRect(xLeft, yUp, musicWidth, panelHeight);
		}
		
		
		g.setColor(Color.WHITE);
		g.drawString(text, x, y);
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	public ActionButton[] getButtons() {
		return buttons;
	}

////////////////////////////////////////////////////////////////////////////////////
}
