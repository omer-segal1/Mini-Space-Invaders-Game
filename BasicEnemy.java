package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Graphics;

/**
 * A simple enemy.
 * Low health and low points.
 */
public class BasicEnemy extends Enemy {

    public BasicEnemy(int x, int y) {
        super(x, y, 1, 10, 1);
    }
   
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Glass dome (light blue)
        g2d.setColor(new Color(150, 200, 255));
        g2d.fillArc(x + width / 4, y, width / 2, height, 0, 180);

        // Flying saucer body (metallic grey)
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(x, y + height / 3, width, height / 2);

        // Small neon lights
        g2d.setColor(Color.CYAN);
        g2d.fillOval(x + width / 4, y + height / 2, 4, 4);
        g2d.fillOval(x + width / 2 - 2, y + height / 2 + 2, 4, 4);
        g2d.fillOval(x + width * 3 / 4 - 4, y + height / 2, 4, 4);
    }
    
    @Override
    public Enemy clone() {
        return new BasicEnemy(x, y);
    }
}