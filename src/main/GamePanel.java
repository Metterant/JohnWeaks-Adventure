package main;

import javax.swing.JPanel;

import entity.pickables.*;

import tile.TileManager;
import util.EntityManager;
import util.GameConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {

    // Thread
    transient Thread gameThread;

    // Constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(GameManager.getInstance().getPlayerController());
    }

    // UI
    transient UI ui = new UI(this);

    /*
     * Orders:
     * TileManager -> GameManager -> EntityManager -> UI
     */

    public void startGameThread() {

        // new Key(10, 10);
        // new Life(15, 10);
        
        // Init Tile Manager
        TileManager.getInstance().loadImages();
        TileManager.getInstance().loadMap("/resources/maps/map_0.txt");
        
        GameManager.getInstance().start();
        // EntityManager.getInstance().entitiesStart();

        // Init UI
        ui.start();

        // System.out.println(biker.pathFinder.search(biker, GameManager.getInstance().player));

        gameThread = new Thread(this);
        gameThread.start();
    }

    // Runnable implementation
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / GameConstants.FPS;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            if (currentTime - lastTime >= drawInterval) {
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
        ui.update();
    }

    /** Implementation of paintComponent inherited from JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        
        TileManager.getInstance().draw(g2);
        EntityManager.getInstance().entitiesDraw(g2);

        ui.draw(g2);

        g2.dispose();
    }
}
