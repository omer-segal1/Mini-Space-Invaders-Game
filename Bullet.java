package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a bullet in the game.
 * A bullet can belong to the player or to an enemy.
 */
public class Bullet extends GameObject implements Movable {

    private int speedY;
    private boolean fromPlayer;

    public Bullet(int x, int y, int speedY, boolean fromPlayer) {
        super(x, y, 6, 12);

        this.speedY = speedY;
        this.fromPlayer = fromPlayer;
    }

    @Override
    public void move() {
        y += speedY;
    }

    /**
     * Checks if the bullet is outside the screen.
     */
    public boolean isOutOfScreen(int panelHeight) {
        return y < 0 || y > panelHeight;
    }

    public boolean isFromPlayer() {
        return fromPlayer;
    }

    @Override
    public void draw(Graphics g) {
        if (fromPlayer) {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height + 4);

            g.setColor(Color.WHITE);
            g.fillOval(x + 2, y + 2, 2, 4);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, width, height + 4);

            g.setColor(Color.ORANGE);
            g.fillOval(x + 1, y + 2, width - 2, height / 2);
        }
    }
}