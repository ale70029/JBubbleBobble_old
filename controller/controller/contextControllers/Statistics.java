package controller.contextControllers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import controller.controllers.ProfilesController;
import model.game.Context;
import model.game.Profile;
import view.userInterface.Button;
import view.userInterface.LoadSlot;
import view.userInterface.MenuButton;
import view.userInterface.SortButton;

/**
 * Class to manage Statistics context.
 * Shows profiles levels, highscores, winned and lost games.
 */
public class Statistics extends GenericContext{
//////////////////////////////////////////
	public static final int NAME=0, LEVEL=1, SCORE=2, WIN=3, LOST=4, TOT=5;
	private MenuButton backButton;
	private LoadSlot preview;
	private SortButton nameSort, levelSort, scoreSort, winSort, lostSort,totSort,current;
	
	List<Profile> profiles;
	private boolean empty;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Statistics instance;
	
	/**
	 * Singleton.
	 */
	public static Statistics getInstance() {
		if(instance==null)
			instance = new Statistics();
		return instance;
	}
	
	private Statistics() {

		nameSort = new SortButton(0,10,110,"Nickname");
		levelSort = new SortButton(1,230,110,"Level");
		scoreSort = new SortButton(2,360,110,"score");
		winSort = new SortButton(3,490,110,"win");
		lostSort = new SortButton(4,560,110,"lost");
		totSort = new SortButton(5,650,110,"tot");
		backButton= new MenuButton(6,MenuButton.CENTER,679,"back",Context.MENU);
		
		buttons = new Button[7];
		buttons[0] = nameSort;
		buttons[1] = levelSort;
		buttons[2] = scoreSort;
		buttons[3] = winSort;
		buttons[4] = lostSort;
		buttons[5] = totSort;
		buttons[6] = backButton;
		
		preview = new LoadSlot(-2, LoadSlot.CENTER, 590, -2);
	}
	
////////////////////////////////////////////////////////////////////////////////////
//UPDATE

	/**
	 * Updates buttons and profiles.
	 */
	public void update() {
		if(toUpdate) {
			profiles = ProfilesController.getInstance().getProfiles();
			empty = profiles.isEmpty();
			//if there are profiles they're sorted by score by default and scoreSort is selected
			if(!empty) {
				index = scoreSort.getMenuIndex();
				selectButton(buttons,index);
				scoreSort.switchOrder();
				resetOthersSorts(scoreSort);
				profiles = ProfilesController.getInstance().sortByScore(scoreSort.getOrder());
			}
			//if there are no profiles back button is selected
			else {
				index = backButton.getMenuIndex();
				selectButton(buttons,index);
			}
			toUpdate = false;
		}
		for(Button b : buttons)
			b.update();
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW
	
	/**
	 * Draws backgrouns, title, alert (no profile to show) or stats, and buttons.
	 * @param g
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		drawTitle(g);
		if(empty) drawAlert(g);
		else {
			preview.draw(g);
			drawStats(g);
		}
		for(Button b : buttons)
			b.draw(g);
		drawStuff(g);
		
	}

//////////////////////////////////////////
	
	private void drawStuff(Graphics g) {
		//line below fields
		g.setColor(Color.WHITE);
		g.drawLine(scale(10), scale(140), scale(725),scale(140));
	}

//////////////////////////////////////////
	
	private void drawAlert(Graphics g) {

		g.setFont(FONT_24);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "no data to show";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		g.setColor(Color.RED);
		g.drawString(text, x, scale(300));
		
		String text2 = "play at least one game";
		int width2 = metrics.stringWidth(text2);
		int x2 = (WINDOW_WIDTH - width2) / 2;
		g.setColor(Color.WHITE);
		g.drawString(text2, x2, scale(340));
		
	}
	
//////////////////////////////////////////
	
	private void drawStats(Graphics g) {
		//draw table header
		g.setColor(Color.WHITE);
		g.setFont(FONT_16);
	
		//draw table
		int totalWin=0;
		int totalLost=0;
		int totalGame =0;
		for(int i=0;i<profiles.size(); i++) {
			Profile p = profiles.get(i);
			int y = scale(i*70 +190);
			g.drawImage(AVATARS[p.getAvatar()], scale(20), scale(i*70) +scale(160), scale(50), scale(50), null);
			g.setColor(Color.WHITE);
			g.drawLine(scale(10), scale(i*70 + 220), scale(725),scale(i*70 +220));
			g.drawString(p.getNickname(), scale(80), y);
			g.setColor(Color.BLUE);
			g.drawString(""+p.getLastLevel(), scale(255), y);
			g.setColor(Color.YELLOW);
			g.drawString(""+p.getHighScore(), scale(360), y);
			g.setColor(Color.GREEN);
			
			totalWin += p.getWin();
			g.drawString(""+totalWin, scale(510), y);
			g.setColor(Color.RED);
			
			totalLost += p.getLost();
			g.drawString(""+totalLost, scale(590), y);
			g.setColor(Color.WHITE);
			
			totalGame = p.getTotal();
			g.drawString(""+totalGame, scale(660), y);
			
			totalWin = 0;
			totalLost = 0;
			totalGame = 0;
			
			
		}
		//draw player
		Profile best = profiles.get(0);
		g.drawImage(AVATARS[best.getAvatar()], preview.getX()+scale(3), preview.getY()+scale(4), scale(70), scale(70), null);
		
		//instruction
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "select a field to order and see";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		
		g.drawString(text, x, scale(560));
		String text2 = "best OR worst player";
		int width2 = metrics.stringWidth(text2);
		int x2 = (WINDOW_WIDTH - width2) / 2;
	
		//draw nicknamer
		g.drawString(text2, x2, scale(580));
		g.setFont(FONT_24);
		g.setColor(current.getOrder() ? Color.GREEN : Color.RED);
		g.drawString(best.getNickname(), preview.getX()+scale(100), preview.getY()+scale(50));	
		
		
		//draw field 
		String text3 = switch(current.getMenuIndex()) {
			case NAME -> "";
			case LEVEL -> best.getLastLevel() + "";
			case SCORE -> best.getHighScore() + "";
			case WIN -> best.getWin() + "";
			case LOST -> best.getLost() + "";
			case TOT -> best.getTotal() + "";
			default -> "";
		};
		g.drawString(text3, preview.getX()+scale(380), preview.getY()+scale(50));
		g.setColor(Color.WHITE);
	}
	
//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_65);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "statistics";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(90);
		g.drawString(text, x, y);
		}

////////////////////////////////////////////////////////////////////////////////////
//INPUT
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			backButton.applyContext();
			//so next time you enter stats is gonna be ordered properly
			scoreSort.setOrder(false);
			return;
		}
	
		////
		else if (e.getKeyCode()== KeyEvent.VK_ENTER) {
			if(nameSort.isSelected()) {
				nameSort.switchOrder();
				profiles = ProfilesController.getInstance().sortByName(nameSort.getOrder());
				resetOthersSorts(nameSort);
				return;
			}
			
			else if(levelSort.isSelected()) {
				levelSort.switchOrder();
				profiles = ProfilesController.getInstance().sortByLevel(levelSort.getOrder());
				resetOthersSorts(levelSort);
				return;
			}
			
			else if(scoreSort.isSelected()) {
				scoreSort.switchOrder();
				profiles = ProfilesController.getInstance().sortByScore(scoreSort.getOrder());
				resetOthersSorts(scoreSort);
				return;
			}
			
			else if(winSort.isSelected()) {
				winSort.switchOrder();
				profiles = ProfilesController.getInstance().sortByWin(winSort.getOrder());
				resetOthersSorts(winSort);
				return;
			}
			
			else if(lostSort.isSelected()) {
				lostSort.switchOrder();
				profiles = ProfilesController.getInstance().sortByLost(lostSort.getOrder());
				resetOthersSorts(lostSort);
				return;
			}
			
			else if(totSort.isSelected()) {
				totSort.switchOrder();
				profiles = ProfilesController.getInstance().sortByTotal(totSort.getOrder());
				resetOthersSorts(totSort);
				return;
			}
			
			else if(backButton.isSelected()) 
				backButton.applyContext();
			
		}
		////
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(index==backButton.getMenuIndex()) index = scoreSort.getMenuIndex();
			else index=backButton.getMenuIndex();
			selectButton(buttons,index);
			return;
		}

		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(index==nameSort.getMenuIndex()) index = totSort.getMenuIndex();
			else index--;
			selectButton(buttons,index);
			return;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(index==totSort.getMenuIndex()) index = nameSort.getMenuIndex();
			else index++;
			selectButton(buttons, index);
			return;
		}
		
	
	}
	
//////////////////////////////////////////
	
	public void mouseMoved(MouseEvent e) {
		
		for(Button b : buttons)
			b.resetBools();
		
		for(Button b : buttons)
			if (b.isIn(e)) 
				b.setSelected(true);
	}
	
//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		
		
		for(Button b : buttons)
			if (b.isIn(e)) 
				if(b.isPressed()) {
					if(b.getMenuIndex()==nameSort.getMenuIndex()) {
						nameSort.switchOrder();
						profiles = ProfilesController.getInstance().sortByName(nameSort.getOrder());
						resetOthersSorts(nameSort);
						return;
					}
					
					else if(b.getMenuIndex()==levelSort.getMenuIndex()) {
						levelSort.switchOrder();
						profiles = ProfilesController.getInstance().sortByLevel(levelSort.getOrder());
						resetOthersSorts(levelSort);
						return;
					}
					
					else if(b.getMenuIndex()==scoreSort.getMenuIndex()) {
						scoreSort.switchOrder();
						profiles = ProfilesController.getInstance().sortByScore(scoreSort.getOrder());
						resetOthersSorts(scoreSort);
						return;
					}
					
					else if(b.getMenuIndex()==winSort.getMenuIndex()) {
						winSort.switchOrder();
						profiles = ProfilesController.getInstance().sortByWin(winSort.getOrder());
						resetOthersSorts(winSort);
						return;
					}
					
					else if(b.getMenuIndex()==lostSort.getMenuIndex()) {
						lostSort.switchOrder();
						profiles = ProfilesController.getInstance().sortByLost(lostSort.getOrder());
						resetOthersSorts(lostSort);
						return;
					}
					
					else if(b.getMenuIndex()==totSort.getMenuIndex()) {
						totSort.switchOrder();
						profiles = ProfilesController.getInstance().sortByTotal(totSort.getOrder());
						resetOthersSorts(totSort);
						return;
					}
					
					else if(b.getMenuIndex()==backButton.getMenuIndex()) {
						backButton.applyContext();
						scoreSort.setOrder(false);
	
					}
				}
		
		
	}
	
//////////////////////////////////////////

	public void mousePressed(MouseEvent e) {
		
		for(Button b : buttons)
			if (b.isIn(e)) 
				if(b.isSelected())
					b.setPressed(true);
	}
//////////////////////////////////////////
	
	/**
	 * Sets all other buttons than "sort" current=false and order=false.
	 * Also saves sort as current SortButton
	 * @param sort button to not reset
	 */
	private void resetOthersSorts(SortButton sort) {
		for(Button b : buttons)
			if(b.getMenuIndex()!=sort.getMenuIndex() && b.getClass()==SortButton.class ) {
				SortButton s = (SortButton) b;
				s.setCurrent(false);
				s.setOrder(false);
			
			if(b.getClass()!=MenuButton.class) this.current = sort;
			}
	}
////////////////////////////////////////////////////////////////////////////////////
}
