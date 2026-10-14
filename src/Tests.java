import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.*;
import java.lang.reflect.Field;

public class Tests {

    // AVATAR TESTS
    private java.util.List<Obstacle> obstacles;
    private java.util.List<Avatar> avatars;

    @BeforeEach
    void setUp() {
        obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(100, 100, 50, 50));
        avatars = new ArrayList<>();
    }

    @Test
    void testAvatarInitialization() {
        Avatar a = new Avatar(10, 10, 20);
        assertTrue(a.getX() >= 0 && a.getY() >= 0);
        assertEquals(20, a.getSize());
    }

    /**
     * Random motion should stay within boundaries
     */
    @Test
    void testRandomMotionWithinBounds() {
        Avatar a = new Avatar(10, 10, 20);
        avatars.add(a);
        for (int i = 0; i < 50; i++) {
            a.move(200, 200, obstacles, avatars);
            assertTrue(a.getX() >= 0 && a.getX() <= 180);
            assertTrue(a.getY() >= 0 && a.getY() <= 180);
        }
    }

    /**
     * linear motion should stop after 4 collisions
     */
    @Test
    void testLinearMotionStopsAfterCollisions() {
        Avatar a = new Avatar(0, 0, 20);
        a.setStrategy("linear"); // force linear motion for testing
        avatars.add(a);

        for (int i = 0; i < 200; i++) {
            a.move(100, 100, obstacles, avatars);
        }
        int xBefore = a.getX(), yBefore = a.getY();
        for (int i = 0; i < 50; i++) a.move(100, 100, obstacles, avatars);
        assertEquals(xBefore, a.getX());
        assertEquals(yBefore, a.getY());
    }

    /**
     * should prevent obstacle collisions
     */
    @Test
    void testCollisionWithObstacle() {
        Avatar a = new Avatar(80, 80, 20); // safely away from (100,100)
        avatars.add(a);
        for (int i = 0; i < 20; i++) a.move(200, 200, obstacles, avatars);
        Rectangle avatarRect = new Rectangle(a.getX(), a.getY(), a.getSize(), a.getSize());
        assertFalse(obstacles.get(0).getBounds().intersects(avatarRect));
    }

    /**
     * Check if linear motion moves smoothly in a consistent direction
     */
    @Test
    void testLinearStrategyMovesConsistently() {
        Avatar a = new Avatar(50, 50, 20);
        a.setStrategy("linear");

        int prevX = a.getX();
        int prevY = a.getY();

        boolean moved = false;

        for (int i = 0; i < 10; i++) {
            a.move(200, 200, Collections.emptyList(), Collections.singletonList(a));
            int newX = a.getX();
            int newY = a.getY();

            if (newX != prevX || newY != prevY) moved = true;
            int dx = newX - prevX;
            int dy = newY - prevY;
            if (i > 0 && moved) {
                assertTrue(Math.abs(dx) <= 5 && Math.abs(dy) <= 5);
            }
            prevX = newX;
            prevY = newY;
        }
        assertTrue(moved);
    }

    /**
     * Check if random strategy varies avatar position over time
     */
    @Test
    void testRandomStrategyVariesOverTime() {
        Avatar a = new Avatar(100, 100, 20);
        a.setStrategy("random");

        Set<Point> visited = new HashSet<>();
        for (int i = 0; i < 30; i++) {
            a.move(200, 200, Collections.emptyList(), Collections.singletonList(a));
            visited.add(new Point(a.getX(), a.getY()));
        }
        assertTrue(visited.size() > 5);
    }


    /**
     * 2 avatars should not overlap
     */
    @Test
    void testAvatarCollisionAvoidance() {
        Avatar a1 = new Avatar(50, 50, 20);
        Avatar a2 = new Avatar(80, 50, 20); // non-overlapping start
        avatars.add(a1);
        avatars.add(a2);
        for (int i = 0; i < 20; i++) {
            a1.move(200, 200, obstacles, avatars);
            a2.move(200, 200, obstacles, avatars);
        }
        Rectangle r1 = new Rectangle(a1.getX(), a1.getY(), a1.getSize(), a1.getSize());
        Rectangle r2 = new Rectangle(a2.getX(), a2.getY(), a2.getSize(), a2.getSize());
        assertFalse(r1.intersects(r2));
    }

    // OBSTACLE TESTS
    @Test
    void testObstacleBounds() {
        Obstacle obs = new Obstacle(50, 60, 100, 40);
        Rectangle r = obs.getBounds();
        assertEquals(50, r.x);
        assertEquals(60, r.y);
        assertEquals(100, r.width);
        assertEquals(40, r.height);
    }

    // GAMEPANEL TESTS

    /**
     * properly sets up and manages avatars, obstacles, and the timer
     */
    @Test
    void testGamePanelInitialization() {
        GamePanel gp = new GamePanel();
        assertNotNull(gp);
    }

    /**
     * check for correct num of avatars
     */
    @Test
    void testGamePanelHasAvatars() throws Exception {
        GamePanel gp = new GamePanel();
        Field f = GamePanel.class.getDeclaredField("avatars");
        f.setAccessible(true);
        Avatar[] arr = (Avatar[]) f.get(gp);
        assertEquals(10, arr.length);
        for (Avatar a : arr) assertNotNull(a);
    }

    /**
     * check obstacle initialization
     */
    @Test
    void testGamePanelHasObstacles() throws Exception {
        GamePanel gp = new GamePanel();
        Field f = GamePanel.class.getDeclaredField("obstacles");
        f.setAccessible(true);
        java.util.List<?> list = (java.util.List<?>) f.get(gp);
        assertTrue(list.size() >= 1);
    }

    @Test
    void testGamePanelTimerExists() throws Exception {
        GamePanel gp = new GamePanel();
        Field f = GamePanel.class.getDeclaredField("timer");
        f.setAccessible(true);
        Object timer = f.get(gp);
        assertNotNull(timer);
    }

    // MAIN FRAME TESTS
    @Test
    void testMainMethodDoesNotThrow() {
        assertDoesNotThrow(() ->
                RandomMotionGameWithObstacles.main(new String[]{})
        );
    }

}
