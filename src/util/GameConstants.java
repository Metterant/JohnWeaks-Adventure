package util;

public class GameConstants {
    public static final int ORIGINAL_TILE_SIZE = 16; // 16 x 16 tile size
    public static final int SCALE = 3;
    
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int MAX_SCREEN_COL = 24;
    public static final int MAX_SCREEN_ROW = 16;
    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

    // Frame-Rate
    public static final int FPS = 60;

    // Hide constructor to avoid instantiating GameConstants
    // as GameConstants is class used to store static constants
    private GameConstants() { }
}
