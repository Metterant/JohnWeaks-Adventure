package entity;

import input.KeyHandler;

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

    /// CONSTRUCTORS
    protected ControllableEntity() {
        super();
    }
    protected ControllableEntity(int row, int col) {
        super(row, col);
    }
    protected ControllableEntity(double locationX, double locationY) {
        super(locationX, locationY);
    }

    protected void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }
}
