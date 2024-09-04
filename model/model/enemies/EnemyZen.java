package model.enemies;

import java.util.Random;
/**
 * Class for enemy kind ZEN, standard enemy behaviour
 */
public class EnemyZen extends Enemy{
////////////////////////////////////////////////////////////////////////////////////
	private static final int VALUE = 200, MAX_SWAP_TIME = 4000, MAX_JUMP_TIME = 300, MAX_FOLLOW_TIME = 5000;
////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Creates a new Enemy Zen, it swaps direction, follows player and jump.
	 * Gives VALUE points at death.
	 * @param x width position
	 * @param y height position
	 */
	public EnemyZen(int x, int y) {
		super(x,y);
		this.swapMaxTime=MAX_SWAP_TIME;
		this.jumpMaxTime=MAX_JUMP_TIME;
		this.followMaxTime=MAX_FOLLOW_TIME;
		this.points=VALUE;
		this.random = new Random();
		this.thread = new Thread(this);
		thread.start();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//THREAD
	
	/**
	 * Runs the thread of Enemy Zen, to decide how much to stay in a bubble,
	 * when to follow player, how much time before disappearing after death,
	 * how much time before swappind direction, jumping and following player.
	 * A random number 0/100 is given to randomize some actions.
	 */
	@Override
	public void run() {

		while(true) {
			
			try {
				Thread.sleep(200); // To prevent CPU explotion
				currentTime = System.currentTimeMillis();
				destiny=random.nextInt(0,101);//to randomize a bit some actions
				
				if(currentTime-swapTime > swapMaxTime)
					if(destiny<50&&alive) forceSwap(); //50% chance
				
				
				if(currentTime-jumpTime > jumpMaxTime)
					if(destiny<50&&alive) forceJump(); //50% chance
				
				if(currentTime - inBubbleTime > inBubbleMaxTime) {
					setInBubble(false); //free enemies after inBubblMaxTime
					killable = false;
				}
				
				if(currentTime - followTime > followMaxTime) {
					if(destiny<20&&alive&!following) 
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
