package brickGame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class Initialize {
    public static void initializeMenu(Main main) {
        Pane menuPane = new Pane();

        // Set background image
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("Menu.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        menuPane.setBackground(new Background(backgroundImage));

        // Create buttons
        Button startGameButton = new Button("Start Game");
        Button endlessModeButton = new Button("Endless Mode");

        // Apply styles to buttons
        startGameButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        endlessModeButton.setStyle("-fx-font-size: 16px; -fx-background-color: #008CBA; -fx-text-fill: white;");

        // Set button positions
        startGameButton.setTranslateX(200);
        startGameButton.setTranslateY(250);
        endlessModeButton.setTranslateX(200);
        endlessModeButton.setTranslateY(300);

        // Button actions
        startGameButton.setOnAction(event -> main.initGame(false));
        endlessModeButton.setOnAction(event -> main.initGame(true));

        // Add buttons to the menuPane
        menuPane.getChildren().addAll(startGameButton, endlessModeButton);

        // Create the scene
        Scene menuScene = new Scene(menuPane, main.sceneWidth, main.sceneHeight);
        main.primaryStage.setScene(menuScene);
        main.primaryStage.setTitle("Menu");
        main.primaryStage.show();
    }

    public static void initializeGame(boolean endlessMode, Main main) {
        main.isEndlessMode = endlessMode;

        // Create and set up game scene
        main.root = new Pane();

        // Initialize labels
        main.scoreLabel = new Label("Score: " + main.score);
        // Set the level label based on the mode
        main.levelLabel = new Label(main.isEndlessMode ? "Level: Endless" : "Level: " + main.level);
        main.levelLabel.setTranslateY(20);
        main.heartLabel = new Label("Heart : " + main.heart);
        main.heartLabel.setTranslateX(main.sceneWidth - 70);
        main.root.getChildren().addAll(main.scoreLabel, main.heartLabel, main.levelLabel);

        Scene gameScene = new Scene(main.root, main.sceneWidth, main.sceneHeight);
        gameScene.setOnKeyPressed(main);

        gameScene.getStylesheets().add("style.css");

        // Initialize game components
        main.initBall();
        main.initBreak();
        main.initBoard();

        // Add game components to root
        main.root.getChildren().addAll(main.rect, main.ball);
        for (Block block : main.blocks) {
            main.root.getChildren().add(block.rect);
        }

        // Setup and start the game engine
        main.engine = new GameEngine();
        main.engine.setOnAction(main);
        main.engine.setFps(120);
        main.engine.start();

        main.primaryStage.setTitle("Game");
        main.primaryStage.setScene(gameScene);
        main.primaryStage.show();
    }
}
