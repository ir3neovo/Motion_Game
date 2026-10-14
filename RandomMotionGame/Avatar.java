import java.awt.*;
import java.util.*;

class Avatar {
    private int x, y, size;
    private Color color;
    private Random rand;

    public Avatar(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.rand = new Random();
        this.color = new Color(randColor(), randColor(), randColor());
    }

    private int randColor() {
        return 100 + rand.nextInt(156);
    }

    public void move(int panelWidth, int panelHeight, java.util.List<Obstacle> obstacles) {
        int dx = rand.nextInt(11) - 5;
        int dy = rand.nextInt(11) - 5;

        int newX = Math.max(0, Math.min(x + dx, panelWidth - size));
        int newY = Math.max(0, Math.min(y + dy, panelHeight - size));

        Rectangle futureBounds = new Rectangle(newX, newY, size, size);

        for (Obstacle obs : obstacles) {
            if (obs.getBounds().intersects(futureBounds)) {
                return;
            }
        }

        x = newX;
        y = newY;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }
}