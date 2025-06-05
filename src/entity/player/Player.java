package entity.player;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Bullet;
import entity.ControllableEntity;
import entity.Enemy;
import entity.Entity;
import input.InputController;
import main.GameManager;
import tile.TileConstants;
import tile.TileManager;
import util.CollisionHandler;
import util.EnemyCollidable;
import util.EntityManager;
import util.GameConstants;
import util.pathfinding.TileCoords;

public class Player extends ControllableEntity implements EnemyCollidable {
    // State
    /**
     * Inside {@Link entity.Player} <p>
     * 
     * Enum for determining the state of an animmation of a Player 
     */
    private PlayerAnimState animState;
    private PlayerAnimState lastState;

    // Power-up
    /**
     * Inside {@Link entity.Player} <P>
     * 
     * Enum for determining what powerup the player is holding
     */
    private PlayerPowerup currentPowerup;
    PlayerStatusEffect statusEffect = new PlayerStatusEffect();

    // Shooting mode
    /**
     * Inside {@Link entity.Player} <P>
     * 
     * Enum for determining what shooting mode the player is using
     */
    private int shootFramesPerShot = GameConstants.Player.BASE_FRAMES_PER_SHOT;
    private PlayerShootingMode currentShootingMode;
    private PlayerShootingMode defauPlayerShootingMode;

    // Sprites
    private BufferedImage[] walkingDownSprite, walkingUpSprite, walkingSideSprite, walkingShootingDownSprite;
    private BufferedImage idleUpSprite, idleDownSprite;
    private int frameCounter, frameNum;
    private boolean isFlipped;

    // Input
    private float lastInputY = 0;

    // Logic-related
    private double lastPosX;
    private double lastPosY;
    private int lastShootInputY;
    private boolean isMoving;
    private boolean lastSpaceInput;
    
    // Collison Handler
    CollisionHandler collisionHandler;

    // Shooting Logic
    private int shootingFrameCount = 0;

    //#region CONSTRUCTORS
    public Player(InputController keyHandler) {
        super();
        
        this.keyHandler = keyHandler;
    }
    public Player(int row, int col, InputController keyHandler) {
        super(row, col);

        this.keyHandler = keyHandler;
    }
    public Player(double locationX, double locationY, InputController keyHandler) {
        super(locationX, locationY);

        this.keyHandler = keyHandler;
    }
    //#endregion

    private void setDefaultValues() {
        lastPosX = posX;
        lastPosY = posY;
        movementSpeed = 2.5d;
        lastState = animState = PlayerAnimState.IDLE_DOWN;

        currentPowerup = PlayerPowerup.NONE;
    
        walkingDownSprite = new BufferedImage[4];
        walkingUpSprite = new BufferedImage[4];
        walkingSideSprite = new BufferedImage[4];
        walkingShootingDownSprite = new BufferedImage[4];
        
        lastShootInputY = 0;
        frameCounter = 0;

        collisionBoxWidth = 9 + (GameConstants.ORIGINAL_TILE_SIZE - 16) / 2;
        collisionBoxHeight = 9 + (GameConstants.ORIGINAL_TILE_SIZE - 16) / 2;
        
        collisionBox = new Rectangle();
        initBoxHelper(4, 8, collisionBoxWidth, collisionBoxHeight);
        
        // Collision Handler
        collisionHandler = new CollisionHandler();
        
        // SHOOTING MODE
        currentShootingMode = PlayerShootingMode.NORMAL;
    }
    
    /**
     * From {@link util.Renderable} interface <p>
     * 
     * Load Sprites from "resources"
     */
    @Override
    public void loadImages() {
        try {
            idleDownSprite = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_idle1.png"));
            idleUpSprite = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_idle2.png"));
            
            // Walking Down
            walkingDownSprite[0] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_down1.png"));
            walkingDownSprite[1] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_down2.png"));
            walkingDownSprite[2] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_down3.png"));
            walkingDownSprite[3] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_down4.png"));
            
            // Walking Up
            walkingUpSprite[0] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_up1.png"));
            walkingUpSprite[1] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_up2.png"));
            walkingUpSprite[2] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_up3.png"));
            walkingUpSprite[3] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_up4.png"));
            
            // Walking to the Side (shooting included)
            walkingSideSprite[0] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_side1.png"));
            walkingSideSprite[1] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_side2.png"));
            walkingSideSprite[2] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_side3.png"));
            walkingSideSprite[3] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_side4.png"));
            
            // Walking Down and Shooting
            walkingShootingDownSprite[0] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down1.png"));
            walkingShootingDownSprite[1] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down2.png"));
            walkingShootingDownSprite[2] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down3.png"));
            walkingShootingDownSprite[3] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down4.png"));
            
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    //#region START
    @Override
    public void start() {
        setDefaultValues();
        super.start();
    }
    //#endregion
    

    //#region UPDATE
    @Override
    public void update() {
        // MOVEMENT
        double desiredAxialDisplacement = movementSpeed;
        double desiredPosX, desiredPosY;
        if (keyHandler.getInputMoveX() != 0 && keyHandler.getInputMoveY() != 0)
            desiredAxialDisplacement /= Math.sqrt(2);

        desiredPosX = posX + keyHandler.getInputMoveX() * desiredAxialDisplacement;
        desiredPosY = posY - keyHandler.getInputMoveY() * desiredAxialDisplacement;

        setPositionX(desiredPosX);
        setPositionY(desiredPosY);

        // POWERUP INPUT
        if (!lastSpaceInput && keyHandler.getSpaceInput()) {
            usePowerup(currentPowerup);
            currentPowerup = PlayerPowerup.NONE;
        }
        lastSpaceInput = keyHandler.getSpaceInput();

        // TIMERS
        handleStatusEffect();
        
        // Collision
        TileCoords collidedTile = collisionHandler.checkTile(this, desiredPosX, desiredPosY, desiredAxialDisplacement, keyHandler.getInputMoveX(), keyHandler.getInputMoveY());
        collisionHandler.checkPickable(this);
        checkEnemy();

        // If the collided Tile is Next Round Tile, then move onto next round
        if (EntityManager.getInstance().getEnemyCount() == 0 && collidedTile != null && TileManager.getInstance().tileMapNum[collidedTile.getCol()][collidedTile.getRow()] == TileConstants.NEXT_ROUND_TILE && GameManager.getInstance().getRoundTimerFrames() <= 0)
            GameManager.getInstance().nextRound();
        
        
        // SHOOTING
        handleShooting();

        // LOGIC
        isMoving = true;
        if (lastPosX == posX && lastPosY == posY)
            isMoving = false;
        lastPosX = posX;
        lastPosY = posY;
        
        animLogic();
        lastInputY = keyHandler.getInputMoveY();
        lastShootInputY = keyHandler.getInputShootY();
        
        // ANIMATION
        // Increase the counter every 8 frames
        if (frameCounter >= 8) {
            frameNum++;
            if (frameNum == 4) {
                frameNum = 0;
            }
            frameCounter = 0;
        }
        frameCounter++;
        
        /** DEBUGGING **/
        // System.out.println(speedBoostTimer);
        // System.out.printf("x=%.1f, y=%.1f\n", posX, posY);
    }
    //#endregion

    public void draw(Graphics2D g2) {
        switch (animState) {
            case IDLE_UP:
                image = idleUpSprite;
                break;
            case IDLE_DOWN:
                image = idleDownSprite;
                break;
            case WALKING_DOWN:
                image = walkingDownSprite[frameNum];
                break;
            case WALKING_UP:
                image = walkingUpSprite[frameNum];
                break;
            case WALKING_RIGHT:
                image = walkingSideSprite[frameNum];
                break;
            case WALKING_LEFT:
                isFlipped = true;
                image = walkingSideSprite[frameNum];
                break;
            case SHOOTING_DOWN_WALKING:
                image = walkingShootingDownSprite[frameNum];
                break;
            case SHOOTING_UP_WALKING:
                image = walkingUpSprite[frameNum]; // Reuse walking up sprite for shooting
                break;
            case SHOOTING_DOWN_STILL:
                image = walkingShootingDownSprite[1]; // Use the second frame of side sprite for shooting down
                break;
            case SHOOTING_RIGHT_STILL:
                image = walkingSideSprite[1]; // Use the second frame of side sprite for shooting to the side
                break;
            case SHOOTING_LEFT_STILL:
                isFlipped = true;
                image = walkingSideSprite[1]; // Use the second frame of side sprite for shooting
                break;
            case SHOOTING_UP_STILL:
                image = idleUpSprite; // Reuse idle up sprite for shooting
                break;
            default:
                image = idleDownSprite; // Default to down if direction is invalid
                break;
        }

        if (image != null) {
            if (isFlipped) {
                // Flip the sprite
                g2.drawImage(image, (int)posX + GameConstants.TILE_SIZE, (int)posY, -GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
            }
            else g2.drawImage(image, (int)posX, (int)posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
            
        }
        // Draw collision Box
        // g2.setColor(java.awt.Color.red);
        // g2.drawRect(collisionBox.x, collisionBox.y, collisionBoxWidth, collisionBoxHeight);
    }

    /**
     * A method that changes Animation State of the Player
     * 
     * At the same time, it changes frameCounter and animation-related variables 
     */ 
    private void setAnimState(PlayerAnimState newState) {
        if (animState != newState) {
            animState = newState;
            // frameCounter = 0;
            // frameNum = 0;
        }
    }
    
    /** A method that determines animation state logically using the inputs provided */
    private void animLogic() {
        isFlipped = false;
        /*
         * ANIMATION LOGIC:
         * - Priortize shooting animation:
         *   + Always Shoot to the side when 2 components whose values are not 0 (flip if neccessary)
         *   + Shoot like normal when the player is standing still or when only one component (x or y) is not 0
         * - When not shooting:
         *   + When the horizontal component (x) of the movement input value is not 0, always walk to the side
         *  
         */

        // Walking animation (vertical and standing still)
        if (keyHandler.getInputMoveY() > 0) {
            setAnimState(PlayerAnimState.WALKING_UP);
        }
        else if (keyHandler.getInputMoveY() < 0) {
            setAnimState(PlayerAnimState.WALKING_DOWN);
        }
        else  { 
            if (lastShootInputY > 0|| lastInputY > 0 || lastState == PlayerAnimState.IDLE_UP) setAnimState(PlayerAnimState.IDLE_UP);
            else setAnimState(PlayerAnimState.IDLE_DOWN);
        }

        // Walking and Shooting
        if (keyHandler.getInputMoveY() != 0) {
            if (keyHandler.getInputShootY() > 0)
                setAnimState(PlayerAnimState.SHOOTING_UP_WALKING);
            if (keyHandler.getInputShootY() < 0)
                setAnimState(PlayerAnimState.SHOOTING_DOWN_WALKING);
        }

        // Walking animation (horizontal)
        if (keyHandler.getInputMoveX() > 0) 
            setAnimState(PlayerAnimState.WALKING_RIGHT);
            
        else if (keyHandler.getInputMoveX() < 0)
            setAnimState(PlayerAnimState.WALKING_LEFT);

        // Handle shooting animation
        handleShootingAnim();

        lastState = animState;
    }

    /** Handle Shooting animation */
    private void handleShootingAnim() {
        if (isMoving) {
            handleMovingShooting();
        } else {
            handleStillShooting();
        }
    }

    /** Handle Shooting Animation while moving */
    private void handleMovingShooting() {
        if (keyHandler.getInputShootY() < 0) {
            // Set animState to shooting down in case the player doesn't shoot diagonally
            setAnimState(PlayerAnimState.SHOOTING_DOWN_WALKING);
        }
        // Horizontal direction
        handleHorizontalMovingShooting();
        if (keyHandler.getInputShootY() > 0) {
            setAnimState(PlayerAnimState.SHOOTING_UP_WALKING);
        }
    }

    /** Handle Shooting animation while moving horizontally */
    private void handleHorizontalMovingShooting() {
        if (keyHandler.getInputShootX() > 0) {
            setAnimState(PlayerAnimState.WALKING_RIGHT);
        } else if (keyHandler.getInputShootX() < 0) {
            setAnimState(PlayerAnimState.WALKING_LEFT);
        }
    }

    /** Handle Shooting animation while standing still */
    private void handleStillShooting() {
        // Shooting down
        if (keyHandler.getInputShootY() < 0) {
            setAnimState(PlayerAnimState.SHOOTING_DOWN_STILL);
        }

        // Horizontal shooting
        if (keyHandler.getInputShootX() > 0) {
            setAnimState(PlayerAnimState.SHOOTING_RIGHT_STILL);
        } else if (keyHandler.getInputShootX() < 0) {
            setAnimState(PlayerAnimState.SHOOTING_LEFT_STILL);
        }
        // Shooting up
        if (keyHandler.getInputShootY() > 0) {
            setAnimState(PlayerAnimState.SHOOTING_UP_STILL);
        }
    }

    private void usePowerup(PlayerPowerup powerup) {
        switch (powerup) {
            case NONE:
                // No powerup to use
                break;
            case SPEED_BOOST:
                useSpeedBoost();
                break;
            case SHOTGUN:
                useShotgun();
                break;
            case MACHINE_GUN:
                useMachineGun();
                break;
            case OCTOSHOT:
                useOctoshot();
                break;
            case DETONATION:
                useDetonation();
                break;
            default:
                // Handle unexpected powerup cases
                break;
        }
    }

    //#region SHOOTING

    private void handleShooting() {
        if (shootingFrameCount > 0) 
            shootingFrameCount--;
        
        if (shootingFrameCount == 0 && (keyHandler.getInputShootX() != 0 || keyHandler.getInputShootY() != 0)) {
            shootBullet();
            shootingFrameCount = shootFramesPerShot;
        }
    }

    private void shootBullet() {
        double directionX = keyHandler.getInputShootX();
        double directionY = keyHandler.getInputShootY();

        if (keyHandler.getInputShootX() != 0 && keyHandler.getInputShootY() != 0) {
            directionX /= Math.sqrt(2);
            directionY /= Math.sqrt(2);
        }

        // Mixed means octashot and shotgun at the same time
        if (currentShootingMode == PlayerShootingMode.MIXED || currentShootingMode == PlayerShootingMode.OCTOSHOT) {
            for (double i = 0; i < 8; i++) {
                double angle = Math.toRadians(i * 45);
                double dx = Math.cos(angle);
                double dy = Math.sin(angle);
                if (currentShootingMode == PlayerShootingMode.MIXED)
                    shotGunShot(dx, dy);
                else
                    new Bullet(posX, posY, dx, dy, PlayerStats.getDamage());
            }
        }
        else if (currentShootingMode == PlayerShootingMode.SHOTGUN)
            shotGunShot(directionX, directionY);
        else {
            new Bullet(posX, posY, directionX, directionY, PlayerStats.getDamage());
        }

    }
    /** Shot 3 Bullets */
    private void shotGunShot(double directionX, double directionY) {
        new Bullet(posX, posY, directionX, directionY, PlayerStats.getDamage());
        new Bullet(posX, posY, directionX, directionY, Math.toRadians(12d), PlayerStats.getDamage());
        new Bullet(posX, posY, directionX, directionY, Math.toRadians(-12d), PlayerStats.getDamage());
    }
    //#endregion

    //#region POWERUPS
    /**
     * Get the current unused Power-up the player is holding
     * 
     * @return An enum {@Link Player.PlayerPowerup}
     */
    public PlayerPowerup getPowerup() {
        return currentPowerup;
    }
    
    /**
     * Try giving the Player a powerup. <P>
     * If its powerup slot is occupied, use the powerup
     * 
     * @param powerup : PlayerPowerup that is going to be provided
     */
    public void setPowerup(PlayerPowerup powerup) {
        if (currentPowerup != PlayerPowerup.NONE) {
            usePowerup(powerup);
            return;
        }
        currentPowerup = powerup;
    }

    /** Use speed boost power-up */
    private void useSpeedBoost() {
        statusEffect.setEffectDuration(PlayerStatusEffect.SPEED_BOOST, GameConstants.Player.SPEED_BOOST_DURATION_FRAMES);
        setMovementSpeed(GameConstants.Player.BASE_SPEED + GameConstants.Player.BOOSTED_SPPED);
    }

    /** Use shotgun power-up */
    private void useShotgun() {
        statusEffect.setEffectDuration(PlayerStatusEffect.SHOTGUN, GameConstants.Player.SHOTGUN_DURATION_FRAMES);
        currentShootingMode = PlayerShootingMode.SHOTGUN;
    }

    /** Use machine gun power-up */
    private void useMachineGun() {
        statusEffect.setEffectDuration(PlayerStatusEffect.MACHINE_GUN, GameConstants.Player.MACHINE_GUN_DURATION_FRAMES);
        
        shootFramesPerShot = GameConstants.Player.BASE_FRAMES_PER_SHOT / 2;
    }

    /** Use octoshot power-up */
    private void useOctoshot() {
        statusEffect.setEffectDuration(PlayerStatusEffect.OCTOSHOT, GameConstants.Player.OCTOSHOT_DURATION_FRAMES);
        currentShootingMode = PlayerShootingMode.OCTOSHOT;
    }

    private void useDetonation() {
        // Not implemented yet. Add detonation logic here if needed.
        // throw new UnsupportedOperationException("Detonation power-up not implemented yet.");
    }

    //#endregion

    private void handleStatusEffect() {
        // Handle Timer
        statusEffect.decreaseTimer();

        if (statusEffect.getEffectDuration(PlayerStatusEffect.SPEED_BOOST) <= 0) {
            setMovementSpeed(PlayerStats.getCurrentBaseSpeed());
        }
        if (statusEffect.getEffectDuration(PlayerStatusEffect.SHOTGUN) <= 0 && currentShootingMode == PlayerShootingMode.SHOTGUN) {
            currentShootingMode = PlayerShootingMode.NORMAL;
        }
        if (statusEffect.getEffectDuration(PlayerStatusEffect.OCTOSHOT) <= 0 && currentShootingMode == PlayerShootingMode.OCTOSHOT) {
            currentShootingMode = PlayerShootingMode.NORMAL;
        }
        if (statusEffect.getEffectDuration(PlayerStatusEffect.MACHINE_GUN) <= 0) {
            shootFramesPerShot = GameConstants.Player.BASE_FRAMES_PER_SHOT;
        }
    }

    @Override
    public void dispose() {
        currentPowerup = PlayerPowerup.NONE;
        playerDied();
        GameManager.getInstance().setPlayerDied(true);

        PlayerStats.removeLife();
    }

    private void playerDied() {
        // Remove all Pickables and Enemies
        EntityManager.getInstance().removeAllEnemies();
        EntityManager.getInstance().removeAllPickables();
    }

    @Override
    public void checkEnemy() {
        // Get entities
        var entities = EntityManager.getInstance().instantiatedEntities;
        // Iterate through the list
        for (int i = 0; i < entities.size(); i++) {
            Entity nearbyEntity = entities.get(i);
            if (nearbyEntity == null || nearbyEntity == this) continue;

            if (nearbyEntity instanceof Enemy && collisionHandler.isColliding(nearbyEntity, this))
                EntityManager.getInstance().destroyEntity(this);
        }
    }
}
