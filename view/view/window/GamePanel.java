package view.window;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import main.Game;
import static controller.contextControllers.GenericContext.WINDOW_HEIGHT;
import static controller.contextControllers.GenericContext.WINDOW_WIDTH;

@SuppressWarnings("serial")
/**
 * Creates the only panel that will be drawd differently based on current Context.
 */
public class GamePanel extends JPanel {
////////////////////////////////////////////////////////////////////////////////////
	private Game game;
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	/**
	 * New panel with Generic State default width and height.
	 * @param game gameEngine to call specifics draw methods.
	 */
	public GamePanel(Game game) {
		
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		setBackground(Color.BLACK);
		setVisible(true);
		this.game = game;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW 
	


	/**
	 * Calls game.render() that will call current Context draw method.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		game.draw(g);
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
