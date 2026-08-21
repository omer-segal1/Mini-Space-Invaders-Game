package game;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Graphics;

/**
 * A strong enemy.
 * It has more health and gives more points.
 */
public class StrongEnemy extends Enemy {

    public StrongEnemy(int x, int y) {
        super(x, y, 3, 30, 1);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Base alien head (Brighter, more toxic purple)
        g2d.setColor(new Color(140, 20, 180)); // Vibrant purple instead of dark purple
        int[] xHead = {x + 5, x + width - 5, x + width, x + width / 2, x};
        int[] yHead = {y + 5, y + 5, y + height / 2 + 5, y + height - 2, y + height / 2 + 5};
        g2d.fillPolygon(xHead, yHead, 5);

        // 2. Inner armor plate (Bright glowing Magenta)
        g2d.setColor(Color.MAGENTA); 
        int[] xPlate = {x + 10, x + width - 10, x + width / 2};
        int[] yPlate = {y + 10, y + 10, y + height / 2};
        g2d.fillPolygon(xPlate, yPlate, 3);

        // 3. Evil glowing red eyes (Slanted sharply downwards for a furious look)
        g2d.setColor(Color.RED);
        // Left eye
        int[] xLeftEye = {x + 6, x + width / 2 - 4, x + width / 2 - 2};
        int[] yLeftEye = {y + height / 3, y + height / 2 + 2, y + height / 2 - 5};
        g2d.fillPolygon(xLeftEye, yLeftEye, 3);
        
        // Right eye
        int[] xRightEye = {x + width - 6, x + width / 2 + 4, x + width / 2 + 2};
        int[] yRightEye = {y + height / 3, y + height / 2 + 2, y + height / 2 - 5};
        g2d.fillPolygon(xRightEye, yRightEye, 3);

        // 4. White glowing eye cores (Makes the red pop and look dangerous)
        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(new int[]{x + 12, x + width / 2 - 5, x + width / 2 - 3}, 
                        new int[]{y + height / 3 + 4, y + height / 2, y + height / 2 - 3}, 3);
        g2d.fillPolygon(new int[]{x + width - 12, x + width / 2 + 5, x + width / 2 + 3}, 
                        new int[]{y + height / 3 + 4, y + height / 2, y + height / 2 - 3}, 3);

        // 5. Sharp metallic fangs/mandibles at the bottom
        g2d.setColor(Color.LIGHT_GRAY);
        // Left fang
        g2d.fillPolygon(new int[]{x + width / 2 - 8, x + width / 2 - 2, x + width / 2 - 5}, 
                        new int[]{y + height / 2 + 8, y + height / 2 + 8, y + height - 2}, 3);
        // Right fang
        g2d.fillPolygon(new int[]{x + width / 2 + 8, x + width / 2 + 2, x + width / 2 + 5}, 
                        new int[]{y + height / 2 + 8, y + height / 2 + 8, y + height - 2}, 3);
    }

    @Override
    public Enemy clone() {
        return new StrongEnemy(x, y);
    }
}