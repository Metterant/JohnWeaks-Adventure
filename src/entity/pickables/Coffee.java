package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerPowerup;

public class Coffee extends Pickable {
    //#region CONSTRUCTORS
    public Coffee() {
        super();
    }
    public Coffee(int row, int col) {
        super(row, col);
    }
    public Coffee(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();

        name = "Coffee";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/coffee.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }
    
    @Override
    public void getPickedUp(Player player) {        
        player.setPowerup(PlayerPowerup.SPEED_BOOST);
    }
}
