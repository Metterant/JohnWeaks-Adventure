package main;

import java.util.Random;

import entity.player.Player;
import entity.player.PlayerStats;
import input.PlayerController;
import tile.TileManager;
import util.EntityManager;
import util.GameComponent;
import util.GameConstants;
import util.RenewableSingleton;
import util.Shop;
import util.Spawner;
import util.pathfinding.PathFinder;

public class GameManager implements GameComponent, RenewableSingleton {

    // Player input controller
    private final PlayerController playerController = new PlayerController();

    // Random instance for reuse
    private final Random random = new Random();

    // Game stage state
    private int currentStage = 1;
    private int preStageTimerFrames; 
    private int stageDurationFrames;
    private int stageTimerFrames = GameConstants.Game.BASE_ROUND_DURATION_FRAMES;
    private boolean isStageOver;
    private boolean isGameOver; 

    // Player state
    public Player player = new Player(playerController);
    private boolean playerDied = false;
    private int respawnTimerFrames = 0;

    // Pathfinding utility
    public final PathFinder pathFinder = new PathFinder();

    // Enemy spawner
    private final Spawner spawner = new Spawner();

    // Shop
    public final Shop shop = new Shop();
    private boolean isInShop = false;
    
    // Update tick counter
    private int updateTick = 0;

    // Debugs
    public boolean debugMode = false;

    /**
     * @return the object that register player input
     */
    public PlayerController getPlayerController() { return playerController; }

    /**
     * Returns the number of frames to be counted to end the stage (level)
     * @return the number of frames
     */
    public int getStageTimerFrames() {
        return stageTimerFrames;
    }
    
    /**
     * Returns the number of remaining frames to be counted
     * @return The amount of frames to be counted left
     */
    public int getStageDurationFrames() {
        return stageDurationFrames;
    }

    /**
     * Returns a number that tells the current stage (level)
     * @return a number that indicates the current stage
     */
    public int getCurentStage() { return currentStage; }

    /**
     * Returns a boolean that indicates that the Player is living <p>
     * @return true if the player has died and hasn't respawned, otherwise false 
     */
    public boolean getPlayerDied() { return playerDied; }

    /**
     * Checks if the current stage is over
     * @return true if the stage is over. Otherwise, false
     */
    public boolean isStageOver() { return isStageOver; }

    /**
     * Checks if the game is over
     * @return true if the game is over. Otherwise, false
     */
    public boolean isGameOver() { return isGameOver; }

    /**
     * Checks if the Player is in shopping stage (level)
     * @return true if the current stage is shop. Otherwise, false
     */
    public boolean isInShop() { return isInShop; }

    /**
     * Sets the boolean that determines if the player is still kicking butts,
     * and activate respawn timer
     * @param playerDied : true if the player has died and hasn't respawned, otherwise false 
     */
    public void setPlayerDied(boolean playerDied) {
        respawnTimerFrames = GameConstants.Game.RESPAWN_DURATION_FRAMES;
        GameManager.getInstance().playerDied = playerDied;
    }

    //#region GameComponent
    @Override
    public void start() {
        currentStage = 1;
        preStageTimerFrames = GameConstants.Game.PREROUND_DURATION_FRAMES;
        stageTimerFrames    = stageDurationFrames = GameConstants.Game.BASE_ROUND_DURATION_FRAMES;
    }
    
    @Override
    public void update() {
        if (PlayerStats.getLivesLeft() < 0)
            gameOver();

        isStageOver = ((EntityManager.getInstance().getEnemyCount() == 0 && stageTimerFrames <= 0) || debugMode);

        if (preStageTimerFrames > 0)
            preStageTimerFrames--;

        if (playerDied) {
            if (respawnTimerFrames == 0)
                respawnPlayer();

            if (respawnTimerFrames > 0)
                respawnTimerFrames--;
        
        } else {
            if (stageTimerFrames > 0 && preStageTimerFrames == 0)
                stageTimerFrames--;
        }

        updateTick++;

        // Spawner
        if (!isInShop && preStageTimerFrames == 0 && stageTimerFrames > 0 && !playerDied && player != null 
            && updateTick % Math.max(GameConstants.FPS + 30 - currentStage * 10, 40) == 0) {
            spawner.spawnEnemyHorde();
        }
    }
    //#endregion

    //#region SINGLETON
    private static GameManager instance = new GameManager();
    // Return the instance of GameManager
    public static GameManager getInstance() {
        return instance;
    }

    /* UTILS */

    /** Goes to next stage */
    public void nextStage() {
        /* CLEAN UP */
        // Clear items
        EntityManager.getInstance().removeAllEnemies();
        EntityManager.getInstance().removeAllPickables();
        EntityManager.getInstance().removeAllBullets();

        currentStage++; 
        
        if (currentStage % 3 != 0) {
            isInShop = false;
            stageDurationFrames += GameConstants.Game.INCREMENT_DURATION_FRAMES;  
            stageTimerFrames    = stageDurationFrames;  
            preStageTimerFrames = GameConstants.Game.PREROUND_DURATION_FRAMES;
        }
        else {
            stageTimerFrames    = 0;  
            preStageTimerFrames = 0;

            isInShop = true;
            enterShop();
        }

        // Reset player position
        player.setPositionX(GameConstants.DEFAULT_SPAWN_X);
        player.setPositionY((double)GameConstants.TILE_SIZE * 2); // Hard-coded

        if (currentStage < 10)
            TileManager.getInstance().loadMap(GameConstants.Game.getMapStrings()[currentStage - 1]);
        else 
            TileManager.getInstance().loadMap(GameConstants.Game.getMapPool()[random.nextInt(GameConstants.Game.MAPS_COUNT)]);
    }

    /** Change Player instance */
    private void respawnPlayer() {
        player = new Player(playerController);
        playerDied = false;
    }

    private void enterShop() {
        shop.enterShop();
    }
    
    /** Handle Game End */
    private void gameOver() {
        isGameOver = true;
    }

    // Hides constructor of the singleton Class
    private GameManager() { }
    //#endregion

    public static void resetSingleton() {
        instance = new GameManager();
        // instance.respawnPlayer();
    }

}
