package main;

import javax.swing.JPanel;

import entity.Player;
import tile.Tile;
import tile.TileManager;
import util.CollisionHandler;
import util.GameConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {

    // KeyHandler
    transient KeyHandler keyHandler = new KeyHandler();

    // Thread
    transient Thread gameThread;

    // Player
    transient Player player =  new Player(this, keyHandler);

    // Constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        player.setDefaultValues();
        player.getImages();

        TileManager.getInstance().getImages();
        TileManager.getInstance().loadMap("/resources/maps/map_test.txt");

        gameThread = new Thread(this);
        gameThread.start();
    }

    // Runnable implementation
    @Override
    public void run() {
        double drawInterval = 1000000000.0/GameConstants.FPS;
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

    /**
     * Repeating method that handles repeating logic every frame
     */
    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        
        TileManager.getInstance().draw(g2);
        player.draw(g2);

        g2.dispose();
    }
}
