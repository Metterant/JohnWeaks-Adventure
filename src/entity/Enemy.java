package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import input.AIController;
import main.GameManager;
import util.EntityManager;
import util.GameConstants;
import util.pathfinding.TileCoords;

public abstract class Enemy extends ControllableEntity {
    protected int health;

    protected int spriteCount = 1;
    protected BufferedImage[] sprites;
    
    // Animation
    protected int frameCount = 0;
    protected int spriteFrame = 0; 
    protected int damageFrameCount = 0;

    // TileCoords
    private TileCoords currentTileCoords; // The TileCoords the enemy is standing on
    private TileCoords nextTileCoords;

    // Target Player
    private Entity targetEntity = GameManager.getInstance().player;

    // PathFinding
    private int pathFindingCounter = Integer.MAX_VALUE;

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public TileCoords getCurrTileCoords() { return currentTileCoords; }
    /**
     * Retrieves the TileCoords the Enemy is heading to
     * @return TileCoords which is returned from a pathFinder search
     */
    public TileCoords getNextTileCoords() { return nextTileCoords; }

    protected Enemy () {
        super();
    }
    protected Enemy(int rol, int col) {
        super(rol, col);
    }
    protected Enemy(double positionX, double positionY) {
        super(positionX, positionY);
    }

    @Override
    public void start() {
        if (spriteCount == 0)
            throw new IllegalStateException("Illegal number of Sprites.");
        sprites = new BufferedImage[spriteCount];
        
        keyHandler = new AIController(this);

        super.start();
    }

    @Override
    public void update() {
        super.update();
        
        // ANIMATION
        frameCount++;
        if (frameCount >= 35) {
            spriteFrame = (spriteFrame < spriteCount) ? spriteFrame + 1 : 0; 
            frameCount = 0;
        }
        if (damageFrameCount > 0)
            damageFrameCount--;
        
        // TILE COORDS
        TileCoords targetTileCoords = getTileCoords(targetEntity);
        currentTileCoords = getTileCoords(this);
        
        // PATH FINDING
        // perform a search if the Enemy has reached the next Tile or an amount of time has passed after the last search 
        if (pathFindingCounter > (GameConstants.Game.PATHFINDER_PERIOD_FRAMES) || checkIfReachedPosition(nextTileCoords)) {
            pathFindingCounter = 0;
            try {
                nextTileCoords = GameManager.getInstance().pathFinder.search(getTileCoords(this), targetTileCoords);
            } 
            catch (IllegalArgumentException e) {
                logger.warning(e.getMessage());
            }
        }
        pathFindingCounter++;
        keyHandler.calculateInput();

        // MOVEMENT
        move();
    }

    @Override
    public void draw(Graphics2D g2) {

        // Apply a tint to the sprite before drawing
        BufferedImage tintedSprite = new BufferedImage(GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        // Create a seperate tint for tintedSprite
        Graphics2D tg2 = tintedSprite.createGraphics();
        tg2.drawImage(sprites[spriteFrame], 0, 0, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
        
        if (damageFrameCount > 0)
            tg2.setComposite(java.awt.AlphaComposite.SrcAtop.derive(1f)); // 100% opacity
        else
            tg2.setComposite(java.awt.AlphaComposite.SrcAtop.derive(0f)); // 100% opacity

        tg2.fillRect(0, 0, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
        tg2.dispose();

        g2.drawImage(tintedSprite, (int)posX, (int)posY, null);

        // Draw PathFinding
        // TileCoords test = getTileCoords(this);
        // g2.setColor(Color.red);
        // g2.drawRect(
        //     test.getCol() * GameConstants.TILE_SIZE,
        //     test.getRow() * GameConstants.TILE_SIZE,
        //     GameConstants.TILE_SIZE,
        //     GameConstants.TILE_SIZE
        // );
        // g2.setColor(Color.blue);
        // g2.drawRect(
        //     nextTileCoords.getCol() * GameConstants.TILE_SIZE,
        //     nextTileCoords.getRow() * GameConstants.TILE_SIZE,
        //     GameConstants.TILE_SIZE,
        //     GameConstants.TILE_SIZE
        // );
    }

    /**
     * Damage Enemy
     * 
     * @param damage : number of damage
     * @return Remaining number of damage
     */
    public int applyDamage(int damage) {
        if (damage < 0) throw new IllegalArgumentException("Damage can't be less than 0");

        health -= damage;

        damageFrameCount = 10;
        if (health <= 0) {
            EntityManager.getInstance().destroyEntity(this);
            return -health;
        }
        else return 0;
    }

    /**
     * Get TileCoords from the position of an Entity
     * @param entity : The entity whose position we want to retrieve
     * @return TileCoords which has row and col properties
     */
    private TileCoords getTileCoords(Entity entity) {
        double centerX = entity.collisionBox.x + (double) entity.collisionBoxWidth / 2;
        double centerY = entity.collisionBox.y + (double) entity.collisionBoxHeight / 2;

        return new TileCoords((int)centerY / GameConstants.TILE_SIZE, (int)centerX / GameConstants.TILE_SIZE);
    }

    /** Movement method */
    private void move() {
        // MOVEMENT
        double desiredAxialDisplacement = movementSpeed;
        double desiredPosX, desiredPosY;

        desiredPosX = posX + keyHandler.getInputMoveX() * desiredAxialDisplacement;
        desiredPosY = posY - keyHandler.getInputMoveY() * desiredAxialDisplacement;

        setPositionX(desiredPosX);
        setPositionY(desiredPosY);
    }
    
    /**
     * Checks if the Enemy is currently standing on the specified TileCoords
     * @param targetTile : the TileCoords
     * @return a boolean determined by checking whether the Enemy is being very close to the targetTile
     */
    private boolean checkIfReachedPosition(TileCoords targetTile) {
        double delta = (double)GameConstants.TILE_SIZE / 20;
        int targetX = GameConstants.TILE_SIZE * targetTile.getCol();
        int targetY = GameConstants.TILE_SIZE * targetTile.getRow();

        return ((posX > targetX - delta) && (posX < targetX + delta) && (posY > targetY - delta) && (posY < targetY + delta));
    }
}
