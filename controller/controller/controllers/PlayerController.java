package controller.controllers;

import static model.player.Player.PLAYER_SIZE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.filesManagers.AudioManager;
import controller.filesManagers.FileManager;
import main.Game;
import model.player.Player;


/**
 * Class to manage player (update and draw)
 */
public class PlayerController implements AnimationController{
////////////////////////////////////////////////////////////////////////////////////
	private static final int SHIELD_SIZE = 100;

	private Player player;
	
	private BufferedImage[][] playerAnimations;
	private BufferedImage shield;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static PlayerController instance;
	
	/**
	 * Singleton.
	 */
	public static PlayerController getInstance() {
		if(instance==null)
			instance = new PlayerController();
		return instance;
	}
	
	private PlayerController() {
		this.player= Player.getInstance();
		playerAnimations = FileManager.PLAYER_ANIMATIONS;
		shield = FileManager.SHIELD;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Update player stats, animations and sound effect.
	 */
	public void update() {
		player.updatePlayer();
		updateAnimation(player,getPlayerSpriteAmount(player.getAction()));
		setAnimation(player);
		updateSoundEffects();
	}
	
//////////////////////////////////////////
	
	private void updateSoundEffects() {
		if(player.isHitted()&&player.isAlive()) AudioManager.getInstance().playSound(AudioManager.HURT_SOUND);
		if(!player.isAlive()) AudioManager.getInstance().playSound(AudioManager.PLAYER_DEATH_SOUND);
		if(player.isJumping()) AudioManager.getInstance().playSound(AudioManager.JUMP_SOUND);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws player animations
	 * @param g Graphics engine
	 */
	public void draw(Graphics g) {
		if(player.getAniIndex()<getPlayerSpriteAmount(player.getAction()))
			g.drawImage(playerAnimations[player.getAction()][player.getAniIndex()], Game.scale(player.getX()), Game.scale(player.getY()),
					Game.scale(PLAYER_SIZE),Game.scale(PLAYER_SIZE), null);
		if(player.isShield())
			g.drawImage(shield,Game.scale(player.getX()-16), Game.scale(player.getY()-16),Game.scale(SHIELD_SIZE),Game.scale(SHIELD_SIZE),null);
	}
	
////////////////////////////////////////////////////////////////////////////////////
// GET - SET
	
	public Player getPlayer() {return player;}
	
////////////////////////////////////////////////////////////////////////////////////	
}
