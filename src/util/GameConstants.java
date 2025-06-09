package util;

public class GameConstants {
    /** Sprite pixels, Tile size on the screen */
    public static final int ORIGINAL_TILE_SIZE = 18;
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

    /** UI Constants */
    public class UI {
        public static final int POWERUP_FRAME_SIDE = 20 * SCALE;

        private UI() { }
    }

    /** Game Constants */
    public class Game {
        /* Total frames = FPS * Seconds */
        public static final int BASE_ROUND_DURATION_FRAMES = FPS * 60; // 45 seconds
        public static final int INCREMENT_DURATION_FRAMES = FPS * 10;  // + 10 seconds after each round
        public static final int RESPAWN_DURATION_FRAMES = FPS * 4; // 4 seconds
        public static final int PREROUND_DURATION_FRAMES = FPS * 5; // 8 seconds

        public static final int PATHFINDER_PERIOD_FRAMES = FPS / 2; // Half a second

        public static final int PICKABLE_TTL = FPS * 12; // 12 seconds

        public static final int PLAYER_LIVES = 3;

        // DROP RATES
        public static final float COIN_DROP_RATE    = 0.075f; // 7.5%
        public static final float POWERUP_DROP_RATE = 0.050f; // 5.0 %
        public static final float LIFE_DROP_RATE    = 0.005f; // 0.5 %

        // MAP RESOURCES
        public static final int MAPS_COUNT = 6;
        protected static final String[] MAP_STRINGS = new String[10]; 
        protected static final String[] MAP_POOL = new String[MAPS_COUNT]; 

        static {
            String shopString = "/resources/maps/shop.txt";

            MAP_STRINGS[0] = "/resources/maps/map_0.txt";
            MAP_STRINGS[1] = "/resources/maps/map_1.txt";
            MAP_STRINGS[2] = shopString;
            MAP_STRINGS[3] = "/resources/maps/map_2.txt";
            MAP_STRINGS[4] = "/resources/maps/map_3.txt";
            MAP_STRINGS[5] = shopString;
            MAP_STRINGS[6] = "/resources/maps/map_4.txt";
            MAP_STRINGS[7] = "/resources/maps/map_5.txt";
            MAP_STRINGS[8] = shopString;
            MAP_STRINGS[9] = "/resources/maps/map_5.txt";

            MAP_POOL[0] = "/resources/maps/map_0.txt";
            MAP_POOL[1] = "/resources/maps/map_1.txt";
            MAP_POOL[2] = "/resources/maps/map_2.txt";
            MAP_POOL[3] = "/resources/maps/map_3.txt";
            MAP_POOL[4] = "/resources/maps/map_4.txt";
            MAP_POOL[5] = "/resources/maps/map_5.txt";
        }

        // MAP RESOURCES Getters
        public static String[] getMapStrings() { return MAP_STRINGS; }
        public static String[] getMapPool()    { return MAP_POOL; }

        private Game() { }
    }

    /* Player Constants */
    public class Player {
        // Durations
        public static final int SPEED_BOOST_DURATION_FRAMES = FPS * 15; // 15 seconds
        public static final int SHOTGUN_DURATION_FRAMES     = FPS * 12; // 12 seconds
        public static final int MACHINE_GUN_DURATION_FRAMES = FPS * 12; // 12 seconds
        public static final int OCTOSHOT_DURATION_FRAMES    = FPS * 10; // 10 seconds

        // Stats
        public static final double SPEED_BASE   = 2.5d;
        public static final double SPEED_RUNNER = 3.25d;
        public static final double BOOSTED_SPPED = 1.0d;

        public static final int DAMAGE_BASE = 1;
        public static final int DAMAGE_LEVEL_2 = 3;
        public static final int DAMAGE_LEVEL_3 = 6;
        public static final int BASE_FRAMES_PER_SHOT = 25;

        private Player() { }
    }

    // Hide constructor to avoid instantiating GameConstants
    // as GameConstants is class used to store static constants
    private GameConstants() { }
}
