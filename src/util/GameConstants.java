package util;

public class GameConstants {
    public static final int ORIGINAL_TILE_SIZE = 16; // 16 x 16 tile size
    public static final int SCALE = 2;
    
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int MAX_SCREEN_COL = 33;
    public static final int MAX_SCREEN_ROW = 21;
    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

    // Center of the screen
    public static final double DEFAULT_SPAWN_X = (double) ((MAX_SCREEN_COL - 1) * TILE_SIZE) / 2;
    public static final double DEFAULT_SPAWN_Y = (double) ((MAX_SCREEN_ROW - 1) * TILE_SIZE) / 2;

    // Frame-Rate
    public static final int FPS = 60;

    // Hide constructor to avoid instantiating GameConstants
    // as GameConstants is class used to store static constants
    private GameConstants() { }
}
