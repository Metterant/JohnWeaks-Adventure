package entity;

import util.Renderable;

public abstract class Entity implements Renderable {
    protected double posX, posY;
    protected double movementSpeed;   

    @Override
    public void getImages() { }
}
