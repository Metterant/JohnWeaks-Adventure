package entity;

public abstract class Pickable extends Entity {

    /** Name of Pickable item */
    protected String name;

    public String getPickable() { return name; }

    //#region CONSTRUCTORS
    protected Pickable() {
        super();
    }
    protected Pickable(int row, int col) {
        super(row, col);
    }
    protected Pickable(double positionX, double positionY) {
        super(positionX, positionY);
    }
    //#endregion
    
    /**
     * Handle picking up action
     * 
     * @param entity Player that is going to pick up this item
     */
    public abstract void getPickedUp(Player player);
}
