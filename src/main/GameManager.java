package main;

import util.GameComponent;
import util.GameConstants;

public class GameManager implements GameComponent {

    private int currentRound = 1; // Default round

    private int roundDuration;
    private int roundTimer = GameConstants.Game.BASE_ROUND_DURATION;

    
    public int getRoundTimer() {
        return roundTimer;
    }
    
    public int getRoundDuration() {
        return roundDuration;
    }

    /* UTILS */

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
