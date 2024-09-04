package model.collectables;

import static model.game.Level.TILE_SIZE;

import model.player.Player;
/**
 * Superclass for Collectables, such as Items and Powers.
 */
public class Collectable {
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Size occupied by an item in width and height
	 */
	public static final int COLLECTABLE_SIZE = 48;
	private final int MIDDLE = COLLECTABLE_SIZE/2;

	protected int x,y;
	protected int kind,points;
	protected boolean taken,falling;
	protected Player player;
////////////////////////////////////////////////////////////////////////////////////
//NEW
	/**
	 * Constructor for a new collectable.
	 * @param x width index;
	 * @param y height index;
	 * @param kind kind of collectable to create, further information in Item and Power classes.
	 */
	public Collectable(int x, int y,int kind) {
		this.x=x;
		this.y=y;
		this.kind = kind;
		this.player = Player.getInstance();
	}
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Flag collectable as taken when player walks on it, and let collectables fall on the nearest ground.
	 * If collectable is in a level with a boss, special behavior is applied because such levels have different pattern that would
	 * impede collectables to fall.
	 */
	public void updateStatus() {
		if(playerCollision()) 
			setTaken(true);
		
		if(falling) 
			if (fallCollision()&&player.getCurrentLevel().isBossFight()) {
				if(y <= TILE_SIZE*6) {
					y=TILE_SIZE*6+20;
					return;
				}
			}
		
			if(fallCollision()) 
				setFalling(false);	
	}
	
//////////////////////////////////////////
	
	/**
	 * Checks if there is ground in collectable fall trajectory.
	 * If there is ground, return true, else update collectable y index and return false.
	 * 
	 */
	private boolean fallCollision() {
		int[][] tiles = player.getCurrentLevel().getLevelData();
		
		int yCheck = (y + COLLECTABLE_SIZE) / TILE_SIZE;
		int xCheck = (x+COLLECTABLE_SIZE/2)/TILE_SIZE;
		
		if(tiles[yCheck][xCheck]!=0) return true;
		else {
			y+=2;
			return false;
		}
	}
	
//////////////////////////////////////////
	
	/**
	 * Checks if collectable collide with player.
	 * If it does return true, else return false.
	 * 
	 */
	public boolean playerCollision() {
		if( x+MIDDLE>=player.getX() && x+MIDDLE<=player.getX()+player.getSize()
		    && y+MIDDLE>=player.getY()&& y+MIDDLE<= player.getY()+player.getSize())return true;
		
		else return false;
		

	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET


	public int getX() {return x;}
	public int getY() {return y;}
	
//////////////////////////////////////////
	
	public int getPoints() {return points;}
	
//////////////////////////////////////////
	
	public boolean isTaken() {return taken;}
	public void setTaken(boolean taken) {this.taken = taken;}
	
//////////////////////////////////////////
	
	public Player getPlayer() {return player;}
	
//////////////////////////////////////////
	
	public int getKind() {return kind;}
	
//////////////////////////////////////////
	
	public void setFalling(boolean falling) {this.falling = falling;}
	
////////////////////////////////////////////////////////////////////////////////////
}
