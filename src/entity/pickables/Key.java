package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;

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

    @Override
    public void getPickedUp(Player player) {
        System.out.println("Picked up a Key");
    }
}
