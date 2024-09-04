package model.collectables;

import java.util.ArrayList;
import java.util.List;
/*
 * Class used to collect collectables, items and powers togheter.
 * Singleton pattern is adopted.
 */
public class CollectablesContainer {
////////////////////////////////////////////////////////////////////////////////////
	private List<Collectable> collectables;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static CollectablesContainer instance;
	/**
	 * Singleton
	 */
	public static CollectablesContainer getInstance() {
		if(instance==null)
			instance = new CollectablesContainer();
		return instance;
	}
	
	private CollectablesContainer() {
		this.collectables = new ArrayList<Collectable>();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Adds a new Item to the collectables list.
	 * @param x width position.
	 * @param y height position.
	 * @param kind kind of item. See further information in Item class.
	 */
	public void addItem(int x, int y, int kind) {
		Item item = new Item(x,y,kind);
		item.setFalling(true);
		collectables.add(item);
		
		}
	/**
	 * Adds an already created Item to the collectables list.
	 * @param item
	 */
	public void addItem(Item item) {
		item.setFalling(true);
		collectables.add(item);
}
	
//////////////////////////////////////////
	
	/**
	 * Adds a new Power to the collectables list.
	 * @param x width position.
	 * @param y height position.
	 * @param kind kind of power. See further information in Power class.
	 */

	public void addPower(int x, int y, int kind) {
		Power power = new Power(x,y,kind);
		power.setFalling(true);
		collectables.add(power);
	}
	
	/**
	 * Adds an already created Power to the collectables list.
	 * @param item
	 */
	public void addPower(Power power) {
		power.setFalling(true);
		collectables.add(power);
		}
	
////////////////////////////////////////////////////////////////////////////////////
// GET - SET

	public List<Collectable> getCollectables() {return collectables;}
	
////////////////////////////////////////////////////////////////////////////////////
}
