import java.awt.*;
import java.awt.image.BufferedImage;

public class Captive {
    public int x = 660, y = 98;
    public boolean rescued;
    public BufferedImage sprite;

    public Captive(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void rescue() {
        rescued = true;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 42);
    }

    public void draw(Graphics g) {
        if (!rescued) {
            g.drawImage(sprite, x, y, 32, 42, null);
        }
    }
}
