package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import util.GameConstants;

public abstract class Enemy extends ControllableEntity {
    public int health = 1;

    protected int spriteCount = 1;
    protected BufferedImage[] sprites;
    
    // Animation
    protected int frameCount = 0;
    protected int spriteFrame = 0; 

    protected Enemy () {
        super();
    }
    protected Enemy(int rol, int col) {
        super(rol, col);
    }
    protected Enemy(double positionX, double positionY) {
        super(positionX, positionY);
    }

    @Override
    public void start() {
        if (spriteCount == 0)
            throw new IllegalStateException("Illegal number of Sprites.");
        sprites = new BufferedImage[spriteCount];

        super.start();
    }

    @Override
    public void update() {
        super.update();

        frameCount++;
        if (frameCount >= 35) {
            spriteFrame = (spriteFrame < spriteCount) ? spriteFrame + 1 : 0; 
            frameCount = 0;
        }

        // DEBUGS
        // System.out.println(spriteFrame + " " + frameCount);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(sprites[spriteFrame], (int)posX, (int)posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
    }
}
