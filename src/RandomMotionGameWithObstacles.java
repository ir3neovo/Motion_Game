import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Entry point of the Random Motion Game with Obstacles.
 * This class sets up the JFrame and embeds a {@link GamePanel}
 * that handles all game logic and rendering.
 *
 * @author Irene Wang
 * @since 2025-11-01
 */
public class RandomMotionGameWithObstacles extends JFrame {
    /**
     * Constructs and displays the game window.
     * The window contains a {@link GamePanel} and initializes
     * the game with default settings (800×600 pixels).
     */
    public RandomMotionGameWithObstacles() {
        setTitle("Random Motion Game with Obstacles");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GamePanel());
        setVisible(true);
    }

    /**
     * Main() that starts the game.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandomMotionGameWithObstacles::new);
    }
}