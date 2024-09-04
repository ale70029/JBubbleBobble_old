package controller.controllers;

import static model.collectables.Collectable.COLLECTABLE_SIZE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import controller.filesManagers.AudioManager;
import controller.filesManagers.FileManager;
import main.Game;
import model.collectables.Collectable;
import model.collectables.CollectablesContainer;
import model.collectables.Item;
import model.collectables.Power;
import model.player.Player;

/**
 * Class to manage Collectables (update and draw)
 */
public class CollectablesController {
////////////////////////////////////////////////////////////////////////////////////
	//images
	private BufferedImage[] itemsImages,powersImages;
	//controll
	private CollectablesContainer collectablesContainer;

////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static CollectablesController instance;
	
	/**
	 * Singleton.
	 */
	public static CollectablesController getInstance() {
		if(instance==null)
			instance = new CollectablesController();
		return instance;
	}
	
	private CollectablesController() {
		collectablesContainer = CollectablesContainer.getInstance();
		itemsImages = FileManager.ITEMS;
		powersImages = FileManager.POWERS;
		
	}

////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates collectables.
	 * When a collectable is taken, a sound is played, points are given (or power) and collectable is deleted.
	 */
	public void updateCollectables() {
		try {
			for(Collectable collectable:collectablesContainer.getCollectables()) {
				collectable.updateStatus();
				if(collectable.isTaken()) {
					Player player = Player.getInstance();
					if(collectable.getClass()==Item.class) {
						Item item = (Item)collectable;
						player.setScore(player.getScore()+(item.getPoints()*player.getMultiplier()));
						AudioManager.getInstance().playSound(AudioManager.ITEM_SOUND);
					}
					else if(collectable.getClass()==Power.class) {
						Power power = (Power)collectable;
						collectable.getPlayer().applyPowerUp(power);
						AudioManager.getInstance().playSound(AudioManager.POWER_SOUND);
					}
					collectablesContainer.getCollectables().remove(collectable);
					break;
				}
			}
		}catch (ConcurrentModificationException e) {}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draw collectables.
	 * @param g Graphics engine
	 */
	public void draw(Graphics g){
		try {
			for(Collectable collectable:collectablesContainer.getCollectables()) {
				if(collectable.getClass()==Item.class) {
					g.drawImage(itemsImages[collectable.getKind()], Game.scale(collectable.getX()), Game.scale(collectable.getY()),
							Game.scale(COLLECTABLE_SIZE), Game.scale(COLLECTABLE_SIZE),null);
				}
				else if (collectable.getClass()==Power.class) {
					g.drawImage(powersImages[collectable.getKind()], Game.scale(collectable.getX()), Game.scale(collectable.getY()),
							Game.scale(COLLECTABLE_SIZE), Game.scale(COLLECTABLE_SIZE),null);
				}
			}
		}catch(ConcurrentModificationException e) {} //likely to be thrown beacuse of array modifications during for loops
		
		}

////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////
}
