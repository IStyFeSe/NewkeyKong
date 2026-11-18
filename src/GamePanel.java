import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

public class GamePanel extends JPanel {
    public Timer timer;
    public Level level;
    public Assets assets = new Assets();
    public String state = "MENU";
    public int score, lives;
    public boolean[] keys = new boolean[600];
    public long lastTick;
    Rectangle startButton = new Rectangle(290, 240, 220, 50);
    Rectangle exitButton = new Rectangle(290, 310, 220, 50);

    public static void main(String[] args) {
        App.main(args);
    }

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(8, 15, 45));
        int[] codes = {
            KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN,
            KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S,
            KeyEvent.VK_SPACE, KeyEvent.VK_F, KeyEvent.VK_P, KeyEvent.VK_ESCAPE,
            KeyEvent.VK_ENTER, KeyEvent.VK_R
        };
        for (int i = 0; i < codes.length; i++) {
            int code = codes[i];
            bindKey(code, false);
            bindKey(code, true);
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!state.equals("MENU")) {
                    return;
                }
                if (startButton.contains(e.getPoint())) {
                    startGame();
                }
                if (exitButton.contains(e.getPoint())) {
                    System.exit(0);
                }
            }
        });
        lastTick = System.nanoTime();
        timer = new Timer(16, e -> {
            long now = System.nanoTime();
            double dt = Math.min((now - lastTick) / 1_000_000_000.0, 0.04);
            lastTick = now;
            update(dt);
            repaint();
        });
        timer.start();
    }

    private void bindKey(int code, boolean released) {
        String name = code + ":" + released;
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(code, 0, released), name);
        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (released) {
                    keys[code] = false;
                    return;
                }
                if (keys[code]) {
                    return;
                }
                keys[code] = true;
                if (code == KeyEvent.VK_ESCAPE) {
                    state = "MENU";
                    clearKeys();
                } else if (code == KeyEvent.VK_P) {
                    togglePause();
                } else if (code == KeyEvent.VK_ENTER && state.equals("MENU")) {
                    startGame();
                } else if (code == KeyEvent.VK_R && (state.equals("WIN") || state.equals("LOSE"))) {
                    startGame();
                }
            }
        });
    }

    public void clearKeys() {
        Arrays.fill(keys, false);
    }

    public void startGame() {
        level = new Level(assets);
        score = 0;
        lives = 3;
        clearKeys();
        state = "GAME";
        lastTick = System.nanoTime();
    }

    public void togglePause() {
        if (state.equals("GAME")) {
            state = "PAUSE";
        } else if (state.equals("PAUSE")) {
            state = "GAME";
        }
    }

    public void update(double dt) {
        if (!state.equals("GAME")) {
            return;
        }
        level.update(dt, keys);
        checkInteractions();
    }

    public void checkInteractions() {
        Player player = level.player;
        if (player.y > 600) {
            loseLife();
            return;
        }
        for (int i = 0; i < level.hammers.size(); i++) {
            Hammer hammer = level.hammers.get(i);
            if (hammer.available && player.getBounds().intersects(hammer.getBounds())) {
                hammer.collect(player);
            }
        }
        for (int i = 0; i < level.barrels.size(); i++) {
            Barrel barrel = level.barrels.get(i);
            if (!barrel.active) {
                continue;
            }
            if (player.getAttackBounds().intersects(barrel.getBounds())) {
                barrel.destroy();
                addScore(100);
                continue;
            }
            if (player.invincibleTime == 0 && player.getBounds().intersects(barrel.getBounds())) {
                loseLife();
                return;
            }
            if (!barrel.jumpScored && !player.onGround && !player.onLadder
                && Math.abs(player.x + player.width / 2.0 - barrel.x - 14) < 22
                && player.y + player.height < barrel.y && barrel.y - player.y < 100) {
                barrel.jumpScored = true;
                addScore(50);
            }
        }
        if (!level.gorilla.defeated && player.getAttackBounds().intersects(level.gorilla.getBounds())) {
            level.gorilla.takeHit();
            addScore(500);
        }
        if (!level.captive.rescued && player.onGround
            && Math.abs(player.y + player.height - level.captivePlatform.y) < 1
            && player.getBounds().intersects(level.captive.getBounds())) {
            level.captive.rescue();
            addScore(1000);
            state = "WIN";
            clearKeys();
        }
    }

    public void addScore(int value) {
        score += value;
    }

    public void loseLife() {
        lives--;
        clearKeys();
        if (lives <= 0) {
            state = "LOSE";
        } else {
            level.resetAfterDeath();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        if (state.equals("MENU")) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 44));
            g.drawString("NewkeyKong", 250, 150);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.setColor(new Color(45, 150, 75));
            g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
            g.setColor(new Color(190, 50, 50));
            g.fillRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
            g.setColor(Color.WHITE);
            g.drawString("СТАРТ", 365, 272);
            g.drawString("ВЫХОД", 360, 342);
            drawHelp(g);
            return;
        }
        level.draw(g);
        g.setColor(Color.WHITE);
        g.drawString("Очки: " + score, 30, 32);
        for (int i = 0; i < lives; i++) {
            g.drawImage(assets.heart, 650 + i * 36, 10, 28, 28, null);
        }
        if (level.player.hammerTime > 0) {
            g.drawString("Молоток: " + (int) Math.ceil(level.player.hammerTime), 250, 32);
        }
        drawHelp(g);
        if (!state.equals("GAME")) {
            g.setColor(new Color(0, 0, 0, 190));
            g.fillRect(130, 200, 540, 160);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            String title;
            if (state.equals("PAUSE")) {
                title = "Пауза — P: продолжить";
            } else if (state.equals("WIN")) {
                title = "Пленница спасена! Победа!";
            } else {
                title = "Жизни закончились";
            }
            g.drawString(title, 200, 260);
            if (state.equals("PAUSE")) {
                g.drawString("Esc: в меню", 200, 310);
            } else {
                g.drawString("R: новая игра   Esc: в меню", 200, 310);
            }
        }
    }

    private void drawHelp(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 15));
        g.drawString("A/D или ←/→: ходить   W/S или ↑/↓: лестница   Пробел: прыжок", 40, 540);
        g.drawString("F: удар молотком   P: пауза   Esc: меню. Доберись до пленницы!", 40, 566);
    }
}
