package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import util.EntityManager;
import util.GameConstants;

public abstract class Enemy extends ControllableEntity {
    protected int health;

    protected int spriteCount = 1;
    protected BufferedImage[] sprites;
    
    // Animation
    protected int frameCount = 0;
    protected int spriteFrame = 0; 

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

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

    /**
     * Damage Enemy
     * 
     * @param damage : number of damage
     * @return Remaining number of damage
     */
    public int applyDamage(int damage) {
        if (damage < 0) throw new IllegalArgumentException("Damage can't be less than 0");

        health -= damage;

        if (health <= 0) {
            EntityManager.getInstance().destroyEntity(this);
            return -health;
        }
        else return 0;
    }
}
