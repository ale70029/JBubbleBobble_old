package controller.controllers;

import static model.game.Level.TILE_SIZE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;

import controller.filesManagers.AudioManager;
import controller.filesManagers.FileManager;
import main.Game;
import model.collectables.CollectablesContainer;
import model.enemies.EnemiesContainer;
import model.game.Level;
import model.player.Player;

@SuppressWarnings("deprecation")
/**
 * Class to manage levels (update, draw).
 * Implementes logic for next level, restart level and restart game.
 * Observed by HeadBar to draw current level number and by ProfilesController to update current profile currentLevel.
 */
public class LevelController extends Observable{
////////////////////////////////////////////////////////////////////////////////////
	private Level currentLevel;
	private Level[]levels;
	private BufferedImage[] tiles;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static LevelController instance;
	
	/**
	 * Singleton.
	 */
	public static LevelController getInstance() {
		if(instance==null)
			instance = new LevelController();
		return instance;
	}
	
	private LevelController() {
		levels = FileManager.LEVELS;
		tiles = FileManager.TILES;
		currentLevel = levels[1];
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Reads level data and draw the right tile for every place in level data.
	 * @param g graphic engine
	 * @param level current level
	 */
	public void draw(Graphics g) {
        int[][] levelData = currentLevel.getLevelData();
        
        for (int y = 2; y < levelData.length; y++) {
            for (int x = 0; x < levelData[y].length; x++) {
            	int index = levelData[y][x];
                g.drawImage(tiles[index], Game.scale(x*TILE_SIZE), Game.scale(y*TILE_SIZE), Game.scale(TILE_SIZE), Game.scale(TILE_SIZE), null);
            }
        }
	}


////////////////////////////////////////////////////////////////////////////////////
//UTILITIES 
	
	/**
	 * Sets current level as levels[index], syncs, notifies observers.
	 * @param index level to apply
	 */
	public void applyLevel(int index) {
		
		currentLevel = levels[index];
		currentLevel.setStart(true);
		sync();
		setChanged();
		notifyObservers(currentLevel);
	}
	
//////////////////////////////////////////
	
	/**
	 * Applies current level again.
	 */
	public void restartLevel() {
		int levelNum = currentLevel.getLevelNumber();
		applyLevel(levelNum);
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Applies next level, plays next level sound and save player last score.
	 */
	public void nextLevel() {
		Player.getInstance().setLastScore(Player.getInstance().getScore());
		int nextLevel = currentLevel.getLevelNumber()+1;
		applyLevel(nextLevel);
		AudioManager.getInstance().playSound(AudioManager.NEXT_LEVEL_SOUND);
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Applies first level
	 */
	public void restartGame() {
		Player.getInstance().setLastScore(Player.getInstance().getScore());
		applyLevel(1);
		ProfilesController.getInstance().saveCurrentProfile();
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Makes sure that current level is correctly setted in every situation where needed, that game is saved and other stuff.
	 */
	private void sync() {
		CollectablesContainer.getInstance().getCollectables().clear();
		EnemiesContainer.getInstance().setCurrentLevel(currentLevel);
		PlayerController.getInstance().getPlayer().setCurrentLevel(currentLevel);
		ProfilesController.getInstance().loadProfiles();

	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - "SET"

	
	public Level[] getLevels() {return levels;}

//////////////////////////////////////////
	
	public Level getCurrentLevel() {return currentLevel;}

////////////////////////////////////////////////////////////////////////////////////
}
