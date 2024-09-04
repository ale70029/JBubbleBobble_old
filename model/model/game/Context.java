package model.game;
/**
 * Enum for current game context, used by Game class to decide what to update/draw.
 * Starts with menu.
 */
public enum Context {

	MENU,
	LOAD,
	NEW_GAME,
	DELETE,
	PLAYING,
	PAUSE,
	NEXT_LEVEL,
	GAME_OVER,
	WIN,
	TUTORIAL,
	STATISTICS,
	OPTIONS,
	CREDITS,
	EASTER_EGG,
	QUIT;
	
	//START CONTEXT
	public static Context current = MENU;

}
