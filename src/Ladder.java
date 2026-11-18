import java.awt.*;
import java.awt.image.BufferedImage;

public class Ladder {
    public int x, y, width = 36, height;
    public Platform lowerPlatform, upperPlatform;
    BufferedImage sprite;

    public Ladder(int x, Platform upper, Platform lower, BufferedImage sprite) {
        this.x = x;
        upperPlatform = upper;
        lowerPlatform = lower;
        y = upper.y;
        height = lower.y - upper.y;
        this.sprite = sprite;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.drawImage(sprite, x, y, width, height, null);
    }
}
