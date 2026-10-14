import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The main panel of the Random Motion Game.
 * Handles drawing all game elements (avatars and obstacles) and
 * updates avatar positions at a fixed time interval using a Swing timer.
 *
 * @author Irene Wang
 * @since 2025-11-01
 */
class GamePanel extends JPanel implements ActionListener {
    /**
     * The number of avatars in the game.
     */
    private final int NUM_AVATARS = 10;
    /**
     * The size (diameter) of each avatar.
     */
    private final int AVATAR_SIZE = 20;
    /**
     * Array holding all avatars currently in the game.
     */
    private final Avatar[] avatars;
    /**
     * List of obstacles present in the game.
     */
    private final java.util.List<Obstacle> obstacles;
    /**
     * Swing timer used to periodically update the game state and repaint the screen.
     */
    private final javax.swing.Timer timer;

    /**
     * Creates and initializes the game panel.
     * Randomly positions avatars within the panel and places a few
     * predefined rectangular obstacles. A Swing timer updates the game state
     * every 100 milliseconds.
     */
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

    /**
     * Paints all game elements, including obstacles and avatars.
     *
     * @param g the Graphics context for drawing
     */
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

    /**
     * Called automatically by the Swing timer every 100 ms.
     * Moves all avatars according to their motion strategies and
     * repaints the game panel.
     *
     * @param e the ActionEvent triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (Avatar avatar : avatars) { // slight modification: pass avatars as para
            avatar.move(getWidth(), getHeight(), obstacles, Arrays.asList(avatars));
        }
        repaint();
    }
}