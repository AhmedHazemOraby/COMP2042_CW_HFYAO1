package brickGame;

import javafx.application.Platform;
/**
 * This class represents the game engine for a brick game.
 * It manages the game loop, handles pausing and stopping the game, and updates the game state.
 */
public class GameEngine {

    private OnAction onAction;
    private int fps = 60;
    private Thread gameLoopThread;
    private boolean isStopped = true;
    private boolean isPaused = false;
    /**
     * Toggles the pause state of the game.
     * If the game is running, it will be paused; if paused, it will resume.
     */
    public void togglePause() {
        isPaused = !isPaused;
    }
    /**
     * Sets the OnAction interface for handling game events.
     *
     * @param onAction The OnAction instance containing methods to be executed during the game loop.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }
    /**
     * Sets the frames per second (fps) for the game loop.
     * This determines how often the game state is updated.
     *
     * @param fps The desired frames per second.
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps; // Convert fps to milliseconds
    }
    /**
     * The main game loop that handles updating and physics.
     * Runs continuously in a separate thread until interrupted.
     */
    private synchronized void gameLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!isPaused) {
                try {
                    Platform.runLater(() -> {
                        onAction.onTime(System.currentTimeMillis());
                        onAction.onUpdate();
                        onAction.onPhysicsUpdate();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(fps); // Sleep even when paused to avoid high CPU usage
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    /**
     * Initializes the game engine.
     * Prepares the game for starting by setting up initial states.
     */
    // Initialize the game engine
    private void initialize() {
        try {
            // Run UI-related code in the JavaFX Application Thread
            Platform.runLater(() -> onAction.onInit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Starts the game engine.
     * Initializes the game and begins the game loop in a new thread.
     */
    // Start the game engine
    public void start() {
        initialize();
        isStopped = false;
        gameLoopThread = new Thread(this::gameLoop);
        gameLoopThread.start();
    }
    /**
     * Stops the game engine.
     * Gracefully terminates the game loop and any associated resources.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            interruptThread(gameLoopThread);
        }
    }
    /**
     * Helper method to interrupt a thread gracefully.
     *
     * @param thread The thread to be interrupted.
     */
    private void interruptThread(Thread thread) {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                e.printStackTrace();
            }
        }
    }
    /**
     * Interface for handling game actions.
     * Methods defined here are called at different stages of the game loop.
     */
    public interface OnAction {
        void onUpdate();          // Called on each frame update
        void onInit();            // Called during initialization
        void onPhysicsUpdate();   // Called on each physics update
        void onTime(long time);   // Called with the current time
    }
}
