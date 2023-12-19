package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;
import java.util.Random;
/**
 * This class represents a bonus item in the brick game.
 * It manages the properties and appearance of the bonus, such as its position and image.
 */
public class Bonus implements Serializable {
    /**
     * Constructs a new Bonus at the specified row and column.
     * The position is calculated based on the dimensions and padding of the blocks.
     *
     * @param row    The row position where the bonus will appear.
     * @param column The column position where the bonus will appear.
     */
    public Rectangle choco;
    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;
    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }
    /**
     * Initializes and draws the bonus item.
     * Sets the size, position, and image of the bonus.
     * The image is randomly selected from two available options.
     */
    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "Bonus.png";
        } else {
            url = "Bonus1.png";
        }

        choco.setFill(new ImagePattern(new Image(url)));
    }
}
