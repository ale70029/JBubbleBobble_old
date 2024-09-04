package controller.contextControllers;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Observable;

import controller.controllers.BubblesController;
import controller.controllers.CollectablesController;
import controller.controllers.EnemiesController;
import controller.controllers.LevelController;
import controller.controllers.PlayerController;
import model.collectables.CollectablesContainer;
import model.enemies.EnemiesContainer;
import model.game.Context;
import model.player.Player;
import view.userInterface.HiddenButton;
import view.window.HeadBar;


/**
 * Class to manage Playing context.
 * Updates game logic (player,enemies,bubbles,collectables)
 * 
 * Observed by HeadBar to draw difficulty.
 */
@SuppressWarnings("deprecation")

public class Playing extends Observable{
	public static final int EASY=0,NORMAL=1,HARD=2;
	private int difficulty;
	private HiddenButton hiddenPauseButton,hiddenWinButton,hiddenNextLevelButton;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	private static Playing instance;

	public static Playing getInstance() {
		if(instance==null)
			instance = new Playing();
		return instance;
	}
	
	private Playing() {
		difficulty = NORMAL;
		hiddenPauseButton = new HiddenButton(Context.PAUSE);
		hiddenWinButton = new HiddenButton(Context.WIN);
		hiddenNextLevelButton  = new HiddenButton(Context.NEXT_LEVEL);
	}
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates player, bubbles, enemies, collectables.
	 * If there are no more enemies and collectables, if there is another level goes to Next Level context controller,
	 * else goes to Win context controller.
	 */
	public void update() {
		
		PlayerController.getInstance().update();
		BubblesController.getInstance().update();
		EnemiesController.getInstance().update();
		CollectablesController.getInstance().updateCollectables();
		
		if(EnemiesContainer.getInstance().getEnemies().isEmpty())
			if(CollectablesContainer.getInstance().getCollectables().isEmpty()) {
				int nextLevel = LevelController.getInstance().getCurrentLevel().getLevelNumber()+1;
				if(nextLevel>=LevelController.getInstance().getLevels().length)
					hiddenWinButton.applyContext();
				else{
					LevelController.getInstance().nextLevel();
					hiddenNextLevelButton.applyContext();
				}
			}
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws level, player, bubbles, enemies, collectables and HeadBar.
	 * @param g Graphics engine.
	 */
	public void draw(Graphics g) {
		LevelController.getInstance().draw(g);
		PlayerController.getInstance().draw(g);
		BubblesController.getInstance().draw(g);
		EnemiesController.getInstance().draw(g);
		CollectablesController.getInstance().draw(g);
		HeadBar.getInstance().draw(g);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INPUT
	
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:  Player.getInstance().setLeft(true);    break;
		case KeyEvent.VK_RIGHT: Player.getInstance().setRight(true);   break;
		case KeyEvent.VK_UP:    Player.getInstance().setJumping(true); break;
		case KeyEvent.VK_SPACE:
			if(Player.getInstance().isShootingAllowed()) {  //to prevent infinite shooting
				Player.getInstance().setShootingAllowed(false);
				Player.getInstance().setAttacking(true);
				Player.getInstance().setShooting(true);
			}
			break;
		}
	}
//////////////////////////////////////////
	
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:   Player.getInstance().setLeft(false);  break;
		case KeyEvent.VK_RIGHT:  Player.getInstance().setRight(false); break;
		case KeyEvent.VK_ESCAPE: hiddenPauseButton.applyContext();   break;
		}
	}
////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets game difficulty, apply relative Player multiplier and notify observers.
	 */
	public void setDifficulty() {
		if(difficulty==HARD) difficulty=EASY;
		else ++difficulty;
		
		int multiplier = switch(difficulty) {
			case EASY ->1;
			case NORMAL ->2;
			case HARD ->3;
			default ->1;
		};
		Player.getInstance().setMultiplier(multiplier);
		setChanged();
		notifyObservers(this);
	}

	public int getDifficulty() {
		return difficulty;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	//TODO freeze game if focus lost
	
//	public void windowFocusLost() {
//		Gamestate.state=Gamestate.PAUSE;
//	}

////////////////////////////////////////////////////////////////////////////////////

}
