package game;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Manages saving and loading high scores from a text file.
 */
public class HighScoreManager {
    
    private static final String FILE_NAME = "highscores.txt";
    private static final int MAX_SCORES = 10;

    /**
     * Represents a single player's score entry.
     */
    public static class ScoreEntry implements Comparable<ScoreEntry> {
        String name;
        int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        /**
         * Sorts scores in descending order (highest to lowest).
         */
        @Override
        public int compareTo(ScoreEntry other) {
            return Integer.compare(other.score, this.score);
        }
    }

    /**
     * Checks if the given score qualifies for the top 10 leaderboard.
     */
    public static boolean isTopScore(int score) {
        if (score == 0) return false;
        
        List<ScoreEntry> scores = getScores();
        
        // If the leaderboard is not full yet, any score qualifies
        if (scores.size() < MAX_SCORES) return true; 
        
        // Check if the score beats the lowest score currently on the leaderboard
        return score > scores.get(scores.size() - 1).score; 
    }

    /**
     * Adds a new player's score to the leaderboard and updates the file.
     */
    public static void addScore(String name, int score) {
        List<ScoreEntry> scores = getScores();
        scores.add(new ScoreEntry(name, score));
        
        // Re-sort the list after adding the new score
        Collections.sort(scores); 

        // Keep only the top MAX_SCORES (10) entries
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }
        
        saveScores(scores);
    }

    /**
     * Reads and returns all high scores from the text file.
     */
    public static List<ScoreEntry> getScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        try {
            File file = new File(FILE_NAME);
            
            // Return an empty list if the file doesn't exist yet
            if (!file.exists()) return scores;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // The name and score are separated by a comma
                String[] parts = line.split(","); 
                if (parts.length == 2) {
                    scores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scores;
    }

    /**
     * Saves the current list of scores back into the text file.
     */
    private static void saveScores(List<ScoreEntry> scores) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
            for (ScoreEntry entry : scores) {
                // Write to the file in "name,score" format
                writer.println(entry.name + "," + entry.score); 
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}