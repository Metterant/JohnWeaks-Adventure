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

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /** Load all the fonts */
    private void loadFonts() {
        arial40 = new Font("Arial", Font.PLAIN, 40);

        try {
            InputStream is = getClass().getResourceAsStream("/resources/fonts/RobotoRemix.ttf");
            robotoRemixFont = Font.createFont(Font.TRUETYPE_FONT, is);
            robotoRemixSizedFont = robotoRemixFont.deriveFont(40f);

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

        drawPowerup(g2, 20, 20, 2, 2, 2);
    }
    /**
     * Draw Powerup Frame
     * 
     * @param g2 : Graphics 2D
     * @param posX : The root horizontal position of the Powerup Frame
     * @param posY : The root vertical position of the Powerup Frame
     * @param offsetX : Horizontal offset of the Powerup Icon in pixels
     * @param offsetY : Vertical offset of the Powerup Icon in pixels 
     * @param scale : The size scale of the whole Powerup Frame 
     */
    private void drawPowerup(Graphics2D g2,int posX, int posY, int offsetX, int offsetY, int scale) {
        g2.drawImage(powerupIcon, 
            posX + (offsetX * GameConstants.SCALE) * scale, 
            posY + (offsetY * GameConstants.SCALE) * scale, 
            (GameConstants.TILE_SIZE - offsetX * 2 * GameConstants.SCALE) * scale, 
            (GameConstants.TILE_SIZE - offsetY * 2 * GameConstants.SCALE) * scale, 
            null);
        g2.drawImage(powerupFrame, posX, posY, GameConstants.TILE_SIZE * scale, GameConstants.TILE_SIZE * scale, null);
    }

    @Override
    public void start() {
        loadFonts();
        loadImages();
    }

    @Override
    public void update() { }
}
