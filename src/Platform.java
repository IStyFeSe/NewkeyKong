import java.awt.*;
import java.awt.image.BufferedImage;

public class Platform {
    public int x, y, width, height = 16;
    BufferedImage sprite;

    public Platform(int x, int y, int width, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.sprite = sprite;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        for (int offset = 0; offset < width; offset += 36) {
            g.drawImage(sprite, x + offset, y, Math.min(36, width - offset), height, null);
        }
    }
}
