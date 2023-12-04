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


public class Score {
    // Show the score label on the screen
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

    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        // Set the font for the label
        label.setFont(new Font("Arial", 18)); // Adjust the font size and family as needed

        Platform.runLater(() -> main.root.getChildren().add(label));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> {
                    main.root.getChildren().remove(label);
                    // Ensure that any additional cleanup or actions are performed here
                })
        );
        timeline.play();

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    final int finalI = i;  // Create a final variable
                    Platform.runLater(() -> {
                        int scaleValue = Math.abs(finalI - 10);
                        label.setScaleX(scaleValue);
                        label.setScaleY(scaleValue);
                        label.setOpacity((20 - finalI) / 20.0);
                    });
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

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
            restart.setOnAction(event -> main.restartGame());

            main.root.getChildren().addAll(label, restart);
        });
    }

    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            main.root.getChildren().addAll(label);
        });
    }
}
