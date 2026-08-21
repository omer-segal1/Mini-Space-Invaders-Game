package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class manages the main logic of the game.
 * It holds the player, enemies, bullets and score.
 */
public class GameManager {

    private int panelWidth;
    private int panelHeight;

    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private ArrayList<PowerUp> powerUps;

    private ScoreManager scoreManager;
    private boolean gameOver;
    private boolean playerWon;

    private Random random;
    private int waveNumber;

    public GameManager(int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        int playerX = panelWidth / 2 - 30;
        int playerY = panelHeight - 60;

        player = new Player(playerX, playerY, panelWidth);
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        powerUps = new ArrayList<PowerUp>();

        scoreManager = new ScoreManager();
        gameOver = false;
        playerWon = false;

        random = new Random();

        waveNumber = 1;
        createWave();
    }

    private void createWave() {
        try {
            enemies.clear();

            // Calculate number of enemies: (Wave * 2) + 1. 
            // Wave 1 = 3 enemies, Wave 2 = 5 enemies... Wave 20 = 41 enemies!
            int enemiesToSpawn = (waveNumber * 2) + 1; 

            for (int i = 0; i < enemiesToSpawn; i++) {
                // Generate a random number from 0 to 99 to determine enemy type
                int enemyType = random.nextInt(100);
                
                if (waveNumber <= 3) {
                    // Early waves: Mostly Basic, sometimes Fast
                    if (enemyType < 80) addEnemy(new BasicEnemy(getRandomX(), getRandomY()));
                    else addEnemy(new FastEnemy(getRandomX(), getRandomY()));
                } else if (waveNumber <= 8) {
                    // Mid waves: Mixed Basic, Fast, and some Strong
                    if (enemyType < 50) addEnemy(new BasicEnemy(getRandomX(), getRandomY()));
                    else if (enemyType < 85) addEnemy(new FastEnemy(getRandomX(), getRandomY()));
                    else addEnemy(new StrongEnemy(getRandomX(), getRandomY()));
                } else { 
                    // Late waves (9-20): High chance for Fast and Strong enemies
                    if (enemyType < 30) addEnemy(new BasicEnemy(getRandomX(), getRandomY()));
                    else if (enemyType < 60) addEnemy(new FastEnemy(getRandomX(), getRandomY()));
                    else addEnemy(new StrongEnemy(getRandomX(), getRandomY()));
                }
            }
            
         // Every 3 waves, starting from wave 7, drop an Extra Life power-up
            if (waveNumber >= 7 && waveNumber % 3 == 0) {
                powerUps.add(new PowerUp(panelWidth / 2, -30, PowerUp.TYPE_LIFE));
            }
            
            sortEnemiesByPoints();

        } catch (GameException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private int getRandomX() {
        // Spawn anywhere from X=20 to X=400.
        // The enemies will travel 250 pixels horizontally.
        // This means the rightmost enemy will reach X=650 (near the right edge of the 700px screen)
        // and the leftmost enemy will return to X=20 (near the left edge), preventing overcrowding!
        return random.nextInt(380) + 20; 
    }

    // פעולת עזר להגרלת מיקום בגובה המסך (ציר ה-Y)
    private int getRandomY() {
        // מגריל גובה רק בחלק העליון של המסך (בין 40 ל-140) כדי שיהיה לך זמן לירות בהם
        return random.nextInt(100) + 40;
    }

    /**
     * Updates all objects in the game.
     */
    public void updateGame() {
        if (gameOver) {
            return;
        }

        updateBullets();
        updateEnemies();
        updatePowerUps();
        spawnRandomShields();
        enemyRandomShoot();
        checkPlayerBulletsHitEnemies();
        checkEnemyBulletsHitPlayer();
        checkEnemiesReachedBottom();
        checkWin();
    }

    private void updateBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.move();

            if (bullet.isOutOfScreen(panelHeight)) {
                bullets.remove(i);
                i--;
            }
        }
    }
    
    /**
     * Handles movement and collision of power-ups.
     */
    private void updatePowerUps() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            p.move();

            // Check if player caught the power-up
            if (p.intersects(player)) {
                if (p.getType() == PowerUp.TYPE_SHIELD) {
                    player.activateShield();
                } else if (p.getType() == PowerUp.TYPE_LIFE) {
                    player.addLife();
                }
                powerUps.remove(i);
                i--;
                continue;
            }

            // Remove if it falls out of bounds
            if (p.isOutOfScreen(panelHeight)) {
                powerUps.remove(i);
                i--;
            }
        }
    }

    /**
     * Randomly spawns a shield power-up during gameplay, starting from wave 7.
     */
    private void spawnRandomShields() {
        // Only spawn power-ups from wave 7 and above
        if (waveNumber < 7) {
            return;
        }
        
        // Greatly reduced the chance to drop a shield (now around 1 in 1200 frames)
        if (random.nextInt(1200) == 0) {
            int randomX = random.nextInt(panelWidth - 40) + 20;
            powerUps.add(new PowerUp(randomX, -20, PowerUp.TYPE_SHIELD));
        }
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    /**
     * Randomly chooses enemies to shoot.
     * The number is small so the game will not be too hard.
     */
    private void enemyRandomShoot() {
        if (enemies.isEmpty()) {
            return;
        }

        int chance = random.nextInt(100);

        if (chance < 2) {
            int index = random.nextInt(enemies.size());
            Enemy enemy = enemies.get(index);
            bullets.add(enemy.shoot());
        }
    }

    private void checkPlayerBulletsHitEnemies() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);

            if (!bullet.isFromPlayer()) {
                continue;
            }

            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);

                if (bullet.intersects(enemy)) {
                    enemy.hit();
                    bullets.remove(i);
                    i--;

                    if (enemy.isDead()) {
                    	scoreManager.addPoints(enemy.getPoints());
                        enemies.remove(j);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Checks if enemy bullets hit the player.
     */
    private void checkEnemyBulletsHitPlayer() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);

            if (bullet.isFromPlayer()) {
                continue;
            }

            if (bullet.intersects(player)) {
                player.loseLife();
                bullets.remove(i);
                i--;

                if (player.getLives() <= 0) {
                    gameOver = true;
                    playerWon = false;
                    showEndMessage();
                }
            }
        }
    }

    /**
     * If an enemy reaches the bottom area, the player loses.
     */
    private void checkEnemiesReachedBottom() {
        for (Enemy enemy : enemies) {
            if (enemy.getY() + enemy.getHeight() >= panelHeight - 30) {
                gameOver = true;
                playerWon = false;
                showEndMessage();
                return;
            }
        }
    }

    private void checkWin() {
        if (!enemies.isEmpty()) {
            return;
        }

        waveNumber++;

        // Changed max levels from 3 to 20
        if (waveNumber > 20) {
            gameOver = true;
            playerWon = true;
            showEndMessage();
            return;
        }

        createWave();
    }
    
    private void showEndMessage() {
        int finalScore = scoreManager.getScore();
        String message;

        if (playerWon) {
            message = "You won!\nFinal score: " + finalScore;
        } else {
            message = "Game over!\nFinal score: " + finalScore;
        }

        // 1. Check if the player achieved a high score and ask for their name
        if (HighScoreManager.isTopScore(finalScore)) {
            String name = JOptionPane.showInputDialog(null, 
                    message + "\n\nYou made it to the TOP 10!\nEnter your name:", 
                    "New High Score!", JOptionPane.PLAIN_MESSAGE);
            
            if (name != null && !name.trim().isEmpty()) {
                // Remove commas to prevent bugs, since we use commas as a separator in the text file
                name = name.replace(",", "");
                HighScoreManager.addScore(name.trim(), finalScore);
            }
        } else {
            JOptionPane.showMessageDialog(null, message);
        }

        // 2. Show the end menu with 3 options
        showEndMenu();
    }

    private void showEndMenu() {
        String[] options = {"Play Again", "Leaderboard", "Main Menu"};
        int choice = JOptionPane.showOptionDialog(null,
                "What would you like to do next?",
                "Game Finished",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            // Play again
            restartGame();
        } else if (choice == 1) {
            // Show leaderboard
            showLeaderboard();
            
            // Return to the end menu after closing the leaderboard
            showEndMenu(); 
        } else if (choice == 2) {
            // Go to main menu
            showMainMenu();
        } else {
            // Exit if the player closed the window (clicked the X button)
            System.exit(0);
        }
    }

    private void showLeaderboard() {
        List<HighScoreManager.ScoreEntry> scores = HighScoreManager.getScores();
        StringBuilder sb = new StringBuilder("--- TOP 10 SCORES ---\n\n");
        
        if (scores.isEmpty()) {
            sb.append("No scores yet. Be the first!\n");
        } else {
            int rank = 1;
            for (HighScoreManager.ScoreEntry entry : scores) {
                sb.append(rank).append(". ").append(entry.name).append(" - ").append(entry.score).append(" Points\n");
                rank++;
            }
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMainMenu() {
        String[] options = {"Start Game", "Leaderboard", "Exit"};
        int choice = JOptionPane.showOptionDialog(null,
                "Welcome to Mini Space Invaders!\n\n" +
                "Game rules:\n" +
                "- Move the spaceship with LEFT and RIGHT arrows.\n" +
                "- Shoot enemies with SPACE.\n" +
                "- Destroy all enemies to win.\n\n",
                "Main Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) {
            // Show leaderboard
            showLeaderboard();
            // Show the main menu again after closing the leaderboard
            showMainMenu(); 
        } else if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
            // Exit the game
            System.exit(0);
        } else {
            // Start the game (choice == 0)
            restartGame();
        }
    }
    
    public void restartGame() {
        int playerX = panelWidth / 2 - 30;
        int playerY = panelHeight - 60;

        player = new Player(playerX, playerY, panelWidth);
        bullets.clear();
        enemies.clear();
        powerUps.clear();
        scoreManager.reset();

        gameOver = false;
        playerWon = false;
        waveNumber = 1;

        createWave();
    }

    public void drawGame(Graphics g) {
    	g.setColor(Color.DARK_GRAY);

    	for (int i = 0; i < 80; i++) {

    	    int starX = (i * 83) % panelWidth;
    	    int starY = (i * 47) % panelHeight;

    	    g.fillRect(starX, starY, 2, 2);
    	}
        player.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        
        for (PowerUp p : powerUps) {
            p.draw(g);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        drawText(g);
    }

    public void movePlayerLeft() {
        if (!gameOver) {
            player.moveLeft();
        }
    }

    public void movePlayerRight() {
        if (!gameOver) {
            player.moveRight();
        }
    }

    public void playerShoot() {
        if (!gameOver) {
            Bullet newBullet = player.shoot();
            
            // Only add the bullet to the game if it's not null (cooldown allowed it)
            if (newBullet != null) {
                bullets.add(newBullet);
            }
        }
    }

    private void drawText(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Mini Space Invaders", 20, 25);
        g.drawString("Lives: " + player.getLives(), 20, 45);
        g.drawString("Score: " + scoreManager.getScore(), 20, 65);
        
        // Updated text to display out of 20 waves instead of 3
        g.drawString("Attack round: " + waveNumber + " / 20", 20, 85);
        
        g.drawString("Enemies: " + enemies.size(), 20, 105);
        g.drawString("LEFT/RIGHT move | SPACE shoot", 20, 125);

        if (gameOver) {
            if (playerWon) {
                g.drawString("YOU WON!", 300, 250);
            } else {
                g.drawString("GAME OVER", 300, 250);
            }
        }
    }
    
    /**
     * Adds a new enemy to the game.
     */
    public void addEnemy(Enemy enemy) throws GameException {
        if (enemy == null) {
            throw new GameException("Cannot add a null enemy.");
        }

        enemies.add(enemy);
    }

    /**
     * Removes an enemy by index.
     */
    public void removeEnemy(int index) throws GameException {
        if (index < 0 || index >= enemies.size()) {
            throw new GameException("Invalid enemy index.");
        }

        enemies.remove(index);
    }

    /**
     * Searches for the first enemy of a specific type.
     * Example: "BasicEnemy", "FastEnemy", "StrongEnemy".
     */
    public Enemy findEnemyByType(String type) throws GameException {
        if (type == null || type.length() == 0) {
            throw new GameException("Enemy type cannot be empty.");
        }

        for (Enemy enemy : enemies) {
            if (enemy.getType().equalsIgnoreCase(type)) {
                return enemy;
            }
        }

        throw new GameException("Enemy was not found.");
    }

    /**
     * Sorts enemies by points from low to high.
     */
    public void sortEnemiesByPoints() {
        Collections.sort(enemies, new Comparator<Enemy>() {
            @Override
            public int compare(Enemy e1, Enemy e2) {
                return e1.getPoints() - e2.getPoints();
            }
        });
    }

    public void cloneEnemy(int index) throws GameException {
        if (index < 0 || index >= enemies.size()) {
            throw new GameException("Invalid enemy index.");
        }

        Enemy original = enemies.get(index);
        Enemy copy = original.clone();

        // Move the cloned enemy a little so it will not be exactly on the original.
        copy.x += 50;
        
        // Sync the movement cycle so the clone moves uniformly with the rest!
        copy.syncMovement(original);

        enemies.add(copy);
    }

    /**
     * Returns a text report of all enemies.
     */
    public String getEnemiesReport() {
        if (enemies.isEmpty()) {
            return "There are no enemies in the game.";
        }

        String report = "";

        for (int i = 0; i < enemies.size(); i++) {
            report += i + ". " + enemies.get(i).getDetails() + "\n";
        }

        return report;
    }

}