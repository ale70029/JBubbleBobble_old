package model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import model.collectables.CollectablesContainer;
import model.collectables.Item;
import model.collectables.Power;
import model.enemies.EnemiesContainer;
import model.enemies.Enemy;

import static model.collectables.Item.MAX_ITEMS;
import static model.collectables.Power.MAX_POWERS;
import static model.game.Level.TILE_SIZE;
import static model.player.Bubble.BUBBLE_NORMAL;
import static model.player.Bubble.BUBBLE_POWER;
import static model.player.Bubble.POP_TIME;


/**
 * Class to store Bubble instances.
 * The bubbles list can contain at max MAX_BUBBLES.

 */
public class BubblesContainer {
////////////////////////////////////////////////////////////////////////////////////
	private final int MAX_BUBBLES = 5,POWER_BUBBLE_CHANCES = 20;
	
	private List<Bubble> bubbles;
	private CollectablesContainer collectablesContainer;
	private Player player;
	private Random random;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETONE
	private static BubblesContainer instance;
	
	/**
	 * Singleton.
	 */
	public static BubblesContainer getInstance() {
		if(instance==null)
			instance = new BubblesContainer();
		return instance;
	}
	
	private BubblesContainer() {
		this.player = Player.getInstance();
		this.collectablesContainer = CollectablesContainer.getInstance();
		this.bubbles= new ArrayList<Bubble>();
		this.random = new Random();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//BUBBLE FABRIC
	
	/**
	 * Creates a bubble if there are less than "MAX_BUBBLES" bubbles in the game.
	 * Every bubble is created with a timer to let them explode after each bubble "popTime" milliseconds.
	 * Bubble are created with player last position and direction when shooting, and a random kind (Normal/Power)
	 * A timer will remove bubbles after POP_TIME milliseconds, and in case of a Power bubble will leave behind an item
	 * or a powerUp.
	 * 
	 */
	public boolean createBubble() {
		
		if(bubbleAvailable()) {
			
			Bubble bubble = new Bubble(player.getX(),player.getY(),player.getLastDirection(),randomKind());
			
			Timer timer = new Timer();
			bubbles.add(bubble);

			//timer to remove bubble
			TimerTask removeBubble = new TimerTask() {
	            @Override
	            public void run() {
	            	//power bubble gives an item
	            	if(bubble.getKind()==BUBBLE_POWER) {
						int type = random.nextInt(0,101);
						if(type>EnemiesContainer.POWER_POSSIBILITY) {
							int kind = random.nextInt(0,MAX_ITEMS);
							Item item = new Item(bubble.getX(),TILE_SIZE*2,kind);
							collectablesContainer.addItem(item);
						}
						else {
							int kind = random.nextInt(0,MAX_POWERS);
							Power power = new Power(bubble.getX(),TILE_SIZE*2,kind);
							collectablesContainer.addPower(power);
							
						}
						bubbles.remove(bubble);
					}
	            	//normal bubble just pop
					else if(bubble.getKind()==BUBBLE_NORMAL) {
						bubbles.remove(bubble);
					
					}
	            }
	        };
	        timer.schedule(removeBubble,POP_TIME);
	        
	        return true;
		}
		return false;
	}
	
//////////////////////////////////////////
	
	/**
	 * Randomizer for bubbles kind
	 * @return 0 if normal bubble, 1 if power bubble
	 */
	private int randomKind() {
		int destiny = random.nextInt(0,101);
		if (destiny <= POWER_BUBBLE_CHANCES) return 1;
		return 0;
	}
	
//////////////////////////////////////////
	
	/**
	 * Count bubbles in game (including bubbles with enemies inside)
	 * @return true if there are less than MAX_BUBBLES, else false.
	 */
	private boolean bubbleAvailable() {
		if(player.isHitted()) return false;
		
		int availableBubbles = MAX_BUBBLES;
		for(Enemy e : EnemiesContainer.getInstance().getEnemies())
			if(e.isInBubble())
				availableBubbles--;
		availableBubbles -= bubbles.size();
		
		if(availableBubbles>0) return true;
		else return false;
	}

////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	public List<Bubble> getBubbles() {return bubbles;}
	
////////////////////////////////////////////////////////////////////////////////////
}
