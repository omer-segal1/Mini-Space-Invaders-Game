package game;

import java.awt.Graphics;

/**
 * Abstract base class for every object that appears in the game.
 * For example: player, enemies and bullets.
 */
public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Every game object must know how to draw itself.
     */
    public abstract void draw(Graphics g);

    /**
     * Simple collision check between two game objects.
     */
    public boolean intersects(GameObject other) {
        return x < other.x + other.width &&
               x + width > other.x &&
               y < other.y + other.height &&
               y + height > other.y;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}