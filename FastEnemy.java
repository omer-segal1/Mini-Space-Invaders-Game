package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Graphics;

/**
 * A faster enemy.
 * It gives more points because it is harder to hit.
 */
public class FastEnemy extends Enemy {

    public FastEnemy(int x, int y) {
        super(x, y, 1, 20, 2);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Interceptor body (pointed downwards)
        g2d.setColor(Color.ORANGE);
        int[] xDart = {x + width / 2, x + width, x + width / 2, x};
        int[] yDart = {y + height, y, y + height / 4, y};
        g2d.fillPolygon(xDart, yDart, 4);

        // Energy core in the middle
        g2d.setColor(Color.RED);
        g2d.fillOval(x + width / 2 - 3, y + height / 4 + 2, 6, 6);
    }
    
    @Override
    public Enemy clone() {
        return new FastEnemy(x, y);
    }
}