package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
/**
 * This class contains methods for handling the physics of the brick game.
 * It includes the movement of the ball, collision detection, and updates related to bonuses.
 */
public class Physics {
    /**
     * Handles the physics of the ball movement and collisions.
     * Updates the ball's velocity and direction based on interactions with game objects.
     *
     * @param main The main game class where the ball physics are applied.
     */
    public static void BallPhysics(Main main) {
        main.v = ((main.time - main.hitTime) / 1000.000) + 1.000;

        if (main.goDownBall) {
            main.yBall += main.vY;
        } else {
            main.yBall -= main.vY;
        }

        if (main.goRightBall) {
            main.xBall += main.vX;
        } else {
            main.xBall -= main.vX;
        }

        if (main.yBall <= 0) {
            main.vX = 2.000;
            main.resetColideFlags();
            main.goDownBall = true;
            return;
        }
        if (main.yBall >= main.sceneHeight) {
            main.goDownBall = false;
            if (!main.isGoldStatus) {
                // TODO gameover
                main.heart--;
                new Score().show(main.sceneWidth / 2, main.sceneHeight / 2, -1, main);

                if (main.heart == 0) {
                    new Score().showGameOver(main);
                    main.engine.stop();
                }
            }
            return;
        }

        if (main.yBall >= main.yBreak - main.ballRadius) {
            if (main.xBall >= main.xBreak && main.xBall <= main.xBreak + main.breakWidth) {
                main.hitTime = main.time;
                main.resetColideFlags();
                main.colideToBreak = true;
                main.goDownBall = false;

                double relation = (main.xBall - main.centerBreakX) / (main.breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    main.vX = 0;
                    main.vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    main.vX = (Math.abs(relation) * 1.5) + (main.level / 3.500);
                } else {
                    main.vX = (Math.abs(relation) * 2) + (main.level / 3.500);
                }

                if (main.xBall - main.centerBreakX > 0) {
                    main.colideToBreakAndMoveToRight = true;
                } else {
                    main.colideToBreakAndMoveToRight = false;
                }
            }
        }

        if (main.xBall >= main.sceneWidth) {
            main.resetColideFlags();
            main.vX = 2.000;
            main.colideToRightWall = true;
        }

        if (main.xBall <= 0) {
            main.resetColideFlags();
            main.vX = 2.000;
            main.colideToLeftWall = true;
        }

        if (main.colideToBreak) {
            if (main.colideToBreakAndMoveToRight) {
                main.goRightBall = true;
            } else {
                main.goRightBall = false;
            }
        }

        if (main.colideToRightWall) {
            main.goRightBall = false;
        }

        if (main.colideToLeftWall) {
            main.goRightBall = true;
        }

        if (main.colideToRightBlock) {
            main.goRightBall = true;
        }

        if (main.colideToLeftBlock) {
            main.goRightBall = true;
        }

        if (main.colideToTopBlock) {
            main.goDownBall = false;
        }

        if (main.colideToBottomBlock) {
            main.goDownBall = true;
        }
    }
    /**
     * Updates the physics state of the game.
     * This includes checking for destroyed blocks, updating ball physics,
     * and handling bonuses and their interactions with the player's paddle.
     *
     * @param main The main game class where the physics update is applied.
     */
    public static void PhysicsUpdater(Main main) {
        Platform.runLater(() -> {
            main.checkDestroyedCount();
            main.setPhysicsToBall();

            if (main.time - main.goldTime > 5000) {
                main.ball.setFill(new ImagePattern(new Image("pokeball.png")));
                main.root.getStyleClass().remove("goldRoot");
                main.isGoldStatus = false;
            }

            for (Bonus choco : main.chocos) {
                if (choco.y > main.sceneHeight || choco.taken) {
                    continue;
                }
                if (choco.y >= main.yBreak && choco.y <= main.yBreak + main.breakHeight && choco.x >= main.xBreak && choco.x <= main.xBreak + main.breakWidth) {
                    System.out.println("You Got it and +3 score for you");
                    choco.taken = true;
                    choco.choco.setVisible(false);
                    main.score += 3;
                    new Score().show(choco.x, choco.y, 3, main);
                }
                choco.y += ((main.time - choco.timeCreated) / 1000.000) + 1.000;
            }
        });
    }
}
