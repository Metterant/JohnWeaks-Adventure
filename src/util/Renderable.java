package util;

import java.awt.Graphics2D;

public interface Renderable {
    public abstract void loadImages();

    public abstract void draw(Graphics2D g2);
}
