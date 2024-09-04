package model.collectables;
/**
 * Class to create Powers. Powers give the player special abilities such as shield, extra life of fast movement.
 * If a Power is collected when is already equipped it gives points instead.
 */
public class Power extends Collectable{
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Index to differentiate Powers
	 */
	public static final int SHIELD = 0,LIFE = 1,FAST = 2;
	/**
	 * Number of different kind of Powers that can be created.
	 */
	public static final int MAX_POWERS = 3;
////////////////////////////////////////////////////////////////////////////////////
//NEW	
	
	/**
	 * Creates a new Power.
	 * @param x width index
	 * @param y height index
	 * @param kind kind of power, points are assigned specifically with a switch on this
	 */
	public Power(int x, int y, int kind) {
		super(x, y,kind);
		switch(kind) {
		case SHIELD: 
			super.points = 300;
			break;
		case LIFE:
			super.points = 1000;
			break;
		case FAST:
			super.points = 600;
			break;
		default:
			break;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
