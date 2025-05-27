package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerPowerup;

public class Shotgun extends Pickable {
    //#region CONSTRUCTORS
    public Shotgun() {
        super();
    }
    public Shotgun(int row, int col) {
        super(row, col);
    }
    public Shotgun(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();

        name = "Shotgun";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/shotgun.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void getPickedUp(Player player) {        
        player.setPowerup(PlayerPowerup.SHOTGUN);
    }

    
}
