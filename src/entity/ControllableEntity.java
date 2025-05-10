package entity;

import input.InputController;

public abstract class ControllableEntity extends Entity {
    protected double movementSpeed;  
    
    // Input
    public InputController keyHandler;

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

    protected void setKeyHandler(InputController keyHandler) {
        this.keyHandler = keyHandler;
    }
}
