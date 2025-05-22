package entity.pickables;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import util.GameConstants;

public class Key extends Pickable {
    //#region CONSTRUCTORS
    public Key() {
        super();
    }
    public Key(int row, int col) {
        super(row, col);
    }
    public Key(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();

        name = "Key";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/key.png"));
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
        System.out.println("Picked up a Key");
    }

    @Override
    public void dispose() {
        
    }
}
