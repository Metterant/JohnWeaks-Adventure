package entity.upgrade_pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.UpgradePickable;
import entity.player.Player;
import entity.player.PlayerStats;
import util.GameConstants;

public class GunLevel2 extends UpgradePickable {
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
        itemCost = 12;

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
        return (PlayerStats.getGunLevel() < 2 && PlayerStats.removeCoins(itemCost));
    }

    @Override
    public void getPickedUp(Player player) {
        super.getPickedUp(player);
        PlayerStats.setDamage(GameConstants.Player.DAMAGE_LEVEL_2);
        PlayerStats.setGunLevel(2);
    }
}
