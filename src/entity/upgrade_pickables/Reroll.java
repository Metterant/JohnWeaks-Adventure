package entity.upgrade_pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.UpgradePickable;
import entity.player.Player;
import entity.player.PlayerStats;
import main.GameManager;
import util.EntityManager;

public class Reroll extends UpgradePickable {
    //#region CONSTRUCTORS
    public Reroll() {
        super();
    }
    public Reroll(int row, int col) {
        super(row, col);
    }
    public Reroll(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();
        itemCost = 2;

        name = "Reroll";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/reroll.png"));
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
        if (GameManager.getInstance().isInShop()) {
            EntityManager.getInstance().removeAllPickables();
            GameManager.getInstance().shop.spawnItems();
        }
    }
}

