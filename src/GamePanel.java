import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
// я еще не опредиллся с меню поэтому пока сделал это
public class GamePanel extends JFrame {
    String path = "assets/sprites/";
    public BufferedImage player, gorilla, captive, platform, ladder;
    public BufferedImage barrel, hammer, heart, fire;

    GamePanel() {
        try {
            player = ImageIO.read(new File(path + "player_idle.png"));
            gorilla = ImageIO.read(new File(path + "gorilla_idle.png"));
            captive = ImageIO.read(new File(path + "captive_idle.png"));
            platform = ImageIO.read(new File(path + "platform.png"));
            ladder = ImageIO.read(new File(path + "ladder.png"));
            barrel = ImageIO.read(new File(path + "barrel.png"));
            hammer = ImageIO.read(new File(path + "hammer.png"));
            heart = ImageIO.read(new File(path + "heart.png"));
            fire = ImageIO.read(new File(path + "fire_1.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setSize(800, 600);
        setTitle("NewkeyKong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.translate(getInsets().left, getInsets().top);
        g.setColor(new Color(8, 15, 45));
        g.fillRect(0, 0, 800, 600);

        for (int y = 140; y <= 492; y += 88) {
            for (int x = 40; x < 760; x += 36) {
                g.drawImage(platform, x, y, 36, 16, this);
            }
        }

        g.drawImage(ladder, 650, 156, 36, 72, this);
        g.drawImage(ladder, 130, 244, 36, 72, this);
        g.drawImage(ladder, 650, 332, 36, 72, this);
        g.drawImage(ladder, 130, 420, 36, 72, this);

        g.drawImage(player, 75, 438, 54, 54, this);
        g.drawImage(gorilla, 80, 52, 64, 96, this);
        g.drawImage(captive, 680, 86, 54, 54, this);

        g.drawImage(barrel, 360, 104, 36, 36, this);
        g.drawImage(barrel, 500, 192, 36, 36, this);
        g.drawImage(barrel, 250, 280, 36, 36, this);
        g.drawImage(barrel, 570, 368, 36, 36, this);
        g.drawImage(barrel, 400, 456, 36, 36, this);

        g.drawImage(hammer, 380, 174, 21, 54, this);
        g.drawImage(hammer, 570, 86, 21, 54, this);
        g.drawImage(fire, 420, 368, 36, 36, this);

        g.drawImage(heart, 620, 20, 36, 36, this);
        g.drawImage(heart, 660, 20, 36, 36, this);
        g.drawImage(heart, 700, 20, 36, 36, this);
        g.translate(-getInsets().left, -getInsets().top);
    }

    public static void main(String[] args) {
        new GamePanel();
    }
}