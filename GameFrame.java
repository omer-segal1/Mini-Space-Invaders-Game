package game;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.List;

/**
 * The main window of the game.
 * It contains the GamePanel where the game is drawn.
 */
public class GameFrame extends JFrame {

    public GameFrame() {
        super("Mini Space Invaders");

        // Show the interactive main menu before starting the game
        showStartupMenu();

        GamePanel panel = new GamePanel();
        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Displays the main menu loop when the game starts.
     */
    private void showStartupMenu() {
        while (true) {
            String[] options = {"Start Game", "Leaderboard", "Exit"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Welcome to Mini Space Invaders!\n\n" +
                    "Game rules:\n" +
                    "- Move with LEFT and RIGHT arrows.\n" +
                    "- Shoot with SPACE.\n" +
                    "- Destroy all enemies to win.\n" +
                    "- If an enemy reaches the bottom, you lose.\n" +
                    "- If enemy bullets hit you 3 times, you lose.\n\n" +
                    "What would you like to do?",
                    "Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 1) {
                // Show the leaderboard and then loop back to the menu
                showLeaderboard();
            } else if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
                // Exit the application
                System.exit(0);
            } else {
                // Break the loop and proceed to load the game
                break; 
            }
        }
    }

    /**
     * Reads the scores from HighScoreManager and displays them.
     */
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
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
}