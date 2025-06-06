package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerStats;
import util.GameConstants;

public class GunLevel2 extends Pickable {
    //#region CONSTRUCTORS
    public GunLevel2() {
        super();
    }
    public GunLevel2(int row, int col) {
        super(row, col);
    }
    public GunLevel2(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();
        timeToLive = -1; // imperishable

        name = "GunLevel2";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/gun_level_2.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }
    
    @Override
    public boolean checkPickupConditions() {
        return (PlayerStats.getGunLevel() < 2 && PlayerStats.removeCoins(10));
    }

    @Override
    public void getPickedUp(Player player) {
        PlayerStats.setDamage(GameConstants.Player.DAMAGE_LEVEL_2);
        PlayerStats.setGunLevel(2);
    }
}
