package util;

import java.util.List; // Lists
import java.util.Collections; // Return a collection
import java.util.Arrays; // Perform manipulations on a collection
import java.util.Random;
import util.pathfinding.TileCoords;

import entity.enemy.*;

public class Spawner {
    private TileCoords[][] defaultSpawnTileCoords = new TileCoords[4][];
    private Random random;

    // Indices for directions
    static final int NORTH = 0;
    static final int EAST = 1;
    static final int WEST = 2;
    static final int SOUTH = 3;

    /* Ideas for making the spawner
     * - Spawn a horde of enemies after a certain amount of frames
     */

    public Spawner() {
        random = new Random();

        defaultSpawnTileCoords[NORTH] = new TileCoords[] {
            new TileCoords(2, 15),
            new TileCoords(2, 16),
            new TileCoords(2, 17)
        };
        defaultSpawnTileCoords[EAST] = new TileCoords[] {
            new TileCoords(9, 8),
            new TileCoords(10, 8),
            new TileCoords(11, 8)
        };
        defaultSpawnTileCoords[WEST] = new TileCoords[] {
            new TileCoords(9, 24),
            new TileCoords(10, 24),
            new TileCoords(11, 24)
        };
        defaultSpawnTileCoords[SOUTH] = new TileCoords[] {
            new TileCoords(18, 15),
            new TileCoords(18, 16),
            new TileCoords(18, 17)
        };
    }

    /**
     * Spawn a random horde of Enemies (from 1 to 3 Enemies) from 1 out of 4 directions
     */
    public void spawnEnemyHorde() {
        // Pick a random direction
        int direction = random.nextInt(4); // 0=NORTH, 1=EAST, 2=WEST, 3=SOUTH
        TileCoords[] spawnTiles = defaultSpawnTileCoords[direction];

        // Pick a random number of bikers to spawn (1 to 3, but not more than available tiles)
        int maxBikers = Math.min(3, spawnTiles.length);
        int numBikers = 1 + random.nextInt(maxBikers);

        // Shuffle the spawnTiles array to avoid duplicates
        List<TileCoords> tileList = Arrays.asList(spawnTiles.clone());
        Collections.shuffle(tileList, random);

        for (int i = 0; i < numBikers; i++) {
            TileCoords chosenTileCoords = tileList.get(i);
            int chosenRow = chosenTileCoords.getRow();
            int chosenCol = chosenTileCoords.getCol();
            new Biker(chosenRow, chosenCol);
        }
    }

}
