package controller.controllers;

import static model.collectables.Item.MAX_ITEMS;
import static model.collectables.Power.MAX_POWERS;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import java.util.Random;

import controller.filesManagers.AudioManager;
import controller.filesManagers.FileManager;
import main.Game;
import model.collectables.CollectablesContainer;
import model.enemies.EnemiesContainer;
import model.enemies.Enemy;
import model.enemies.EnemyBoss;
import model.enemies.EnemyInvader;
import model.enemies.EnemyMighta;
import model.enemies.EnemyZen;
import model.player.Bubble;
import model.player.BubblesContainer;

/**
 * Class to manage enemies (update and draw)
 */
public class EnemiesController implements AnimationController{
////////////////////////////////////////////////////////////////////////////////////
	private EnemiesContainer enemiesContainer;
	private BubblesContainer bubblesContainer;
	private CollectablesContainer collectablesContainer;
	
	private BufferedImage[][] enemyZanAnimations,enemyInvaderAnimations,enemyMightaAnimations,bossAnimations;
	private Random random;

////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	private static EnemiesController instance;
	
	/**
	 * Singleton.
	 */
	public static EnemiesController getInstance() {
		if(instance==null)
			instance = new EnemiesController();
		return instance;
	}
	
	private EnemiesController() { 
		this.enemyZanAnimations=FileManager.ENEMY_ZEN_ANIMATIONS;
		this.enemyInvaderAnimations=FileManager.ENEMY_INVADER_ANIMATIONS;
		this.enemyMightaAnimations=FileManager.ENEMY_MIGHTA_ANIMATIONS;
		this.bossAnimations=FileManager.ENEMY_BOSS_ANIMATIONS;
		
		this.enemiesContainer = EnemiesContainer.getInstance();
		this.enemiesContainer.setCurrentLevel(LevelController.getInstance().getCurrentLevel());
		this.enemiesContainer.loadEnemies();
		
		this.bubblesContainer = BubblesContainer.getInstance();
		this.collectablesContainer = CollectablesContainer.getInstance();

		this.random= new Random();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE

	/**
	* Updates enemies in game.
	* 
	* Updates position and animation.
	* If is detected a collision between a bubble and an enemy, the enemy is trapped in the bubble.
	* When enemies are dead (toRemove) it creates a random collectable in enemy position and then remove the enemy from the enemiesContainer.
	* If there are no enemies and no collectable, forces next level.
	*/
	public void update() {
		try {
			for(Enemy enemy:enemiesContainer.getEnemies()) {
				//if enemy is dead puts a collectable in enemy position
				if(enemy.isToRemove()) {
					//create collectable
					int type = random.nextInt(0,101);
					if(type>EnemiesContainer.POWER_POSSIBILITY) {
						int kind = random.nextInt(0,MAX_ITEMS);
						collectablesContainer.addItem(enemy.getX(),enemy.getY(),kind);
					}
					else {
						int kind = random.nextInt(0,MAX_POWERS);
						collectablesContainer.addPower(enemy.getX(),enemy.getY(),kind);
					}
					//remove enemy
					enemy.addPoints();
					enemiesContainer.getEnemies().remove(enemy);
					break;
				}
				
				if(!enemy.isAlive())AudioManager.getInstance().playSound(AudioManager.ENEMY_DEATH_SOUND);
				
				//update enemy
				enemy.updatePosition();
				updateAnimation(enemy,ENEMY_MAX_SPRITES);
				setAnimation(enemy);
				//put enemy in bubble
				for(Bubble bubble: bubblesContainer.getBubbles()) {
					if(enemy.getClass()==EnemyBoss.class) {
						if(!bubble.isToRemove()&&bubble.enemyCollision(enemy)) {
								enemy.setHitted(true);
								bubble.setToRemove(true);
								
								AudioManager.getInstance().playSound(AudioManager.BUBBLE_HIT_SOUND);
								
						}
					}
					else if(!bubble.isToRemove()&&bubble.enemyCollision(enemy)&&!enemy.isInBubble()) {
						enemy.setInBubble(true);
						bubble.setToRemove(true);
						AudioManager.getInstance().playSound(AudioManager.BUBBLE_HIT_SOUND);
					}
				}
			}
		}catch(ConcurrentModificationException e) {} //likely to be thrown beacuse of array modifications during for loops
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	
	public static int scale(int value) {
		return (int) (value/Game.SCALE);
	}
	/**
	* Draws enemies with Graphics, images are taken from enemiesAnimation depending on current action and animation index.
	* @param g
	*/
	public void draw(Graphics g){
	try {
		for(Enemy enemy:enemiesContainer.getEnemies()) {
			if(enemy.getAniIndex()<ENEMY_MAX_SPRITES)
				//ZEN
				if(enemy.getClass()==EnemyZen.class)
					g.drawImage(enemyZanAnimations[enemy.getAction()][enemy.getAniIndex()],
								scale(enemy.getX()), scale(enemy.getY()), scale(enemy.getSize()), scale(enemy.getSize()), null);
				//INVADER
				else if(enemy.getClass()==EnemyInvader.class)
					g.drawImage(enemyInvaderAnimations[enemy.getAction()][enemy.getAniIndex()],
							scale(enemy.getX()), scale(enemy.getY()), scale(enemy.getSize()), scale(enemy.getSize()), null);
				//MIGHTA
				else if(enemy.getClass()==EnemyMighta.class)
					g.drawImage(enemyMightaAnimations[enemy.getAction()][enemy.getAniIndex()],
							scale(enemy.getX()), scale(enemy.getY()), scale(enemy.getSize()), scale(enemy.getSize()), null);
				//BOSS
				else if(enemy.getClass()==EnemyBoss.class) {
					EnemyBoss boss = (EnemyBoss) enemy;
					g.drawImage(bossAnimations[boss.getAction()][boss.getAniIndex()],
							scale(boss.getX()), scale(boss.getY()), scale(boss.getSize()), scale(boss.getSize()), null);
					//boss healt bar
					if(boss.isAlive()) {
						g.setColor(Color.RED);
						g.fill3DRect(scale(boss.getX()-5), scale(boss.getY()-20), scale(boss.getHP()/5), scale(10), true);
					}
				}
		}
		
	}catch(ConcurrentModificationException e) {} //likely to be thrown beacuse of array modifications during for loops
	
	}
////////////////////////////////////////////////////////////////////////////////////
}
