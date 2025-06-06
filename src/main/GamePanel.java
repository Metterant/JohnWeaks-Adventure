package main;

import javax.swing.JPanel;

import entity.player.PlayerStats;
import tile.TileManager;
import ui.*;
import util.EntityManager;
import util.GameConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {

    // Thread
    transient Thread gameThread;
    transient Thread oldThread;
    
    // UI
    transient Paused pausedUI;
    transient UI gameUI;

    // Constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(GameManager.getInstance().getPlayerController());
    }

    public void startGameThread() {
        initGame();

        gameThread = new Thread(this);
        gameThread.start();
    }

    // Runnable implementation
    @Override
    public void run() {
        // Wait for the old Thread to die before running
        if (oldThread != null && oldThread.isAlive() && Thread.currentThread() != oldThread) {
            try {
                oldThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        double drawInterval = 1000000000.0 / GameConstants.FPS;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            if (currentTime - lastTime >= drawInterval) {
                pausedUI.update();
                if (!pausedUI.isVisible())
                    update(); 
                repaint();
                lastTime = currentTime;
            }
        }
    }

    /** Repeating method that handles repeating logic every frame */
    public void update() {
        GameManager.getInstance().update();
        EntityManager.getInstance().update();
        gameUI.update();
    }

    /** Implementation of paintComponent inherited from JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        
        TileManager.getInstance().draw(g2);
        EntityManager.getInstance().entitiesDraw(g2);

        gameUI.draw(g2);
        if (pausedUI.isVisible()) 
            pausedUI.draw(g2);

        g2.dispose();
    }

    /** Initialize Game Components */
    public void initGame() {
        /*
        * Orders:
        * TileManager -> GameManager -> EntityManager -> UI
        */

        // Init Tile Manager
        TileManager.getInstance().loadImages();
        TileManager.getInstance().loadMap("/resources/maps/map_0.txt");
        
        GameManager.getInstance().start();
        // EntityManager.getInstance().entitiesStart();
    
        // Init UI
        gameUI = new UI(this);
        pausedUI = new Paused(this);
    
        gameUI.start();
        pausedUI.start();
    }

    /** Restart the Game */
    public void restartGame() {
        /*
         * - Remove the current keyListener
         * - Reset Singletons and PlayerStats
         * - Create a new Thread
         * - Assign the new keyListener
         */
        this.removeKeyListener(GameManager.getInstance().getPlayerController());
        EntityManager.resetSingleton();
        GameManager.resetSingleton();
        PlayerStats.resetStats();
            
        createNewGameThread();
        this.addKeyListener(GameManager.getInstance().getPlayerController());
    }

    /** Restart the game by terminating the old Thread and creating a new one */
    public void createNewGameThread() {
        oldThread = gameThread;
        gameThread = null;

        startGameThread();
    }
}
