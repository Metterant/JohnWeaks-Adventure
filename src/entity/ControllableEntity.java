package entity;

import java.awt.RenderingHints.Key;

import input.KeyHandler;
import util.GameConstants;

public abstract class ControllableEntity extends Entity {
    protected double movementSpeed;  
    
    // Input
    public KeyHandler keyHandler;

    // Getter
    public double getMovementSpeed() { return movementSpeed; }

    // Setter
    public void setMovementSpeed(double movementSpeed) { this.movementSpeed = movementSpeed; } 
    // public void setPositionX(double posX) { this.posX = posX; }
    // public void setPositionY(double posY) { this.posY = posY; }

    protected void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }
}
