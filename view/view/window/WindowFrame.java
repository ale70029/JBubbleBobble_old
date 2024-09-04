package view.window;

import javax.swing.JFrame;

@SuppressWarnings("serial")

/**
 * Main JFrame used to store GamePanel.
 */
public class WindowFrame extends JFrame {
	

	/**
	 * Creates the main JFrame to display Game Panel.
	 * Can't be resized, is set to open in the middle of the screen and kill application on close.
	 * @param gamePanel JPanel to be added
	 */
	public WindowFrame(GamePanel gamePanel){
		setTitle("JBubbleBobble");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(gamePanel);
		
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
