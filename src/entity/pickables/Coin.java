package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerStats;

public class Coin extends Pickable {
    //#region CONSTRUCTORS
    public Coin() {
        super();
    }
    public Coin(int row, int col) {
        super(row, col);
    }
    public Coin(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();

        name = "Coin";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/coin.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void getPickedUp(Player player) {
        PlayerStats.addCoins(1);        
    }
}
