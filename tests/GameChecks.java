import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

public class GameChecks {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel game = new GamePanel();
            game.timer.stop();

            boolean menuWorks = game.state.equals("MENU");
            game.startGame();
            boolean startWorks = game.state.equals("GAME") && game.lives == 3;

            double oldX = game.level.player.x;
            game.keys[KeyEvent.VK_RIGHT] = true;
            game.update(0.016);
            game.clearKeys();
            boolean movementWorks = game.level.player.x > oldX;

            double oldY = game.level.player.y;
            game.keys[KeyEvent.VK_SPACE] = true;
            game.update(0.016);
            game.clearKeys();
            boolean jumpWorks = game.level.player.y < oldY;

            game.level.gorilla.spawnTimer = 0;
            game.update(0.016);
            boolean barrelsWork = game.level.barrels.size() == 1;

            Player player = game.level.player;
            game.level.barrels.clear();
            game.level.barrels.add(new Barrel(player.x, player.y, game.assets));
            game.checkInteractions();
            boolean damageWorks = game.lives == 2;

            boolean result = menuWorks && startWorks && movementWorks
                    && jumpWorks && barrelsWork && damageWorks;
            System.out.println(result);
            if (!result) {
                System.exit(1);
            }
        });
    }
}