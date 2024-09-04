/**
 * Package with classes that controls file reading and writing.
 */
package controller.filesManagers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class to load and manage audio.
 * A custom music player is implemented.
 * Fun mode lets you play music instead of Game original song.
 */
public class AudioManager {
////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sounds indexes in sounds Clip array.
	 */
	public static final int JUMP_SOUND = 0,SHOOT_SOUND = 1,POWER_SOUND = 2,ITEM_SOUND = 3,BUBBLE_HIT_SOUND = 4,HURT_SOUND = 5,
							ENEMY_DEATH_SOUND = 6,PLAYER_DEATH_SOUND = 7,NEXT_LEVEL_SOUND = 8,BOSS_SOUND = 9;
	/**
	 * Original bubble bobble song index in songs.
	 */
	public static final int ORIGINAL_THEME = 0;
	
	private static final float MUSIC_VOLUME = 0.2f, SOUND_VOLUME = 0.1f;
	
	private Clip[] songs;
	private Clip[] sounds;
	private Clip currentSong; //currentSound;
	private int currentIndex;
	private boolean musicOn,soundOn,funOn;
	private boolean ended,switched,previous,next,enabled=true;
	
	private Timer timer;
////////////////////////////////////////////////////////////////////////////////////
//SINGLETON
	
	private static AudioManager instance;
	/**
	 * Singleton
	 */
	public static AudioManager getInstance() {
		if (instance == null)
			instance = new AudioManager();
		return instance;
	}

	private AudioManager() {
		songs = new Clip[1];
		sounds = new Clip[1];
		loadAudio();
		
		soundOn = true;
		musicOn = true;
		funOn = true;
		
		currentIndex = 0;
		currentSong = songs[currentIndex];
//		currentSound = sounds[0];
		
		
		timer = new Timer();
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//AUDIO CONTROLL

	/**
	 * Updates music.
	 * A timer prevents AudioManager to malfunction if called too many times in a short time.
	 * Sets current songs, next song, previous song, mute and fun mode.
	 */
	public void update() {
		
		if(enabled) {
			//silence music
			if(!musicOn) {
				currentSong.stop();
				ended=false;
				enabled = false;
			}
			else {
				
				//switch from fun mode to normal mode
				if(switched) {
					if(funOn) {
						if(currentIndex==0) currentIndex =1;
						playSong(currentIndex);
					}
					else playSong(ORIGINAL_THEME);
					
					switched = false;
					ended = false;
					enabled = false;
				}
				
				//music has stopped
				if(ended) {
					//previous song
					if(funOn&&previous) {
						playSong(--currentIndex);
						previous=false;
					}
					//next song
					else if(funOn&&next) {
						playSong(++currentIndex);
						next=false;
					}
					//song endend, play next
					else if(funOn &&!previous &&!next) playSong(++currentIndex);
					
					//repeat original song
					else playSong(ORIGINAL_THEME);
					ended = false;
					enabled = false;
				}
			}
			
			TimerTask delay = new TimerTask() {
	            @Override
	            public void run() {
	                enabled = true;
	            }
	        };
			timer.schedule(delay, 1000);
		}
	}
	
//////////////////////////////////////////
	
	/**
	 * Play song found at index i.
	 * Stops current songs and finds next song to play and set as current.
	 * @param i index of songs array
	 */
	public void playSong(int i) {
		currentSong.stop();
		currentSong.setFramePosition(0);
		
		if(funOn) {
			if(i>=songs.length) currentIndex=1;
			else if(i<=0) currentIndex = songs.length-1;
			else currentIndex = i;
		}
		else currentIndex = 0;
		
		currentSong= songs[currentIndex];
		currentSong.setFramePosition(0);
		currentSong.start();
		previous = false;
	}
//////////////////////////////////////////
	
	/**
	 * Plays sound at i index.
	 * @param i index of sounds array
	 */
	public void playSound(int i) {
		
//		currentSound.stop();
		if(soundOn) {
			sounds[i].start();
//			currentSound = sounds[i];
//			currentSound.start();
			}
	}
	
//////////////////////////////////////////
	
	/**
	 * Switches from music on to off and vice versa
	 */
	public void switchMusic() {
		musicOn = !musicOn;
		switched = true;
		}
	
//////////////////////////////////////////
	
	/**
	 * switches from sound on to off and vice versa
	 */
	public void switchSound() {soundOn = !soundOn;}
	
//////////////////////////////////////////
	
	/**
	 * Switches from fun mode on to off and vice versa
	 */
	public void switchMode() {
		funOn = !funOn;
		switched = true;
	}
	
//////////////////////////////////////////
	
	/**
	 * Starts music for the first time
	 */
	public void startMusic() {
		if(funOn) playSong(1);
		else playSong(ORIGINAL_THEME);		
	}
	
//////////////////////////////////////////
	
	/**
	 * Plays next song in the array
	 */
	public void nextSong() { 
		if(funOn) {
			currentSong.stop();
			next=true; 
		}
	}
	
//////////////////////////////////////////
	
	/**
	 * Plays previous song in the array 
	 */
	public void previousSong() {
		if(funOn) {
			currentSong.stop();
			previous= true;
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//INIT
	
	/**
	 * Loads music and sounds and add a Listener to the clips for when sounds are stopped.
	 */
	private void loadAudio() {
		try {
			//load songs
			File songFolder = new File("media/audio/songs");
			File[] songFiles = songFolder.listFiles();
			songs = new Clip[songFiles.length];
			for(int i=0;i<songFiles.length;i++) {
				FileInputStream fileIn = new FileInputStream(songFiles[i]);
                BufferedInputStream bufIn = new BufferedInputStream(fileIn);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(MUSIC_VOLUME) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                        	ended = true;
                        }
                    }
                });
                songs[i] = clip;
			}
			//load SFX sounds
			File soundFolder = new File("media/audio/sounds");
			File[] soundFiles = soundFolder.listFiles();
			sounds = new Clip[soundFiles.length];
			for(int i=0;i<soundFiles.length;i++) {
				FileInputStream fileIn = new FileInputStream(soundFiles[i]);
                BufferedInputStream bufIn = new BufferedInputStream(fileIn);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(SOUND_VOLUME) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.setFramePosition(0);
                        }
                    }
                });
                sounds[i] = clip;
			}
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException  e) {e.printStackTrace();}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//GET - SET
	
	/**
	 * Gives a song name
	 * @return a string withe the currentSong name
	 */
	public String getSongName() {
		String song = switch(currentIndex) {
		case 0 -> "bubble bobble";
		case 1 -> "what is love";
		case 2 -> "tainted love";
		case 3 -> "Sweet Dreams";
		case 4 -> "psycho killer";
		case 5 -> "should i stay or should i go";
		case 6 -> "you spin me round";
		case 7 -> "eye of the tiger";
		case 8 -> "master of puppets";
		default -> "";
		};
		
		return song;
	}

//////////////////////////////////////////
	
	public boolean isFunOn() {return funOn;}
	
////////////////////////////////////////////////////////////////////////////////////
}


