import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public double x = 100, y = 450, vx, vy, speed = 190, jumpSpeed = 340;
    public int width = 32, height = 42, direction = 1;
    public boolean onGround, onLadder;
    public double hammerTime, attackTime, attackCooldown, invincibleTime, animationTime;
    Assets assets;

    public Player(Assets assets) {
        this.assets = assets;
    }

    public void update(double dt, Level level, boolean[] keys) {
        hammerTime = Math.max(0, hammerTime - dt);
        attackTime = Math.max(0, attackTime - dt);
        attackCooldown = Math.max(0, attackCooldown - dt);
        invincibleTime = Math.max(0, invincibleTime - dt);
        animationTime += dt;
        vx = 0;
        if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) {
            vx = -speed;
        }
        if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) {
            vx = speed;
        }
        if (vx > 0) {
            direction = 1;
        } else if (vx < 0) {
            direction = -1;
        }
        boolean up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
        boolean down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
        Ladder near = null;
        for (int i = 0; i < level.ladders.size(); i++) {
            Ladder ladder = level.ladders.get(i);
            double feet = y + height;
            if (x + width / 2.0 >= ladder.x && x + width / 2.0 <= ladder.x + ladder.width
                && feet >= ladder.y - 2 && feet <= ladder.y + ladder.height + 2
                && (!up || feet > ladder.upperPlatform.y + 0.1)
                && (!down || feet < ladder.lowerPlatform.y - 0.1)) {
                near = ladder;
                break;
            }
        }
        if (near == null) {
            onLadder = false;
        }
        if (near != null && (up || down)) {
            onLadder = true;
            onGround = false;
            vy = 0;
            x = near.x + (near.width - width) / 2.0;
            if (down) {
                y += 120 * dt;
            } else {
                y -= 120 * dt;
            }
            y = Math.max(near.upperPlatform.y - height, Math.min(near.lowerPlatform.y - height, y));
            if (y == near.upperPlatform.y - height || y == near.lowerPlatform.y - height) {
                onLadder = false;
                onGround = true;
            }
        }
        if (keys[KeyEvent.VK_SPACE]) {
            jump();
            keys[KeyEvent.VK_SPACE] = false;
        }
        if (keys[KeyEvent.VK_F]) {
            attack();
        }
        double oldFeet = y + height;
        x = Math.max(0, Math.min(800 - width, x + vx * dt));
        if (!onLadder) {
            vy += 900 * dt;
            y += vy * dt;
            onGround = false;
            for (int i = 0; i < level.platforms.size(); i++) {
                Platform platform = level.platforms.get(i);
                if (vy >= 0 && oldFeet <= platform.y + 1 && y + height >= platform.y
                    && x + width > platform.x && x < platform.x + platform.width) {
                    y = platform.y - height;
                    vy = 0;
                    onGround = true;
                    break;
                }
            }
        }
    }

    public void jump() {
        if (onGround || onLadder) {
            vy = -jumpSpeed;
            onGround = false;
            onLadder = false;
        }
    }

    public void attack() {
        if (hammerTime > 0 && attackCooldown == 0) {
            attackTime = 0.22;
            attackCooldown = 0.4;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public Rectangle getAttackBounds() {
        if (attackTime <= 0 || hammerTime <= 0) {
            return new Rectangle();
        }
        int attackX = (int) x - 40;
        if (direction == 1) {
            attackX = (int) x + width;
        }
        return new Rectangle(attackX, (int) y - 12, 40, 54);
    }

    public void draw(Graphics g) {
        if (invincibleTime > 0 && (int) (invincibleTime * 10) % 2 == 0) {
            return;
        }
        java.awt.image.BufferedImage sprite = assets.player;
        if (vx != 0 && (int) (animationTime * 8) % 2 == 0) {
            sprite = assets.playerWalk;
        }
        if (direction == -1) {
            g.drawImage(sprite, (int) x + width, (int) y, -width, height, null);
        } else {
            g.drawImage(sprite, (int) x, (int) y, width, height, null);
        }
        if (hammerTime > 0) {
            int hammerX = (int) x - 22;
            if (direction == 1) {
                hammerX = (int) x + width;
            }
            int hammerY = (int) y;
            if (attackTime > 0) {
                hammerY -= 12;
            }
            g.drawImage(assets.hammer, hammerX, hammerY, 22, 42, null);
        }
    }
}
