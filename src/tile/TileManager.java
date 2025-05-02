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
    public Tile[] tile;
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
        tile = new Tile[10];
        tileMapNum = new int[GameConstants.MAX_SCREEN_COL][GameConstants.MAX_SCREEN_ROW];
    }
    
    @Override
    public void getImages() {
        try {
            // ASPALT
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/aspalt.png"));

            // GRASS
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/grass.png"));

            // WATER
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/water.png"));
            tile[2].isCollidable = true;
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

                for (int j = 0; j < numbers.length; j++) {
                    tileMapNum[j][i] = Integer.parseInt(numbers[j]);
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

                g2.drawImage(tile[tileMapNum[i][j]].image, posX, posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
            }
        }
    }
}
