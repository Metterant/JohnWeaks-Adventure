package entity;

import util.Renderable;

public abstract class Entity implements Renderable {
    protected float posX, posY;
    protected float movementSpeed;   

    @Override
    public void getImages() { }
}
