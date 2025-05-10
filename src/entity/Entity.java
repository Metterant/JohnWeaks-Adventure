package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import util.EntityManager;
import util.GameComponent;
import util.GameConstants;
import util.Renderable;

import java.util.logging.Logger;

public abstract class Entity implements Renderable, GameComponent {
    protected double posX, posY;
    
    /** Current Sprite of an Entity */
    protected BufferedImage image;
    
    // COLLISION HANDLING
    /** A rectangle box used for checking collision */
    public Rectangle collisionBox;
    /** Collision Box Wid in sprite pixels */
    protected int collisionBoxWidth = GameConstants.ORIGINAL_TILE_SIZE;
    /** Collision Box Height in sprite pixels */
    protected int collisionBoxHeight = GameConstants.ORIGINAL_TILE_SIZE;
    /** Collision Box horizontal offset */
    public int offsetX = 0;
    /** Collision Box vertical offset */
    public int offsetY = 0;

    protected boolean isColliding = false;

    public double getPositionX() { return posX; }
    public double getPositionY() { return posY; }
    
    /** Logger */
    public Logger logger = Logger.getLogger(getClass().getName());

    public void setPositionX(double posX) {
        // double displacement = posX - this.posX;
        this.posX = posX; 
        
        // collisionBox.x += (int)displacement;
        moveCollisionBox();
    }
    public void setPositionY(double posY) { 
        // double displacement = posY - this.posY;
        this.posY = posY; 
        
        // collisionBox.y += (int)displacement;
        moveCollisionBox();
    }

    /** Instantiate Entity with default position (0, 0) */
    protected Entity() { 
        posX = GameConstants.DEFAULT_SPAWN_X;
        posY = GameConstants.DEFAULT_SPAWN_Y;

        initBoxPosition();
        EntityManager.getInstance().addInstance(this);
    }
    
    /** Instantiate Entity on the TileMap */
    protected Entity(int row, int col) {
        posX = (double) col * GameConstants.TILE_SIZE;
        posY = (double) row * GameConstants.TILE_SIZE;

        initBoxPosition();
        EntityManager.getInstance().addInstance(this);
    }

    /** Instantiate Entity on using World Coordinates */
    protected Entity(double locationX, double locationY) {
        posX = locationX;
        posY = locationY;

        initBoxPosition();
        EntityManager.getInstance().addInstance(this);
    }

    /** Default initialization of Collision Box */
    protected void initBoxPosition() {
        collisionBox = new Rectangle((int)posX, (int)posY, 0, 0);
        collisionBox.width = collisionBoxWidth * GameConstants.SCALE;
        collisionBox.height = collisionBoxHeight * GameConstants.SCALE;
    }

    /** Initialization of collisionBox with Offset and Size variation <p>
     * 
     * @param offsetX : horizontal offset of collisionBox in sprite pixels
     * @param offsetY : vertical offset of collisionBox in sprite pixels
     * @param width : width of the collisionBox in sprite pixels
     * @param height : height of the collisionBox in sprite pixels
     */
    protected void initBoxHelper(int offsetX, int offsetY, int width, int height) {
        this.offsetX = offsetX * GameConstants.SCALE;
        this.offsetY = offsetY * GameConstants.SCALE;

        collisionBox.x = (int)posX + this.offsetX;
        collisionBox.y = (int)posY + this.offsetY;
        collisionBoxWidth = collisionBox.width = width * GameConstants.SCALE;
        collisionBoxHeight = collisionBox.height = height * GameConstants.SCALE;
    }
    
    /** Move Collision Box position */
    protected void moveCollisionBox() {
        collisionBox.x = (int)posX + offsetX;
        collisionBox.y = (int)posY + offsetY;
    }

    /** Start method */
    public void start() {
        loadImages();
    }

    /** Update method */
    public void update() { }

    /** How the entity would act when it is killed */
    public abstract void dispose();

    //#region DEBUGS

    public void debugPosition() {
        String toPrint = "[DEBUG] PosX : %.1f, PosY: %.1f";
        toPrint = String.format(toPrint, posX, posY);
        logger.info(toPrint);
    }
}
