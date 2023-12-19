package brickGame;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
/**
 * This class handles the display and animation of score-related information on the screen.
 * It includes methods to show score changes and display the game-over screen.
 */
public class Score {
    /**
     * Shows a score change on the screen with an animation.
     * Positive scores are displayed in green, and negative scores in red.
     * The score label fades and grows in size before disappearing.
     *
     * @param x     The x-coordinate where the score will be displayed.
     * @param y     The y-coordinate where the score will be displayed.
     * @param score The score to be displayed.
     * @param main  The main game class where the score is to be displayed.
     */
    public void show(final double x, final double y, int score, final Main main) {
        String sign;
        String textColor;

        if (score >= 0) {
            sign = "+";
            textColor = "green";
        } else {
            sign = "";
            textColor = "red";
        }
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);
        // Apply CSS styling for text color
        label.setStyle("-fx-text-fill: " + textColor + ";");
        Platform.runLater(() -> main.root.getChildren().add(label));
        // Gradually fade away the label over time using a FadeTransition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), label);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> {
            // Remove the label from the scene when the fade transition is finished
            Platform.runLater(() -> main.root.getChildren().remove(label));
        });
        fadeTransition.play();

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    label.setScaleX(i);
                    label.setScaleY(i);
                    label.setOpacity((20 - i) / 20.0);
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * Displays the game over screen.
     * Shows a 'Game Over' label and a 'Restart' button to restart the game.
     *
     * @param main The main game class where the game over screen is displayed.
     */
    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> GameRules.Restart(main));

            main.root.getChildren().addAll(label, restart);
        });
    }
}
