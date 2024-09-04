package model.enemies;
/**
 * Class for enemy kind BOSS, similar to Enemy Mighta but can't be trapped in a bubble and his life points decreases every time a bubble hits him.
 */
public class EnemyBoss extends EnemyMighta{
////////////////////////////////////////////////////////////////////////////////////
	private static final int ENEMY_BOSS_SIZE = 192,LIFE_POINTS = 1000,VALUE = 2000,DAMAGE = 20, DEFAULT_SPEED =2;
	private int HP,damage;
	private long hitTime, hitMaxTime = 500;

////////////////////////////////////////////////////////////////////////////////////
//NEW
	
	/**
	 * Creates a new EnemyBoss with Life points, faster and with damage points every time he's hitted.
	 * Gives VALUE points when killed.
	 * @param x width position
	 * @param y height position
	 */
	public EnemyBoss(int x, int y) {
		super(x, y);
		this.size = ENEMY_BOSS_SIZE;
		this.HP   = LIFE_POINTS;
		this.damage = DAMAGE;
		this.speed  = DEFAULT_SPEED;
		this.points = VALUE;	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Updates enemy position based on direction, collision and status.
	 */
	@Override
	public void updatePosition() {

		//enemies still if player not in start position
		if(!currentLevel.isStart()) {
			//player hitted
			if(playerCollision()&&alive&&!player.isShield()) {
				player.hitted();
			}
			//killed by player
			if(HP<=0&&alive) {
				setAlive(false);
				deathTime = System.currentTimeMillis();
			}
			
			if(!alive)
				if(fallCollision(currentLevel)) setFalling(false);
				else setFalling(true);
			
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
				}
			}
			//moving
			if(alive) {
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
		}
    }
	
//////////////////////////////////////////
	
	/**
	 * Detects if a collision with the player has happened.
	 * @return true is collision has happened, else false.
	 */
	@Override 
	public boolean playerCollision() {
		if(player.getX()>=x && player.getX()+player.getSize()<= x+size)
			if(player.getY()>=y && player.getY()+player.getSize() <= y+size)
				return true;
		
		return false;
	}
	
//////////////////////////////////////////
	
	/**
	 * When he's hitted by a bubble, "damage" is subtracted from lifePoints, and hitTime is updated with current time, also "hurt" is set on true to control action in Thread.
	 */
	@Override
	public void setHitted(boolean hitted) {
		if(hitted) {
			hurt=true;
			hitTime = System.currentTimeMillis();
			HP-=damage;
			hitted = false;
			return;
		}
		else return;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//THREAD
	
	/**
	 * Runs the thread of EnemyBoss, to decide when to follow player, how much time before disappearing after death,
	 * and how much time to stay hurt when hitted.
	 * A random number 0/100 is given to randomize some actions.
	 */
	@Override
	public void run() {

		while(true) {
			
			try {
				Thread.sleep(200); // To prevent CPU explosion
				currentTime = System.currentTimeMillis();
				destiny=random.nextInt(0,101);//to randomize a bit some actions
				
				if(currentTime - followTime > followMaxTime) {
					if(destiny<60&&alive&!following) 
						followPlayer();
					else if (following) stopFollowing();
				}
				
				if(currentTime - deathTime > deathMaxTime)
					if(!alive)
						setToRemove(true);
				
				if(currentTime - hitTime > hitMaxTime)
					if(hurt)
						hurt=false;
					
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	public int getHP() {return HP;}
	

////////////////////////////////////////////////////////////////////////////////////
}
