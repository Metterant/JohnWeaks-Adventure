package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import util.GameComponent;
import util.GameConstants;
import util.Renderable;

public class UI implements Renderable, GameComponent {
    GamePanel gamePanel;

    Font arial40;
    Font robotoRemixFont, robotoRemixSizedFont;

    // Powerup
    private BufferedImage powerupFrame;
    private BufferedImage powerupIcon;

    // Lives
    private BufferedImage playerLivesIcon;
    private int numberOfLivesLeft = 3;

    // Timer
    private BufferedImage timerIcon;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /** Load all the fonts */
    private void loadFonts() {
        arial40 = new Font("Arial", Font.PLAIN, 40);

        try {
            InputStream is = getClass().getResourceAsStream("/resources/fonts/RobotoRemix.ttf");
            robotoRemixFont = Font.createFont(Font.TRUETYPE_FONT, is);
            robotoRemixSizedFont = robotoRemixFont.deriveFont(35f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        } 
    }

    /** Load all the images */
    @Override
    public void loadImages() {
        try {
            powerupFrame = ImageIO.read(getClass().getResourceAsStream("/resources/ui/powerup_frame.png"));
            // Testing
            powerupIcon = ImageIO.read(getClass().getResourceAsStream("/resources/pickables/key.png"));

            // Lives
            playerLivesIcon = ImageIO.read(getClass().getResourceAsStream("/resources/ui/lives_icon.png"));

            // Timer
            timerIcon = ImageIO.read(getClass().getResourceAsStream("/resources/ui/clock.png"));

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

        // Draw Powerup
        drawPowerup(g2, 190, 140, 2, 2, 1);
        
        // Draw Timer
        drawTimer(g2, 30, 20);

        // Draw Lives
        drawLives(g2, 190, 90);
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
        g2.drawImage(powerupFrame, posX, posY, GameConstants.TILE_SIZE * scale, (GameConstants.TILE_SIZE + 3) * scale, null);

        g2.drawImage(powerupIcon, 
            posX + (offsetX * GameConstants.SCALE) * scale, 
            posY + (offsetY * GameConstants.SCALE) * scale, 
            (GameConstants.TILE_SIZE - offsetX * 2 * GameConstants.SCALE) * scale, 
            (GameConstants.TILE_SIZE - offsetY * 2 * GameConstants.SCALE) * scale, 
            null);
    }

    /**
     * Draw timer bar and icon on the screen (horizontal position is automatically adjusted)
     * 
     * @param g2 : Graphics 2D
     * @param posY : Vertical position
     * @param height : Height of the timer in screen pixels
     */
    private void drawTimer(Graphics2D g2, int posY, int height) {
        float remainingRatio = (float) GameManager.getInstance().getRoundTimer() / GameManager.getInstance().getRoundDuration();
        // System.out.println(remainingRatio);
        
        int width = GameConstants.TILE_SIZE * 17; // 17 Tiles wide

        // Find the base horizontal position
        int posX = (GameConstants.SCREEN_WIDTH - width) / 2;

        g2.setColor(new Color(0x05ed01));
        g2.fillRect(posX, posY, (int)(remainingRatio * width), height);
        g2.drawImage(timerIcon, posX - 45, posY - 7, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
    }

    private void drawLives(Graphics2D g2, int posX, int posY) {
        g2.drawImage(playerLivesIcon, posX, posY, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
        
        String toDrawTextFormat = "x%d";
        String toDrawText = String.format(toDrawTextFormat, numberOfLivesLeft);
        g2.drawString(toDrawText, posX + 35, posY + 27);
    }

    @Override
    public void start() {
        loadFonts();
        loadImages();
    }

    @Override
    public void update() { }
}
