package brickGame;

import javafx.scene.input.KeyEvent;
/**
 * This class encapsulates the game rules and behaviors for the brick game.
 * It handles key events, player movements, game states, and level progression.
 */
public class GameRules {
    /**
     * Handles key events during the game.
     * It defines the actions to be taken when specific keys are pressed.
     *
     * @param event The KeyEvent triggered by the user's action.
     * @param main  The main game class where the actions are applied.
     */
    public static void HandleKeys(KeyEvent event, Main main) {
        switch (event.getCode()) {
            case LEFT:
                main.move(main.LEFT);
                break;
            case RIGHT:
                main.move(main.RIGHT);
                break;
            case DOWN:
                // setPhysicsToBall();
                break;
            case P:
                if (main.engine != null) {
                    main.engine.togglePause();
                }
                break;
        }
    }
    /**
     * Manages the movement of the player based on the given direction.
     * It creates a new thread for smooth movement handling.
     *
     * @param direction The direction of the movement (left or right).
     * @param main      The main game class where the movement is applied.
     */
    public static void Movement(int direction, Main main) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (main.xBreak == (main.sceneWidth - main.breakWidth) && direction == main.RIGHT) {
                        return;
                    }
                    if (main.xBreak == 0 && direction == main.LEFT) {
                        return;
                    }
                    if (direction == main.RIGHT) {
                        main.xBreak++;
                    } else {
                        main.xBreak--;
                    }
                    main.centerBreakX = main.xBreak + main.halfBreakWidth;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();
    }
    /**
     * Resets various flags related to collisions in the game.
     * This method is typically called to reset the game state.
     *
     * @param main The main game class where the flags are reset.
     */
    public static void Flags(Main main) {
        main.colideToBreak = false;
        main.colideToBreakAndMoveToRight = false;
        main.colideToRightWall = false;
        main.colideToLeftWall = false;
        main.colideToRightBlock = false;
        main.colideToBottomBlock = false;
        main.colideToLeftBlock = false;
        main.colideToTopBlock = false;
    }
    /**
     * Restarts the game by resetting all relevant game parameters.
     * This includes level, score, heart count, and other game state flags.
     *
     * @param main The main game class where the game is restarted.
     */
    public static void Restart(Main main) {
        try {
            main.level = 1; // Set the level to 1
            main.heart = 3;
            main.score = 0;
            main.vX = 2.000;
            main.destroyedBlockCount = 0;
            main.resetColideFlags();
            main.goDownBall = true;

            main.isGoldStatus = false;
            main.isExistHeartBlock = false;
            main.hitTime = 0;
            main.time = 0;
            main.goldTime = 0;

            main.blocks.clear();
            main.chocos.clear();

            main.start(main.primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Checks if all blocks in the level are destroyed and proceeds to the next level if true.
     * It ensures that the level progression happens only once per level completion.
     *
     * @param main The main game class where the level check is performed.
     */
    public static void CheckDestroy(Main main) {
        if (main.destroyedBlockCount == main.blocks.size() && !main.isNextLevelCalled) {
            main.isLevelCompleted = true;
            main.isNextLevelCalled = true;  // Set the flag to true to prevent multiple calls
            main.nextLevel();
        }
    }
}
