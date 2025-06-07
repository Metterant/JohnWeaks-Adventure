package util;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import entity.player.PlayerStats;
import entity.upgrade_pickables.*;

public class Shop {
    public static final int RUNNER_SHOES = 0;
    public static final int GUN_LEVEL_UP = 1;
    public static final int BURST_SHOT   = 2;
    private Set<Integer> itemPool;

    Random random;
    
    public Shop() {
        random = new Random();
        itemPool = new HashSet<>();

        itemPool.add(RUNNER_SHOES);
        itemPool.add(GUN_LEVEL_UP);
        itemPool.add(BURST_SHOT);
    }

    public void enterShop() {
        if (itemPool.isEmpty())
            return;

        // Checks if the pool has more than 2 items so that it would make sense to spawn Reroll Pickable            
        if (itemPool.size() > 2) {
            int rerollSpawnRow = Math.max(GameConstants.MAX_SCREEN_ROW / 2 - 4, 0);
            int rerollSpawnCol = Math.max(GameConstants.MAX_SCREEN_COL / 2 - 5, 0);

            new Reroll(rerollSpawnRow, rerollSpawnCol);
        }

        spawnItems();
    }

    /** Try spawning items on the floor */
    public void spawnItems() {
        if (itemPool.isEmpty())
            return;

        int spawnCol1 = Math.max(GameConstants.MAX_SCREEN_COL / 2 - 2, 0);
        int spawnCol2 = Math.min(GameConstants.MAX_SCREEN_COL / 2 + 2, GameConstants.MAX_SCREEN_COL);

        // If there are more than 1 available upgrade Pickables to drop
        List<Integer> poolToList = new ArrayList<>(itemPool);
        if (poolToList.size() > 1) {
            Collections.shuffle(poolToList, random);

            spawnItemOn(poolToList.get(0), GameConstants.MAX_SCREEN_ROW / 2, spawnCol1);
            spawnItemOn(poolToList.get(1), GameConstants.MAX_SCREEN_ROW / 2, spawnCol2);
        }
        else {
            spawnItemOn(poolToList.get(0), GameConstants.MAX_SCREEN_ROW / 2, GameConstants.MAX_SCREEN_COL/ 2);
        }
    }

    /**
     * Spawn a specified item on the TileMap
     * @param item An int corresponds to an upgrade Pickable
     * @param row the row the Pickable will be spawned on the TileMap
     * @param col the column the Pickable will be spawned on the TileMap 
     */
    private void spawnItemOn(int item, int row, int col) {
        switch (item) {
            case RUNNER_SHOES:
                new RunnerShoes(row, col);
                break;
            case GUN_LEVEL_UP:
                if (PlayerStats.getGunLevel() == 1)
                    new GunLevel2(row, col);
                if (PlayerStats.getGunLevel() == 2)
                    new GunLevel3(row, col);
                break;
            case BURST_SHOT:
                new BurstShot(row, col);
                break;
            default:
                break;
        }
    }

    /**
     * Remove an upgrade Pickable from Item Pool
     * @param itemToBeRemoved An int corresponds to an upgrade Pickable to be removed from the Item Pool
     */
    public void removePickableFromItemPool(int itemToBeRemoved) {
        itemPool.remove(itemToBeRemoved);
    } 
}
