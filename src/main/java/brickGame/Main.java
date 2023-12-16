package brickGame;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    // Constants for direction
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    // Game variables
    private int level = 1;
    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;
    private int breakWidth = 130;
    private int breakHeight = 30;
    private int halfBreakWidth = breakWidth / 2;
    private int sceneWidth = 500;
    private int sceneHeight = 700;
    private Circle ball;
    private double xBall;
    private double yBall;
    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;
    private boolean isLevelCompleted = false;
    private Rectangle rect;
    private int ballRadius = 10;
    private int destroyedBlockCount = 0;
    private double v = 1.000;
    private int heart = 3;
    private int score = 0;
    private long time = 0;
    private long hitTime = 0;
    private long goldTime = 0;
    private GameEngine engine;
    public static String savePath = "D:/save/save.mdds";
    public static String savePathDir = "D:/save/";
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Bonus> chocos = new ArrayList<>();
    private Color[] colors = {
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN
    };
    public Pane root;
    private Label scoreLabel;
    private Label heartLabel;
    private Label levelLabel;
    private boolean loadFromSave = false;
    Stage primaryStage;
    Button load = null;
    Button newGame = null;

    @Override
    public void onInit() {
        // Initialization code here
    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initMenu();
    }
    private void initMenu() {
        Pane menuPane = new Pane();

        // Set background image
        BackgroundImage backgroundImage = new BackgroundImage(new Image("Menu.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        menuPane.setBackground(new Background(backgroundImage));

        Button startGameButton = new Button("Start Game");
        startGameButton.setTranslateX(200); // Set X position
        startGameButton.setTranslateY(250); // Set Y position

        startGameButton.setOnAction(event -> initGame());

        menuPane.getChildren().add(startGameButton);
        Scene menuScene = new Scene(menuPane, sceneWidth, sceneHeight);
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Menu");
        primaryStage.show();
    }

    private void initGame() {
        // Create and set up game scene
        root = new Pane();
        // Initialize labels
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);
        root.getChildren().addAll(scoreLabel, heartLabel, levelLabel);

        Scene gameScene = new Scene(root, sceneWidth, sceneHeight);
        gameScene.setOnKeyPressed(this);

        // Add stylesheets if any
        gameScene.getStylesheets().add("style.css");

        // Initialize game components
        initBall();
        initBreak();
        initBoard();

        // Add game components to root
        root.getChildren().addAll(rect, ball);
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }

        // Setup and start the game engine
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();

        primaryStage.setTitle("Game");
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    private void startGame() {
        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);

        root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        Scene gameScene = new Scene(root, sceneWidth, sceneHeight);
        gameScene.getStylesheets().add("style.css");
        gameScene.setOnKeyPressed(this);

        primaryStage.setTitle("Game");
        primaryStage.setScene(gameScene);
        primaryStage.show();

        // Initialize game components
        initBall();
        initBreak();
        initBoard();

        // Start the game engine
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
    }

    private void initBoard() {
        Random random = new Random();
        int blocksPerRow = 4; // Maximum number of blocks per row

        // Start with 2 rows, then increase by 1 each level until reaching 11 rows
        int rowsForCurrentLevel = Math.min(2 + (level - 1), 9);

        for (int i = 0; i < rowsForCurrentLevel; i++) {
            for (int j = 0; j < blocksPerRow; j++) {
                int r = random.nextInt(100); // Generate a random number between 0 and 99 for block type determination

                int type;
                if (r < 10) {
                    // 10% chance for BLOCK_CHOCO
                    type = Block.BLOCK_CHOCO;
                } else if (r < 20) {
                    // 10% chance for BLOCK_HEART if it doesn't exist, otherwise BLOCK_NORMAL
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
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
                        default: type = Block.BLOCK_NORMAL; // Should not happen
                    }
                }

                blocks.add(new Block(i, j, colors[random.nextInt(colors.length)], type));
            }
        }
    }



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:
                move(RIGHT);
                break;
            case DOWN:
                // setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
            case P:
                if (engine != null) {
                    engine.togglePause();
                }
                break;
        }
    }

    float oldXBreak;

    private void move(final int direction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                        return;
                    }
                    if (xBreak == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        xBreak++;
                    } else {
                        xBreak--;
                    }
                    centerBreakX = xBreak + halfBreakWidth;
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

    private void initBall() {
        xBall = sceneWidth / 2.0;
        yBall = sceneHeight / 2.0;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("pokeball.png")));
    }

    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        xBreak = (sceneWidth - breakWidth) / 2.0;
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        rect.setFill(pattern);
    }



    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean colideToRightBlock          = false;
    private boolean colideToBottomBlock         = false;
    private boolean colideToLeftBlock           = false;
    private boolean colideToTopBlock            = false;

     double vX = 2.000;
     double vY = 2.000;


    private void resetColideFlags() {
        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;
        colideToRightBlock = false;
        colideToBottomBlock = false;
        colideToLeftBlock = false;
        colideToTopBlock = false;
    }

    private void setPhysicsToBall() {
        v = ((time - hitTime) / 1000.000) + 1.000;

        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (yBall <= 0) {
            vX = 2.000;
            resetColideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeight) {
            goDownBall = false;
            if (!isGoldStatus) {
                // TODO gameover
                heart--;
                new Score().show(sceneWidth / 2, sceneHeight / 2, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }
            }
            return;
        }

        if (yBall >= yBreak - ballRadius) {
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetColideFlags();
                colideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                }

                if (xBall - centerBreakX > 0) {
                    colideToBreakAndMoveToRight = true;
                } else {
                    colideToBreakAndMoveToRight = false;
                }
            }
        }

        if (xBall >= sceneWidth) {
            resetColideFlags();
            vX = 2.000;
            colideToRightWall = true;
        }

        if (xBall <= 0) {
            resetColideFlags();
            vX = 2.000;
            colideToLeftWall = true;
        }

        if (colideToBreak) {
            if (colideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
        }

        if (colideToRightWall) {
            goRightBall = false;
        }

        if (colideToLeftWall) {
            goRightBall = true;
        }

        if (colideToRightBlock) {
            goRightBall = true;
        }

        if (colideToLeftBlock) {
            goRightBall = true;
        }

        if (colideToTopBlock) {
            goDownBall = false;
        }

        if (colideToBottomBlock) {
            goDownBall = true;
        }
    }

    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            isLevelCompleted = true;
            nextLevel();
        }
    }

    private void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(destroyedBlockCount);

                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xBreak);
                outputStream.writeDouble(yBreak);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);

                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(colideToBreak);
                outputStream.writeBoolean(colideToBreakAndMoveToRight);
                outputStream.writeBoolean(colideToRightWall);
                outputStream.writeBoolean(colideToLeftWall);
                outputStream.writeBoolean(colideToRightBlock);
                outputStream.writeBoolean(colideToBottomBlock);
                outputStream.writeBoolean(colideToLeftBlock);
                outputStream.writeBoolean(colideToTopBlock);

                ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                }

                outputStream.writeObject(blockSerializables);

                new Score().showMessage("Game Saved", Main.this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadGame() {
        LoadSave loadSave = new LoadSave();
        loadSave.read();

        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStauts;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        colideToBreak = loadSave.colideToBreak;
        colideToBreakAndMoveToRight = loadSave.colideToBreakAndMoveToRight;
        colideToRightWall = loadSave.colideToRightWall;
        colideToLeftWall = loadSave.colideToLeftWall;
        colideToRightBlock = loadSave.colideToRightBlock;
        colideToBottomBlock = loadSave.colideToBottomBlock;
        colideToLeftBlock = loadSave.colideToLeftBlock;
        colideToTopBlock = loadSave.colideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }

        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                vX = 2.000;
                engine.stop();
                resetColideFlags();
                goDownBall = true;

                if (isLevelCompleted) {
                    level++;  // Increment the level
                    levelLabel.setText("Level: " + level);  // Update the level label
                    isLevelCompleted = false;
                }

                isGoldStatus = false;
                isExistHeartBlock = false;

                hitTime = 0;
                time = 0;
                goldTime = 0;

                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;

                initBall();
                initBreak();
                initBoard();

                // Clear and re-add game components to root
                root.getChildren().clear();
                root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
                for (Block block : blocks) {
                    root.getChildren().add(block.rect);
                }

                // Restart the game engine for the next level
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void restartGame() {
        try {
            level = 1; // Set the level to 1
            heart = 3;
            score = 0;
            vX = 2.000;
            destroyedBlockCount = 0;
            resetColideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score);
            heartLabel.setText("Heart : " + heart);

            rect.setX(xBreak);
            rect.setY(yBreak);
            ball.setCenterX(xBall);
            ball.setCenterY(yBall);

            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }

            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                if (hitCode != Block.NO_HIT) {
                    score += 1;

                    new Score().show(block.x, block.y, 1, Main.this);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(() -> {
                            root.getChildren().add(choco.choco);
                        });
                        chocos.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("Ultraball.png")));
                        root.getStyleClass().add("goldRoot");
                        isGoldStatus = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        heart++;
                    }

                    switch (hitCode) {
                        case Block.HIT_BOTTOM:
                        case Block.HIT_TOP:
                            goDownBall = !goDownBall;
                            break;
                        case Block.HIT_LEFT:
                        case Block.HIT_RIGHT:
                            goRightBall = !goRightBall;
                            break;
                    }
                }
            }

            checkDestroyedCount();
        });
    }

    @Override
    public void onPhysicsUpdate() {
        Platform.runLater(() -> {
            checkDestroyedCount();
            setPhysicsToBall();

            if (time - goldTime > 5000) {
                ball.setFill(new ImagePattern(new Image("pokeball.png")));
                root.getStyleClass().remove("goldRoot");
                isGoldStatus = false;
            }

            for (Bonus choco : chocos) {
                if (choco.y > sceneHeight || choco.taken) {
                    continue;
                }
                if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                    System.out.println("You Got it and +3 score for you");
                    choco.taken = true;
                    choco.choco.setVisible(false);
                    score += 3;
                    new Score().show(choco.x, choco.y, 3, this);
                }
                choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
            }
        });
    }

    //TODO hit to break and some work here...
    //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
}
