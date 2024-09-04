/**
 * Package with classes that controls model classes. They manage and update data and draw entities.
 */
package controller.controllers;

import java.awt.Graphics;

import model.enemies.EnemyBoss;
import model.enemies.EnemyInvader;
import model.enemies.EnemyMighta;
import model.enemies.EnemyZen;
import model.game.Entity;
import model.player.Bubble;
import model.player.Player;

import static model.player.Bubble.BUBBLE_NORMAL;
import static model.player.Bubble.BUBBLE_POWER;
import static model.player.Player.LAST_DIRECTION_RIGHT;

/**
 * Gives all needed instruments to animate Entities.
 */
public interface AnimationController {
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Actions indexes for Enemy Zen
	 */
	static final int ENEMY_ZAN_LEFT = 0,ENEMY_ZAN_RIGHT = 1,ENEMY_ZAN_BUBBLE = 2,ENEMY_ZAN_DEAD = 3;
	
	/**
	 * Actions indexes for Enemy Invader
	 */
	static final int ENEMY_INVADER_CALM = 0,ENEMY_INVADER_MAD = 1,ENEMY_INVADER_BUBBLE = 2,ENEMY_INVADER_DEAD = 3;
	
	/**
	 * Actions indexes for Enemy Mighta
	 */
	static final int ENEMY_MIGHTA_LEFT = 0,ENEMY_MIGHTA_RIGHT = 1,ENEMY_MIGHTA_BUBBLE = 2,ENEMY_MIGHTA_DEAD = 3;
	
	/**
	 * Actions indexes for Enemy Boss
	 */
	static final int ENEMY_BOSS_LEFT =0,ENEMY_BOSS_RIGHT =1,ENEMY_BOSS_HURT_LEFT =2,ENEMY_BOSS_HURT_RIGHT =3,ENEMY_BOSS_DEAD =4;
	
	/**
	 * Actions indexes for Player
	 */
	static final int DRAGON_STILL_RIGHT = 0,DRAGON_STILL_LEFT = 1,DRAGON_RIGHT = 2,DRAGON_LEFT = 3,DRAGON_JUMPING = 4,
					 DRAGON_SHOOT_LEFT = 5,DRAGON_SHOOT_RIGHT = 6,DRAGON_DEATH = 7,DRAGON_DAMAGE_LEFT = 8,DRAGON_DAMAGE_RIGHT = 9;
	/**
	 * Actions indexes for Bubbles
	 */
	static final int BUBBLE_NORMAL_SHOOT = 0,BUBBLE_NORMAL_UP = 1,BUBBLE_POWER_SHOOT = 2,BUBBLE_POWER_UP = 3;
	
	/**
	 * Limits for animations sprites
	 */
	static final int PLAYER_MAX_INDEX = 10,ENEMY_MAX_INDEX = 4,ENEMY_MAX_SPRITES = 4,BUBBLE_MAX_SPRITES = 6;
	
	/**
	 * General animation speed applied to every Entity
	 */
	static final int ANIMATION_SPEED = 20;
////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates entity actions to animate
	 */
	void update();
	
//////////////////////////////////////////
	/**
	 * Draws animations
	 * @param g Graphics graphic motor
	 */
	void draw(Graphics g);
	
////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates animation index, wich will be used to select proper animation image in animations BufferedImage arrays for every entity.
	 * aniTick and aniIndex are parameters of entities.
	 * When aniTicks reaches ANIMATION_SPEED, the animation image index is increased and aniTick is resetted.
	 * When aniIndex reaches maxAniIndex is resetted.
	 * @param entity Entity to be animated
	 * @param maxAniIndex Max index for specific action,sometimes is the same number for every entity action,
	 *  sometimes is not (for Player there is a specific method below)
	 */
	default void updateAnimation(Entity entity, int maxAniIndex){
		entity.aniTick++;
		if (entity.aniTick >= ANIMATION_SPEED) {
			entity.aniTick = 0;
			entity.aniIndex++;
			if (entity.aniIndex >= maxAniIndex) 
				entity.aniIndex = 0;
		}
	}
	
//////////////////////////////////////////
	
	/**
	 * Updates animation stats for an enemy.
	 * @param enemy
	 */
	default void setAnimation(Entity entity) {
	//////////////ENEMIES
		// ENEMY ZEN
		if(entity.getClass()==EnemyZen.class) {
			int startAni = entity.getAction();
			
			if(entity.isAlive()) {
				
				if(entity.isInBubble())  entity.setAction(ENEMY_ZAN_BUBBLE);
				else {
					if(entity.isLeft())  entity.setAction(ENEMY_ZAN_LEFT);
					if(entity.isRight()) entity.setAction(ENEMY_ZAN_RIGHT);
				}
			}
			else entity.setAction(ENEMY_ZAN_DEAD);
			
			if (startAni != entity.getAction()) {
				entity.aniTick = 0;
				entity.aniIndex = 0;
			}
		}
		// ENEMY INVADER
		else if(entity.getClass()==EnemyInvader.class) {
			int startAni = entity.getAction();
			
			if(entity.isAlive()) {
				if(entity.isFollowing()&&!entity.isInBubble()) entity.setAction(ENEMY_INVADER_MAD);
				else if(entity.isInBubble()) entity.setAction(ENEMY_INVADER_BUBBLE);
				else {
					entity.setAction(ENEMY_INVADER_CALM);
				}
			}
			else entity.setAction(ENEMY_INVADER_DEAD);
			
			if (startAni != entity.getAction()) {
				entity.aniTick = 0;
				entity.aniIndex = 0;
			}
		}
		// ENEMY MIGHTA
		else if(entity.getClass()==EnemyMighta.class) {
			int startAni = entity.getAction();
			
			if(entity.isAlive()) {
				if(entity.isInBubble())  entity.setAction(ENEMY_MIGHTA_BUBBLE);
				else {
					if(entity.isLeft())  entity.setAction(ENEMY_MIGHTA_LEFT);
					if(entity.isRight()) entity.setAction(ENEMY_MIGHTA_RIGHT);
				}
			}
			else entity.setAction(ENEMY_MIGHTA_DEAD);
			
			if (startAni != entity.getAction()) {
				entity.aniTick = 0;
				entity.aniIndex = 0;
			}
		}
		//BOSS
		else if(entity.getClass()==EnemyBoss.class) {
			int startAni = entity.getAction();
			
			if(entity.isAlive()) {
				if(entity.isHurt()) {
					if(entity.isLeft())  entity.setAction(ENEMY_BOSS_HURT_LEFT);
					if(entity.isRight()) entity.setAction(ENEMY_BOSS_HURT_RIGHT);
				}
				else {
					if(entity.isLeft())  entity.setAction(ENEMY_BOSS_LEFT);
					if(entity.isRight()) entity.setAction(ENEMY_BOSS_RIGHT);
				}
			}
			else entity.setAction(ENEMY_BOSS_DEAD);
			
			if (startAni != entity.getAction()) {
				entity.aniTick = 0;
				entity.aniIndex = 0;
			}
		}
	//////////////PLAYER
		else if(entity.getClass()==Player.class) {
			int startAni = entity.getAction();
			//floating to start point
			if(!entity.isAlive()){
				entity.setAction(DRAGON_DEATH);
				return;
			}
			if(entity.getCurrentLevel().isStart()&&entity.isHitted()&&entity.isAlive()) {
				if(entity.getCurrentLevel().getStartX()<entity.getX())entity.setAction(DRAGON_DAMAGE_LEFT);
				else entity.setAction(DRAGON_DAMAGE_RIGHT);
			
			}
			if(entity.getCurrentLevel().isStart()&&!entity.isHitted()&&entity.isAlive()) entity.setAction(DRAGON_JUMPING);
			if(!entity.isHitted()) {
			//running
				if(entity.isMoving()){
					if(entity.isLeft()) entity.setAction(DRAGON_LEFT);
					else if(entity.isRight())entity.setAction(DRAGON_RIGHT);
				}
				//still
				else{
					if(entity.getLastDirection()==LAST_DIRECTION_RIGHT)entity.setAction(DRAGON_STILL_RIGHT);
					else entity.setAction(DRAGON_STILL_LEFT);
				}
				//jumping or falling
				if(entity.isJumping()||entity.isFalling()) entity.setAction(DRAGON_JUMPING);
				//shooting
				if (entity.isAttacking()) {
					if(entity.getLastDirection()==LAST_DIRECTION_RIGHT) entity.setAction(DRAGON_SHOOT_RIGHT);
					else entity.setAction(DRAGON_SHOOT_LEFT);
				}
			}
			//to prevent flickering
			if (startAni != entity.getAction()) {
				entity.aniTick = 0;
				entity.aniIndex = 0;
			}
		}
	//////////////BUBBLES
		else if(entity.getClass()==Bubble.class) {
			int startAni = entity.getAction();
			if(entity.getKind() == BUBBLE_NORMAL) {
				if(entity.isMoving()) entity.setAction(BUBBLE_NORMAL_SHOOT);
				if(entity.isJumping()) entity.setAction(BUBBLE_NORMAL_UP);
			}
			else if(entity.getKind() == BUBBLE_POWER) {
				if(entity.isMoving()) entity.setAction(BUBBLE_POWER_SHOOT);
				if(entity.isJumping()) entity.setAction(BUBBLE_POWER_UP);
			}
			if (startAni != entity.getAction()) {
				entity.aniTick = 0;
				entity.aniIndex = 0;
			}
		}
		
	}
	
//////////////////////////////////////////
	
	/**
	 * Gives player animation sprite amount for every action.
	 * @param action Used in a switch to return corresponding sprite amount
	 * @return sprite amount (int)
	 */
	default int getPlayerSpriteAmount(int action) {
		switch(action) {
			case DRAGON_STILL_LEFT: 
			case DRAGON_STILL_RIGHT:
				return 1;
			case DRAGON_RIGHT:
			case DRAGON_LEFT:
			case DRAGON_JUMPING :
			case DRAGON_SHOOT_LEFT :
			case DRAGON_SHOOT_RIGHT :
			case DRAGON_DEATH :
			case DRAGON_DAMAGE_LEFT :
			case DRAGON_DAMAGE_RIGHT :
				return 4;
			
			default : return 0;
	
			}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
