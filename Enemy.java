package game;

import java.awt.Graphics;

/**
 * Abstract class for all enemies in the game.
 * Each enemy has health, points, and speed.
 */
public abstract class Enemy extends GameObject implements Movable, Shootable, Cloneable {

    protected int health;
    protected int points;
    protected int speed;

    // --- VARIABLES FOR HORIZONTAL MOVEMENT ---
    protected int currentDistX = 0;
    protected int dirX = 1; // 1 for moving right, -1 for moving left
    protected int speedX = 2; // Horizontal movement speed
    protected int maxDistX = 250; // The distance they walk before dropping and reversing

    public Enemy(int x, int y, int health, int points, int speed) {
        super(x, y, 40, 25);

        this.health = health;
        this.points = points;
        this.speed = speed;
    }

    /**
     * Enemy moves left/right for a wide distance (maxDistX), then drops down.
     */
    @Override
    public void move() {
        // Move horizontally
        x += dirX * speedX;
        currentDistX += speedX;

        // When reaching the movement limit, reverse direction and drop down
        if (currentDistX >= maxDistX) {
            dirX *= -1;         // Reverse direction
            currentDistX = 0;   // Reset horizontal distance
            
            // Move down according to the individual enemy's vertical speed.
            y += speed * 30; 
        }
    }

    /**
     * Keeps a cloned enemy in sync with the original's movement cycle,
     * ensuring unified movement across all enemies.
     */
    public void syncMovement(Enemy original) {
        this.currentDistX = original.currentDistX;
        this.dirX = original.dirX;
    }

    /**
     * Enemy creates a bullet that goes down.
     */
    @Override
    public Bullet shoot() {
        int bulletX = x + width / 2 - 3;
        int bulletY = y + height;

        return new Bullet(bulletX, bulletY, 3, false);
    }

    /**
     * Enemy loses one health point when hit.
     */
    public void hit() {
        health--;
    }

    /**
     * Checks if the enemy is destroyed.
     */
    public boolean isDead() {
        return health <= 0;
    }

    public int getPoints() {
        return points;
    }

    public int getHealth() {
        return health;
    }

    /**
     * Used for cloning an enemy to create waves.
     */
    @Override
    public abstract Enemy clone();

    @Override
    public abstract void draw(Graphics g);
    
    /**
     * Returns the name of the enemy type.
     */
    public String getType() {
        return getClass().getSimpleName();
    }

    /**
     * Returns a short text description of the enemy.
     */
    public String getDetails() {
        return getType() + " | Health: " + health + " | Points: " + points;
    }
}