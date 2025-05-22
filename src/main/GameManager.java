package main;

import entity.enemy.Biker;
import entity.player.Player;
import input.PlayerController;
import util.GameComponent;
import util.GameConstants;
import util.Spawner;
import util.pathfinding.PathFinder;

public class GameManager implements GameComponent {

    // KeyHandler
    PlayerController playerController = new PlayerController();

    private int currentRound = 1; // Default round

    private int roundDuration;
    private int roundTimer = GameConstants.Game.BASE_ROUND_DURATION_FRAMES;

    // PLAYER
    public Player player = new Player(playerController);

    // PATHFINDER
    public final PathFinder pathFinder = new PathFinder();

    // SPAWNER
    private Spawner spawner = new Spawner();

    public int updateTick = 0;

    /**
     * @return the object that register player input
     */
    public PlayerController getPlayerController() { return playerController; }

    /**
     * Returns the number of frames to be counted to end the round
     * @return the number of frames
     */
    public int getRoundTimer() {
        return roundTimer;
    }
    
    /**
     * Returns the number of remaining frames to be counted
     * @return The amount of frames to be counted left
     */
    public int getRoundDuration() {
        return roundDuration;
    }

    /* UTILS */

    /** Goes to next round */
    public void nextRound() {
        currentRound++; 
        roundTimer = GameConstants.Game.BASE_ROUND_DURATION_FRAMES * (currentRound - 1) * GameConstants.Game.INCREMENT_DURATION_FRAMES;  
    }

    //#region GameComponent
    @Override
    public void start() {
        currentRound = 1;
        roundTimer = roundDuration = GameConstants.Game.BASE_ROUND_DURATION_FRAMES;
    }
    
    @Override
    public void update() {
        roundTimer--;
        updateTick++;

        if (updateTick % (GameConstants.FPS + 30) == 0) {
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

    // Hides constructor of the singleton Class
    private GameManager() { }
    //#endregion
}
