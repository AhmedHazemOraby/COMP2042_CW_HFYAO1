package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
/**
 * This class contains methods for updating the game state.
 * It includes functions for progressing to the next level and updating in-game elements.
 */
public class Updater {
    /**
     * Advances the game to the next level.
     * Handles game state reset, level incrementation, and UI updates.
     * In normal mode, checks for winning condition and displays a win message if achieved.
     *
     * @param main The main game class where the next level is processed.
     */
    public static void NextLevel(Main main) {
        Platform.runLater(() -> {
            try {
                // Stop the game engine
                main.engine.stop();
                // Reset game variables
                main.vX = 2.000;
                main.resetColideFlags();
                main.goDownBall = true;
                // Check for winning condition in normal mode
                if (!main.isEndlessMode && main.level == 17) {
                    main.level++; // Increment level to the winning level
                    // Display winning message and restart option in normal mode
                    Label label = new Label("You Win!");
                    label.setTranslateX(200);
                    label.setTranslateY(250);
                    label.setScaleX(2);
                    label.setScaleY(2);
                    Button restart = new Button("Restart");
                    restart.setTranslateX(220);
                    restart.setTranslateY(300);
                    restart.setOnAction(event -> main.restartGame());
                    main.root.getChildren().clear();
                    main.root.getChildren().addAll(label, restart);
                    return; // Exit the method
                }
                // Increment the level for both normal and endless modes
                main.level++;
                main.levelLabel.setText("Level: " + (main.isEndlessMode ? "Endless" : main.level));
                // Game state resets and initializations for both modes
                main.isLevelCompleted = false;
                main.isGoldStatus = false;
                main.isExistHeartBlock = false;
                main.isNextLevelCalled = false;

                main.hitTime = 0;
                main.time = 0;
                main.goldTime = 0;

                main.blocks.clear();
                main.chocos.clear();
                main.destroyedBlockCount = 0;

                main.initBall();
                main.initBreak();
                main.initBoard();

                main.root.getChildren().clear();
                main.root.getChildren().addAll(main.rect, main.ball, main.scoreLabel, main.heartLabel, main.levelLabel);
                for (Block block : main.blocks) {
                    main.root.getChildren().add(block.rect);
                }

                // Restart the game engine
                main.engine = new GameEngine();
                main.engine.setOnAction(main);
                main.engine.setFps(120);
                main.engine.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * Updates various elements of the game state during each frame.
     * This includes updating the score and heart labels, the position of the paddle and ball,
     * and handling block destruction and bonus generation.
     *
     * @param main The main game class where the update is applied.
     */
    public static void update(Main main) {
        Platform.runLater(() -> {
            main.scoreLabel.setText("Score: " + main.score);
            main.heartLabel.setText("Heart : " + main.heart);
            main.rect.setX(main.xBreak);
            main.rect.setY(main.yBreak);
            main.ball.setCenterX(main.xBall);
            main.ball.setCenterY(main.yBall);
            for (Bonus choco : main.chocos) {
                choco.choco.setY(choco.y);
            }
            for (final Block block : main.blocks) {
                int hitCode = block.checkHitToBlock(main.xBall, main.yBall, main.ballRadius);
                if (hitCode != Block.NO_HIT) {
                    main.score += 1;

                    new Score().show(block.x, block.y, 1, main);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    main.destroyedBlockCount++;

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = main.time;
                        Platform.runLater(() -> {
                            main.root.getChildren().add(choco.choco);
                        });
                        main.chocos.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        main.goldTime = main.time;
                        main.ball.setFill(new ImagePattern(new Image("Ultraball.png")));
                        main.root.getStyleClass().add("goldRoot");
                        main.isGoldStatus = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        main.heart++;
                    }

                    switch (hitCode) {
                        case Block.HIT_BOTTOM:
                        case Block.HIT_TOP:
                            main.goDownBall = !main.goDownBall;
                            break;
                        case Block.HIT_LEFT:
                        case Block.HIT_RIGHT:
                            main.goRightBall = !main.goRightBall;
                            break;
                    }
                }
            }
            main.checkDestroyedCount();
        });
    }
}
