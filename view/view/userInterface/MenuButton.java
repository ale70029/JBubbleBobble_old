package view.userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.contextControllers.*;
import controller.filesManagers.FileManager;
import model.game.Context;

import static controller.contextControllers.GenericContext.WINDOW_WIDTH;
import static controller.filesManagers.FileManager.MENU_BUTTON_HEIGHT;
import static controller.filesManagers.FileManager.MENU_BUTTON_WIDTH;

/**
 * Class for Menu Buttons.
 * They apply a specific Context when pressed.
 */
public class MenuButton extends Button{
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Index to put button in the middle of the panel.
	 */
	public static final int CENTER;
	
	private static BufferedImage[] buttonImages;
	private static Font font;
	private String text;
	private Context context;
	
	static {
		CENTER = (WINDOW_WIDTH-scale(MENU_BUTTON_WIDTH))/2;
		buttonImages = FileManager.MENU_BUTTON;
		font = new Font("Arcade Normal", Font.TRUETYPE_FONT,scale(18));
	}
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	/**
	 * Creates a new Menu Button with given parameters.
	 * @param menuIndex index of buttons array where the button will be stored.
	 * @param x width position
	 * @param y height position
	 * @param text to show on button
	 * @param context Context to apply when pressed 
	 */
	public MenuButton(int menuIndex, int x, int y,String text, Context state) {
		super(menuIndex,x,y,MENU_BUTTON_WIDTH,MENU_BUTTON_HEIGHT);
		this.text = text;
		this.context = state;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws button based on status Index and draws button text
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(buttonImages[statusIndex], x, y, width, height, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int textW = metrics.stringWidth(text);
		int textH = metrics.getHeight();
		int textX = x + (width - textW) / 2;
        int textY = y + (height - textH) ;
		g.drawString(text, textX, textY);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES 
	
	/**
	 * Changes current Context and sets relative Context Controller to update (needed for profiles updating and default selected button)
	 */
	public void applyContext() {
		Context.current = context;
		switch(context) {
			case CREDITS   : Credits.getInstance().setToUpdate(true);    break;
			case DELETE    : Delete.getInstance().setToUpdate(true);     break;
			case GAME_OVER : GameOver.getInstance().setToUpdate(true);   break;
			case LOAD      : Load.getInstance().setToUpdate(true);       break;
			case MENU      : Menu.getInstance().setToUpdate(true);       break;
			case NEW_GAME  : NewGame.getInstance().setToUpdate(true);    break;
			case NEXT_LEVEL: NextLevel.getInstance().setToUpdate(true);  break;
			case OPTIONS   : Options.getInstance().setToUpdate(true);    break;
			case PAUSE     : Pause.getInstance().setToUpdate(true);      break;
			case PLAYING   : break;
			case QUIT      : break;
			case STATISTICS: Statistics.getInstance().setToUpdate(true); break;
			case TUTORIAL  : Tutorial.getInstance().setToUpdate(true);   break;
			case WIN       : Win.getInstance().setToUpdate(true);        break;
			default:break;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
