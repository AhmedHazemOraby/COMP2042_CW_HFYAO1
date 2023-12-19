package brickGame;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
/**
 * The main class for the brick game, extending JavaFX Application.
 * This class initializes and manages the main game loop, game scenes, and game state.
 */
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    // Constants for direction
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    // Game variables
    public boolean isNextLevelCalled = false;
    public boolean isEndlessMode = false;
    public int level = 1;
    public double xBreak = 0.0f;
    public double centerBreakX;
    public double yBreak = 640.0f;
    public int breakWidth = 130;
    public int breakHeight = 30;
    public int halfBreakWidth = breakWidth / 2;
    public int sceneWidth = 500;
    public int sceneHeight = 700;
    public Circle ball;
    public double xBall;
    public double yBall;
    public boolean isGoldStatus = false;
    public boolean isExistHeartBlock = false;
    public boolean isLevelCompleted = false;
    public Rectangle rect;
    public int ballRadius = 10;
    public int destroyedBlockCount = 0;
    public double v = 1.000;
    public int heart = 3;
    public int score = 0;
    public long time = 0;
    public long hitTime = 0;
    public long goldTime = 0;
    public GameEngine engine;
    public ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Bonus> chocos = new ArrayList<>();
    public Color[] colors = {
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
    public Label scoreLabel;
    public Label heartLabel;
    public Label levelLabel;
    public boolean goDownBall                  = true;
    public boolean goRightBall                 = true;
    public boolean colideToBreak               = false;
    public boolean colideToBreakAndMoveToRight = true;
    public boolean colideToRightWall           = false;
    public boolean colideToLeftWall            = false;
    public boolean colideToRightBlock          = false;
    public boolean colideToBottomBlock         = false;
    public boolean colideToLeftBlock           = false;
    public boolean colideToTopBlock            = false;
    double vX = 2.000;
    double vY = 2.000;
    Stage primaryStage;
    @Override
    public void onInit() {
    }
    @Override
    public void onTime(long time) {
        this.time = time;
    }
    /**
     * The starting point of the application.
     * Initializes the main menu of the game.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     * @throws Exception If any issue occurs during the start-up.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initMenu();
    }
    private void initMenu() {
        Initialize.initializeMenu(this);
    }
    public void initGame(boolean endlessMode) {
        Initialize.initializeGame(endlessMode,this);
    }
    public void initBoard() {
        InitializationBoard.initializationBoard(this);
    }
    /**
     * The main entry point for all JavaFX applications.
     * The method is called after the application class is loaded and
     * constructed.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void handle(KeyEvent event) {
        GameRules.HandleKeys(event, this);
    }
    public void move(final int direction) {
        GameRules.Movement(direction, this);
    }
    public void initBall() {
        InitializationBoard.initializeBall(this);
    }
    public void initBreak() {
        InitializationBoard.initializeBreak(this);
    }
    public void resetColideFlags() {
        GameRules.Flags(this);
    }
    public void setPhysicsToBall() {
        Physics.BallPhysics(this);
    }
    public void checkDestroyedCount() {
        GameRules.CheckDestroy(this);
    }
    public void nextLevel() {Updater.NextLevel(this);}
    public void restartGame() {
        GameRules.Restart(this);
    }
    @Override
    public void onUpdate() {Updater.update(this);}
    @Override
    public void onPhysicsUpdate() {
        Physics.PhysicsUpdater(this);
    }
}
