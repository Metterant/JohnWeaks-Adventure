package entity.upgrade_pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.UpgradePickable;
import entity.player.Player;
import entity.player.PlayerStats;
import main.GameManager;
import util.Shop;

public class BurstShot extends UpgradePickable {
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
        itemCost = 15;

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
        return (PlayerStats.removeCoins(itemCost));
    }

    @Override
    public void getPickedUp(Player player) {
        super.getPickedUp(player);
        GameManager.getInstance().shop.removePickableFromItemPool(Shop.BURST_SHOT);
        PlayerStats.setHasBurstShot(true);
    }
}
