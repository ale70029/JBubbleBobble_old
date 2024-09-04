package model.player;

import model.collectables.Power;
import model.game.Context;
import model.game.Entity;
import model.game.Level;



/**
 * Class for player charachter in game.
 * A Thread is given for time management.
 * Observed by HeadBar to update score,healt and powerUps rendering.
 */
@SuppressWarnings("deprecation")
public class Player extends Entity implements Runnable{
////////////////////////////////////////////////////////////////////////////////////
	/*
	 * Index for direction
	 */
	public static final int LAST_DIRECTION_RIGHT = 0,LAST_DIRECTION_LEFT  = 1;
	/**
	 * Player default stats
	 */
	public static final int MAX_HEALTH = 5, PLAYER_SIZE = 64,PLAYER_DEFAULT_SPEED = 2, PLAYER_DEFAULT_MULTIPLIER = 2;
	/**
	 * Thread max times.
	 */
	private final long MAX_SHOOT_TIME  = 200, MAX_SHIELD_TIME = 3000, MAX_FAST_TIME = 3000, MAX_DEATH_TIME = 2000;
	
	//stats
	private int health,lastHealth;
	private int score,lastScore,multiplier;
	private boolean shield,fast;
	private boolean shootingAllowed;
	//Thread
	private Thread playerThread;
	private boolean threadRunning=true;
	private Object lock = new Object();
	private long currentTime,shootTime,shieldTime,fastTime,deathTime;
	
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Player instance;
	/**
	 * Singleton.
	 */
	public static Player getInstance() {
		if(instance==null)
			instance = new Player();
		return instance;
	}

	private Player() {
		super();
		this.size=PLAYER_SIZE;
		this.multiplier=PLAYER_DEFAULT_MULTIPLIER;
		this.speed=PLAYER_DEFAULT_SPEED;
		this.health=MAX_HEALTH;
		this.lastHealth=health;
		setInCenter();
		this.playerThread= new Thread(this);
		this.playerThread.start();
	}

////////////////////////////////////////////////////////////////////////////////////
//MOVEMENT
	
	/**
	 * Update player position based on player stats, collisions and direction
	 */
	public void updatePlayer() {
		//when level begins move player to start position and give shield
		if(currentLevel.isStart()) {
			movePlayerToStartPoint();
			setShield(true);
			shieldTime = System.currentTimeMillis();
			//first headbar update
			setChanged();
			notifyObservers(this);
		}
		//level has started
		else {
			//player is dead
			if(health<1) {
				setAlive(false);
				setShield(false);
				if(deathTime==0) deathTime=System.currentTimeMillis();
				
			}
			//player is fast
			if(fast) setSpeed(3);
			//player moves
			if(!hitted&&alive) {
				//player still
				setMoving(false);
				if(!right && !left && !jumping && !falling) return;
				//walk on nothing makes player fall
				if(!jumping && !fallCollision(currentLevel)) setFalling(true);
				//left
				if(left)  {
					setLastDirection(LAST_DIRECTION_LEFT);
					int value = x-speed;
					if(!horizontalCollision(value,currentLevel)) setX(value);
				}
				//right
				if(right)  {
					setLastDirection(LAST_DIRECTION_RIGHT);
					int valueX = x+speed;
					if(!horizontalCollision(valueX,currentLevel)) setX(valueX);			
				}
				//jumping
				if(jumping) {
					if(!falling) {
						int value = y - jumpSpeed;
						if(!roofCollision()) {
							setY(value);
							incrementJump();
						}
						//roof collision makes player fall
						else{
							setFalling(true);
							setJumping(false);
							setJumpIndex(0);
						}
					}
					//jump up to jumpMax index, then fall
					if(jumpIndex>=jumpMax) {
						setFalling(true);
						setJumping(false);
						setJumpIndex(0);
					}
				}
				//falling
				if(falling) {
					if(!fallCollision(currentLevel)){
						int value = y + jumpSpeed;
						setY(value);
					}
					//stops when there is ground belove
					else setFalling(false);
				}
				setMoving(true);
			}
		}
	}
	
//////////////////////////////////////////
	
	/**
	 * Update player position to match level start point, first in vertical axis and then in horizzontal axis.
	 */
	public void movePlayerToStartPoint() {
		
		
		//first verticaly
		if(y!=currentLevel.getStartY()) {
			if(y<currentLevel.getStartY()) y+=speed;
			if(y>currentLevel.getStartY()) y-=speed;
		}
		//then horizontaly
		if(y==currentLevel.getStartY()) {
			if(x<currentLevel.getStartX()) x+=speed;
			if(x>currentLevel.getStartX()) x-=speed;
		}
		//stop
		if(x==currentLevel.getStartX() && y==currentLevel.getStartY()) {
			currentLevel.setStart(false);
			resetPlayerMovements();
			setShootingAllowed(true);
		}
		//normalize x & y ONLY BEACUSE SPEED IS EVEN
		if(x%2!=0) x++;
		if(y%2!=0) y++;
	}
	
//////////////////////////////////////////
	
	private void setInCenter() {
		this.x=334;
		this.y=0;
		setFalling(true);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * When called, removes a life, and if still alive makes enemy go back to start position.
	 * lastHealth keeps track of new healt quantity and observers are notified.
	 * If healt is 0, triggers player death by updating deathTime.
	 */
	public void hitted() {
		if(alive) {
			health--;
			lastHealth = health;
			setShield(true);
			setHitted(true);
			setChanged();
			notifyObservers(this);
			
			//alive - back to start position
			if(health>=1)currentLevel.setStart(true);
			//death
			else deathTime = System.currentTimeMillis();
		}
	}
	
//////////////////////////////////////////
	/**
	 * Applies powerups to the player and notify observers.
	 * @param powerUp power to apply
	 */
	public void applyPowerUp(Power powerUp) {
		//SHIELD
		if(powerUp.getKind()==Power.SHIELD) {
			if(isShield())
				setScore(score+(powerUp.getPoints()*multiplier));
			else setShield(true);
		}
		//LIFE
		else if(powerUp.getKind()==Power.LIFE) {
			if(health<MAX_HEALTH) setHealth(++health);
			else setScore(score+(powerUp.getPoints()*multiplier));
		}
		//FAST
		else if(powerUp.getKind()==Power.FAST) {
			if(isFast())
				setScore(score+(powerUp.getPoints()*multiplier));
			else setFast(true);
		}
		setChanged();
		notifyObservers(this);
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
//RESET
	
	private void restartReset() {
		setScore(lastScore);
		setHealth(lastHealth);
		setFast(false);
		setSpeed(PLAYER_DEFAULT_SPEED);
		setAlive(true);
		resetPlayerMovements();
		setInCenter();
		setFalling(true);
		resetTimes();
		resumeThread();
	}
	
//////////////////////////////////////////

	private void resetPlayerMovements() {
		setLeft(false);
		setRight(false);
		setFalling(false);
		setShooting(false);
		setAttacking(false);
		setHitted(false);
	}
	
//////////////////////////////////////////
	
	private void resetTimes() {
		shootTime=0;
		shieldTime=0;
		fastTime=0;
		deathTime=0;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//THREAD
	
	/**
	 * Runs the thread of Player, to decide how much to wait before shooting again,
	 * shield duration, fast mode duration, how much time before disappearing after death.
	 * When players die, current context is setted on GAME_OVER and thread is stopped.
	 */
	@Override
	public void run() {
		while(true) {
			//to resume thread
			synchronized (lock) {
				while (!threadRunning) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
				}
			}
			//to manage time
			while (threadRunning) {
				try {
					Thread.sleep(200); // To prevent CPU explotion
					currentTime = System.currentTimeMillis();
					
					//shoot
					if(shootTime!=0) {
						if(currentTime-shootTime>MAX_SHOOT_TIME)
							setShootingAllowed(true);
							setAttacking(false);
					}
					//shield
					if(currentTime-shieldTime>MAX_SHIELD_TIME)
						setShield(false);
					
					//fast
					if(fastTime!=0) {
						if(currentTime-fastTime>MAX_FAST_TIME)
							setFast(false);
							setSpeed(PLAYER_DEFAULT_SPEED);
					}
					
					//death
					if(deathTime!=0) {
						if(currentTime-deathTime>MAX_DEATH_TIME) {
							Context.current= Context.GAME_OVER;
							stopThread();
						}	
					}
						
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	
	}
		
//////////////////////////////////////////
	
	private void stopThread() {
        synchronized (lock) {
        	threadRunning = false;
        }
    }
	
//////////////////////////////////////////
	
	private void resumeThread() {
        synchronized (lock) {
        	threadRunning = true;
            lock.notify();
        }
    }
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET

	public int getHealth() {return health;}
	
	/**
	 * Notify observers
	 */
	public void setHealth(int healt) {
		this.health = healt;
		setLastHealth(health);
		setChanged();
		notifyObservers(this);
	}
	
//////////////////////////////////////////
	
	public boolean isShield() {return shield;}
	
	/**
	 * Notify observers and sets shieldTime if shield is true
	 */
	public void setShield(boolean shield) {
		this.shield = shield;
		if(shield) shieldTime= System.currentTimeMillis();
		setChanged();
		notifyObservers(this);
	}
	
//////////////////////////////////////////
	
	public boolean isFast() {return fast;}
	
	/**
	 * Notify observers and sets fastTime if fast is true
	 */
	public void setFast(boolean fast) {
		this.fast = fast;
		if(fast)fastTime = System.currentTimeMillis();
		setChanged();
		notifyObservers(this);
	}
	
//////////////////////////////////////////
	
	public int getScore() {return score;}
	
	/**
	 * Notify observers
	 */
	public void setScore(int score) {
		this.score = score;
		setChanged();
		notifyObservers(this);
	}
	
//////////////////////////////////////////
	
	@Override
	/**
	 * Sets shootTime is shooting is true
	 */
	public void setShooting(boolean shooting) {
		this.shooting=shooting;
		if(shooting) shootTime=System.currentTimeMillis();
	}
	
//////////////////////////////////////////
	
	public void setLastScore(int lastScore) {this.lastScore = lastScore;}
	
//////////////////////////////////////////
	
	public boolean isShootingAllowed() {return shootingAllowed;}
	public void setShootingAllowed(boolean shootingAllowed) {this.shootingAllowed = shootingAllowed;}
	
//////////////////////////////////////////
	
	public int getLastHealth() {return lastHealth;}
	public void setLastHealth(int lastHealth) {this.lastHealth = lastHealth;}
	
//////////////////////////////////////////
	
	@Override
	/**
	 * Restart reset is called when currentLevel is set.
	 */
	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
		restartReset();
	}
	
//////////////////////////////////////////
	
	public int getMultiplier() {return multiplier;}
	public void setMultiplier(int multiplier) {this.multiplier = multiplier;}
	
////////////////////////////////////////////////////////////////////////////////////
}
