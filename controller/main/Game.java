package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import controller.contextControllers.*;
import controller.controllers.*;
import controller.inputManagers.*;
import controller.filesManagers.AudioManager;
import model.game.Context;
import model.player.Player;
import view.window.GamePanel;
import view.window.HeadBar;
import view.window.WindowFrame;


/**
 * Class to control the whole game. Initializes all needed things.
 * Has a thread that updates needed controllers based on given UPS (update per seconds)
 * and draws current context based on FPS (frame per second).
 * An UPS value higher than FPS makes sure that the game run smoothly, but higher UPS result in higher speed in game.
 * Optimal UPS value is 180, 200 if you like it fast, over 200 it just makes extremely difficult to play.
 * FPS optimal value is around 60, higher than that it doesn't improve smoothness but it just makes the program heavier.
 */
public class Game implements Runnable{
	
	//gamestates
	private Playing playing;
	private Menu menu;
	private Load load;
	private Delete delete;
	private NewGame newGame;
	private Pause pause;
	private GameOver gameOver;
	private NextLevel nextLevel;
	private Tutorial tutorial;
	private Win win;
	private Statistics statistics;
	private Options options;
	private Credits credits;
	private EasterEgg easterEgg;
	//inputs
	private KeyboardInputs keyboardInputs;
	private MouseInputs mouseInputs;

	//View
	private GamePanel gamePanel;
	private WindowFrame windowFrame;
	
	//GameLoop
	private Thread gameThread;
	private static final int FPS = 60, UPS = 180;
	
	//Scale
	public static double SCALE;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Game instance;
	
	/**
	 * Singleton.
	 */
	public static Game getInstance() {
		if(instance==null)
			instance = new Game();
		return instance;
	}
	
	/**
	 * Initializes contexts controllers, WindowFrame, controllers and observers, then starts thread. 
	 */
	private Game() {
		
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCALE = (double)1536/(double)screenSize.width; // 1536 screen width used for development
	
		loadContextControllers();
		loadFrame();
		loadControllers();
		addObservers();
		
		gameThread = new Thread(this);
		gameThread.start();

	}

////////////////////////////////////////////////////////////////////////////////////
//INIT

	@SuppressWarnings("deprecation")
	private void addObservers() {
		Player.getInstance().addObserver(HeadBar.getInstance());
		Playing.getInstance().addObserver(HeadBar.getInstance());
		LevelController.getInstance().addObserver(HeadBar.getInstance());
		
		Player.getInstance().addObserver(ProfilesController.getInstance());
		LevelController.getInstance().addObserver(ProfilesController.getInstance());
	}

//////////////////////////////////////////
	
	private void loadControllers() {
		LevelController.getInstance();
		EnemiesController.getInstance();
		PlayerController.getInstance();
	}

//////////////////////////////////////////
	
	private void loadFrame() {
		gamePanel = new GamePanel(this);
		windowFrame = new WindowFrame(gamePanel);
		//inputs focus
		windowFrame.setFocusable(false);
		keyboardInputs = new KeyboardInputs();
		mouseInputs = new MouseInputs();
		gamePanel.addKeyListener(keyboardInputs);
		gamePanel.addMouseListener(mouseInputs);
		gamePanel.addMouseMotionListener(mouseInputs);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
	}
	
//////////////////////////////////////////
	
	private void loadContextControllers() {
		menu = Menu.getInstance();
		load = Load.getInstance();
		newGame = NewGame.getInstance();
		playing = Playing.getInstance();
		pause = Pause.getInstance();
		gameOver = GameOver.getInstance();
		nextLevel = NextLevel.getInstance();
		delete = Delete.getInstance();
		tutorial =  Tutorial.getInstance();
		win = Win.getInstance();
		statistics = Statistics.getInstance();
		options = Options.getInstance();
		credits = Credits.getInstance();
		easterEgg = EasterEgg.getInstance();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//THREAD
	
	/**
	 * Runs a game loop based on frames per second and updates per seconds.
	 * When is time to render, gamePanel is repainted and current context draw method is called by gamePanel with this.render().
	 * When is time to update, current context update method is called by this.update().
	 * 
	 */

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS;
		double timePerUpdate = 1000000000.0 / UPS;
		long previousTime = System.nanoTime();
		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			//time to update
			if (deltaU >= 1) {
				update();
				deltaU--;
			}
			
			//time to render
			if (deltaF >= 1) {		
				gamePanel.repaint();
				deltaF--;
			}
		}
	}

////////////////////////////////////////////////////////////////////////////////////
//CONTEXT CONTROLL
	
	/**
	 * Updates current context and AudioManager.
	 */
	public void update() {
		
		AudioManager.getInstance().update();
		
		switch (Context.current) {
			case MENU:       menu.update();       break;
			case LOAD:       load.update();       break;
			case DELETE:     delete.update();     break;
			case PLAYING:    playing.update();    break;
			case STATISTICS: statistics.update(); break;
			case OPTIONS:    options.update();    break;
			case CREDITS:    credits.update();	  break;
			case NEW_GAME:   newGame.update();    break;
			case GAME_OVER:  gameOver.update();   break;
			case NEXT_LEVEL: nextLevel.update();  break;
			case PAUSE:      pause.update();	  break;
			case TUTORIAL:   tutorial.update();	  break;
			case WIN:        win.update();        break;
			case EASTER_EGG: easterEgg.update();  break;
			case QUIT: 		 System.exit(0);
			default: break;
		}
	}
	
//////////////////////////////////////////
	
	/**
	 * Draws current context.
	 * @param g
	 */
	public void draw(Graphics g) {
		switch (Context.current) {
			case MENU:       menu.draw(g);       break;
			case LOAD:       load.draw(g);       break;
			case STATISTICS: statistics.draw(g); break;
			case DELETE:     delete.draw(g);     break;
			case PLAYING:    playing.draw(g);    break;
			case CREDITS:    credits.draw(g);    break;
			case NEW_GAME:   newGame.draw(g);    break;
			case GAME_OVER:  gameOver.draw(g);   break;
			case NEXT_LEVEL: nextLevel.draw(g);  break;
			case OPTIONS:    options.draw(g);    break;
			case PAUSE:      pause.draw(g);      break;
			case TUTORIAL:   tutorial.draw(g);   break;
			case WIN:        win.draw(g);        break;
			case EASTER_EGG: easterEgg.draw(g);  break;
			default: break;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//SCALE
	
	public static int scale(int value) {
		if(SCALE==0) SCALE=0.1;
		return (int) (value/SCALE);
	}
	
//////////////////////////////////////////
	
	public static int unScale(int value) {
		return (int) (value*SCALE);
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
