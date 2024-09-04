package model.enemies;

import static model.game.Level.TILE_SIZE;

import java.util.Random;

import model.game.Level;
/**
 * Class for enemy kind INVADER, unlike other enemies it floats in air.
 */
public class EnemyMighta extends Enemy{
////////////////////////////////////////////////////////////////////////////////////
	private static final int VALUE = 600,MAX_FOLLOW_TIME=5000;
////////////////////////////////////////////////////////////////////////////////////
//NEW
	
	/**
	 * Creates a new enemy Mighta,follows player longer and floats around.
	 * Gives VALUE points when killed.
	 * @param x width position
	 * @param y height position
	 */
	public EnemyMighta(int x, int y) {
		super(x,y);
		this.followMaxTime=MAX_FOLLOW_TIME;
		this.points=VALUE;
		this.jumping = true;
		this.random = new Random();
		this.thread = new Thread(this);
		thread.start();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Updates enemy position based on collision, direction and status.
	 */
	@Override
	public void updatePosition() {

		//enemies still if player not in start position
		if(!currentLevel.isStart()) {
			//player hitted
			if(playerCollision()&&!inBubble&&alive&&!player.isShield()) {
				player.hitted();
			}
			//killed by player
			if(playerCollision()&&inBubble&&alive&&killable) {
				setAlive(false);
				deathTime = System.currentTimeMillis();
			}
			
			if(!alive)
				if(fallCollision(currentLevel)) setFalling(false);
			
			//falling to ground
			if(falling) {
				if(!frameCollision(x,y + jumpSpeed)){
					setY(y + jumpSpeed);
				}
				else {
					setFalling(false);
					setJumping(true);
				}
			}
			
			//jumping
			if(jumping) {
				if(!falling) {
					//can jump 
					if(!frameCollision(x, y - jumpSpeed)) {
						setY(y - jumpSpeed);
						
					}
					//roof collision
					else{
						
						setFalling(true);
						setJumping(false);
					}
					//enemy inBubble stays on the roof and becomes killable
					if(inBubble&&frameCollision(x, y - jumpSpeed)) {
						killable = true;
						setJumping(false);
						setFalling(false);
					}
				}
			}
			//moving
			if(alive&&!inBubble) {
				if(right){
					if(!frameCollision(x+speed,y)) setX(x+speed);
					else{
						setRight(false);
						setLeft(true);
					}
					
				}
				if(left) {
					if(!frameCollision(x-speed,y)) setX(x-speed);
					else{
						setLeft(false);
						setRight(true);
						
					}
				}
			}
			//make enemy jump up to roof
			if(inBubble&&alive) {
				setFalling(false);
				setJumping(true);
			}
			if(inBubble&&!alive) {
				setFalling(true);
				setJumping(false);
			}
		}
	}

//////////////////////////////////////////
	
	/**
	 * Detect if next position will collide with level borders.
	 * @param x next width position
	 * @param y next height position
	 * @return true if collision would occur, else false
	 */
	protected boolean frameCollision(int x, int y) {
		if(x <= TILE_SIZE    || x+size >= (Level.LEVEL_COLS-1)*TILE_SIZE) return true; 
		if(y <  2*TILE_SIZE  || y+size >  (Level.LEVEL_ROWS-1)*TILE_SIZE) return true; //2 beacause first 2 rows are empty
		return false;
		
	}
	
//////////////////////////////////////////
	
	/**
	 * Follows player direction and updates followTime.
	 */
	@Override
	public void followPlayer() {

		if(player.getY() < y){
			setFalling(true);
		}
		else setJumping(true);

		if(player.getX()< x) {
			setLeft(true);
			setRight(false);
		}
		if(player.getX() > x) {
			setLeft(false);
			setRight(true);
		}
		followTime = System.currentTimeMillis();
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//THREAD 
	
	/**
	 * Runs the thread of Enemy Mighta, to decide how much to stay in a bubble,
	 * when to follow player and how much time before disappearing after death.
	 *  A random number 0/100 is given to randomize some actions.
	 */
	@Override
	public void run() {

		while(true) {
			
			try {
				Thread.sleep(200); // To prevent CPU explotion
				currentTime = System.currentTimeMillis();
				destiny=random.nextInt(0,101);//to randomize a bit some actions
				if(currentTime - inBubbleTime > inBubbleMaxTime) {
					setInBubble(false); //free enemies after inBubblMaxTime
					killable = false;
				}
				
				if(currentTime - followTime > followMaxTime) {
					if(destiny<60&&alive&!following) 
						followPlayer();
					else if (following) stopFollowing();
				}
				
				if(currentTime - deathTime > deathMaxTime)
					if(!alive)
						setToRemove(true);
					
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}