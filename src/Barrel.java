import java.awt.*;

public class Barrel {
    public double x, y, speed = 130, vy, angle;
    public int direction = 1, platformIndex;
    public boolean active = true, jumpScored;
    boolean falling;
    Assets assets;

    public Barrel(double x, double y, Assets assets) {
        this.x = x;
        this.y = y;
        this.assets = assets;
    }

    public void update(double dt, Level level) {
        angle += direction * speed * dt / 14;
        Platform platform = level.platforms.get(platformIndex);
        if (!falling) {
            x += direction * speed * dt;
            if (x + 28 < platform.x || x > platform.x + platform.width) {
                falling = true;
                vy = 0;
            }
        } else {
            vy += 900 * dt;
            y += vy * dt;
            if (platformIndex + 1 < level.platforms.size()) {
                Platform next = level.platforms.get(platformIndex + 1);
                if (next.y > platform.y && y + 28 >= next.y) {
                    platformIndex++;
                    y = next.y - 28;
                    direction = -direction;
                    falling = false;
                    jumpScored = false;
                }
            }
            if (y > 600) {
                destroy();
            }
        }
    }

    public void destroy() {
        active = false;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 28, 28);
    }

    public void draw(Graphics g) {
        Graphics2D copy = (Graphics2D) g.create();
        copy.rotate(angle, x + 14, y + 14);
        copy.drawImage(assets.barrel, (int) x, (int) y, 28, 28, null);
        copy.dispose();
    }
}
