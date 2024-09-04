package model.game;

import static model.game.Level.TILE_SIZE;

import java.util.Observable;

@SuppressWarnings("deprecation")

/**
 * General class for moving entities like enemies, bubbles and player.
 */
public class Entity extends Observable{
////////////////////////////////////////////////////////////////////////////////////
	protected static final int ENTITY_DEFAULT_JUMP_SPEED = 2;
	protected static final int ENTITY_DEFAULT_MAX_JUMP = 96;

	//position
	protected int x,y;
	protected int size;
	//movement
	protected boolean left,right;
	protected int lastDirection;
	protected boolean moving,jumping,falling,attacking,shooting,hitted,hurt,inBubble,following;
	protected int action,kind;
	protected boolean alive;
	//speed
	protected int speed;
	protected int jumpSpeed,jumpIndex,jumpMax;
	//animation
	public int aniTick,aniIndex;

	//level
	protected Level currentLevel;
////////////////////////////////////////////////////////////////////////////////////
//NEW

	public Entity() {
		this.jumpSpeed = ENTITY_DEFAULT_JUMP_SPEED;
		this.jumpMax = ENTITY_DEFAULT_MAX_JUMP;
		this.alive=true;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//COLLISIONS
	
	/**
	 * Detects if a collision will happen in value width position (consideting entity y position as height)
	 * @param value width position
	 * @param level currentLevel
	 * @return true if a collision is detected, else false
	 */
	public boolean horizontalCollision(int value,Level level) {
		int[][] tiles = level.getLevelData();
		
		int xCheckL = value/TILE_SIZE;
		int xCheckR = (value + size) / TILE_SIZE;
		int yCheck= (y+size-1)/TILE_SIZE;
		
		if(tiles[yCheck][xCheckL]!=0||tiles[yCheck][xCheckR]!=0) return true;
		return false;
	}

//////////////////////////////////////////
	
	/**
	 * Detecs if a collision happened below the entity, used to fall logic.
	 * @param level current level
	 * @return true if a collision is detected, else false
	 */
	public boolean fallCollision(Level level) {
		int[][] tiles = level.getLevelData();
		
		int yCheck = (y + size) / TILE_SIZE;
		int xCheck = (x+size/2)/TILE_SIZE;
		
		if(tiles[yCheck][xCheck]!=0) return true;
		return false;
	}
	
//////////////////////////////////////////
	
	/**
	 * Detect if entity is colliding with roof.
	 * @return true if its colliding, else false
	 */
	public boolean roofCollision() {
		int checkY = y/TILE_SIZE;
		if(checkY <2) return true;
		return false;
	}
	
//////////////////////////////////////////
	
	/**
	 * Detects if entity is colliding with left or right border of the level
	 * @param x current entity position
	 * @return ture if colliding, else false
	 */
	public boolean frameCollision(int x) {
		if(x<=TILE_SIZE || x+size>=(Level.LEVEL_COLS-1)*TILE_SIZE) return true; 
		return false;
	}
	
////////////////////////////////////////////////////////////////////////////////////
// GET - SET
	
	
	public int getX() {return x;}
	public void setX(int x) {this.x = x;}
	
//////////////////////////////////////////
	
	public int getY() {return y;}
	public void setY(int y) {this.y = y;}
	
//////////////////////////////////////////
	
	public boolean isLeft() {return left;}
	public void setLeft(boolean left) {this.left = left;}
	
//////////////////////////////////////////
	
	public boolean isRight() {return right;}
	public void setRight(boolean right) {this.right = right;}
	
//////////////////////////////////////////
	
	public boolean isMoving() {return moving;}
	public void setMoving(boolean moving) {this.moving = moving;}
	
//////////////////////////////////////////
	
	public boolean isJumping() {return jumping;}
	public void setJumping(boolean jumping) {this.jumping = jumping;}
	
//////////////////////////////////////////
	
	public boolean isFalling() {return falling;}
	public void setFalling(boolean falling) {this.falling = falling;}
	
//////////////////////////////////////////
	
	public boolean isAttacking() {return attacking;}
	public void setAttacking(boolean attacking) {this.attacking = attacking;}
	
//////////////////////////////////////////
	
	public boolean isShooting() {return shooting;}
	public void setShooting(boolean shooting) {this.shooting = shooting;}
	
//////////////////////////////////////////
	
	public boolean isAlive() {return alive;}
	public void setAlive(boolean alive) {this.alive = alive;}
	
//////////////////////////////////////////
	
	public boolean isHitted() {return hitted;}
	public void setHitted(boolean hitted) {this.hitted = hitted;}
	
//////////////////////////////////////////
	
	public int getAction() {return action;}
	public void setAction(int action) {this.action = action;}
	
//////////////////////////////////////////
	
	public void setSpeed(int speed) {this.speed = speed;}
	
//////////////////////////////////////////

	public int getSize() {return size;}
	
//////////////////////////////////////////
	
	public void setJumpIndex(int jumpIndex) {this.jumpIndex = jumpIndex;}
	
//////////////////////////////////////////
	
	public void incrementJump() {jumpIndex+=jumpSpeed;}
	
//////////////////////////////////////////

	public int getLastDirection() {return lastDirection;}
	public void setLastDirection(int direction) {lastDirection = direction;} 
	
//////////////////////////////////////////

	public Level getCurrentLevel() {return currentLevel;}
	public void setCurrentLevel(Level currentLevel) {this.currentLevel = currentLevel;}
	
//////////////////////////////////////////

	public boolean isHurt() {return hurt;}
	public void setHurt(boolean hurt) {this.hurt = hurt;}
	
//////////////////////////////////////////

	public boolean isInBubble() {return inBubble;}
	public void setInBubble(boolean inBubble) {this.inBubble = inBubble;}
	
//////////////////////////////////////////

	public boolean isFollowing() {return following;}
	public void setFollowing(boolean following) {this.following = following;}

//////////////////////////////////////////
	
	public int getKind() {return kind;}
	
//////////////////////////////////////////

	public int getAniIndex() {return aniIndex;}

////////////////////////////////////////////////////////////////////////////////////
}
