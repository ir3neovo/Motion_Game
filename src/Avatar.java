import java.awt.*;
import java.util.*;

/**
 * Represents a moving avatar in the game.
 * Each avatar is assigned a random color and one of two motion strategies:
 * linear or random movement.
 *
 * @author Irene Wang
 * @since 2025-11-01
 */
class Avatar {
    /**
     * The x-coordinate of the avatar's current position on the screen.
     */
    private int x;
    /**
     * The y-coordinate of the avatar's current position on the screen.
     */
    private int y;
    /**
     * The size (diameter) of the avatar.
     */
    private int size;
    /**
     * The color of the avatar, randomly assigned at creation.
     */
    private Color color;
    /**
     * A Random object used for generating random numbers (e.g., position, color).
     */
    private Random rand;
    /**
     * The motion strategy used by the avatar (e.g., linear or random movement).
     */
    private MotionStrategy motionStrategy;

    /**
     * Constructs a new Avatar at the given position with the specified size.
     * Each avatar is randomly assigned a single motion algorithm ONCE
     *
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @param size the avatar's diameter in pixels
     */
    public Avatar(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.rand = new Random();
        this.color = new Color(randColor(), randColor(), randColor());

        if (rand.nextBoolean()) {
            motionStrategy = new RandomMotionStrategy();
        } else {
            motionStrategy = new LinearMotionStrategy();
        }
    }

    private int randColor() {
        return 100 + rand.nextInt(156);
    }

    /**
     * Moves the avatar according to its motion strategy.
     *
     * @param panelWidth  the width of the game panel
     * @param panelHeight the height of the game panel
     * @param obstacles   list of obstacles in the scene
     * @param avatars     list of other avatars
     */
    public void move(int panelWidth, int panelHeight, java.util.List<Obstacle> obstacles, java.util.List<Avatar> avatars) {
        motionStrategy.move(this, panelWidth, panelHeight, obstacles, avatars);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    // accessors used for tests
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    /**
     * Sets the motion strategy of this avatar manually.
     * This me thod is primarily used for testing purposes to overridethe randomly assigned
     * strategy at initialization.
     *
     * @param strategyType the strategy type ("linear" or "random")
     * @throws IllegalArgumentException if the given strategy type is not recognized
     */
    public void setStrategy(String strategyType) {
        if ("linear".equalsIgnoreCase(strategyType)) {
            this.motionStrategy = new LinearMotionStrategy();
        } else if ("random".equalsIgnoreCase(strategyType)) {
            this.motionStrategy = new RandomMotionStrategy();
        } else {
            throw new IllegalArgumentException("Unknown strategy type: " + strategyType);
        }
    }

    /**
     * Checks whether the avatar's next movement would result in a collision.
     * A collision occurs if the avatar would move outside the panel boundaries,
     * intersect an obstacle, or overlap with another avatar.
     *
     * @param future      the bounding rectangle representing the avatar's next position
     * @param panelWidth  the width of the game panel
     * @param panelHeight the height of the game panel
     * @param obstacles   the list of obstacles in the game
     * @param avatars     the list of all avatars in the game
     * @return {@code true} if a collision would occur; {@code false} otherwise
     */
    private boolean collides(Rectangle future, int panelWidth, int panelHeight,
                             java.util.List<Obstacle> obstacles, java.util.List<Avatar> avatars) {
        // panel boundaries
        if (future.x < 0 || future.y < 0 || future.x + future.width > panelWidth ||
                future.y + future.height > panelHeight) {
            return true;
        }
        // obstacles
        for (Obstacle obs : obstacles) {
            if (obs.getBounds().intersects(future)) return true;
        }
        // other avatars
        for (Avatar other : avatars) {
            if (other == this) continue;
            Rectangle otherBounds = new Rectangle(other.x, other.y, other.size, other.size);
            if (future.intersects(otherBounds)) return true;
        }
        return false;
    }

    /**
     * Defines a generic movement strategy for an avatar.
     * Implementations specify how an avatar updates its position
     * based on the game state and nearby obstacles or other avatars.
     */
    private interface MotionStrategy {
        /**
         * Moves the specified avatar according to the motion strategy.
         *
         * @param avatar      the avatar to move
         * @param panelWidth  the width of the game panel (used for boundary checks)
         * @param panelHeight the height of the game panel (used for boundary checks)
         * @param obstacles   the list of obstacles to avoid during movement
         * @param avatars     the list of other avatars to avoid during movement
         */
        void move(Avatar avatar, int panelWidth, int panelHeight, java.util.List<Obstacle> obstacles,
                  java.util.List<Avatar> avatars);
    }

    /**
     * A motion strategy that moves avatars in random directions
     * by small increments on each update cycle.
     */
    private class RandomMotionStrategy implements MotionStrategy {
        /**
         * Moves the avatar to a nearby random position, avoiding collisions.
         * The avatar moves by a random offset in the range [-5, 5] pixels
         * along both the x and y axes.
         *
         * @param avatar      the avatar to move
         * @param panelWidth  the width of the game panel
         * @param panelHeight the height of the game panel
         * @param obstacles   the list of obstacles
         * @param avatars     the list of all avatars
         */
        public void move(Avatar avatar, int panelWidth, int panelHeight, java.util.List<Obstacle> obstacles,
                         java.util.List<Avatar> avatars) {
            int dx = rand.nextInt(11) - 5; // range [-5, 5]
            int dy = rand.nextInt(11) - 5;
            int newX = Math.max(0, Math.min(avatar.x + dx, panelWidth - size));
            int newY = Math.max(0, Math.min(avatar.y + dy, panelHeight - size));

            Rectangle future = new Rectangle(newX, newY, size, size);
            if (!avatar.collides(future, panelWidth, panelHeight, obstacles, avatars)) {
                avatar.setPosition(newX, newY);
            }
        }
    }

    /**
     * A motion strategy where avatars move in a straight line until they
     * encounter an obstacle/boundary/another avatar, at which point their
     * direction changes. After four direction changes, the avatar stops.
     */
    private class LinearMotionStrategy implements MotionStrategy {
        /**
         * The horizontal velocity component of the avatar's movement.
         */
        private int dx;

        /**
         * The vertical velocity component of the avatar's movement.
         */
        private int dy;

        /**
         * The number of times the avatar has changed direction after collisions.
         */
        private int directionChanges;

        /**
         * Indicates whether the avatar has stopped moving after reaching the collision limit.
         */
        private boolean stopped = false;

        /**
         * Constructs a new LinearMotionStrategy with a random initial direction.
         */
        public LinearMotionStrategy() {
            pickNewDirection();
        }

        /**
         * Moves the avatar in a straight line. When a collision occurs,
         * the avatar changes direction. After four direction changes,
         * the avatar stops moving.
         *
         * @param avatar      the avatar to move
         * @param panelWidth  the width of the game panel
         * @param panelHeight the height of the game panel
         * @param obstacles   the list of obstacles
         * @param avatars     the list of all avatars
         */
        public void move(Avatar avatar, int panelWidth, int panelHeight, java.util.List<Obstacle> obstacles,
                         java.util.List<Avatar> avatars) {
            if (stopped) return;
            int newX = avatar.x + dx;
            int newY = avatar.y + dy;
            Rectangle future = new Rectangle(newX, newY, size, size);

            if (avatar.collides(future, panelWidth, panelHeight, obstacles, avatars)) {
                directionChanges++;
                if (directionChanges >= 4) { // pick random dir. 4 times then stop moving
                    stopped = true;
                    return;
                }
                pickNewDirection();
            } else {
                avatar.setPosition(newX, newY);
            }
        }

        /**
         * Picks a new random direction for linear motion.
         * The avatar moves either horizontally or vertically
         * by a fixed step size of 5 pixels.
         */
        private void pickNewDirection() {
            dx = dy = 0;
            if (rand.nextBoolean()) {
                dx = rand.nextBoolean() ? 5 : -5;
            } else {
                dy = rand.nextBoolean() ? 5 : -5;
            }
        }
    }
}
