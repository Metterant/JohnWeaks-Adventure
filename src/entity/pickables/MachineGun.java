package entity.pickables;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Pickable;
import entity.player.Player;
import entity.player.PlayerPowerup;

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

    @Override
    public void getPickedUp(Player player) {        
        player.setPowerup(PlayerPowerup.MACHINE_GUN);
    }

    
}
