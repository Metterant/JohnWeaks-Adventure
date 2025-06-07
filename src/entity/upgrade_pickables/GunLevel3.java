package entity.upgrade_pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.UpgradePickable;
import entity.player.Player;
import entity.player.PlayerStats;
import main.GameManager;
import util.GameConstants;
import util.Shop;

public class GunLevel3 extends UpgradePickable {
    //#region CONSTRUCTORS
    public GunLevel3() {
        super();
    }
    public GunLevel3(int row, int col) {
        super(row, col);
    }
    public GunLevel3(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();
        itemCost = 15;

        name = "GunLevel3";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/gun_level_3.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }
    
    @Override
    public boolean checkPickupConditions() {
        return (PlayerStats.removeCoins(itemCost));
    }

    @Override
    public void getPickedUp(Player player) {
        super.getPickedUp(player);
        GameManager.getInstance().shop.removePickableFromItemPool(Shop.GUN_LEVEL_UP);
        PlayerStats.setDamage(GameConstants.Player.DAMAGE_LEVEL_3);
        PlayerStats.setGunLevel(3);
    }
}
