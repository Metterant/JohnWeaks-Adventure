package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerStats;
import util.GameConstants;

public class BurstShot extends Pickable {
    //#region CONSTRUCTORS
    public BurstShot() {
        super();
    }
    public BurstShot(int row, int col) {
        super(row, col);
    }
    public BurstShot(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();
        timeToLive = -1; // imperishable

        name = "BurstShot";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/burst_shot.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }
    
    @Override
    public boolean checkPickupConditions() {
        return (PlayerStats.removeCoins(15));
    }

    @Override
    public void getPickedUp(Player player) {
        PlayerStats.setHasBurstShot(true);
    }
}
