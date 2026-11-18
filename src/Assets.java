import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Assets {
    public BufferedImage player = load("player_idle.png"), playerWalk = load("player_walk.png");
    public BufferedImage gorilla = load("gorilla_idle.png"), gorillaThrow = load("gorilla_throw.png");
    public BufferedImage captive = load("captive_idle.png"), platform = load("platform.png");
    public BufferedImage ladder = load("ladder.png"), barrel = load("barrel.png");
    public BufferedImage hammer = load("hammer.png"), heart = load("heart.png");

    private BufferedImage load(String name) {
        try {
            return ImageIO.read(new File("assets/sprites/" + name));
        } catch (IOException e) {
            return null;
        }
    }
}
