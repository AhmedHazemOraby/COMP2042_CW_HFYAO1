package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    public int row;
    public int column;


    public boolean isDestroyed = false;

    private Color color;
    public int type;

    public int x;
    public int y;

    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;
    public Rectangle rect;


    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;


    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }


    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        double blockRight = x + width;
        double blockBottom = y + height;

        // Check collision with bottom side
        if (yBall + ballRadius >= y && yBall - ballRadius <= blockBottom && xBall >= x && xBall <= blockRight) {
            return HIT_BOTTOM;
        }

        // Check collision with top side
        if (yBall - ballRadius <= blockBottom && yBall + ballRadius >= y && xBall >= x && xBall <= blockRight) {
            return HIT_TOP;
        }

        // Check collision with right side
        if (xBall + ballRadius >= x && xBall - ballRadius <= blockRight && yBall >= y && yBall <= blockBottom) {
            return HIT_RIGHT;
        }

        // Check collision with left side
        if (xBall - ballRadius <= blockRight && xBall + ballRadius >= x && yBall >= y && yBall <= blockBottom) {
            return HIT_LEFT;
        }

        // Check collision with top right corner
        if (Math.pow(xBall - blockRight, 2) + Math.pow(yBall - y, 2) <= Math.pow(ballRadius, 2)) {
            return HIT_TOP;
        }

        // Check collision with top left corner
        if (Math.pow(xBall - x, 2) + Math.pow(yBall - y, 2) <= Math.pow(ballRadius, 2)) {
            return HIT_TOP;
        }

        // Check collision with bottom right corner
        if (Math.pow(xBall - blockRight, 2) + Math.pow(yBall - blockBottom, 2) <= Math.pow(ballRadius, 2)) {
            return HIT_BOTTOM;
        }

        // Check collision with bottom left corner
        if (Math.pow(xBall - x, 2) + Math.pow(yBall - blockBottom, 2) <= Math.pow(ballRadius, 2)) {
            return HIT_BOTTOM;
        }

        return NO_HIT;
    }

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
