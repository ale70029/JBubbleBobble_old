package controller.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import controller.filesManagers.FileManager;
import model.game.Level;
import model.game.Profile;
import model.player.Player;

@SuppressWarnings("deprecation")
/**
 * Class to manage profiles (add, save, load)
 * Observes LevelController to save last level, and Player to save scores and health.
 */
public class ProfilesController implements Observer{
////////////////////////////////////////////////////////////////////////////////////
	private List<Profile> profiles;
	private Profile currentProfile;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static ProfilesController instance;
	
	/**Singleton.
	 *
	 */
	public static ProfilesController getInstance() {
		if(instance==null)
			instance = new ProfilesController();
		return instance;
	}
	
	private ProfilesController() {
		profiles = new ArrayList<Profile>();
		loadProfiles();
		firstStart();
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INIT 
	
	/**
	 * Needed until a profile is loaded.
	 */
	private void firstStart() {
		Profile start = new Profile("Start",0);
		start.setHighScore(0);
		start.setLastLevel(0);
		start.setLastScore(0);
		currentProfile=start;
	}

////////////////////////////////////////////////////////////////////////////////////
//UTILITIES
	
	/**
	 * Adds a new profile with given nickname and avatar index and sets it as current, then saves it.
	 * @param nickname
	 * @param avatar index of avatars images array.
	 */
	public void addProfile(String nickname, int avatar) {
		Profile profile = new Profile(nickname,avatar);
		profile.setLastLevel(1);
		profiles.add(profile);
		setCurrentProfile(profile);
		saveCurrentProfile();
	}
//////////////////////////////////////////
	
	/**
	 * Save current profiles (calls FileManager saveProfile method)
	 */
	public void saveCurrentProfile() {
		FileManager.saveProfile(currentProfile);
	}
	
//////////////////////////////////////////
	
	/**
	 * Loads profiles from FileManager loadProfiles method
	 */
	public void loadProfiles() {
		profiles = FileManager.loadProfiles();
	}
	
//////////////////////////////////////////
	
	/**
	* Updates profiles list and returns it
	* @return profiles
	*/
	public List<Profile> updateProfiles() {
		profiles.clear();
		loadProfiles();
		return profiles;
	}

//////////////////////////////////////////
	
	/**
	* Updates lost count and saves
	*/
	public void addLost() {
		currentProfile.setLost(currentProfile.getLost()+1);
		saveCurrentProfile();
	}

//////////////////////////////////////////

	/**
	* Updates win count and saves
	*/
	public void addWin() {
		currentProfile.setWin(currentProfile.getWin()+1);
		saveCurrentProfile();
	}	

////////////////////////////////////////////////////////////////////////////////////
//SORTING
	
	/**
	 * Sorts profiles by score. If order is true, they are sorted from highest to lowest, else from lowest to highest
	 * @param order to decide how to sort
	 * @return a sorted List<Profile>
	 */
	public List<Profile> sortByScore(boolean order) {
        List<Profile> sorted = 
        	profiles.stream()
            .sorted(order ? Comparator.comparingInt(Profile::getHighScore).reversed()
                          : Comparator.comparingInt(Profile::getHighScore) )
            .collect(Collectors.toList());
        return sorted;
    }
	
//////////////////////////////////////////
	
	/**
	 * Sorts profiles by name. If order is true, they are sorted in alphabetic order ( a,b,c..) else reversed
	 * @param order to decide how to sort
	 * @return a sorted List<Profile>
	 */
	public List<Profile> sortByName(boolean order) {
        List<Profile> sorted = 
        	profiles.stream()
            .sorted(order ? Comparator.comparing(Profile::getNickname)
                          : Comparator.comparing(Profile::getNickname).reversed() )
            .collect(Collectors.toList());

        return sorted;
    }
	
//////////////////////////////////////////
	
	/**
	 * Sorts profiles by level. If order is true, they are sorted in descending order ( 10,6,2..) else reversed
	 * @param order to decide how to sort
	 * @return a sorted List<Profile>
	 */
	public List<Profile> sortByLevel(boolean order) {
        List<Profile> sorted = 
        	profiles.stream()
        	.sorted(order ? Comparator.comparingInt(Profile::getLastLevel).reversed()
        				  : Comparator.comparingInt(Profile::getLastLevel) )
            .collect(Collectors.toList());

        return sorted;
    }
	
//////////////////////////////////////////
	
	/**
	 * Sorts profiles by won games. If order is true, they are sorted in descending order ( 10,6,2..) else reversed
	 * @param order to decide how to sort
	 * @return a sorted List<Profile>
	 */
	public List<Profile> sortByWin(boolean order) {
        List<Profile> sorted =
        	profiles.stream()
        	.sorted(order ? Comparator.comparingInt(Profile::getWin).reversed()
        				  : Comparator.comparingInt(Profile::getWin) )
        	.collect(Collectors.toList());

        return sorted;
    }
	
//////////////////////////////////////////
	
	/**
	 * Sorts profiles by lost games. If order is true, they are sorted in descending order ( 10,6,2..) else reversed
	 * @param order to decide how to sort
	 * @return a sorted List<Profile>
	 */
	public List<Profile> sortByLost(boolean order) {
        List<Profile> sorted = 
        	profiles.stream()
        	.sorted(order ? Comparator.comparingInt(Profile::getLost).reversed()
        				  : Comparator.comparingInt(Profile::getLost) )
            .collect(Collectors.toList());
        return sorted;
    }
	
//////////////////////////////////////////
	
	/**
	 * Sorts profiles by total games. If order is true, they are sorted in descending order ( 10,6,2..) else reversed
	 * @param order to decide how to sort
	 * @return a sorted List<Profile>
	 */
	public List<Profile> sortByTotal(boolean order) {
        List<Profile> sorted = 
        	profiles.stream()
        	.sorted(order ? Comparator.comparingInt(Profile::getTotal).reversed()
        				  : Comparator.comparingInt(Profile::getTotal) )
            .collect(Collectors.toList());
        return sorted;
    }
		

////////////////////////////////////////////////////////////////////////////////////
//OBSERVER
	/**
	 * Observes Level to set currentProfile last level.
	 * Observers Player to add score and set currentProfile last healt with player's health.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o.getClass()==LevelController.class) {
			Level lvl = (Level)arg;
			currentProfile.setLastLevel(lvl.getLevelNumber());
		}
		else if(o.getClass()==Player.class) {
			Player pl = (Player)arg;
			currentProfile.addScore(pl.getScore());
			currentProfile.setLastHealth(pl.getLastHealth());
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	
	public List<Profile> getProfiles() {return profiles;}

//////////////////////////////////////////
	
	public Profile getCurrentProfile() {return currentProfile;}

//////////////////////////////////////////
	
	/**
	 * Sets currentProfile stats to player and level controller.
	 * @param currentProfile profile with stats to set
	 */
	public void setCurrentProfile(Profile currentProfile) {
		this.currentProfile = currentProfile;
		int life = currentProfile.getLastHealth();
		Player.getInstance().setScore(currentProfile.getLastScore());
		Player.getInstance().setLastScore(currentProfile.getLastScore());
		Player.getInstance().setHealth(life);
		LevelController.getInstance().applyLevel(currentProfile.getLastLevel());
	}
	
////////////////////////////////////////////////////////////////////////////////////	
}
