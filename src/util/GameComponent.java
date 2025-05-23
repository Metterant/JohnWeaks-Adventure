package util;

/**
 * Interface for game components that require initialization and per-frame updates.
 * Implementing classes should define logic for start-up (start) and frame updates (update).
 */
public interface GameComponent {
    /** Start method used for handling initialization */
    public void start();
    
    /** Update method used for repeating tasks every frame */
    public void update();
}
