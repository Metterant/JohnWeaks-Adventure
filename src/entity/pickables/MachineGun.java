package entity.pickables;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerPowerup;
import util.GameConstants;

public class MachineGun extends Pickable {
    //#region CONSTRUCTORS
    public MachineGun() {
        super();
    }
    public MachineGun(int row, int col) {
        super(row, col);
    }
    public MachineGun(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion

    @Override
    public void start() {
        super.start();

        name = "MachineGun";
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/machine_gun.png"));
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
        System.out.println("Picked up MachineGun");
        
        player.setPowerup(PlayerPowerup.MACHINE_GUN);
    }

    
}
