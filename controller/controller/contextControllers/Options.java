package controller.contextControllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import model.game.Context;
import view.userInterface.ActionButton;
import view.userInterface.Button;
import view.userInterface.MenuButton;


/**
 * Class to manage Options Context.
 ** Audio Options (music , sound, fun mode)
 ** Video Options (background)
 ** Game  Options (difficulty)
 */
public class Options extends GenericContext{
////////////////////////////////////////////////////////////////////////////////////
	private MenuButton backButton;
	private ActionButton musicButton,sfxButton,funButton,difficultButton;
	private Button bk1,bk2,bk3;

////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static Options instance;

	/**
	 * Singleton
	 */
	public static Options getInstance() {
		if(instance==null)
			instance = new Options();
		return instance;
	}
	
	private Options() {
		createButtons();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INIT
	
	private void createButtons() {
		musicButton = new ActionButton(0,scale(10),190,ActionButton.Action.MUSIC);
		sfxButton = new ActionButton(1,scale(10),240,ActionButton.Action.SOUND);
		funButton = new ActionButton(2,scale(10),310,ActionButton.Action.FUN);
		
		
		int bkg_y = 420;
		bk1 = new Button(3,scale(40),bkg_y,150,100);
		bk2 = new Button(4,scale(300),bkg_y,150,100);
		bk3 = new Button(5,scale(550),bkg_y,150,100);
		
		difficultButton = new ActionButton(6,scale(10),600,ActionButton.Action.DIFFICULTY);
		
		backButton= new MenuButton(7,MenuButton.CENTER,679,"back",Context.MENU);
		
		
		buttons = new Button[8];
		buttons[0] = musicButton;
		buttons[1] = sfxButton;
		buttons[2] = funButton;
		
		buttons[3] = bk1;
		buttons[4] = bk2;
		buttons[5] = bk3;
		
		buttons[6] = difficultButton;
		buttons[7] = backButton;
			
	}

////////////////////////////////////////////////////////////////////////////////////
//UPDATE

	/**
	 * Updates buttons.
	 */
	public void update() {
		
		if(toUpdate) {
			index = backButton.getMenuIndex();
			selectButton(buttons,index);
			toUpdate = false;
		}
		for(Button bu: buttons)
			bu.update();
	}

////////////////////////////////////////////////////////////////////////////////////
//DRAW

	/**
	 * Draws background, title, text, buttons.
	 * @param g
	 */
	public void draw(Graphics g) {
		drawBackground(g);
		drawTitle(g);
		drawText(g);
		
		for(Button b:buttons) {
			if(b.getClass()==Button.class)
				 drawBackgroundButton(b,g);
			else b.draw(g);
		}
	}

//////////////////////////////////////////
	
	private void drawTitle(Graphics g) {
		g.setFont(TITLE_80);
		g.setColor(Color.WHITE);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String text = "options";
		int width = metrics.stringWidth(text);
		int x = (WINDOW_WIDTH - width) / 2;
		int y = scale(120);
		g.drawString(text, x, y);
		}
	
//////////////////////////////////////////
	
	private void drawBackgroundButton(Button bk, Graphics g) {
		g.drawImage(getBackground(bk.getMenuIndex()-2), bk.getX(), bk.getY(), bk.getWidth(), bk.getHeight(), null);
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(scale(5)));
		if(bk.isSelected()) {
			g2D.setColor(Color.WHITE);
			g2D.drawRect(bk.getX(), bk.getY(), bk.getWidth(), bk.getHeight());
		}
		else if(bk.isPressed()) {
			g2D.setColor(Color.GREEN);
			g2D.drawRect(bk.getX(), bk.getY(), bk.getWidth(), bk.getHeight());
		}
	}
	
//////////////////////////////////////////
	
	private void drawText(Graphics g) {
		g.setFont(FONT_30);
		g.setColor(Color.WHITE);
		//categories
		g.drawString("AUDIO",scale(20) , scale(170));
		g.drawString("VIDEO",scale(20) , scale(410));
		g.drawString("GAME", scale(20), scale(570));
		
		//sound
		g.setFont(FONT_14);
		g.drawString("Turns music ON and OFF", musicButton.getX()+musicButton.getWidth()+scale(10), musicButton.getY()+scale(25));
		g.drawString("Turns sound ON and OFF", sfxButton.getX()+sfxButton.getWidth()+scale(10), sfxButton.getY()+scale(25));
		
		//bacground
				g.drawString("select background", scale(250), scale(410));
		
		//fun mode
		g.setFont(FONT_12);
		g.drawString("Turns fun ON and OFF. Fun mode lets you",
				funButton.getX()+funButton.getWidth()+scale(10), funButton.getY());
		g.drawString("listen to great 8-bit music!",
				funButton.getX()+funButton.getWidth()+scale(10), funButton.getY()+scale(14));
		g.drawString("If fun is OFF you will only listen to the",
				funButton.getX()+funButton.getWidth()+scale(10),funButton.getY()+scale(28));
		g.drawString("original Bubble Bobble theme song",
				funButton.getX()+funButton.getWidth()+scale(10),funButton.getY()+scale(42));
		
		//difficulty
		g.setColor(Color.GREEN);
		g.drawString("Easy : score*1, if you die",
				difficultButton.getX()+difficultButton.getWidth()+scale(10), difficultButton.getY()-scale(2));
		g.drawString("you retry last level with full life",
				difficultButton.getX()+difficultButton.getWidth()+scale(95), difficultButton.getY()+scale(12));
		g.setColor(Color.BLUE);
		g.drawString("Normal : score*2, if you die",
				difficultButton.getX()+difficultButton.getWidth()+scale(10), difficultButton.getY()+scale(26));
		g.drawString("you retry last level with 2 lifes",
				difficultButton.getX()+difficultButton.getWidth()+scale(120), difficultButton.getY()+scale(40));
		g.setColor(Color.RED);
		g.drawString("hard : score*3, if you die",
				difficultButton.getX()+difficultButton.getWidth()+scale(10), difficultButton.getY()+scale(54));
		g.drawString("you restart the game",
				difficultButton.getX()+difficultButton.getWidth()+scale(95), difficultButton.getY()+scale(68));
	}

////////////////////////////////////////////////////////////////////////////////////
//INPUT
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if(index-1<0) index=buttons.length-1;
			else if(index>=3 && index <=5) index=2;
			else index--;
			selectButton(buttons,index);
			return;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(index+1>=buttons.length) index=0;
			else if(index>=3 && index <=5) index=6;
			else index++;
			selectButton(buttons,index);
			return;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(index<3 || index>5) return;
			else if(index==3) index =5;
			else index--;
			selectButton(buttons,index);
			return;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(index<3 || index>5) return;
			else if(index==5) index =3;
			else index++;
			selectButton(buttons,index);
			return;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				if(index==musicButton.getMenuIndex()) {
					musicButton.switchSound();
					return;
				}
				else if(index==sfxButton.getMenuIndex()) {
					sfxButton.switchSound();
					return;
				}
				else if(index==funButton.getMenuIndex()) {
					funButton.switchMode();
					return;
				}
				else if(index==bk1.getMenuIndex()) {
					GenericContext.setBackground(1);
					return;
				}
				else if(index==bk2.getMenuIndex()) {
					GenericContext.setBackground(2);
					return;
				}
				else if(index==bk3.getMenuIndex()) {
					GenericContext.setBackground(3);
					return;
				}
				else if(index==difficultButton.getMenuIndex()) {
					difficultButton.changeDifficulty();
					return;
				}
				else if (index == backButton.getMenuIndex()) {
					backButton.applyContext();
					return;
				}
				
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			backButton.applyContext();
			return;
		}
	}

//////////////////////////////////////////
	
	public void mouseMoved(MouseEvent e) {
		for(Button b : buttons)
			b.resetBools();
		
		for (Button b : buttons) 
			if (b.isIn(e)) 
				b.setSelected(true);	
	}
	
//////////////////////////////////////////
	
	public void mouseReleased(MouseEvent e) {
		//audio
		if(musicButton.isIn(e)) {
			if(musicButton.isPressed()) {
				musicButton.switchSound();
				return;
			}
		}
		else if(sfxButton.isIn(e)) {
			if(sfxButton.isPressed()) {
				sfxButton.switchSound();
				return;
			}
		}
		else if(funButton.isIn(e)) {
			if(funButton.isPressed()) {
				funButton.switchMode();
				return;
			}
		}
		//video
		else if(bk1.isIn(e)) {
			if(bk1.isPressed()) {
				GenericContext.setBackground(1);
				return;
			}
		}
		else if(bk2.isIn(e)) {
			if(bk2.isPressed()) {
				GenericContext.setBackground(2);
				return;
			}
		}
		else if(bk3.isIn(e)) {
			if(bk3.isPressed()) {
				GenericContext.setBackground(3);
				return;
			}
		}
		//game
		else if(difficultButton.isIn(e)) {
			if(difficultButton.isPressed()) {
				difficultButton.changeDifficulty();
				return;
			}
		}
		//menuButtons
		else if(backButton.isIn(e)) {
			if(backButton.isPressed()) {
				backButton.applyContext();
				return;
			}
		}
	}

//////////////////////////////////////////
	
	public void mousePressed(MouseEvent e) {
		for(Button b : buttons)
			if(b.isIn(e))
				b.setPressed(true);
	}

////////////////////////////////////////////////////////////////////////////////////
	
	
}
