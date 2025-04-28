package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import util.GameConstants;

public class Player extends Entity {
    // State
    public enum PlayerAnimState {
        IDLE_DOWN,
        IDLE_UP,
        WALKING_DOWN,
        WALKING_RIGHT,
        WALKING_LEFT,
        WALKING_UP,
        SHOOTING_DOWN_WALING,
        SHOOTING_UP_WALKING,
        SHOOTING_DOWN_STILL,
        SHOOTING_UP_STILL
    }
    private PlayerAnimState animState;
    private PlayerAnimState lastState;

    // Sprites
    private BufferedImage[] walkingDownSprite, walkingUpSprite, walkingSideSprite, walking_shootingDownSprite;
    private BufferedImage idleUpSprite, idleDownSprite;
    private int frameCounter, frameNum;
    private boolean isFlipped;

    // Etc
    private GamePanel gamePanel;

    // Input
    private KeyHandler keyHandler;
    private int lastInputY = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
    }

    public void setDefaultValues() {
        posX = 100;
        posY = 100;
        movementSpeed = 4;
        lastState = animState = PlayerAnimState.IDLE_DOWN;

        walkingDownSprite = new BufferedImage[4];
        walkingUpSprite = new BufferedImage[4];
        walkingSideSprite = new BufferedImage[4];
        walking_shootingDownSprite = new BufferedImage[4];

        frameCounter = 0;
    }

    @Override
    public void getImages() {
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
            walking_shootingDownSprite[0] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down1.png"));
            walking_shootingDownSprite[1] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down2.png"));
            walking_shootingDownSprite[2] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down3.png"));
            walking_shootingDownSprite[3] = ImageIO.read(getClass().getResourceAsStream("/resources/player/player_walk_shoot_down4.png"));

        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void update() {
        if (keyHandler.getInputMoveY() > 0) {
            setAnimState(PlayerAnimState.WALKING_UP);
        }
        else if (keyHandler.getInputMoveY() < 0) {
            setAnimState(PlayerAnimState.WALKING_DOWN);
        }
        else  { 
            if (lastInputY > 0 || lastState == PlayerAnimState.IDLE_UP) setAnimState(PlayerAnimState.IDLE_UP);
            else setAnimState(PlayerAnimState.IDLE_DOWN);
        }

        if (keyHandler.getInputMoveX() > 0) {
            setAnimState(PlayerAnimState.WALKING_RIGHT);
            isFlipped = false;
        }
        else if (keyHandler.getInputMoveX() < 0) {
            setAnimState(PlayerAnimState.WALKING_LEFT);
            isFlipped = true;
        }
        
        lastState = animState;
        lastInputY = keyHandler.getInputMoveY();
        
        // Normalize Input Vector
        if (Math.abs(keyHandler.getInputMoveX()) == 1 && Math.abs(keyHandler.getInputMoveY()) == 1) {
            posY -= keyHandler.getInputMoveY() * movementSpeed / Math.sqrt(2);
            posX += keyHandler.getInputMoveX() * movementSpeed / Math.sqrt(2);
        }
        else {
            posY -= keyHandler.getInputMoveY() * movementSpeed;
            posX += keyHandler.getInputMoveX() * movementSpeed;
        }

        frameCounter++;
        // Increase the counter every 8 frames
        if (frameCounter >= 8) {
            frameNum++;
            if (frameNum == 4)
                frameNum = 0;
            
            frameCounter = 0;
        }
        // System.out.printf("ShootX:%d, ShootY:%d\n", keyHandler.getInputShootX(), keyHandler.getInputShootY());
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

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
            case WALKING_RIGHT, WALKING_LEFT:
                image = walkingSideSprite[frameNum];
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
    }

    // A method that changes Animation State of the Player
    // At the same time, it changes frameCounter and animation-related variables 
    private void setAnimState(PlayerAnimState newState) {
        if (animState != newState) {
            animState = newState;
            // frameCounter = 0;
            // frameNum = 0;
        }
    }
}
