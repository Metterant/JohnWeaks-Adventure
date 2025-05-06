package entity.pickables;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.Player;
import entity.Player.PlayerPowerup;
import util.GameConstants;

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

    public void draw(Graphics2D g2) {
        g2.drawImage(image, (int)posX, (int)posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
    }

    @Override
    public void getPickedUp(Player player) {
        System.out.println("Picked up Coffee");
        
        player.setPowerup(PlayerPowerup.SPEED_BOOST);
    }

    @Override
    public void dispose() {
        
    }
}
