package game;

import javax.swing.SwingUtilities;

/**
 * Main class of the game.
 * This is the entry point of the program.
 */
public class Main {

    public static void main(String[] args) {

        // Swing programs should start from the Event Dispatch Thread.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFrame frame = new GameFrame();
                frame.setVisible(true);
            }
        });
    }
}