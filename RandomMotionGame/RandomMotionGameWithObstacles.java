import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RandomMotionGameWithObstacles extends JFrame {
    public RandomMotionGameWithObstacles() {
        setTitle("Random Motion Game with Obstacles");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GamePanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandomMotionGameWithObstacles::new);
    }
}