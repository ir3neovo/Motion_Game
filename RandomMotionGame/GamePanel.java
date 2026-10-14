import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class GamePanel extends JPanel implements ActionListener {
    private final int NUM_AVATARS = 10;
    private final int AVATAR_SIZE = 20;
    private final Avatar[] avatars;
    private final java.util.List<Obstacle> obstacles;
    private final javax.swing.Timer timer;

    public GamePanel() {
        setBackground(Color.BLACK);
        avatars = new Avatar[NUM_AVATARS];
        obstacles = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < NUM_AVATARS; i++) {
            int x = rand.nextInt(800 - AVATAR_SIZE);
            int y = rand.nextInt(600 - AVATAR_SIZE);
            avatars[i] = new Avatar(x, y, AVATAR_SIZE);
        }

        obstacles.add(new Obstacle(200, 150, 100, 200));
        obstacles.add(new Obstacle(500, 300, 150, 100));
        obstacles.add(new Obstacle(350, 50, 80, 80));

        timer = new javax.swing.Timer(100, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Obstacle obs : obstacles) {
            obs.draw(g);
        }
        for (Avatar avatar : avatars) {
            avatar.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Avatar avatar : avatars) {
            avatar.move(getWidth(), getHeight(), obstacles);
        }
        repaint();
    }
}