package controller.controllers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import controller.filesManagers.AudioManager;
import controller.filesManagers.FileManager;
import main.Game;
import model.player.Bubble;
import model.player.BubblesContainer;
import model.player.Player;

/**
 * Class to manage Bubbles (update and draw)
 */
public class BubblesController implements AnimationController{
//////////////////////////////////////////
	private BufferedImage[][] bubbleAnimations;

	private BubblesContainer bubblesContainer;
	private Player player;
//////////////////////////////////////////
//SINGLETON
	
	private static BubblesController instance;
	
	/**
	 * Singleton.
	 */
	public static BubblesController getInstance() {
		if(instance==null)
			instance = new BubblesController();
		return instance;
	}
	
	private BubblesController() {
		this.player=Player.getInstance();
		this.bubblesContainer = BubblesContainer.getInstance();
		this.bubbleAnimations = FileManager.BUBBLE_ANIMATIONS;
	}
	
//////////////////////////////////////////
//UPDATE & DRAW
	
	/**
	 * Updates bubbles in game.
	 * Bubbles are created if player is shooting, moved into space and animation is updated.
	 */
	public void update() {
		if(player.isShooting()) {
			if(bubblesContainer.createBubble())
				AudioManager.getInstance().playSound(AudioManager.SHOOT_SOUND);
			player.setShooting(false);
			
		}
		try {
			for(Bubble bubble:bubblesContainer.getBubbles()) {
				if(!bubble.isToRemove()) {
					bubble.controlBubble();
					updateAnimation(bubble,BUBBLE_MAX_SPRITES);
					setAnimation(bubble);
				}
				else {
					bubblesContainer.getBubbles().remove(bubble);
					break;
				}
			}
		}catch(ConcurrentModificationException e) {}; //likely to be thrown beacuse of array modifications during for loops
		
	}
	
//////////////////////////////////////////
	
	/**
	 * Draws bubble with Graphics, images are taken from bubbleAnimations depending on current action and animation index.
	 * @param g
	 */
	public void draw(Graphics g){
		try {
			for(Bubble bubble:bubblesContainer.getBubbles()) {
				if(!bubble.isToRemove()) {
					g.drawImage(bubbleAnimations[bubble.getAction()][bubble.getAniIndex()],
							Game.scale(bubble.getX()), Game.scale(bubble.getY()), Game.scale(bubble.getSize()), Game.scale(bubble.getSize()), null);
				}
			}
		}catch(ConcurrentModificationException e) {} //likely to be thrown beacuse of array modifications during for loops
		
	}

////////////////////////////////////////////////////////////////////////////////////
	
}
