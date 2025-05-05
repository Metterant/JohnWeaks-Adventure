package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gamePanel;

    Font arial40;
    Font robotoRemixFont, robotoRemixSizedFont;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        loadFonts();
    }

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

    public void draw(Graphics2D g2) {   
        g2.setFont(robotoRemixSizedFont);
        g2.setColor(Color.white);
        g2.drawString("Hello there!", 50, 50);
    }
}
