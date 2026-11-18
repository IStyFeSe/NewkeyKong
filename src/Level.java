import java.awt.*;
import java.util.ArrayList;

public class Level {
    public Player player;
    public Gorilla gorilla;
    public Captive captive;
    public Platform captivePlatform;
    public ArrayList<Platform> platforms = new ArrayList<>();
    public ArrayList<Ladder> ladders = new ArrayList<>();
    public ArrayList<Barrel> barrels = new ArrayList<>();
    public ArrayList<Hammer> hammers = new ArrayList<>();
    Assets assets;

    public Level(Assets assets) {
        this.assets = assets;
        load(1);
    }

    public void load(int number) {
        platforms.clear();
        ladders.clear();
        barrels.clear();
        hammers.clear();
        for (int i = 0; i < 5; i++) {
            int platformX = 40;
            if (i % 2 != 0) {
                platformX = 80;
            }
            int platformY = 140 + i * 88;
            Platform platform = new Platform(platformX, platformY, 680, assets.platform);
            platforms.add(platform);
        }
        for (int i = 0; i < 4; i++) {
            int ladderX = 650;
            if (i % 2 != 0) {
                ladderX = 130;
            }
            Platform upperPlatform = platforms.get(i);
            Platform lowerPlatform = platforms.get(i + 1);
            Ladder ladder = new Ladder(ladderX, upperPlatform, lowerPlatform, assets.ladder);
            ladders.add(ladder);
        }
        captivePlatform = new Platform(550, 80, 170, assets.platform);
        platforms.add(captivePlatform);
        ladders.add(new Ladder(570, captivePlatform, platforms.get(0), assets.ladder));
        player = new Player(assets);
        gorilla = new Gorilla(assets);
        captive = new Captive(assets.captive);
        captive.y = captivePlatform.y - 42;
        hammers.add(new Hammer(380, 452, assets));
        hammers.add(new Hammer(480, 276, assets));
        hammers.add(new Hammer(220, 100, assets));
    }

    public void update(double dt, boolean[] keys) {
        player.update(dt, this, keys);
        gorilla.update(dt, this);
        for (int i = 0; i < barrels.size(); i++) {
            Barrel barrel = barrels.get(i);
            barrel.update(dt, this);
        }
        for (int i = 0; i < hammers.size(); i++) {
            Hammer hammer = hammers.get(i);
            hammer.update(dt);
        }
        for (int i = barrels.size() - 1; i >= 0; i--) {
            Barrel barrel = barrels.get(i);
            if (!barrel.active) {
                barrels.remove(i);
            }
        }
    }

    public void resetAfterDeath() {
        player = new Player(assets);
        player.invincibleTime = 2;
        barrels.clear();
        gorilla.spawnTimer = gorilla.spawnInterval;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < ladders.size(); i++) {
            Ladder ladder = ladders.get(i);
            ladder.draw(g);
        }
        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            platform.draw(g);
        }
        for (int i = 0; i < hammers.size(); i++) {
            Hammer hammer = hammers.get(i);
            hammer.draw(g);
        }
        gorilla.draw(g);
        captive.draw(g);
        for (int i = 0; i < barrels.size(); i++) {
            Barrel barrel = barrels.get(i);
            if (barrel.active) {
                barrel.draw(g);
            }
        }
        player.draw(g);
    }
}
