package game;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The panel where the game is displayed.
 * This class also listens to keyboard input.
 */
public class GamePanel extends JPanel implements KeyListener {

    public static final int PANEL_WIDTH = 700;
    public static final int PANEL_HEIGHT = 500;

    private GameManager gameManager;
    private Timer gameTimer;

    // --- NEW: Key state tracking flags ---
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    public GamePanel() {
        gameManager = new GameManager(PANEL_WIDTH, PANEL_HEIGHT);

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(5, 5, 25));

        // The panel must be focusable in order to receive keyboard input.
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        gameTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // --- NEW: Apply continuous movement and shooting based on flags ---
                if (leftPressed) gameManager.movePlayerLeft();
                if (rightPressed) gameManager.movePlayerRight();
                if (spacePressed) gameManager.playerShoot();

                gameManager.updateGame();
                repaint();
            }
        });

        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameManager.drawGame(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Mark the corresponding flag as true when a key is pressed down
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (key == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Mark the corresponding flag as false when the key is released
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        } else if (key == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used at this stage.
    }
}