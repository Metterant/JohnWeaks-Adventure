package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import entity.player.PlayerStats;
import entity.player.PlayerStatusEffect;
import main.GameManager;
import main.GamePanel;
import util.GameComponent;
import util.GameConstants;
import util.Renderable;

public class UI implements Renderable, GameComponent {
    GamePanel gamePanel;

    Font robotoRemixFont, robotoRemixSizedFont;

    // Powerup
    private BufferedImage powerupFrame;
    private BufferedImage powerupIcon;
    private BufferedImage[] powerupIcons;

    private BufferedImage playerLivesIcon;
    private BufferedImage timerIcon;
    private BufferedImage coinIcon;
    private BufferedImage runnerShoesIcon;
    private BufferedImage gunIcon2, gunIcon3;
    private BufferedImage burstShotIcon;

    private BufferedImage[] downArrowSprites;
    private int downArrowFrameCount = 0;
    private int downArrowSpriteFrame = 0;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /** Load all the fonts */
    private void loadFonts() {
        try {
            InputStream is = getClass().getResourceAsStream("/resources/fonts/RobotoRemix.ttf");
            robotoRemixFont = Font.createFont(Font.TRUETYPE_FONT, is);
            robotoRemixSizedFont = robotoRemixFont.deriveFont(28f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        } 
    }

    /** Load all the images */
    @Override
    public void loadImages() {
        try {
            powerupFrame = ImageIO.read(getClass().getResourceAsStream("/resources/ui/powerup_frame.png"));
            // Powerup Icons
            powerupIcons = new BufferedImage[3];
            powerupIcons[PlayerStatusEffect.SPEED_BOOST] = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/coffee.png"));
            powerupIcons[PlayerStatusEffect.SHOTGUN] = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/shotgun.png"));
            powerupIcons[PlayerStatusEffect.MACHINE_GUN] = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/machine_gun.png"));

            loadDownArrowSprites();

            playerLivesIcon = ImageIO.read(getClass().getResourceAsStream("/resources/ui/lives_icon.png"));
            timerIcon = ImageIO.read(getClass().getResourceAsStream("/resources/ui/clock.png"));
            coinIcon = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/coin.png"));
            runnerShoesIcon = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/runner_shoes.png"));
            gunIcon2 = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/gun_level_2.png"));
            gunIcon3 = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/gun_level_3.png"));
            burstShotIcon = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/burst_shot.png"));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Main method used to draw onto the screen */
    public void draw(Graphics2D g2) {   
        g2.setFont(robotoRemixSizedFont);
        g2.setColor(Color.white);
        // g2.drawString("Hello there!", 50, 50);

        drawPowerup(g2, 190, 90, 3, 3, 1);
        drawTimer(g2, 30, 13);
        drawLives(g2, 190, 140);
        drawCoin(g2, 190, 180);
        drawUpgrades(g2);

        handleDownArrowFrame();
        if (GameManager.getInstance().getRoundTimerFrames() <= 0) {
            drawDownArrow(g2);
        }
    }
    /**
     * Draw Powerup Frame
     * 
     * @param g2 : Graphics 2D
     * @param posX : The root horizontal position of the Powerup Frame
     * @param posY : The root vertical position of the Powerup Frame
     * @param offsetX : Horizontal offset of the Powerup Icon in sprite pixels
     * @param offsetY : Vertical offset of the Powerup Icon in sprite pixels 
     * @param scale : The size scale of the whole Powerup Frame 
     */
    private void drawPowerup(Graphics2D g2,int posX, int posY, int offsetX, int offsetY, int scale) {
        g2.setColor(java.awt.Color.gray);
        g2.fillRect(posX, posY, GameConstants.UI.POWERUP_FRAME_SIDE * scale, (GameConstants.UI.POWERUP_FRAME_SIDE + 3) * scale);

        g2.drawImage(powerupFrame, posX, posY, GameConstants.UI.POWERUP_FRAME_SIDE * scale, (GameConstants.UI.POWERUP_FRAME_SIDE + 3) * scale, null);

        if (powerupIcon != null)
            g2.drawImage(powerupIcon, 
                posX + (offsetX * GameConstants.SCALE) * scale, 
                posY + (offsetY * GameConstants.SCALE) * scale, 
                (GameConstants.UI.POWERUP_FRAME_SIDE - offsetX * 2 * GameConstants.SCALE) * scale, 
                (GameConstants.UI.POWERUP_FRAME_SIDE - offsetY * 2 * GameConstants.SCALE) * scale, 
                null);
    }

    private void setPowerupIcon() {
        switch (GameManager.getInstance().player.getPowerup()) {
            case NONE:
                powerupIcon = null;
                break;
            case SPEED_BOOST:
                powerupIcon = powerupIcons[PlayerStatusEffect.SPEED_BOOST];
                break;
            case SHOTGUN:
                powerupIcon = powerupIcons[PlayerStatusEffect.SHOTGUN];
                break;
            case MACHINE_GUN:
                powerupIcon = powerupIcons[PlayerStatusEffect.MACHINE_GUN];
                break;
            default:
                break;
        }
    }

    /**
     * Draw timer bar and icon on the screen (horizontal position is automatically adjusted)
     * 
     * @param g2 : Graphics 2D
     * @param posY : Vertical position
     * @param height : Height of the timer in screen pixels
     */
    private void drawTimer(Graphics2D g2, int posY, int height) {
        float remainingRatio = (float) GameManager.getInstance().getRoundTimerFrames() / GameManager.getInstance().getRoundDurationFrames();
        // System.out.println(remainingRatio);
        
        int width = GameConstants.TILE_SIZE * 17; // 17 Tiles wide

        // Find the base horizontal position
        int posX = (GameConstants.SCREEN_WIDTH - width) / 2;

        g2.setColor(new Color(0x05ed01));
        g2.fillRect(posX, posY, (int)(remainingRatio * width), height);
        g2.drawImage(timerIcon, posX - 45, posY - 10, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
    }

    private void drawLives(Graphics2D g2, int posX, int posY) {
        g2.drawImage(playerLivesIcon, posX, posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
        
        String toDrawTextFormat = "x%d";
        String toDrawText = String.format(toDrawTextFormat, PlayerStats.getLivesLeft());
        g2.drawString(toDrawText, posX + 38, posY + 24);
    }

    private void drawCoin(Graphics2D g2, int posX, int posY) {
        g2.drawImage(coinIcon, posX, posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);

        String toDrawTextFormat = "x%d";
        String toDrawText = String.format(toDrawTextFormat, PlayerStats.getCoins());
        g2.drawString(toDrawText, posX + 38, posY + 24);
    }

    private void drawUpgrades(Graphics2D g2) {
        if (PlayerStats.hasRunnerBoots())
            drawIcon(g2, runnerShoesIcon, 230, 570);
        if (PlayerStats.getGunLevel() == 2)
            drawIcon(g2, gunIcon2, 230, 610);
        if (PlayerStats.getGunLevel() == 3)
            drawIcon(g2, gunIcon3, 230, 610);
        if (PlayerStats.hasBurstShot())
            drawIcon(g2, burstShotIcon, 230, 650);
    }

    /**
     * Draws a sprite onto the screen
     * @param g2 Graphics2D provided by GamePanel
     * @param iconImage the sprite to be drawn
     * @param posX left-most position of the sprite on the screen (in pixels)
     * @param posY top position of the sprite on the screen (in pixels)
     */
    private void drawIcon(Graphics2D g2, BufferedImage iconImage, int posX, int posY) {
        g2.drawImage(iconImage, posX, posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);        
    }

    /**
     * Draws an animating down arrow that points where the next level is
     * @param g2 Graphics2D provided by GamePanel
     */
    private void drawDownArrow(Graphics2D g2) {
        int posX = (GameConstants.MAX_SCREEN_COL / 2) * GameConstants.TILE_SIZE;
        int posY = 16 * GameConstants.TILE_SIZE;

        g2.drawImage(downArrowSprites[downArrowSpriteFrame], posX, posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
    }

    @Override
    public void start() {
        loadFonts();
        loadImages();
    }

    @Override
    public void update() {
        setPowerupIcon();
        handleDownArrowFrame();
    }

    private void loadDownArrowSprites() {
        try {
            downArrowSprites = new BufferedImage[6];

            downArrowSprites[0] = ImageIO.read(getClass().getResourceAsStream("/resources/ui/down_arrow0.png"));
            downArrowSprites[1] = ImageIO.read(getClass().getResourceAsStream("/resources/ui/down_arrow1.png"));
            downArrowSprites[2] = ImageIO.read(getClass().getResourceAsStream("/resources/ui/down_arrow2.png"));
            downArrowSprites[3] = ImageIO.read(getClass().getResourceAsStream("/resources/ui/down_arrow3.png"));
            downArrowSprites[4] = ImageIO.read(getClass().getResourceAsStream("/resources/ui/down_arrow4.png"));
            downArrowSprites[5] = ImageIO.read(getClass().getResourceAsStream("/resources/ui/down_arrow5.png"));

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    /** Set Down Arrow Sprite frame */
    private void handleDownArrowFrame() {
        downArrowFrameCount++;
        if (downArrowFrameCount >= GameConstants.FPS / 4) {
            downArrowFrameCount = 0;

            if (downArrowSpriteFrame == 5)
                downArrowSpriteFrame = 0;
            else downArrowSpriteFrame++; 
        }
    }
}
