package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.CollisionHandler;
import util.EntityManager;
import util.GameConstants;

public class Bullet extends Entity {

    private double directionX, directionY;
    private double bulletSpeed = 4.5d;

    private CollisionHandler collisionHandler = new CollisionHandler();

//#region CONSTRUCTORS
    public Bullet() {
        super();
    }
    public Bullet(int row, int col) {
        super(row, col);
    }
    public Bullet(double posX, double posY) {
        super(posX, posY);
    }

    // With initial direction
    public Bullet(int row, int col, double directionX, double directionY) {
        super(row, col);
        
        this.directionX = directionX;
        this.directionY = directionY;
    }
    public Bullet(double posX, double posY, double directionX, double directionY) {
        super(posX, posY);

        this.directionX = directionX;
        this.directionY = directionY;
    }
//#region

    @Override
    public void start() {
        super.start();
        System.out.println("hi");
        initBoxHelper(7, 7, 4, 4);
    }

    @Override
    public void update() {
        updatePosition();
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
        // g2.setColor(Color.red);
        // g2.fillRect(collisionBox.x, collisionBox.y, collisionBoxWidth, collisionBoxHeight);
    }

    @Override
    public void dispose() {
        // System.out.println("collided");
    }
    
}
