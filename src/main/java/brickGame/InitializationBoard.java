package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Random;
/**
 * This class is responsible for initializing different components of the brick game.
 * It includes methods to set up the game board, the ball, and the player's paddle.
 */
public class InitializationBoard {
    public static void initializationBoard(Main main) {
        Random random = new Random();
        int blocksPerRow = 4; // Maximum number of blocks per row

        // Start with 2 rows, then increase by 1 each level until reaching 9 rows
        int rowsForCurrentLevel = main.isEndlessMode ? 9 : Math.min(2 + (main.level - 1), 9);

        for (int i = 0; i < rowsForCurrentLevel; i++) {
            for (int j = 0; j < blocksPerRow; j++) {
                int r = random.nextInt(100); // Generate a random number between 0 and 99 for block type determination

                int type;
                if (r < 10) {
                    // 10% chance for BLOCK_CHOCO
                    type = Block.BLOCK_CHOCO;
                } else if (r < 20) {
                    // 10% chance for BLOCK_HEART if it doesn't exist, otherwise BLOCK_NORMAL
                    if (!main.isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        main.isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_BLOCK1;
                    }
                } else if (r < 30) {
                    // 10% chance for BLOCK_STAR
                    type = Block.BLOCK_STAR;
                } else {
                    // 70% chance divided equally among each
                    int blockChoice = random.nextInt(5);
                    switch (blockChoice) {
                        case 0: type = Block.BLOCK_BLOCK1; break;
                        case 1: type = Block.BLOCK_BLOCK2; break;
                        case 2: type = Block.BLOCK_BLOCK3; break;
                        case 3: type = Block.BLOCK_BLOCK4; break;
                        case 4: type = Block.BLOCK_BLOCK5; break;
                        default: type = Block.BLOCK_BLOCK5; // Should not happen
                    }
                }

                main.blocks.add(new Block(i, j, main.colors[random.nextInt(main.colors.length)], type));
            }
        }
    }
    /**
     * Initializes the ball in the game.
     * Sets the position and image of the ball at the start of the game.
     *
     * @param main The main game class where the ball is initialized.
     */
    public static void initializeBall(Main main) {
        main.xBall = main.sceneWidth / 2.0;
        main.yBall = main.sceneHeight / 2.0;
        main.ball = new Circle();
        main.ball.setRadius(main.ballRadius);
        main.ball.setFill(new ImagePattern(new Image("pokeball.png")));
    }
    /**
     * Initializes the player's paddle (break).
     * Sets the size, position, and image of the paddle.
     *
     * @param main The main game class where the paddle is initialized.
     */
    public static void initializeBreak(Main main) {
        main.rect = new Rectangle();
        main.rect.setWidth(main.breakWidth);
        main.rect.setHeight(main.breakHeight);
        main.xBreak = (main.sceneWidth - main.breakWidth) / 2.0;
        main.rect.setX(main.xBreak);
        main.rect.setY(main.yBreak);

        ImagePattern pattern = new ImagePattern(new Image("Padel.png"));
        main.rect.setFill(pattern);
    }
}
