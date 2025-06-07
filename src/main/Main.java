package main;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("JohnWeak's Adventure");
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        gamePanel.startGameThread();

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
