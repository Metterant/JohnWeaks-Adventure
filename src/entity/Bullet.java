package entity;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.CollisionHandler;
import util.EnemyCollidable;
import util.EntityManager;
import util.GameConstants;

public class Bullet extends Entity implements EnemyCollidable {

    private double directionX, directionY;
    private double bulletSpeed = 7.5d;
    private int damage;

    private CollisionHandler collisionHandler = new CollisionHandler();

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; } 

//#region CONSTRUCTORS
    public Bullet(int damage) {
        super();
        this.damage = damage;
    }
    public Bullet(int row, int col, int damage) {
        super(row, col);
        this.damage = damage;
    }
    public Bullet(double posX, double posY, int damage) {
        super(posX, posY);
        this.damage = damage;
    }

    // With initial direction
    public Bullet(int row, int col, double directionX, double directionY, int damage) {
        super(row, col);
        
        this.directionX = directionX;
        this.directionY = directionY;
        this.damage = damage;
    }
    public Bullet(double posX, double posY, double directionX, double directionY, int damage) {
        super(posX, posY);

        this.directionX = directionX;
        this.directionY = directionY;
        this.damage = damage;
    }
    // With rotated angle
    public Bullet(int row, int col, double directionX, double directionY, double theta, int damage) {
        super(row, col);
        
        this.directionX = directionX;
        this.directionY = directionY;
        this.damage = damage;

        rotateDirectionWithAngle(theta);
    }
    public Bullet(double posX, double posY, double directionX, double directionY, double theta, int damage) {
        super(posX, posY);

        this.directionX = directionX;
        this.directionY = directionY;
        this.damage = damage;

        rotateDirectionWithAngle(theta);
    }

//#region

    @Override
    public void start() {
        super.start();
        initBoxHelper(8, 8, 2, 2);
    }

    @Override
    public void update() {
        updatePosition();
        checkEnemy();
    }

    void updatePosition() {
        // System.out.printf("DirX : %.1f, DirY : %.1f\n", this.directionX, this.directionY);
        // System.out.printf("PosX : %.1f, PosY : %.1f\n", posX, posY);

        setPositionX(posX + directionX * bulletSpeed);
        setPositionY(posY - directionY * bulletSpeed);
        
        if (collisionHandler.checkTile(this, posX, posY, bulletSpeed, directionX, directionY)) {
            EntityManager.getInstance().destroyEntity(this);
        }
    }

    @Override
    public void loadImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/entities/bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, (int)posX, (int)posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);

        // Draw Collision Box
        // g2.setColor(java.awt.Color.red);
        // g2.drawRect(collisionBox.x, collisionBox.y, collisionBoxWidth, collisionBoxHeight);
    }

    @Override
    public void dispose() {
        // System.out.println("collided");
    }

    private void rotateDirectionWithAngle(double theta) {
        /*
         * x1, y1 : initial values
         * x2, y2 : values of vector's components after being rotated
         * theta : counter-clockwise angle
         */
        double x1 = directionX, y1 = directionY;
        directionX = Math.cos(theta) * x1 - Math.sin(theta) * y1;
        directionY = Math.sin(theta) * x1 + Math.cos(theta) * y1;
    }
    
    @Override
    public void checkEnemy() {
        // Get entities
        var entities = EntityManager.getInstance().instantiatedEntities;
        // Iterate through the list
        for (int i = 0; i < entities.size(); i++) {
            Entity nearbyEntity = entities.get(i);
            if (nearbyEntity == null || nearbyEntity == this) continue;

            if (nearbyEntity instanceof Enemy enemy && collisionHandler.isColliding(nearbyEntity, this)) {
                int remainingDamage = enemy.applyDamage(damage);

                if (remainingDamage == 0) {
                    setPositionToNowhere();
                    // Destroy the bullet after its damage has depleted
                    EntityManager.getInstance().destroyEntity(this);
                }
                // Otherwise reduce damage after penetration
                else damage = remainingDamage;
            }
        }
    }

    /** Sets the Bullet's position outside of the screen to prevent further collisions */
    private void setPositionToNowhere() {
        setPositionX(-100.0);
        setPositionY(-100.0);
    }
}
