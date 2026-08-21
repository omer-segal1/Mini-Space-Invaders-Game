package game;

/**
 * A custom exception for game errors.
 * We use it when the user tries to do an illegal action.
 */
public class GameException extends Exception {

    public GameException(String message) {
        super(message);
    }
}