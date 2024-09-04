package model.player;

import static model.game.Level.TILE_SIZE;

import model.enemies.Enemy;
import model.game.Entity;

/**
 * Class for bubbles.
 * Bubble can be normal, or power (gives an item when popped)
 */
public class Bubble extends Entity{
//////////////////////////////////////////
	/**
	 * Bubbles kind indexes.
	 */
	public static final int BUBBLE_NORMAL = 0,BUBBLE_POWER = 1;
	/**
	 * Bubble stats.
	 */
	public static final int BUBBLE_DIMENSION = 64,MAX_BUBBLE_DISTANCE = 400, POP_TIME = 3000,BUBBLE_DEFAULT_SPEED = 3;
	
	private int kind,currentDistance;
	private boolean toRemove;
////////////////////////////////////////////////////////////////////////////////////
//NEW
	
	/**
	 * Creates a new bubble with default dimension and speed.
	 * @param x width position
	 * @param y height positioon
	 * @param direction movement direction
	 * @param kind type of bubble
	 */
	public Bubble(int x, int y,int direction,int kind) {
		super();
		this.x=x;
		this.y=y;
		this.size=BUBBLE_DIMENSION;
		this.speed=BUBBLE_DEFAULT_SPEED;
		this.moving = true;
		this.lastDirection=direction;	
		this.kind=kind;
	}

////////////////////////////////////////////////////////////////////////////////////
//COLLISIONS
	
	/**
	 * Detects enemy collision. If detected, sets hitten enemy in bubble time.
	 * @param enemy to check collision with
	 * @return true if detected, else false
	 */
	public boolean enemyCollision(Enemy enemy) {
		int diff = BUBBLE_DIMENSION/2;
		
		if((x+diff>=enemy.getX() && x+diff<= enemy.getX()+enemy.getSize() && y+diff>=enemy.getY() && y+diff<= enemy.getY()+enemy.getSize())
		 ||(x+diff>=enemy.getX() && x+diff<=enemy.getX()+enemy.getSize() && y+diff>=enemy.getY() && y+diff<= enemy.getY()+enemy.getSize())) {
			enemy.setInBubbleTime(System.currentTimeMillis());
			return true; 
		}
		
		return false;
	}
	
//////////////////////////////////////////	
	
	/**
	 * Detects if bubble is against left or right level border.
	 */
	public boolean frameCollision(int x) {
		if(x<=TILE_SIZE || x>=20*TILE_SIZE) return true;
		return false;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//MOVEMENT
	
	/**
	 * Moves bubbles in space depending on direction, action and collisions..
	 */
	public void controlBubble() {
		//horizzonatl movement
		if(currentDistance >= MAX_BUBBLE_DISTANCE) {
			setMoving(false);
			setJumping(true);
		}
		
		if(moving) {
			int startX = x;
			//bubble is not colliding with side walls so it moves
			if(!frameCollision(startX)) {
				setX( lastDirection==0 ? (int)(startX + speed) : (int)(startX - speed));
				setCurrentDistance((int)(currentDistance+speed));
			}
			//bubble is colliding with side walls so it goes up
			else {
				setMoving(false);
				setJumping(true);
			}
		}
		
		//vertical movement
		if(jumping) {
			//stuck on roof
			if(roofCollision()) {
				setMoving(false);
				setJumping(false);
			}
			//rising
			else setY(y - jumpSpeed);
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET

	
	public int getCurrentDistance() {return currentDistance;}
	public void setCurrentDistance(int currentDistance) {this.currentDistance = currentDistance;}
	
//////////////////////////////////////////

	public boolean isToRemove() {return toRemove;}
	public void setToRemove(boolean toRemove) {this.toRemove = toRemove;}
	
//////////////////////////////////////////

	public int getKind() {return kind;}

////////////////////////////////////////////////////////////////////////////////////
}
