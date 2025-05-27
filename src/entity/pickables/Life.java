package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerPowerup;
import entity.player.PlayerStats;

public class Life extends Pickable {
    //#region CONSTRUCTORS
    public Life() {
        super();
    }
    public Life(int row, int col) {
        super(row, col);
    }
    public Life(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();

        name = "Life";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/ui/lives_icon.png"));
        }
        catch (IOException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void getPickedUp(Player player) {        
        PlayerStats.addLife();
    }

    
}
