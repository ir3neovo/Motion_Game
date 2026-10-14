import java.awt.*;

/**
 * Represents a rectangular obstacle in the game field.
 * Obstacles act as barriers that avatars cannot pass through.
 * Each obstacle has a fixed position, size (width * height), color.
 *
 * @author Irene Wang
 * @since 2025-11-01
 */
class Obstacle {
    /**
     * The x-coordinate of the obstacle's top-left corner.
     */
    private int x;
    /**
     * The y-coordinate of the obstacle's top-left corner.
     */
    private int y;
    /**
     * The width of the obstacle.
     */
    private int width;
    /**
     * The height of the obstacle.
     */
    private int height;
    /**
     * The color of the obstacle (default is gray).
     */
    private Color color = Color.GRAY;

    /**
     * Constructs a new obstacle with the given position and dimensions.
     *
     * @param x      the x-coordinate of the top-left corner
     * @param y      the y-coordinate of the top-left corner
     * @param width  the width of the obstacle in pixels
     * @param height the height of the obstacle in pixels
     */
    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns a {@link Rectangle} object representing the obstacle's bounds.
     *
     * @return a {@code Rectangle} that defines the obstacle’s position and size
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Draws the obstacle as a filled gray rectangle.
     *
     * @param g the Graphics context used to draw the obstacle
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}