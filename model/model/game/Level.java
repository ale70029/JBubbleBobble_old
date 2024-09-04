package model.game;

import java.util.List;
import java.util.Map;

/**
 * Level class, stores level data in a bidimensional int array,
 * player start position and a map for enemies start position.
 * Boolean "start" is used when level just began, boolean "bossfight" is for levels with bosses.
 */
public class Level{
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Width and height dimension of a single level tile.
	 */
	public static final int TILE_SIZE = 32,LEVEL_ROWS = 24,LEVEL_COLS = 23;
	
	private int[][] levelData;
	private int levelNumber;
	private int startX,startY;
	private Map<Integer, List<Map.Entry<Integer, Integer>>> enemiesMap;
	private boolean start,bossFight;
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	public Level() {this.start=true;}

////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	
	public int[][] getLevelData() {return levelData;}
	public void setLevelData(int[][] levelData) {this.levelData=levelData;}
	
//////////////////////////////////////////
	
	public int getStartX() {return startX;}
	public int getStartY() {return startY;}
	public void setStartPoint(int x,int y) {this.startX=x; this.startY=y;}
	
//////////////////////////////////////////
	
	public Map<Integer, List<Map.Entry<Integer, Integer>>> getEnemiesMap() {return enemiesMap;}
	public void setEnemiesMap(Map<Integer, List<Map.Entry<Integer, Integer>>> enemiesMap) {this.enemiesMap = enemiesMap;}
	
//////////////////////////////////////////

	public int getLevelNumber() {return levelNumber;}
	public void setLevelNumber(int levelNumber) {this.levelNumber = levelNumber;}
	
//////////////////////////////////////////
	
	public boolean isStart() {return start;}
	public void setStart(boolean start) {this.start = start;}
	
//////////////////////////////////////////

	public boolean isBossFight() {return bossFight;}
	public void setBossFight(boolean bossFight) {this.bossFight = bossFight;}
	
////////////////////////////////////////////////////////////////////////////////////	
}
