package ui;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

import input.PlayerController;
import main.GameManager;
import main.GamePanel;
import util.GameComponent;
import util.GameConstants;
import util.Renderable;

public class Paused implements Renderable, GameComponent {
    
    public static final int RESUME = 0;
    public static final int RESTART = 1;
    public static final int QUIT = 2;

    private boolean isVisible = false;
    private boolean lastEscapePressed, lastUpPressed, lastDownPressed;
    private int currentButton = RESUME;

    // String Constants
    private static final String RESUME_TEXT  = "Resume";
    private static final String RESTART_TEXT = "Restart";
    private static final String QUIT_TEXT    = "Quit";

    GamePanel gamePanel;
    PlayerController playerController;
    
    // Font
    Font robotoRemixFont, robotoRemixSizedFont;

    /**
     * Returns a boolean that determines if the the Paused screen is currently visible
     * @return whether current state of Paused screen visible or not 
     */
    public boolean isVisible() { return isVisible; }

    public Paused (GamePanel gamePanel) {
        playerController = GameManager.getInstance().getPlayerController();
        this.gamePanel = gamePanel;
    }

    /**
     * Executes actions base on the current selected button on Paused Menu
     */
    private void chooseCurrentOption() {
        switch (currentButton) {
            case RESUME:
                resume();
                break;
            case RESTART:
                restart();
                break;
            case QUIT:
                quitGame();
                break;
            default:
                break;
        }

        isVisible = false;
        currentButton = RESUME;
    }

    @Override
    public void start() {
        loadFonts();
        loadImages();
    }

    @Override
    public void update() {
        // Handle Entering Paused State
        if (playerController.escapePressed && !lastEscapePressed) {
            switchVisible();
        }
        if (isVisible) {
            switchCurrentButton();

            if (playerController.enterPressed)
                chooseCurrentOption();
        }

        handlePostInput();
        
    }
    @Override
    public void loadImages() {
        // Test
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setFont(robotoRemixSizedFont);
        g2.setColor(Color.white);
        
        drawPausedScreen(g2);
    }

    /** Change the visible state boolean */
    private void switchVisible() { 
        isVisible = !isVisible; 
    }

    /** Change the current selected option in paused menu using up and down keys */
    private void switchCurrentButton() {
        if ((playerController.getInputShootY() < 0 || playerController.getInputMoveY() < 0) && !lastDownPressed) {
            if (currentButton == QUIT)
                currentButton = RESUME;
            else currentButton++;
        }
        if ((playerController.getInputShootY() > 0 || playerController.getInputMoveY() > 0) && !lastUpPressed) {
            if (currentButton == RESUME)
                currentButton = QUIT;
            else currentButton--;
        }
    }

    /** Handle last inputs which can be used for the next frame */
    private void handlePostInput() {
        lastEscapePressed = playerController.escapePressed;

        lastUpPressed = (playerController.getInputShootY() > 0 || playerController.getInputMoveY() > 0);
        lastDownPressed = (playerController.getInputShootY() < 0 || playerController.getInputMoveY() < 0);
    }

    /** Activate resume */
    private void resume() {
        isVisible = false;
    }

    /** Activate restart */
    private void restart() {
        gamePanel.restartGame();
    }

    /** Activate quit game */
    private void quitGame() {
        // gamePanel.(); 
    }

    /** Load all the fonts */
    private void loadFonts() {
        try {
            InputStream is = getClass().getResourceAsStream("/resources/fonts/RobotoRemix.ttf");
            robotoRemixFont = Font.createFont(Font.TRUETYPE_FONT, is);
            robotoRemixSizedFont = robotoRemixFont.deriveFont(38f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        } 
    }

    /**
     * Draw all components of the Paused screen
     * @param g2 Graphics2D used to draw the gamePanel
     */
    private void drawPausedScreen(Graphics2D g2) {
        drawBackground(g2);

        int horizontalMid = GameConstants.SCREEN_WIDTH / 2;
        int resumePosY = (int)(GameConstants.SCREEN_HEIGHT * 0.45);
        int restartPosY = (int)(GameConstants.SCREEN_HEIGHT * 0.5);
        int quitPosY = (int)(GameConstants.SCREEN_HEIGHT * 0.55);

        drawText(g2, horizontalMid, (int)(GameConstants.SCREEN_HEIGHT * 0.1) , "Game Paused", Color.white);

        switch (currentButton) {
            case RESUME:
                drawText(g2, horizontalMid, resumePosY,  RESUME_TEXT,  Color.yellow);
                drawText(g2, horizontalMid, restartPosY, RESTART_TEXT, Color.white);
                drawText(g2, horizontalMid, quitPosY,    QUIT_TEXT,    Color.white);
                break;
            case RESTART:
                drawText(g2, horizontalMid, resumePosY,  RESUME_TEXT,  Color.white);
                drawText(g2, horizontalMid, restartPosY, RESTART_TEXT, Color.yellow);
                drawText(g2, horizontalMid, quitPosY,    QUIT_TEXT,    Color.white);
                break;
            case QUIT:
                drawText(g2, horizontalMid, resumePosY,  RESUME_TEXT,  Color.white);
                drawText(g2, horizontalMid, restartPosY, RESTART_TEXT, Color.white);
                drawText(g2, horizontalMid, quitPosY,    QUIT_TEXT,    Color.yellow);
                break;
            default:
                break;
        }
    }

    /** Draw the opaque black background */
    private void drawBackground(Graphics2D g2) {
        // Draw a semi-opaque gray rectangle covering the whole screen
        Color oldColor = g2.getColor();
        Composite oldComposite = g2.getComposite();
        g2.setColor(new Color(30, 30, 30, 200)); // RGBA, alpha for opacity
        g2.fillRect(0, 0, util.GameConstants.SCREEN_WIDTH, util.GameConstants.SCREEN_HEIGHT);
        g2.setColor(oldColor);
        g2.setComposite(oldComposite);
    }

    /**
     * Draw a colored centered text which pivots on specified position
     * @param g2 Graphics2D used to draw the text
     * @param posX horizontal position
     * @param posY vertical position
     * @param text text to be written
     * @param color the color of the text
     */
    private void drawText(Graphics2D g2, int posX, int posY, String text, Color color) {
        // Retrive old color
        Color oldColor = g2.getColor();
        Composite oldComposite = g2.getComposite();

        g2.setColor(color);

        int textWidth = g2.getFontMetrics().stringWidth(text);
        int drawX = posX - textWidth / 2;
        g2.drawString(text, drawX, posY);

        // Set to old color 
        g2.setColor(oldColor);
        g2.setComposite(oldComposite);
    }
}
