package model.enemies;

import java.util.Random;
/**
 * Class for enemy kind INVADER, see class enemy for general behaviour.
 */
public class EnemyInvader extends Enemy{
////////////////////////////////////////////////////////////////////////////////////
	private static final int VALUE = 400,MAX_JUMP = 192, MAX_JUMP_TIME = 2500, MAX_FOLLOW_TIME=5000;
////////////////////////////////////////////////////////////////////////////////////
//NEW
	
	/**
	 * Creates a new enemy invader, it can jump higer,longer, follows player longer.
	 * Gives VALUE points when killed.
	 * @param x width position
	 * @param y height position
	 */
	public EnemyInvader(int x, int y) {
		super(x,y);
		this.jumpMax= MAX_JUMP;
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
	 * Runs the thread of Enemy Invade, to decide how much to stay in a bubble,
	 * when to follow player, how much time before disappearing after death and
	 * how often to randomly jump. A random number 0/100 is given to randomize some actions.
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
				
				if(currentTime - deathTime > deathMaxTime)
					if(!alive)
						setToRemove(true);
				
				if(currentTime-jumpTime > jumpMaxTime)
					if(destiny<50&&alive) forceJump(); //50% chance for 0,5 second
				
				if(currentTime - followTime > followMaxTime) {
					if(destiny<40&&alive&!following)  
						followPlayer(); 
					else if (following) stopFollowing();
				}
				
					
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
