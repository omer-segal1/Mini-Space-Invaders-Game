package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Represents a power-up dropping from the top of the screen.
 */
public class PowerUp extends GameObject implements Movable {

    public static final int TYPE_SHIELD = 1;
    public static final int TYPE_LIFE = 2;

    private int type;
    private int speedY;

    public PowerUp(int x, int y, int type) {
        super(x, y, 20, 20);
        this.type = type;
        this.speedY = 4; // Falling speed
    }

    public int getType() {
        return type;
    }

    @Override
    public void move() {
        y += speedY;
    }

    public boolean isOutOfScreen(int panelHeight) {
        return y > panelHeight;
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 14));
        
        if (type == TYPE_SHIELD) {
            // Draw Shield Power-up (Cyan circle with 'S')
            g.setColor(Color.CYAN);
            g.fillOval(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawString("S", x + 5, y + 15);
        } else if (type == TYPE_LIFE) {
            // Draw Life Power-up (Pink square with a Heart/Plus)
            g.setColor(Color.PINK);
            g.fillRect(x, y, width, height);
            g.setColor(Color.RED);
            g.drawString("+", x + 5, y + 15);
        }
    }
}