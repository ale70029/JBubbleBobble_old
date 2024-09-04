package view.userInterface;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import main.Game;

/**
 * Generic class for all kinds of buttons.
 * Has an x,y positon, width, height, and a Rectangle "bounds" used to detect if mouse collide with button.
 * A statusIndex is used to know if the button is unselected, selected or pressed.
 * A menuIndex is used to know button location in Context controller buttons array, used to navigate with arrows.
 * Buttons dimensions are automatically scaled except for the x, to let you use already scaled x position such as "MenuButton.CENTER".
 * Else you need to manually scale x.
 */
public class Button {
////////////////////////////////////////////////////////////////////////////////////
	public static final int UNSELECTED = 0,SELECTED = 1,PRESSED = 2;

	protected int x,y;
	protected int width,height;
	protected Rectangle bounds;
	
	protected int statusIndex,menuIndex;
	protected boolean selected,pressed;
////////////////////////////////////////////////////////////////////////////////////
//NEW
	
	/**
	 * Creates a new generic button
	 * @param menuIndex index in buttons array where it will be put
	 * @param x width position
	 * @param y height position
	 * @param width width size
	 * @param height height size
	 * Default statusIndex is unselected
	 */
	public Button(int menuIndex,int x, int y, int width, int height) {
		this.menuIndex=menuIndex;
		this.x = x;
		this.y = Game.scale(y);
		this.width = Game.scale(width);
		this.height = Game.scale(height);
		bounds = new Rectangle(this.x,this.y,this.width,this.height);
		statusIndex = UNSELECTED;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Sets status index based on button stats.
	 */
	public void update() {
		if(!selected) statusIndex = UNSELECTED;
		else if (selected) statusIndex = SELECTED;
		else if (pressed) statusIndex = PRESSED;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * To be overrided by subclasses. If not overrided prints an alert and button position.
	 * @param g
	 */
	public void draw(Graphics g) {System.out.println("Button draw method not implemented.\n"+
													 "Button X : "+x+"\n"+
													 "Button Y : "+y);}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Indicates if mouse is colliding with the button.
	 * @param e mouseEvent to get mouse position
	 * @return true if collligin, else false
	 */
	public boolean isIn(MouseEvent e) {
		return bounds.contains(e.getX(), e.getY());
	}
	
//////////////////////////////////////////
	
	/**
	 * Resets button booleans
	 */
	public void resetBools() {
		selected = false;
		pressed = false;
	}
	
//////////////////////////////////////////
	
	public static int scale(int i) {
		return Game.scale(i);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	
	public int getX() {return x;}
	public int getY() {return y;}

//////////////////////////////////////////
	
	public int getWidth() {return width;}
	
	/**
	 * Sets button width and updates bounds
	 * @param width new width
	 */
	public void setWidth(int width) {
		this.width = Game.scale(width);
		bounds = new Rectangle(x,y,this.width,height);
	}
	
//////////////////////////////////////////
	
	public int getHeight() {return height;}
	
	/**
	 * Sets button height and updates bounds
	 * @param height new height
	 */
	public void setHeight(int height) {
		this.height = Game.scale(height);
		bounds = new Rectangle(x,y,width,this.height);
	}
	
//////////////////////////////////////////
	
	public boolean isSelected() {return selected;}
	public void setSelected(boolean selected) {this.selected = selected;}
	
//////////////////////////////////////////
	
	public boolean isPressed() {return pressed;}
	public void setPressed(boolean pressed) {this.pressed = pressed;}
	
//////////////////////////////////////////

	public int getIndex() {return statusIndex;}
	public void setIndex(int index) {this.statusIndex = index;}
	
//////////////////////////////////////////

	public int getMenuIndex() {return menuIndex;}
	public void setMenuIndex(int menuIndex) {this.menuIndex = menuIndex;}

////////////////////////////////////////////////////////////////////////////////////
}
