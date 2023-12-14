package brickGame;

import javafx.application.Platform;

public class GameEngine {

    private OnAction onAction;
    private int fps = 60;
    private Thread gameLoopThread;
    private boolean isStopped = true;
    private boolean isPaused = false;

    public void togglePause() {
        isPaused = !isPaused;
    }

    public boolean isPaused() {
        return isPaused;
    }


    // Set the OnAction for the game engine
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Set the frames per second (fps) for the game loop
     * @param fps frames per second
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps; // Convert fps to milliseconds
    }

    // The main game loop that handles updating and physics
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


    // Initialize the game engine
    private void initialize() {
        try {
            // Run UI-related code in the JavaFX Application Thread
            Platform.runLater(() -> onAction.onInit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Start the game engine
    public void start() {
        initialize();
        isStopped = false;
        gameLoopThread = new Thread(this::gameLoop);
        gameLoopThread.start();
    }

    // Stop the game engine
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            interruptThread(gameLoopThread);
        }
    }

    // Helper method to interrupt a thread gracefully
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

    public interface OnAction {
        void onUpdate();          // Called on each frame update
        void onInit();            // Called during initialization
        void onPhysicsUpdate();   // Called on each physics update
        void onTime(long time);   // Called with the current time
    }
}
