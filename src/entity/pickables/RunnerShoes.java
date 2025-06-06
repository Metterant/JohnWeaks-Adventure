package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerStats;
import util.GameConstants;

public class RunnerShoes extends Pickable {
    //#region CONSTRUCTORS
    public RunnerShoes() {
        super();
    }
    public RunnerShoes(int row, int col) {
        super(row, col);
    }
    public RunnerShoes(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();
        timeToLive = -1; // imperishable

        name = "RunnerShoes";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/runner_shoes.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }
    
    @Override
    public boolean checkPickupConditions() {
        return (PlayerStats.removeCoins(12));
    }

    @Override
    public void getPickedUp(Player player) {
        PlayerStats.setCurrentBaseSpeed(GameConstants.Player.SPEED_RUNNER);
        PlayerStats.setHasRunnerBoots(true);
    }
}
