package tile;

import java.util.HashMap;
import java.util.Map;

public class TileConstants {
    // TILE NAMES
    public static final int BLACK_TILE   = 0;
    public static final int ASPALT       = 1;
    public static final int GRASS        = 2;
    public static final int WATER        = 3;
    public static final int TRAFFIC_CONE = 4;
    public static final int NEXT_ROUND_TILE = 5;

    protected static final Map<Integer, String> TILE_RESOURCES_MAP = new HashMap<>();
    protected static final Map<Integer, Boolean> TILE_COLLIDABLE_MAP = new HashMap<>();

    static {
        setResources();
        setCollidable();
    }

    private TileConstants() { }

    /** Sets resource location of each Tile sprite */
    private static void setResources() {
        TILE_RESOURCES_MAP.put(BLACK_TILE,   "/resources/tiles/black_tile.png");
        TILE_RESOURCES_MAP.put(ASPALT,       "/resources/tiles/aspalt.png");
        TILE_RESOURCES_MAP.put(GRASS,        "/resources/tiles/grass.png");
        TILE_RESOURCES_MAP.put(WATER,        "/resources/tiles/water.png");
        TILE_RESOURCES_MAP.put(TRAFFIC_CONE, "/resources/tiles/traffic_cone.png");
        TILE_RESOURCES_MAP.put(NEXT_ROUND_TILE, "/resources/tiles/black_tile.png");
    }

    /** Sets each Tile Collidable property */
    private static void setCollidable() {
        TILE_COLLIDABLE_MAP.put(BLACK_TILE,   true);
        TILE_COLLIDABLE_MAP.put(ASPALT,       false);
        TILE_COLLIDABLE_MAP.put(GRASS,        false);
        TILE_COLLIDABLE_MAP.put(WATER,        false);
        TILE_COLLIDABLE_MAP.put(TRAFFIC_CONE, true);
        TILE_COLLIDABLE_MAP.put(NEXT_ROUND_TILE, true);
    }
}
