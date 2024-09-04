package model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.player.Player;

/**
 * Class for player profile, with a nickname and an avatar selection.
 * Stores last played level, last life point, highest score, last score, number of winned and losted games.
 * Also a list with last MAX_SCORES scores.
 * 
 * Only MAX_PROFILES are allowed in game due to technical reasons.
 */
public class Profile{
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Max number of profiles that can exist simultaneously.
	 * 
	 */
	public static final int MAX_PROFILES = 5;
	/**
	 * Max number of scores to be readed from the scores list.
	 */
	public static final int MAX_SCORES = 5;
	
	private String nickname;
	private int avatar;
	
	private int lastLevel,lastHealth,highScore,lastScore,win,lost;
	private List<Integer> scores;

////////////////////////////////////////////////////////////////////////////////////
//NEW 
	
	/**
	 * Creates a new profile with name and avatar.
	 * Initialize score list and last healt as MAX_HEALTH
	 * @param nickname profile name
	 * @param avatar profile picture index
	 */
	public Profile(String nickname, int avatar) {
		this.nickname=nickname;
		this.avatar=avatar;
		this.scores = new ArrayList<Integer>();
		this.lastHealth = Player.MAX_HEALTH;

	}
	
//////////////////////////////////////////
	
	/**
	 * Creates an empty profile
	 */
	public Profile() {} //needed when reading saveFile
	
////////////////////////////////////////////////////////////////////////////////////
//UTILS 
	
	/**
	 * Update scores list with given score, then orders the list in reverse order (best scores first).
	 * If given score is higher than HighScore, highscore is updated.
	 * @param score
	 */
	public void addScore(int score) {
		lastScore = score;
		if(score>highScore) setHighScore(score);
		
		boolean present = scores.contains(score);
		if(present) return;
		else
			scores.add(score);
		//reversed sort to get highest scores first
		scores.sort(Collections.reverseOrder());
	}
	
//////////////////////////////////////////
	
	/**
	 * Gives the highest 5 scores from the scores list.
	 * @return int[] maxScores
	 */
	public int[] getMaxScores() {
		int maxIndex;
		if(scores.size()<MAX_SCORES) maxIndex = scores.size();
		else maxIndex = MAX_SCORES;
		int[] maxScores = new int[maxIndex];
		
		for(int i =0; i<maxIndex;i++) 
			maxScores[i] = (int) scores.get(i);
			
		return maxScores;
	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	
	public String getNickname() {return nickname;}
	public void setNickname(String nickname) {this.nickname = nickname;}
	
//////////////////////////////////////////
	
	public int getAvatar() {return avatar;}
	public void setAvatar(int avatar) {this.avatar = avatar;}
	
//////////////////////////////////////////
	
	public int getLastLevel() {return lastLevel;}
	public void setLastLevel(int lastLevel) {this.lastLevel = lastLevel;}
	
//////////////////////////////////////////

	public int getHighScore() {return highScore;}
	public void setHighScore(int highScore) {this.highScore = highScore;}
	
//////////////////////////////////////////
	
	public int getLastScore() {return lastScore;}
	public void setLastScore(int lastScore) {this.lastScore = lastScore;}
	
//////////////////////////////////////////
	
	public void setScores(List<Integer> scores) {this.scores = scores;}
	
//////////////////////////////////////////

	public int getLastHealth() {return lastHealth;}
	public void setLastHealth(int lastHealth) {this.lastHealth = lastHealth;}
	
//////////////////////////////////////////

	public int getWin() { return win;}
	public void setWin(int win) {this.win = win;}
	
//////////////////////////////////////////
	
	public int getLost() {return lost;}
	public void setLost(int lost) {this.lost = lost;}
	
//////////////////////////////////////////
	
	public int getTotal() {return win + lost; }

////////////////////////////////////////////////////////////////////////////////////
}
