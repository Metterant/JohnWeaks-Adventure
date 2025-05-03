package entity;

public abstract class Pickable extends Entity {

    /** Name of Pickable item */
    protected String name;

    public String getPickable() { return name; }

    /**
     * Handle picking up action
     * 
     * @param entity ControllableEntity that is going to pick up this item
     */
    public abstract void getPickedUp(ControllableEntity entity);
}
