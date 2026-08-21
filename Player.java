package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Graphics;

/**
 * Represents the player spaceship.
 */
public class Player extends GameObject implements Movable, Shootable {

    private int speed;
    private int lives;
    private int panelWidth;
    
    // --- NEW: Time-based shield ---
    private long shieldEndTime;
    
 // --- NEW: Shoot cooldown ---
    private long lastShotTime = 0;
    private static final int SHOOT_DELAY = 250; // Milliseconds between shots (250ms = 4 shots per second)

    public Player(int x, int y, int panelWidth) {
        super(x, y, 60, 20);

        this.panelWidth = panelWidth;
        this.speed = 15;
        this.lives = 3;
        this.shieldEndTime = 0;
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }

    public void moveRight() {
        x += speed;
        if (x + width > panelWidth) x = panelWidth - width;
    }

    @Override
    public void move() {
        // Handled by keyboard input
    }

    @Override
    public Bullet shoot() {
        long currentTime = System.currentTimeMillis();
        
        // Check if enough time has passed since the last shot
        if (currentTime - lastShotTime >= SHOOT_DELAY) {
            lastShotTime = currentTime; // Update the time of the last shot
            
            int bulletX = x + width / 2 - 3;
            int bulletY = y - 12;
            return new Bullet(bulletX, bulletY, -8, true);
        }
        
        // Return null if trying to shoot too fast (cooldown is still active)
        return null;
    }

    // --- POWER-UP LOGIC ---
    
    /**
     * Activates the shield for 5 seconds (5000 milliseconds).
     */
    public void activateShield() {
        this.shieldEndTime = System.currentTimeMillis() + 5000;
    }

    /**
     * Checks if the shield is currently active.
     */
    public boolean isShieldActive() {
        return System.currentTimeMillis() < shieldEndTime;
    }

    public void addLife() {
        this.lives++;
    }

    /**
     * Reduces a life, unless the shield is active.
     */
    public void loseLife() {
        // If shield is not active, the player loses a life
        if (!isShieldActive()) {
            lives--;
        }
        // If the shield is active, nothing happens! The player absorbs the hit.
    }

    public int getLives() {
        return lives;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a glowing aura if the time-based shield is active
        if (isShieldActive()) {
            g2d.setColor(new Color(0, 255, 255, 100)); // Transparent cyan
            g2d.fillOval(x - 10, y - 10, width + 20, height + 20);
            g2d.setColor(Color.CYAN);
            g2d.drawOval(x - 10, y - 10, width + 20, height + 20);
        }

        // Engine exhaust
        g2d.setColor(Color.ORANGE);
        int[] xEngine = {x + width / 2 - 5, x + width / 2, x + width / 2 + 5};
        int[] yEngine = {y + height, y + height + 12, y + height};
        g2d.fillPolygon(xEngine, yEngine, 3);

        // Wings
        g2d.setColor(new Color(0, 180, 0)); 
        int[] xWings = {x, x + width / 2, x + width};
        int[] yWings = {y + height, y + height / 3, y + height};
        g2d.fillPolygon(xWings, yWings, 3);

        // Main body
        g2d.setColor(Color.GREEN);
        g2d.fillOval(x + width / 4, y, width / 2, height);

        // Cockpit
        g2d.setColor(Color.CYAN);
        g2d.fillOval(x + width / 2 - 4, y + 8, 8, 12);
    }
}