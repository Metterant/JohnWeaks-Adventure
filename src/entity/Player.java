package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import input.KeyHandler;
import util.CollisionHandler;
import util.GameConstants;

public class Player extends ControllableEntity {
    // State
    /**
     * Inside {@Link entity.Player} <p>
     * 
     * Enum for determining the state of an animmation of a Player 
     */
    public enum PlayerAnimState {
        IDLE_DOWN,
        IDLE_UP,
        WALKING_DOWN,
        WALKING_RIGHT,
        WALKING_LEFT,
        WALKING_UP,
        SHOOTING_DOWN_WALKING,
        SHOOTING_UP_WALKING,
        SHOOTING_DOWN_STILL,
        SHOOTING_RIGHT_STILL,
        SHOOTING_LEFT_STILL,
        SHOOTING_UP_STILL,
    }
    private PlayerAnimState animState;
    private PlayerAnimState lastState;

    // Power-up
    /**
     * Inside {@Link entity.Player} <P>
     * 
     * Enum for determining what powerup the player is holding
     */
    public enum PlayerPowerup {
        NONE,
        SPEED_BOOST,
        SHOTGUN,
        MACHINE_GUN,
        OCTOSHOT,
        DETONATION,
    }
    private PlayerPowerup currentPowerup;

    // Shooting mode
    /**
     * Inside {@Link entity.Player} <P>
     * 
     * Enum for determining what shooting mode the player is using
     */
    public enum PlayerShootingMode {
        NORMAL,
        SHOTGUN,
        OCTOSHOT,
        MIXED,
    }
    private PlayerShootingMode currentShootingMode;

    // Sprites
    private BufferedImage[] walkingDownSprite, walkingUpSprite, walkingSideSprite, walkingShootingDownSprite;
    private BufferedImage idleUpSprite, idleDownSprite;
    private int frameCounter, frameNum;
    private boolean isFlipped;

    // Input
    private int lastInputY = 0;

    // Logic-related
    private double lastPosX;
    private double lastPosY;
    private int lastShootInputY;
    private boolean isMoving;
    private boolean lastSpaceInput;

    // Collison Handler
    CollisionHandler collisionHandler;

    // Timers
    private int speedBoostTimer;
    private int shotgunTimer;
    private int machineGunTimer;
    private int octoshotTimer;

    //#region CONSTRUCTORS
    public Player(KeyHandler keyHandler) {
        super();
        
        this.keyHandler = keyHandler;
    }
    public Player(int row, int col, KeyHandler keyHandler) {
        super(row, col);

        this.keyHandler = keyHandler;
    }
    public Player(double locationX, double locationY, KeyHandler keyHandler) {
        super(locationX, locationY);

        this.keyHandler = keyHandler;
    }
    //#endregion

    @Override
    public void start() {
        setDefaultValues();
        super.start();
    }

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

        collisionBoxWidth = 8 + (GameConstants.ORIGINAL_TILE_SIZE - 16);
        collisionBoxHeight = 8 + (GameConstants.ORIGINAL_TILE_SIZE - 16);

        collisionBox = new Rectangle();
        initBoxHelper(4, 8, collisionBoxWidth, collisionBoxHeight);

        collisionHandler = new CollisionHandler();
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

    @Override
    public void update() {
        // MOVEMENT
        double desiredAxialDisplacement = movementSpeed;
        double desiredPosX, desiredPosY;
        if (keyHandler.getInputMoveX() != 0 && keyHandler.getInputMoveY() != 0)
            desiredAxialDisplacement /= Math.sqrt(2);

        desiredPosX = posX + keyHandler.getInputMoveX() * desiredAxialDisplacement;
        desiredPosY = posY - keyHandler.getInputMoveY() * desiredAxialDisplacement;

        posX = desiredPosX;
        posY = desiredPosY;

        moveCollisionBox();

        // POWERUP INPUT
        if (!lastSpaceInput && keyHandler.getSpaceInput()) {
            usePowerup(currentPowerup);
            currentPowerup = PlayerPowerup.NONE;
        }
        lastSpaceInput = keyHandler.getSpaceInput();

        // TIMERS
        handleTimers();
        handleTimersEnd();

        
        // Collision
        collisionHandler.checkTile(this, desiredPosX, desiredPosY, desiredAxialDisplacement, keyHandler.getInputMoveX(), keyHandler.getInputMoveY());
        collisionHandler.checkPickable(this);
        
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
        frameCounter++;
        // Increase the counter every 8 frames
        if (frameCounter >= 8) {
            frameNum++;
            if (frameNum == 4)
            frameNum = 0;
            
            frameCounter = 0;
        }
        
        /** DEBUGGING **/
        // System.out.println(speedBoostTimer);
        // System.out.printf("x=%.1f, y=%.1f\n", posX, posY);
    }

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
        // g2.fillRect(collisionBox.x, collisionBox.y, collisionBoxWidth, collisionBoxHeight);
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
        if (keyHandler.getInputShootY() > 0) {
            setAnimState(PlayerAnimState.SHOOTING_UP_STILL);
        } else if (keyHandler.getInputShootY() < 0) {
            setAnimState(PlayerAnimState.SHOOTING_DOWN_STILL);
        } 
        if (keyHandler.getInputShootX() > 0) {
            setAnimState(PlayerAnimState.SHOOTING_RIGHT_STILL);
        } else if (keyHandler.getInputShootX() < 0) {
            setAnimState(PlayerAnimState.SHOOTING_LEFT_STILL);
        }
    }

    private void usePowerup(PlayerPowerup powerup) {
        switch (powerup) {
            case NONE:
                useSpeedBoost();
                // No powerup to use
                break;
            case SPEED_BOOST:
                useSpeedBoost();
                break;
            case SHOTGUN:
                useShotgun();
                break;
            case MACHINE_GUN:
                useMachineGunTimer();
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

    /** Use speed bost */
    private void useSpeedBoost() {
        speedBoostTimer = GameConstants.Player.SPEED_BOOST_DURATION;
        
        setMovementSpeed(GameConstants.Player.PLAYER_DEFAULT_SPEED + GameConstants.Player.PLAYER_BOOSTED_SPPED);
    }
    
    private void useShotgun() {
        shotgunTimer = GameConstants.Player.SHOTGUN_DURATION;
    }

    private void useMachineGunTimer() {
        machineGunTimer = GameConstants.Player.MACHINE_GUN_DURATION;
    }

    private void useOctoshot() {
        octoshotTimer = GameConstants.Player.OCTOSHOT_DURATION;
    }

    private void useDetonation() {

    }

//#endregion

    /** Handle timers */
    private void handleTimers() {
        if (speedBoostTimer > 0) 
            speedBoostTimer--;
        if (shotgunTimer > 0) 
            shotgunTimer--;
        if (machineGunTimer > 0) 
            shotgunTimer--;
        if (octoshotTimer > 0) 
            octoshotTimer--;
    }

    private void handleTimersEnd() {
        if (speedBoostTimer <= 0) {
            setMovementSpeed(GameConstants.Player.PLAYER_DEFAULT_SPEED);
        }
        if (shotgunTimer <= 0) {
            
        }
    }

    @Override
    public void dispose() {
        // TODO Add die   
    }
}
