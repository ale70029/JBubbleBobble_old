package model.enemies;


import java.util.Random;

import model.game.Entity;
import model.player.Player;
/**
 * Class to create and manage Enemies. A thread is in charge for time management for every single enemy.
 */
public class Enemy extends Entity implements Runnable {
////////////////////////////////////////////////////////////////////////////////////
	public static final int ENEMY_DEFAULT_SPEED= 1,ENEMY_DEFAULT_SIZE = 48;
	protected final long IN_BUBBLE_MAX_TIME = 6000, DEATH_MAX_TIME = 1500;

	protected Player player;
	protected Random random;
	protected Thread thread;

	protected int points,destiny;
	protected boolean inBubble,toRemove,following,killable;

	protected long currentTime,swapTime,jumpTime,inBubbleTime,deathTime,followTime,
				   swapMaxTime,jumpMaxTime,followMaxTime,inBubbleMaxTime, deathMaxTime;
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	protected Enemy(int x, int y) {
		super();
		this.x=x;
		this.y=y;
		this.player=Player.getInstance();
		this.size=ENEMY_DEFAULT_SIZE;
		this.speed=ENEMY_DEFAULT_SPEED;
		this.inBubbleMaxTime=IN_BUBBLE_MAX_TIME;
		this.deathMaxTime=DEATH_MAX_TIME;
		this.left=true; //why??
	}

////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Updates enemy position based on collision, states and direction.
	 */
	public void updatePosition() {
		//enemies still if player not in start position
		if(!currentLevel.isStart()) {
			//collision with player
			if(playerCollision()&&!inBubble&&alive&&!player.isShield()) {
				player.hitted();
			}
			//killed by player
			if(playerCollision()&&inBubble&&alive&&killable) {
				setAlive(false);
				deathTime = System.currentTimeMillis();
			}
			//Walking on nothing makes enemy fall
			if(!jumping && !fallCollision(currentLevel)) setFalling(true);
			
			//falling to ground
			if(falling) {
				if(!fallCollision(currentLevel)){
					setY(y + jumpSpeed);
				}
				else setFalling(false);
			}
			
			//jumping
			if(jumping) {
				if(!falling) {
					//can jump 
					if(!roofCollision()) {
						setY(y - jumpSpeed);
						incrementJump();
					}
					//roof collision
					else{
						setFalling(true);
						setJumping(false);
					}
					//reset jump stats if not trapped in bubble, else goes up to the roof
					if(!inBubble) {
						if(jumpIndex>=96) {
							setJumping(false);
							setJumpIndex(0);
					    }
					}
					//enemy inBubble stays on the roof and becomes killable
					if(inBubble&&roofCollision()) {
						killable = true;
						setJumping(false);
						setFalling(false);
					}
					
				}
			}
			//moving
			if(alive&&!inBubble) {
				if(right){
					int value = x+speed;
					if(!horizontalCollision(value, currentLevel)) setX(value);
					else forceSwap(); //change direction if against the wall
				}
				if(left) {
					int value = x-speed;
					if(!horizontalCollision(value, currentLevel)) setX(value);
					else forceSwap(); //change direction if against the wall
				}
			}
			//make enemy trapped in bubble jump up to roof
			if(inBubble&&alive) {
				setFalling(false);
				setJumping(true);
			}
			//make dead enemy drop on ground
			if(inBubble&&!alive) {
				setFalling(true);
				setJumping(false);
			}
		}
	}
	
//////////////////////////////////////////

	/**
	 * Adds enemy points to the player when killed, multiplied by player multiplier.
	 */
	public void addPoints() {player.setScore(player.getScore()+(points*player.getMultiplier()));}

//////////////////////////////////////////
	
	
	/**
	 * Forces enemy to follow player direction to meet 
	 */
	protected void followPlayer() {
		startFollowing();
		if(player.getY() < y && y>128) {
			if(!falling)setJumping(true);
		}

		if(player.getX()+player.getSize() < x) {
			setLeft(true);
			setRight(false);
		}
		if(player.getX() > x) {
			setLeft(false);
			setRight(true);
		}
	}
	
//////////////////////////////////////////
	
	/**
	 * Start following mode and sets followTime to current time.
	 */
	protected void startFollowing() {
		following = true;
		followTime = System.currentTimeMillis();
		setSpeed(ENEMY_DEFAULT_SPEED+1);  //TODO may give problems 
	}
	
//////////////////////////////////////////
	
	/**
	 * Stops following mode and 
	 */
	protected void stopFollowing() {
		following = false;
		setSpeed(ENEMY_DEFAULT_SPEED);
		
	}

//////////////////////////////////////////
	
	/**
	 * Forces enemy to jump if player is above and sets jumpTime to current time.
	 */
	protected void forceJump() {
		if(player.getY()+16 < y) setJumping(true);
		jumpTime = System.currentTimeMillis();
	}

//////////////////////////////////////////
	
	/**
	 * Swap direction from left to right and vice versa and sets swapTime to current time.
	 */
	protected void forceSwap() {
		left = !left;
		right = !right;
		swapTime = System.currentTimeMillis();
	}
	
//////////////////////////////////////////
	
	/**
	 * Return true if enemy position collides with player position, else return false
	 */
	protected boolean playerCollision() {
		if(x>=player.getX()&&x<=player.getX()+player.getSize())
			if(y+size>=player.getY()&&y<=player.getY()+player.getSize())
				return true;
		
		return false;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//THREAD
	
	/**
	 * Overrided by subClasses
	 */
	@Override
	public void run() {}

////////////////////////////////////////////////////////////////////////////////////
//GET - SET

	
	public boolean isInBubble() {return inBubble;}
	public void setInBubble(boolean inBubble) {this.inBubble = inBubble;}
	
//////////////////////////////////////////
	
	public void setInBubbleTime(long inBubbleTime) {this.inBubbleTime = inBubbleTime;}
	
//////////////////////////////////////////
	
	public boolean isToRemove() {return toRemove;}
	public void setToRemove(boolean toRemove) {this.toRemove = toRemove;}
	
////////////////////////////////////////////////////////////////////////////////////
	
	

}
