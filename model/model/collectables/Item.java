package model.collectables;
/**
 * Class to create an Item. An item gives points when collected.
 */
public class Item extends Collectable {
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Number of different kind of Item that can be created.
	 */
	public static final int MAX_ITEMS =10;
////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	/**
	 * Creates a new Item.
	 * @param x width position.
	 * @param y height position.
	 * @param kind kind of item. Item points will be calculated on this.
	 */
	public Item(int x, int y,int kind) {
		super(x, y, kind);
		super.points = kind*100 + 100;
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
