package controller.contextControllers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import controller.filesManagers.AudioManager;
import controller.filesManagers.FileManager;
import model.game.Context;
import view.userInterface.Button;
import view.userInterface.MenuButton;

/**
 * Class to manage Menu Context.
 * Can navigate between load, new game, options,statistics,credits and quit.
 */
public class Menu extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	//background
	private BufferedImage menuDecoration;
    //buttons
	private MenuButton loadButton,newGameButton,optionsButton,statsButton,creditsButton,quitButton;
	private MenuButton[] buttons;
	private boolean firstStart;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Menu instance;

	/**
	 * Singleton.
	 */
	public static Menu getInstance() {
			if(instance==null)
				instance = new Menu();
			return instance;
		}

	private Menu() {
		menuDecoration = FileManager.MENU_DECORATION;
		loadButtons();
		AudioManager.getInstance().startMusic();
		firstStart=true;
		
	}
	
//////////////////////////////////////////
//INIT
	
	private void loadButtons() {
		loadButton = new MenuButton(0,MenuButton.CENTER,266, "load", Context.LOAD);
		newGameButton = new MenuButton(1,MenuButton.CENTER,338, "new game", Context.NEW_GAME); 
		optionsButton = new MenuButton(2,MenuButton.CENTER,410, "options", Context.OPTIONS);
		statsButton = new MenuButton(3,MenuButton.CENTER,482,"stats",Context.STATISTICS);
		creditsButton = new MenuButton(4,MenuButton.CENTER,606, "credits", Context.CREDITS);
		quitButton = new MenuButton(5,MenuButton.CENTER,678, "quit", Context.QUIT);

		buttons = new MenuButton[6];
		buttons[0] = loadButton;
		buttons[1] = newGameButton; 
		buttons[2] = optionsButton;
		buttons[3] = statsButton;
		buttons[4] = creditsButton;
		buttons[5] = quitButton;
		
		index = newGameButton.getMenuIndex();
		selectButton(buttons,index);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates buttons. Automatically select new game when Menu is opened.
	 */
	public void update() {
		if(toUpdate) {
			index = newGameButton.getMenuIndex();
			selectButton(buttons,index);
			toUpdate = false;
		}
		
		for (MenuButton mb : buttons)
			mb.update();
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws background, menu decoration, title and buttons.
	 * @param g
	 */
	public void draw(Graphics g) {
		
		drawBackground(g);
		g.drawImage(menuDecoration, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
		drawTitle(g);

		for (MenuButton mb : buttons)
			mb.draw(g);
	}

//////////////////////////////////////////

	private void drawTitle(Graphics g) {
		//title
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int line1 = metrics.stringWidth("JBUBBLE");
		int line2 = metrics.stringWidth("BOBBLE");
		int x1 = (WINDOW_WIDTH - line1) / 2;
		int y1 = scale(120);
		int x2 = (WINDOW_WIDTH - line2) / 2;
		int y2 = scale(220);
		g.drawString("JBUBBLE", x1, y1);
		g.drawString("BOBBLE", x2,y2);
		//version
		g.setFont(FONT_12);
		metrics = g.getFontMetrics(g.getFont());
		int line3 = metrics.stringWidth(main.JBubbleBobble.VERSION);
		int x3 = (WINDOW_WIDTH - line3) / 2;
		int y3 = scale(755);
		g.drawString(main.JBubbleBobble.VERSION, x3, y3);
	}	

////////////////////////////////////////////////////////////////////////////////////
//INPUTS
	
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (mb.isIn(e)) {
				mb.setPressed(true);
				return;
			}
		}
	}

//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		
		for (MenuButton mb : buttons) {
			if (mb.isIn(e)) {
				if (mb.isPressed()) {
					mb.applyContext();
					return;
				}
			}
		}

	}

//////////////////////////////////////////

	public void mouseMoved(MouseEvent e) {
		
		for(Button b : buttons)
			b.resetBools();

		for (MenuButton mb : buttons) {
			if (mb.isIn(e)) 
				mb.setSelected(true);
		}
		
		//Bug fix - when game is opened mouseMoved is called even if mouse is not moved and
		//that would cancel default button selection
		if(firstStart) {
			index = newGameButton.getMenuIndex();
			selectButton(buttons,index);
			firstStart=false;
		}
	}

//////////////////////////////////////////

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
		
		else if (e.getKeyCode() == KeyEvent.VK_ENTER){
			buttons[index].applyContext();
			return;
		}
	}

////////////////////////////////////////////////////////////////////////////////////
}