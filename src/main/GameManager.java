package main;

import entity.player.Player;
import input.PlayerController;
import util.GameComponent;
import util.GameConstants;
import util.Spawner;
import util.pathfinding.PathFinder;

public class GameManager implements GameComponent {

    // Player input controller
    private final PlayerController playerController = new PlayerController();

    // Game round state
    private int currentRound = 1;
    private int roundDurationFrames;
    private int roundTimerFrames = GameConstants.Game.BASE_ROUND_DURATION_FRAMES;

    // Player state
    public Player player = new Player(playerController);
    private boolean playerDied = false;
    private int respawnTimerFrames = 0;

    // Pathfinding utility
    public final PathFinder pathFinder = new PathFinder();

    // Enemy spawner
    private final Spawner spawner = new Spawner();

    // Update tick counter
    public int updateTick = 0;

    /**
     * @return the object that register player input
     */
    public PlayerController getPlayerController() { return playerController; }

    /**
     * Returns the number of frames to be counted to end the round
     * @return the number of frames
     */
    public int getRoundTimerFrames() {
        return roundTimerFrames;
    }
    
    /**
     * Returns the number of remaining frames to be counted
     * @return The amount of frames to be counted left
     */
    public int getRoundDurationFrames() {
        return roundDurationFrames;
    }

    /**
     * Returns a boolean that indicates that the Player is living <p>
     * 
     * @return A boolean that indicates that the Player is living
     */
    public boolean getPlayerDied() { return playerDied; }

    /**
     * Sets the boolean that determines if the player is still kicking butts,
     * and activate respawn timer
     * @param playerDied : a boolean indicates that Player has died
     */
    public void setPlayerDied(boolean playerDied) {
        respawnTimerFrames = GameConstants.Game.RESPAWN_DURATION_FRAMES;
        GameManager.getInstance().playerDied = playerDied;
    }

    /* UTILS */

    /** Goes to next round */
    public void nextRound() {
        currentRound++; 
        roundTimerFrames = GameConstants.Game.BASE_ROUND_DURATION_FRAMES * (currentRound - 1) * GameConstants.Game.INCREMENT_DURATION_FRAMES;  
    }

    //#region GameComponent
    @Override
    public void start() {
        currentRound = 1;
        roundTimerFrames = roundDurationFrames = GameConstants.Game.BASE_ROUND_DURATION_FRAMES;
    }
    
    @Override
    public void update() {

        if (playerDied) {
            if (respawnTimerFrames == 0)
                respawnPlayer();

            if (respawnTimerFrames > 0)
                respawnTimerFrames--;
        
        } else {
            if (roundTimerFrames > 0)
                roundTimerFrames--;
        }

        updateTick++;
        
        // Spawner
        if (roundTimerFrames > 0 && !playerDied && player != null && updateTick % (GameConstants.FPS + 30) == 0) {
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

    /** Change Player instance */
    private void respawnPlayer() {
        player = new Player(playerController);
        playerDied = false;
    }

    // Hides constructor of the singleton Class
    private GameManager() { }
    //#endregion
}
