package view.userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Button specifically designed for sorting fields.
 * Changes color based on type of sorting or selection.
 */
public class SortButton extends Button{
////////////////////////////////////////////////////////////////////////////////////
	private boolean current,order,up;
	private Font font;
	private String field, basicField;
////////////////////////////////////////////////////////////////////////////////////
//NEW

	public SortButton(int menuIndex, int x, int y,String field) {
		super(menuIndex, scale(x), y, 21*field.length(), 20);
		this.field=field;
		this.basicField=field;
		font = new Font("Arcade Normal", Font.TRUETYPE_FONT, scale(16)); 
	}

////////////////////////////////////////////////////////////////////////////////////
//UPDATE
	
	/**
	 * Updates button stats based on current sorting or selection.
	 */
	@Override
	public void update() {
		if(!current) {
			statusIndex = UNSELECTED;
			field = basicField;
		}
		else if(current&&order) up = true;
		else if(current&&!order) up = false;
		if(selected) statusIndex = SELECTED;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws the field name and a "+" if its sorting up or a "-" if its sorting down. Only the name if its not sorting.
	 * If selected draws a rectangle.
	 */
	public void draw(Graphics g) {	
		g.setFont(font);
		
		g.setColor(Color.WHITE);
		
		if (current&&up) {
			g.setColor(Color.GREEN);
			field = basicField+"+";
		}
		
		else if(current&&!up) {
			g.setColor(Color.RED);
			field = basicField+"-";
		}
		
		if(statusIndex==SELECTED) { 
			g.drawRect(x, y, width, height);
		}
		
		g.drawString(field, x, y+scale(16));
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Change order direction e sets as current.
	 */
	public void switchOrder() {
		order = !order;
		current = true;
	}

////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	
	public boolean isCurrent() {return current;}
	public void setCurrent(boolean current) {this.current = current;}
	
//////////////////////////////////////////

	public boolean getOrder() {return order;}
	public void setOrder(boolean order) {this.order = order;}
	
////////////////////////////////////////////////////////////////////////////////////
}
