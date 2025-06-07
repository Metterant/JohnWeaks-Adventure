package ui;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

public class PopUpText {

    int posX, posY;
    Font robotoRemixFont, robotoRemixSizedFont;

    public PopUpText(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        loadFonts();
    }

    /** Load all the fonts */
    private void loadFonts() {
        try {
            InputStream is = getClass().getResourceAsStream("/resources/fonts/RobotoRemix.ttf");
            robotoRemixFont = Font.createFont(Font.TRUETYPE_FONT, is);
            robotoRemixSizedFont = robotoRemixFont.deriveFont(22f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        } 
    }

    public void drawText(String text, Graphics2D g2) {
        // Retrive old color
        Color oldColor = g2.getColor();
        Composite oldComposite = g2.getComposite();

        g2.setFont(robotoRemixSizedFont);
        g2.setColor(Color.yellow);

        int textWidth = g2.getFontMetrics().stringWidth(text);
        int drawX = posX - textWidth / 2;
        g2.drawString(text, drawX, posY);

        // Set to old color 
        g2.setColor(oldColor);
        g2.setComposite(oldComposite); 
    }
}
