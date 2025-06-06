package entity;

import java.awt.Graphics2D;

import entity.player.Player;
import util.EntityManager;
import util.GameConstants;

public abstract class Pickable extends Entity {

    /** Name of Pickable item */
    protected String name;

    protected int timeToLive;

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

    /**
     * Performs checks to determine if the Pickable can be picked up
     * @return whether a condition is met for the item to be picked up
     */
    public boolean checkPickupConditions() {
        return true;
    }

    @Override
    public void start() {
        timeToLive = GameConstants.Game.PICKABLE_TTL;
        super.start();
    }

    @Override
    public void update() {
        /* If TTL ends, the item perishes.
         * However, if TTL is less than 0 (i.e, -1), the item will not disappear.
         */
        if (timeToLive == 0)
            EntityManager.getInstance().destroyEntity(this);

        if (timeToLive > 0)
            timeToLive--;

        super.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        
        // If TTL is greater or equal to 0 and less than or equal to 5 seconds, then the sprite starts blipping 
        if (timeToLive >= 0 && timeToLive <= GameConstants.FPS * 5) {
            if (timeToLive % (GameConstants.FPS / 2) <= (GameConstants.FPS / 4)) {
                g2.drawImage(image, (int)posX, (int)posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
            }
        } else {
            g2.drawImage(image, (int)posX, (int)posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
        }
    }

    @Override
    public void dispose() { }
}
