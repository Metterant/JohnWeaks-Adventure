package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import util.GameConstants;
import util.Renderable;

/**
 * A class used for managing Tile system <p>
 */
public class TileManager implements Renderable {
    // Loaded Tiles
    public Tile[] tiles;
    // Tile Map that stores number (id) of a Tile
    public int[][] tileMapNum;

    // Singleton instance
    private static TileManager instance;

    public static synchronized TileManager getInstance() {
        if (instance == null) {
            instance = new TileManager();
        }
        return instance;
    }

    public TileManager() {
        // Set the amount of unique tiles equal to the amount of tiles defined (has valid resources) in game constants 
        tiles = new Tile[TileConstants.TILE_RESOURCES_MAP.size()];
        tileMapNum = new int[GameConstants.MAX_SCREEN_COL][GameConstants.MAX_SCREEN_ROW];
    }
    
    /**
     * Checks if a Tile in the Tile Map is collidable or not
     * @param row : The row in the Tile Map
     * @param col : The column in the Tile Map
     * @return whether the Tile is collidable or not
     */
    public boolean getIsCollidable(int row, int col) {
        return tiles[tileMapNum[col][row]].isCollidable;
    }

    @Override
    public void loadImages() {
        try {
            for (int i = 0; i < TileConstants.TILE_RESOURCES_MAP.size(); i++) {
                tiles[i] = new Tile();
                tiles[i].image = ImageIO.read(getClass().getResourceAsStream(TileConstants.TILE_RESOURCES_MAP.get(i)));
                tiles[i].isCollidable = TileConstants.TILE_COLLIDABLE_MAP.get(i);
            }

        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Method used for loading maps
     */
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int i = 0; i < GameConstants.MAX_SCREEN_ROW; i++) {
                String line = br.readLine();
                String[] numbers = line.split(" ");

                int spriteCount = TileConstants.TILE_RESOURCES_MAP.size();
                for (int j = 0; j < numbers.length; j++) {
                    int parsedTileNumber = Integer.parseInt(numbers[j]);
                    if (parsedTileNumber >= spriteCount) {
                        String format = "Unexpected Tile Number. (%d > %d)";

                        tileMapNum[j][i] = 0;
                        throw new IllegalStateException(String.format(format, parsedTileNumber, spriteCount));
                    }
                    else 
                        tileMapNum[j][i] = parsedTileNumber;
                }
            }

            br.close();
        } 
        catch (Exception e) {
            e.getStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        /*
         * Tile numbers are stored in a 2d array [col][row]
         * where tiles in the same row are loaded in row direction
         * and tiles in the same column are loaded in the col direction
         */
        for (int i = 0; i < GameConstants.MAX_SCREEN_COL; i++) {
            for (int j = 0; j < GameConstants.MAX_SCREEN_ROW; j++) {
                int posX = i * GameConstants.TILE_SIZE;
                int posY = j * GameConstants.TILE_SIZE;

                g2.drawImage(tiles[tileMapNum[i][j]].image, posX, posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
            }
        }
    }

    /**
     * Returns the tile type number at the given world coordinates.
     * @param positionX The X coordinate in world space.
     * @param positionY The Y coordinate in world space.
     * @return The tile number (as defined in TileConstants) at the specified position.
     */
    public int getTileNum(double positionX, double positionY) {
        int row = (int) positionY / GameConstants.TILE_SIZE;
        int col = (int) positionX / GameConstants.TILE_SIZE;

        return tileMapNum[col][row];
    }
}
