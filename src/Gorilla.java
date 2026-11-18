import java.awt.*;

public class Gorilla {
    public int x = 80, y = 52;
    public double spawnTimer = 1, spawnInterval = 2.8;
    public boolean defeated;
    Assets assets;

    public Gorilla(Assets assets) {
        this.assets = assets;
    }

    public void update(double dt, Level level) {
        if (defeated) {
            return;
        }
        spawnTimer -= dt;
        if (spawnTimer <= 0) {
            level.barrels.add(new Barrel(x + 64, level.platforms.get(0).y - 28, assets));
            spawnTimer = spawnInterval;
        }
    }

    public void takeHit() {
        defeated = true;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 64, 88);
    }

    public void draw(Graphics g) {
        if (!defeated) {
            if (spawnTimer > spawnInterval - 0.4) {
                g.drawImage(assets.gorillaThrow, x, y, 64, 88, null);
            } else {
                g.drawImage(assets.gorilla, x, y, 64, 88, null);
            }
        }
    }
}
