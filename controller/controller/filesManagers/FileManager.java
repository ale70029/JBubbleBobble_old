package controller.filesManagers;

import static model.collectables.Item.MAX_ITEMS;
import static model.collectables.Power.MAX_POWERS;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import model.game.Level;
import model.game.Profile;

/**
 * Class to load and save all kind of needed files (beside audios managed by AudioManager).
 * Loaded images are stored statically in this class to be accesed without needing to reload them.
 * Same for levels.
 * Custom fonts are loaded in the environment.
 */
public class FileManager implements FileNames{
//////////////////////////////////////////
	//images
	public static final Level[] LEVELS;
	public static final BufferedImage[] TILES;
	public static final BufferedImage[][] PLAYER_ANIMATIONS;
	public static final BufferedImage[][] BUBBLE_ANIMATIONS;
	public static final BufferedImage[][] ENEMY_ZEN_ANIMATIONS;
	public static final BufferedImage[][] ENEMY_INVADER_ANIMATIONS;
	public static final BufferedImage[][] ENEMY_MIGHTA_ANIMATIONS;
	public static final BufferedImage[][] ENEMY_BOSS_ANIMATIONS;
	public static final BufferedImage[] ITEMS;
	public static final BufferedImage[] POWERS;
	public static final BufferedImage SHIELD;
	public static final BufferedImage[] AVATARS;
	public static final BufferedImage[] MENU_BUTTON;
	public static final BufferedImage[] SOUND_BUTTON;
	public static final BufferedImage[] KEYBOARD_KEYS;
	public static final BufferedImage GRAVE;
	public static final BufferedImage MENU_DECORATION;
	public static final BufferedImage TROPHY;
	public static final BufferedImage PAUSE_PANEL;
	public static final BufferedImage[] LOAD_BUTTON;
	public static final BufferedImage[] ENEMIES;
	public static final BufferedImage EGG;
	public static final BufferedImage SURPRISE;
	//sizes
	private static final int DRAGON_MAX_INDEX = 10;
	private static final int DRAGON_MAX_SPRITES = 4; 
	private static final int BUBBLE_MAX_INDEX = 4;
	private static final int BUBBLE_MAX_SPRITES = 6;
	private static final int ENEMY_MAX_INDEX = 4;
	private static final int ENEMY_MAX_SPRITES = 4;
	private static final int BOSS_MAX_INDEX = 5;
	private static final int BOSS_MAX_SPRITES = 4;
	private static final int TILES_MAX_INDEX = 13 ;
	private static final int TILE_PIXELS = 64;
	private static final int PLAYER_PIXELS = 16;
	private static final int BUBBLE_ORIGINAL_SIZE = 80;
	private static final int ENEMY_ORIGINAL_SIZE = 192;
	private static final int BOSS_ORIGINAL_SIZE = 240;
	private static final int COLLECTABLE_PIXELS = 50;
	private static final int ORIGINAL_MENU_BUTTON_WIDTH = 512;
	private static final int ORIGINAL_MENU_BUTTON_HEIGHT = 256;
	private static final int AVATAR_MAX_INDEX = 6;
	//public sizes
	public static final int MENU_BUTTON_WIDTH = 172;
	public static final int MENU_BUTTON_HEIGHT = 57;
	public static final int LOAD_SLOT_WIDTH = 585;
	public static final int LOAD_SLOT_HEIGHT = 79;
	public static final int KEY_SIZE = 224;
	public static final int AVATAR_IMAGE_SIZE = 140;
	public static final int SHIELD_IMAGE_SIZE = 256;
	//backgrounds
	public static final BufferedImage BACKGROUND_1 = loadImage(BKG_1);
	public static final BufferedImage BACKGROUND_2 = loadImage(BKG_2);
	public static final BufferedImage BACKGROUND_3 = loadImage(BKG_3);
	public static BufferedImage DEFAULT_BACKGROUND = BACKGROUND_1;
	
	static {
		LEVELS = loadLevels();
		TILES = loadTiles();
		PLAYER_ANIMATIONS = loadPlayerAnimations();
		BUBBLE_ANIMATIONS = loadBubblesAnimations();
		ENEMY_ZEN_ANIMATIONS = loadEnemyZenAnimations();
		ENEMY_INVADER_ANIMATIONS = loadEnemyInvaderAnimations();
		ENEMY_MIGHTA_ANIMATIONS = loadEnemyMightaAnimations();
		ITEMS = loadItemsImages();
		POWERS = loadPowersImages();
		SHIELD = loadShieldImage();
		AVATARS = loadAvatars();
		MENU_BUTTON = loadMenuButtons();
		SOUND_BUTTON = loadActionButtons();
		KEYBOARD_KEYS = loadKeysImages();
		GRAVE = loadImage(GRAVE_IMAGE);
		MENU_DECORATION = loadImage(MENU_DECORATION_IMAGE);
		TROPHY = loadImage(FileNames.TROPHY_IMAGE);
		PAUSE_PANEL = loadImage(PAUSE_PANEL_IMAGE);
		LOAD_BUTTON = loadLoadSlotImages();
		ENEMIES = loadEnemiesImages();
		ENEMY_BOSS_ANIMATIONS = loadEnemyBossAnimations();
		EGG = loadImage(EGG_PIC);
		SURPRISE = loadImage(SURPRISE_PIC);

		loadFonts();
	}

////////////////////////////////////////////////////////////////////////////////////
//PROFILES
	
	
	/**
	 *Saves a profile in a text file named as the profile, with the following line strucure:
	 *
	 * *0 NAME
	 * *1 AVATAR
	 * *2 LEVEL
	 * *3 HEALTH
	 * *4 WIN GAMES
	 * *5 LOST GAMES
	 * *6 LAST SCORE
	 * *7 HIGH SCORE		
	 * *8,9,10,11,12 SCORES
	 *
	 * @param p Profile to save
	 * 
	 * NOTE : DO NOT MESS WITH FILES BECAUSE NO INTEGRITY CONTROLS ARE GIVEN 
	 */
	public static void saveProfile(Profile p) {
		StringBuilder sb = new StringBuilder();
		sb.append(p.getNickname()+"\n")     //0 NAME
		  .append(p.getAvatar()+"\n")		//1 AVATAR
		  .append(p.getLastLevel()+"\n")	//2 LAST LEVEL
		  .append(p.getLastHealth()+"\n")	//3 HEALTH
		  .append(p.getWin()+"\n")			//4 WIN
		  .append(p.getLost()+"\n")			//5 LOST
		  .append(p.getLastScore()+"\n")	//6 LAST SCORE
		  .append(p.getHighScore()+"\n");	//7 HIGH SCORE
		  
		  for(int score : p.getMaxScores()) //8,9,10,11,12 SCORES
			sb.append(score+"\n");
		  
		String save = sb.toString();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("files/saveFiles/"+p.getNickname()+".txt"));
			writer.write(save);
			writer.flush();
			writer.close();
		}catch(IOException e) {e.printStackTrace();}
	}

	
	
//////////////////////////////////////////

	/**
	 * Loads profiles from text files with the following structure.
	 * 
	 * *0 NAME
	 * *1 AVATAR
	 * *2 LEVEL
	 * *3 HEALTH
	 * *4 WIN GAMES
	 * *5 LOST GAMES
	 * *6 LAST SCORE
	 * *7 HIGH SCORE		
	 * *8,9,10,11,12 SCORES
	 *
	 * NOTE : DO NOT MESS WITH FILES BECAUSE NO INTEGRITY CONTROLS ARE GIVEN 
	 * @return
	 */
	public static List<Profile> loadProfiles() {
		List<Profile> profilesTemp= new ArrayList<Profile>();
		File saveFiles = new File("files/saveFiles");
		File[] savings = saveFiles.listFiles();
		String line;
		try {
			for(File file : savings) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				Profile p = new Profile();
				List<Integer> scores = new ArrayList<Integer>();
	            for(int i=0;i<14;i++) { // 14 is last line
	            	line = reader.readLine();
	            	if(line!=null) {
		            	if(i==0) {p.setNickname(line.trim());}								//0 NAME
		            	else if(i==1) {p.setAvatar(Integer.parseInt(line.trim()));}			//1 AVATAR
		            	else if(i==2) {p.setLastLevel(Integer.parseInt(line.trim()));}		//2 LEVEL
		            	else if(i==3) {p.setLastHealth(Integer.parseInt(line.trim()));}  	//3 HEALTH
		            	else if(i==4) {p.setWin(Integer.parseInt(line.trim()));}	        //4 WIN GAMES
		            	else if(i==5) {p.setLost(Integer.parseInt(line.trim()));} 			//5 LOST GAMES
		            	else if(i==6) {p.setLastScore(Integer.parseInt(line.trim()));} 		//6 LAST SCORE
		            	else if(i==7) {p.setHighScore(Integer.parseInt(line.trim()));}		//7 HIGH SCORE		
		            	else if(i>7)  {scores.add(Integer.parseInt(line.trim()));}			//8,9,10,11,12 SCORES
	            	}
	            }
	            p.setScores(scores);
	            profilesTemp.add(p);
	            reader.close(); 
			}
			return profilesTemp;
        } catch (IOException e) {System.out.println("Missing or damaged files.");}
		return null;
		
	}
	
////////////////////////////////////////////////////////////////////////////////////
//LEVELS
	
	/**
	 * Load every level file found in levels.
	 * @return Level array with all levels.
	 */
	private static Level[] loadLevels() {
		File levelFolder = new File("files/levels");
		File[] levelFiles = levelFolder.listFiles();
		if(levelFiles.length<1) System.out.println("Missing or damaged files.");
		Level[] levels = new Level[levelFiles.length];
		Level level;
		for(int i=0;i<levelFiles.length;i++) {
			level = new Level();
			loadLevelTxt(level,levelFiles[i].getPath().toString());
			levels[i] = level;
		}
		return levels;
	}
	
//////////////////////////////////////////
	
	/**
	 * Load level from text file with following line structure:
	 * 
	 * * 0 LEVEL NUMBER
	 * * 1 PLAYER START POSITION
	 * * 2 ENEMY ZEN START POSITIONS
	 * * 3 ENEMY INVADER START POSITIONS
	 * * 4 ENEMY MIGHTA START POSITIONS
	 * * 5 ENEMY BOSS START POSITION
	 * * 6 TO 27 LEVEL TILES  (0 == 0, A == 1, B==2...)
	 * Every number of level tiles is the corrisponding index of tiles images array.
	 * Letter are used to avoid numerical sorting problems ( 10 comes after 1 and before 2 ) and converted back to numbers.
	 * 
	 * @param level Level to create
	 * @param path file to read
	 */
	private static void loadLevelTxt(Level level, String path){
		int playerX =0;
		int playerY =0;
		int levelNumber = 0;
		Map<Integer, List<Map.Entry<Integer, Integer>>> enemiesMap = new HashMap<>();
		int[][] levelTemp = new int[Level.LEVEL_ROWS][Level.LEVEL_COLS];
		
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = "";
            //reading file
            for(int j=0;j<28;j++) {
        		//reading level number
        		if(j==0) {
        			line=br.readLine();
        			levelNumber = Integer.parseInt(line.trim());
        			if(levelNumber == 11) level.setBossFight(true); // BOSS LEVEL
        		}
        		//reading player position
            	if(j==1) {
            		line = br.readLine();
	            	String[] player = line.split(",");
	            	for (int i=0; i < player.length; i+=2) {
	            		playerX = Integer.parseInt(player[i].trim());
                        playerY = Integer.parseInt(player[i + 1].trim());
	            	}
            	}
            	//reading enemies postitions
            	//enemykind 1
            	if(j==2) {
            		line = br.readLine();
            		if(line.charAt(0)=='-') continue;
            		
            		List<Map.Entry<Integer, Integer>> zenList= new ArrayList<>();
	            	String[] enemies = line.split(",");
	            	for (int i=0; i < enemies.length; i+=2) {
	                    int x = Integer.parseInt(enemies[i].trim());
	                    int y = Integer.parseInt(enemies[i + 1].trim());
	                    zenList.add(new AbstractMap.SimpleEntry<>(x,y));
                        enemiesMap.put(0, zenList);
	                }
            	}
            	
            	//enemyKind 2
            	if(j==3) {
            		line = br.readLine();
            		if(line.charAt(0)=='-') continue;
            		
            		List<Map.Entry<Integer, Integer>> invaderList= new ArrayList<>();
	            	String[] enemies = line.split(",");
	            	for (int i=0; i < enemies.length; i+=2) {
                        int x = Integer.parseInt(enemies[i].trim());
                        int y = Integer.parseInt(enemies[i + 1].trim());
                        invaderList.add(new AbstractMap.SimpleEntry<>(x,y));
                        enemiesMap.put(1, invaderList);
	                }
            	}
            	
            	//enemyKind 3
            	if(j==4) {
            		line = br.readLine();
            		if(line.charAt(0)=='-') continue;
            		
            		List<Map.Entry<Integer, Integer>> MightaList= new ArrayList<>();
	            	String[] enemies = line.split(",");
	            	for (int i=0; i < enemies.length; i+=2) {
	            		if(enemies[0].equals("-")) break;
                        int x = Integer.parseInt(enemies[i].trim());
                        int y = Integer.parseInt(enemies[i + 1].trim());
                        MightaList.add(new AbstractMap.SimpleEntry<>(x,y));
                        enemiesMap.put(2, MightaList);
	                }
            	}
            	//boss enemy
            	if(j==5) {
            		line = br.readLine();
            		if(line.charAt(0)=='-') continue;
            		
            		List<Map.Entry<Integer, Integer>> bossList= new ArrayList<>();
	            	String[] enemies = line.split(",");
	            	for (int i=0; i < enemies.length; i+=2) {
                        int x = Integer.parseInt(enemies[i].trim());
                        int y = Integer.parseInt(enemies[i + 1].trim());
                        bossList.add(new AbstractMap.SimpleEntry<>(x,y));
                        enemiesMap.put(3, bossList);
                        
	                }
            	}
            	
	            //reading tiles
            	if(j>5) {
            		//TODO FIX : J-4 because first 2 lines need to be empty for headbar (6-4==2..)
	            	line = br.readLine();
	            	for (int i=0; i <Level.LEVEL_COLS; i++) {
	            		if(line.charAt(i)=='0')
	            			levelTemp[j-4][i] = 0;
	            		else levelTemp[j-4][i] = ((int) line.charAt(i)-64);   // A ->1 B ->2...
	            	}  
            	}
	        }
            //closing and saving
            br.close();
            level.setStartPoint(playerX, playerY);
            level.setLevelData(levelTemp);
            level.setLevelNumber(levelNumber);
            level.setEnemiesMap(enemiesMap);
            
        }catch (IOException | NumberFormatException e) {System.out.println("Missing or damaged files.");}
	}
	
////////////////////////////////////////////////////////////////////////////////////
//IMAGES

	/**
	 * Loads a generic image from a file path into a BufferedImage
	 * @param fileName path to file
	 * @return a BufferedImage
	 */
	private static BufferedImage loadImage(String fileName) {
		BufferedImage img = null;
		InputStream is = FileManager.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException | NullPointerException | IllegalArgumentException e) {
				System.out.println("Missing or damaged files.");
			}
		}
		return img;
	}

//////////////////////////////////////////
//TILES

	/**
	 * Returns level tiles in a BufferedImage[]
	 * Every element is a different tile
	 */
	private static BufferedImage[] loadTiles() {
		BufferedImage tileImage = loadImage(TILES_IMAGES); //immagine con tutte le tiles
		BufferedImage[] tiles = new BufferedImage[TILES_MAX_INDEX];
	
		for (int i = 0; i < TILES_MAX_INDEX; i++) {
			tiles[i] = tileImage.getSubimage(i*TILE_PIXELS,0, TILE_PIXELS, TILE_PIXELS); //tile sprite = 64px x 64 px pixels
		}
		return tiles;
	}

//////////////////////////////////////////
//PLAYER
	
	/**
	 * Returns player animations in a BufferedImage[][]
	 * Every row is a different animation, every column a different frame
	 */
	private static BufferedImage[][] loadPlayerAnimations() {
		BufferedImage[][] playerAnimations = new BufferedImage[DRAGON_MAX_INDEX][DRAGON_MAX_SPRITES];
		BufferedImage temp = loadImage(DRAGON_IMAGES);
		for(int j =0;j<DRAGON_MAX_INDEX;j++)
			for(int i =0; i<DRAGON_MAX_SPRITES;i++)
				playerAnimations[j][i]= temp.getSubimage(i*PLAYER_PIXELS,j*PLAYER_PIXELS ,PLAYER_PIXELS, PLAYER_PIXELS);
		return playerAnimations;
	}
	
//////////////////////////////////////////
	
	/**
	 * Loads bubbles animations.
	 * Every row is a different animation, every column a different frame
	 * @return bubble animations in a BufferedImage[][]
	 */
	private static BufferedImage[][] loadBubblesAnimations(){
		BufferedImage[][] bubbleAnimations = new BufferedImage[BUBBLE_MAX_INDEX][BUBBLE_MAX_SPRITES];
		BufferedImage temp = loadImage(BUBBLES_IMAGES);
		for(int j =0;j<BUBBLE_MAX_INDEX;j++)
			for(int i =0; i<BUBBLE_MAX_SPRITES;i++)
				bubbleAnimations[j][i]= temp.getSubimage(i*BUBBLE_ORIGINAL_SIZE,j*BUBBLE_ORIGINAL_SIZE ,BUBBLE_ORIGINAL_SIZE, BUBBLE_ORIGINAL_SIZE);
		return bubbleAnimations;
	}
	
//////////////////////////////////////////
	
	private static BufferedImage loadShieldImage() {
		BufferedImage temp = loadImage(SHIELD_IMAGE);
		BufferedImage shield = temp.getSubimage(0, 0, SHIELD_IMAGE_SIZE, SHIELD_IMAGE_SIZE);
		return shield;
		
	}
	
//////////////////////////////////////////
//ENEMIES
	
	/**
	 * Loads enemy zen animations.
	 * Every row is a different animation, every column a different frame
	 * @return zen animations in a BufferedImage[][]
	 */
	private static BufferedImage[][] loadEnemyZenAnimations(){
		BufferedImage[][] zanAnimations = new BufferedImage[ENEMY_MAX_INDEX][ENEMY_MAX_SPRITES];
		BufferedImage temp = loadImage(ENEMY_ZEN_IMAGES);
		for(int j =0;j<ENEMY_MAX_INDEX;j++)
			for(int i =0; i<ENEMY_MAX_SPRITES;i++)
				zanAnimations[j][i]= temp.getSubimage(i*ENEMY_ORIGINAL_SIZE,j*ENEMY_ORIGINAL_SIZE ,ENEMY_ORIGINAL_SIZE, ENEMY_ORIGINAL_SIZE);
		return zanAnimations;
	}
	
//////////////////////////////////////////
	
	/**
	* Loads enemy invader animations.
	* Every row is a different animation, every column a different frame
	* @return invader animations in a BufferedImage[][]
	*/
	private static BufferedImage[][] loadEnemyInvaderAnimations(){
		BufferedImage[][] zanAnimations = new BufferedImage[ENEMY_MAX_INDEX][ENEMY_MAX_SPRITES];
		BufferedImage temp = loadImage(ENEMY_INVADER_IMAGES);
		for(int j =0;j<ENEMY_MAX_INDEX;j++)
			for(int i =0; i<ENEMY_MAX_SPRITES;i++)
				zanAnimations[j][i]= temp.getSubimage(i*ENEMY_ORIGINAL_SIZE,j*ENEMY_ORIGINAL_SIZE ,ENEMY_ORIGINAL_SIZE, ENEMY_ORIGINAL_SIZE);
		return zanAnimations;
	}
	
//////////////////////////////////////////
	
	/**
	* Loads enemy mighta animations.
	* Every row is a different animation, every column a different frame
	* @return mighta animations in a BufferedImage[][]
	*/
	private static BufferedImage[][] loadEnemyMightaAnimations(){
		BufferedImage[][] zanAnimations = new BufferedImage[ENEMY_MAX_INDEX][ENEMY_MAX_SPRITES];
		BufferedImage temp = loadImage(ENEMY_MIGHTA_IMAGES);
		for(int j =0;j<ENEMY_MAX_INDEX;j++)
			for(int i =0; i<ENEMY_MAX_SPRITES;i++)
				zanAnimations[j][i]= temp.getSubimage(i*ENEMY_ORIGINAL_SIZE,j*ENEMY_ORIGINAL_SIZE ,ENEMY_ORIGINAL_SIZE, ENEMY_ORIGINAL_SIZE);
		return zanAnimations;
	}
	
	
//////////////////////////////////////////
	
	/**
	* Loads enemy boss animations.
	* Every row is a different animation, every column a different frame
	* @return boss animations in a BufferedImage[][]
	*/
	private static BufferedImage[][] loadEnemyBossAnimations() {
		BufferedImage[][] bossAnimations = new BufferedImage[BOSS_MAX_INDEX][BOSS_MAX_SPRITES];
		BufferedImage temp = loadImage(ENEMY_BOSS_IMAGES);
		for(int j =0;j<BOSS_MAX_INDEX;j++)
			for(int i =0; i<BOSS_MAX_SPRITES;i++)
				bossAnimations[j][i]= temp.getSubimage(i*BOSS_ORIGINAL_SIZE,j*BOSS_ORIGINAL_SIZE ,BOSS_ORIGINAL_SIZE, BOSS_ORIGINAL_SIZE);
		return bossAnimations;
	}
	
//////////////////////////////////////////
//COLLECTABLES
	
	/**
	 * Load items pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadItemsImages() {
		BufferedImage[] collectables = new BufferedImage[MAX_ITEMS];
		BufferedImage temp = loadImage(ITEMS_IMAGE);
		for(int i=0;i<MAX_ITEMS;i++)
			collectables[i] = temp.getSubimage(i*COLLECTABLE_PIXELS, 0, COLLECTABLE_PIXELS, COLLECTABLE_PIXELS);
		return collectables;
	}
	
//////////////////////////////////////////
	
	/**
	 * Load powers pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadPowersImages() {
		BufferedImage[] collectables = new BufferedImage[MAX_POWERS];
		BufferedImage temp = loadImage(POWERS_IMAGES);
		for(int i=0;i<MAX_POWERS;i++)
			collectables[i] = temp.getSubimage(i*COLLECTABLE_PIXELS, 0, COLLECTABLE_PIXELS, COLLECTABLE_PIXELS);
		return collectables;
	}
	

//////////////////////////////////////////
//UI
	
	/**
	 * Load avatars pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadAvatars() {
		BufferedImage[] avatars = new BufferedImage[AVATAR_MAX_INDEX];
		BufferedImage temp = loadImage(AVATARS_IMAGES);
		for(int i=0;i<AVATAR_MAX_INDEX;i++)
			avatars[i] = temp.getSubimage(i*AVATAR_IMAGE_SIZE, 0, AVATAR_IMAGE_SIZE, AVATAR_IMAGE_SIZE);
		return avatars;
		
	}

//////////////////////////////////////////
	
	/**
	 * Load menu button pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadMenuButtons() {
		BufferedImage[] buttons = new BufferedImage[3];
		BufferedImage temp = FileManager.loadImage(MENU_BUTTON_IMAGES);
		for (int i = 0; i < buttons.length; i++)
			buttons[i] = temp.getSubimage(i * ORIGINAL_MENU_BUTTON_WIDTH, 0,
										ORIGINAL_MENU_BUTTON_WIDTH, ORIGINAL_MENU_BUTTON_HEIGHT);
		return buttons;
	}
	
//////////////////////////////////////////
	
	/**
	 * Load action button pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadActionButtons() {
		BufferedImage[] buttons = new BufferedImage[3];
		BufferedImage temp = FileManager.loadImage(ACTION_BUTTON_IMAGES);
		for (int i = 0; i < buttons.length; i++)
			buttons[i] = temp.getSubimage(i * ORIGINAL_MENU_BUTTON_WIDTH, 0,
										ORIGINAL_MENU_BUTTON_WIDTH, ORIGINAL_MENU_BUTTON_HEIGHT);
		return buttons;
	}
	
//////////////////////////////////////////
	
	
	/**
	 * Load keyboard keys pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadKeysImages() {
		BufferedImage[] keys = new BufferedImage[5];
		BufferedImage temp = FileManager.loadImage(FileNames.KEYS_IMAGES);
		for (int i = 0; i < keys.length; i++)
			if(i==4) keys[i] = temp.getSubimage(i * KEY_SIZE, 0, KEY_SIZE*2, KEY_SIZE);
			else keys[i] = temp.getSubimage(i * KEY_SIZE, 0,KEY_SIZE, KEY_SIZE);
		return keys;
	}
	
//////////////////////////////////////////
	
	/**
	 * Load load slot button pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadLoadSlotImages() {
		BufferedImage[] loadImages = new BufferedImage[3];
		BufferedImage temp = FileManager.loadImage(LOAD_SELECTION_IMAGES);
		for (int i = 0; i < loadImages.length; i++)
			loadImages[i] = temp.getSubimage(0, i * LOAD_SLOT_HEIGHT,
					LOAD_SLOT_WIDTH, LOAD_SLOT_HEIGHT);
		return loadImages;
	}
	
//////////////////////////////////////////
	
	/**
	 * Load enemies avatars pictures.
	 * @return a BufferedImage array.
	 */
	private static BufferedImage[] loadEnemiesImages() {
		BufferedImage enemiesImage = loadImage(ENEMIES_AVATARS); //immagine con tutte le tiles
		BufferedImage[] enemies = new BufferedImage[3];
	
		for (int i = 0; i < enemies.length; i++) {
			enemies[i] = enemiesImage.getSubimage(i*AVATAR_IMAGE_SIZE,0, AVATAR_IMAGE_SIZE, AVATAR_IMAGE_SIZE); //tile sprite = 8x8 pixels
		}
		return enemies;
	}
	

////////////////////////////////////////////////////////////////////////////////////
//FONTS
	
	/**
	 * Load fonts and save them in local GraphicEnvironment
	 */
	private static void loadFonts() {
		 try {
			 File file1 = new File(ARCADE_N_FONT);
			 File file2 = new File(ARCADE_I_FONT);
			 File file3 = new File(ARCADE_R_FONT);
			 Font font1 = Font.createFont(Font.TRUETYPE_FONT, file1);
			 Font font2 = Font.createFont(Font.TRUETYPE_FONT, file2);
			 Font font3 = Font.createFont(Font.TRUETYPE_FONT, file3);
			 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	         ge.registerFont(font1);
	         ge.registerFont(font2);
	         ge.registerFont(font3);
		 } catch (FontFormatException | IOException e) {e.printStackTrace();}
	}
	
////////////////////////////////////////////////////////////////////////////////////
}
