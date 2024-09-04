package model.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.game.Level;
/**
 * Class to store all enemies in a level and load them.
 * Singleton pattern is adopted.
 */
public class EnemiesContainer {
////////////////////////////////////////////////////////////////////////////////////
	private static final int ZEN = 0,INVADER = 1,MIGHTA = 2,BOSS = 3;

	private List<Enemy> enemies;
	private Level currentLevel;
	public static final int POWER_POSSIBILITY = 20;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static EnemiesContainer instance;
	/**
	 * Singleton
	 */
	public static EnemiesContainer getInstance() {
		if(instance==null)
			instance = new EnemiesContainer();
		return instance;
	}

	private EnemiesContainer() { 
		this.enemies = new ArrayList<Enemy>();
	}

////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Creates enemies based on current level "Enemies Map" and store them in the container "enemies".
	 * Clears enemies list before loading new ones.
	 * Called when a Level is set to the current level field.
	 * See @Level for further informations.
	 */
	public void loadEnemies() {

		enemies.clear();
		for (Map.Entry<Integer, List<Map.Entry<Integer, Integer>>> entry :currentLevel.getEnemiesMap().entrySet()) {
	        Integer kind = entry.getKey();
	        List<Map.Entry<Integer, Integer>> position = entry.getValue();
	        for (Map.Entry<Integer, Integer> xy : position) {
	            Integer x = xy.getKey();
	            Integer y = xy.getValue();
	            	
	            if(kind==ZEN) {
	            	EnemyZen zen = new EnemyZen(x,y);
	            	zen.setCurrentLevel(currentLevel);
	            	if(currentLevel.getLevelNumber()==12) zen.setAlive(false); // used by bonus level
	            	enemies.add(zen);
	            }
	            else if(kind==INVADER) {
					EnemyInvader invader = new EnemyInvader(x,y);
					invader.setCurrentLevel(currentLevel);
	            	enemies.add(invader);  	
				}
	            else if(kind==MIGHTA) {
					EnemyMighta mighta = new EnemyMighta(x,y);
					mighta.setCurrentLevel(currentLevel);
	            	enemies.add(mighta);
				}
	            
	            else if(kind==BOSS) {
	            	EnemyBoss boss = new EnemyBoss(x,y);
	            	boss.setCurrentLevel(currentLevel);
	            	enemies.add(boss);
	            }
	        }
	    }
	}
////////////////////////////////////////////////////////////////////////////////////	
//GET - SET


	public List<Enemy> getEnemies() {return enemies;}
	
//////////////////////////////////////////
	
	/**
	 * Sets current level and updates enemies.
	 * @param currentLevel Level to be set.
	 */
	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
		loadEnemies();
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
