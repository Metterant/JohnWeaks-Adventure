package entity;

import java.awt.Rectangle;

import main.KeyHandler;

import util.Renderable;

public abstract class Entity implements Renderable {
    protected double posX, posY;
    protected double movementSpeed;  

    // Input
    public KeyHandler keyHandler;

    // COLLISION HANDLING

    /** A rectangle box used for checking collision */
    public Rectangle collisionBox;
    /** Collision Box Wid in pixels */
    protected int collisionBoxWidth = 0;
    /** Collision Box Height in pixels */
    protected int collisionBoxHeight = 0;

    protected boolean isColliding = false;

    public double getPositionX() { return posX; }
    public double getPositionY() { return posY; }
    public double getMovementSpeed() { return movementSpeed; }

    public void setPositionX(double posX) { this.posX = posX; }
    public void setPositionY(double posY) { this.posY = posY; }

    @Override
    public void getImages() { }
}
