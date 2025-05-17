package main;

import entity.Player;
import input.PlayerController;
import util.GameComponent;
import util.GameConstants;

public class GameManager implements GameComponent {

    // KeyHandler
    PlayerController playerController = new PlayerController();

    private int currentRound = 1; // Default round

    private int roundDuration;
    private int roundTimer = GameConstants.Game.BASE_ROUND_DURATION;

    // PLAYER
    public Player player = new Player(playerController);

    public int updateTick = 0;

    /**
     * @return the object that register player input
     */
    public PlayerController getPlayerController() { return playerController; }

    /**
     * Return the number of frames to be counted to end the round
     * @return the number of frames
     */
    public int getRoundTimer() {
        return roundTimer;
    }
    
    /**
     * Return the number of remaining frames to be counted
     * @return The amount of frames to be counted left
     */
    public int getRoundDuration() {
        return roundDuration;
    }

    /* UTILS */

    /** Go to next round */
    public void nextRound() {
        currentRound++; 
        roundTimer = GameConstants.Game.BASE_ROUND_DURATION * (currentRound - 1) * GameConstants.Game.INCREMENT_DURATION;  
    }

    //#region GameComponent
    @Override
    public void start() {
        currentRound = 1;
        roundTimer = roundDuration = GameConstants.Game.BASE_ROUND_DURATION;
    }
    
    @Override
    public void update() {
        roundTimer--;
        updateTick++;
    }
    //#endregion

    //#region SINGLETON
    private static GameManager instance = new GameManager();
    // Return the instance of GameManager
    public static GameManager getInstance() {
        return instance;
    }

    // Hide constructor of the singleton Class
    private GameManager() { }
    //#endregion
}
