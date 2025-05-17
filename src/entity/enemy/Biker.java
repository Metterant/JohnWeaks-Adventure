package entity.enemy;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Enemy;
import util.pathfinding.PathFinder;

public class Biker extends Enemy {

    //#region CONSTRUCTORS
    public Biker() {
        super();
    }
    public Biker(int row, int col) {
        super(row, col);
    }
    public Biker(double positionX, double positionY) {
        super(positionX, positionY);
    }

    @Override
    public void loadImages() {
        try {
            sprites[0] = ImageIO.read(getClass().getResourceAsStream("/resources/entities/biker/biker1.png"));
            sprites[1] = ImageIO.read(getClass().getResourceAsStream("/resources/entities/biker/biker2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        logger.info("Biker died");
    }
    
    @Override
    public void start() {
        health = 2; 
        spriteCount = 2;

        super.start();
        pathFinder = new PathFinder();
    }
}
