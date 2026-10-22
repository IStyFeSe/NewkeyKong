import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class App extends JFrame {
    MouseListener ml = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() - getInsets().left;
            int y = e.getY() - getInsets().top;

            if (x >= 600 && x < 740 && y >= 20 && y < 60) {
                new GamePanel();
                dispose();
            }
            if (x >= 600 && x < 740 && y >= 80 && y < 120) {
                System.exit(0);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };

    App() {
        setSize(800, 600);
        setTitle("NewkeyKong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        addMouseListener(ml);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(getInsets().left, getInsets().top);
        g.setColor(new Color(8, 15, 45));
        g.fillRect(0, 0, 800, 600);

        g.setColor(new Color(45, 150, 75));
        g.fillRect(600, 20, 140, 40);
        g.setColor(new Color(190, 50, 50));
        g.fillRect(600, 80, 140, 40);

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("СТАРТ", 633, 47);
        g.drawString("ВЫХОД", 628, 107);
        g.translate(-getInsets().left, -getInsets().top);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}