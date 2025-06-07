package entity.upgrade_pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.UpgradePickable;
import entity.player.Player;
import entity.player.PlayerStats;
import main.GameManager;
import util.GameConstants;
import util.Shop;

public class RunnerShoes extends UpgradePickable {
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
        itemCost = 15;

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
        return (PlayerStats.removeCoins(itemCost));
    }

    @Override
    public void getPickedUp(Player player) {
        super.getPickedUp(player);
        GameManager.getInstance().shop.removePickableFromItemPool(Shop.RUNNER_SHOES);
        PlayerStats.setCurrentBaseSpeed(GameConstants.Player.SPEED_RUNNER);
        PlayerStats.setHasRunnerBoots(true);
    }
}
