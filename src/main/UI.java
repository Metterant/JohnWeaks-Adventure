package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class UI {
    GamePanel gamePanel;

    Font arial40;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        arial40 = new Font("MettPix", Font.PLAIN, 40);
    }

    public void draw(Graphics2D g2) {
        g2.setFont(arial40);
        g2.setColor(Color.white);
        g2.drawString("Hello there!", 50, 50);
    }
}
