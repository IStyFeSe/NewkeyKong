import java.awt.*;

public class Hammer {
    public int x, y;
    public boolean available = true;
    public double duration = 8, respawnTimer;
    Assets assets;

    public Hammer(int x, int y, Assets assets) {
        this.x = x;
        this.y = y;
        this.assets = assets;
    }

    public void update(double dt) {
        if (!available) {
            respawnTimer -= dt;
            if (respawnTimer <= 0) {
                available = true;
            }
        }
    }

    public void collect(Player player) {
        if (!available) {
            return;
        }
        available = false;
        respawnTimer = 15;
        player.hammerTime = duration;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 22, 40);
    }

    public void draw(Graphics g) {
        if (available) {
            g.drawImage(assets.hammer, x, y, 22, 40, null);
        }
    }
}
