package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
/**
 * This class represents a block in the brick game.
 * It manages the properties and behaviors of each block, including its position, color, and hit detection.
 */
public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);
    public int row;
    public int column;
    public boolean isDestroyed = false;
    private Color color;
    public int type;

    public int x;
    public int y;
    public Rectangle rect;
    private int width;
    private int height;
    private int paddingTop;
    private int paddingH;
    // Constants representing different hit types and block types
    public static final int NO_HIT = -1;
    public static final int HIT_RIGHT = 0;
    public static final int HIT_BOTTOM = 1;
    public static final int HIT_LEFT = 2;
    public static final int HIT_TOP = 3;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_BLOCK1 = 103;
    public static int BLOCK_BLOCK2 = 104;
    public static int BLOCK_BLOCK3 = 105;
    public static int BLOCK_BLOCK4 = 106;
    public static int BLOCK_BLOCK5 = 107;
    /**
     * Constructs a new Block with specified row, column, color, and type.
     * Initializes the dimensions and padding of the block.
     *
     * @param row    The row position of the block.
     * @param column The column position of the block.
     * @param color  The color of the block.
     * @param type   The type of the block.
     */
    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        // Initialize width, height, paddingTop, and paddingH in the constructor
        this.width = 100;
        this.height = 30;
        this.paddingTop = height * 2;
        this.paddingH = 50;

        draw();
    }
    /**
     * Draws the block by setting its position and applying the appropriate fill based on its type.
     */
    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("Block 6.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("StarBlock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_BLOCK1) {
            Image image = new Image("Block 1.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_BLOCK2) {
            Image image = new Image("Block 2.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_BLOCK3) {
            Image image = new Image("Block 3.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_BLOCK4) {
            Image image = new Image("Block 4.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_BLOCK5) {
            Image image = new Image("Block 5.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }
    }
    /**
     * Checks if the block has been hit by a ball.
     * Determines the side of the block hit and marks it as destroyed if hit.
     *
     * @param xBall      The x-coordinate of the ball.
     * @param yBall      The y-coordinate of the ball.
     * @param ballRadius The radius of the ball.
     * @return The side of the block that was hit or NO_HIT if no hit occurred.
     */
    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        double blockRight = x + width;
        double blockBottom = y + height;

        boolean hitBottom = yBall + ballRadius >= y && yBall - ballRadius <= blockBottom;
        boolean hitTop = yBall - ballRadius <= blockBottom && yBall + ballRadius >= y;
        boolean hitRight = xBall + ballRadius >= x && xBall - ballRadius <= blockRight;
        boolean hitLeft = xBall - ballRadius <= blockRight && xBall + ballRadius >= x;

        if (hitBottom && xBall >= x && xBall <= blockRight) {
            isDestroyed = true;
            return HIT_BOTTOM;
        }

        if (hitTop && xBall >= x && xBall <= blockRight) {
            isDestroyed = true;
            return HIT_TOP;
        }

        if (hitRight && yBall >= y && yBall <= blockBottom) {
            isDestroyed = true;
            return HIT_RIGHT;
        }

        if (hitLeft && yBall >= y && yBall <= blockBottom) {
            isDestroyed = true;
            return HIT_LEFT;
        }

        return NO_HIT;
    }
    // Static methods to get padding and dimensions of the block
    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }
}
