package com.staspavlov.taggame.controller;

import com.staspavlov.taggame.game.Game;
import com.staspavlov.taggame.model.ElementSize;
import com.staspavlov.taggame.model.Model;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for game area.
 * @author Stanislav Pavlov <mail@staspavlov.com>
 */
public class GameController implements Initializable {

    /**
     * Model.
     */
    private final Model model;

    /**
     * Game stage.
     */
    private final Stage gameStage;

    /**
     * Wizard stage.
     */
    private final Stage wizardStage;

    /**
     * Resource bundle.
     */
    private ResourceBundle resourceBundle;

    /**
     * Current menu bar.
     */
    @FXML
    private MenuBar menuBar;

    /**
     * Container for grid of buttons.
     */
    @FXML
    private AnchorPane gameArea;

    /**
     * Container for buttons.
     */
    @FXML
    private GridPane gameGrid;

    /**
     * Current tool bar.
     */
    @FXML
    private ToolBar toolBar;

    /**
     * Number of seconds.
     */
    @FXML
    private Text timeCounter;

    /**
     * Number of moves.
     */
    @FXML
    private Text movesCounter;

    /**
     * List of buttons.
     */
    private Button[] gameBtns;

    /**
     * Timer.
     */
    private final Timer timer = new Timer();

    /**
     * Game change listener.
     */
    private final ChangeListener<Game> gameListener = new ChangeListener<Game>() {
        @Override
        public void changed(ObservableValue<? extends Game> observable, Game oldValue, Game newValue) {
            initGameArea();
        }
    };

    /**
     * Element size change listener.
     */
    private final ChangeListener<ElementSize> elementSizeListener = new ChangeListener<ElementSize>() {
        @Override
        public void changed(ObservableValue<? extends ElementSize> observable, ElementSize oldValue, ElementSize newValue) {
            initGameArea();
        }
    };

    /**
     * Creates GameController instance.
     * @param model Model
     * @param gameStage Game stage
     * @param wizardStage Wizard stage
     */
    public GameController(Model model, Stage gameStage, Stage wizardStage) {
        this.model = model;
        this.gameStage = gameStage;
        this.wizardStage = wizardStage;
    }

    /**
     * Initializes controller.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resourceBundle = rb;
        initGameArea();

        // Reinit game area on game change
        model.getGameProperty().addListener(gameListener);

        // Adjust game area on elements size change
        model.getElementSizeProperty().addListener(elementSizeListener);

        // Start timer
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Game game = model.getGame();
                timeCounter.setText(String.valueOf(game.getSecondsCount()));
            }
        }, 0L, 1000L);
    }

    /**
     * Do clean up.
     */
    public void cleanUp() {
        model.getGameProperty().removeListener(gameListener);
        model.getElementSizeProperty().removeListener(elementSizeListener);
        timer.cancel();
    }

    /**
     * Processes request for create new game.
     * @param event
     */
    @FXML
    public void wizardAction(ActionEvent event) {
        wizardStage.showAndWait();
    }

    /**
     * Processes request for close application.
     * @param event
     */
    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Processes request for set RU locale.
     * @param event
     */
    @FXML
    public void setLocaleRuAction(ActionEvent event) {
        model.setLocale(new Locale("ru"));
    }

    /**
     * Processes request for set EN locale.
     * @param event
     */
    @FXML
    public void setLocaleEnAction(ActionEvent event) {
        model.setLocale(new Locale("en"));
    }

    /**
     * Processes request for set small element size.
     * @param event
     */
    @FXML
    public void setElementSizeSmallAction(ActionEvent event) {
        model.setElementSize(ElementSize.SMALL);
    }

    /**
     * Processes request for set normal element size.
     * @param event
     */
    @FXML
    public void setElementSizeNormalAction(ActionEvent event) {
        model.setElementSize(ElementSize.NORMAL);
    }

    /**
     * Processes request for set large element size.
     * @param event
     */
    @FXML
    public void setElementSizeLargeAction(ActionEvent event) {
        model.setElementSize(ElementSize.LARGE);
    }

    /**
     * Starts new game.
     */
    private void initGameArea() {
        adjustGameAreaSize();
        initGameBtns();
        initGameGrid();
        renderGameArea();
    }

    /**
     * Adjusts window to fit game size.
     */
    private void adjustGameAreaSize() {
        Game game = model.getGame();
        ElementSize size = model.getElementSize();
        double windowWidth = game.getCols() * size.getWidth();
        double windowHeight = game.getRows() * size.getHeight() + menuBar.getHeight() + toolBar.getHeight();
        gameStage.setMinWidth(windowWidth);
        gameStage.setWidth(windowWidth);
        gameStage.setMinHeight(windowHeight);
        gameStage.setHeight(windowHeight);
    }

    /**
     * Creates buttons for game area.
     */
    private void initGameBtns() {
        Game game = model.getGame();
        int count = game.getSize() - 1;
        gameBtns = new Button[count];
        for (int i = 0; i < count; i++) {
            Button btn = new Button(String.valueOf(i + 1));
            btn.setMaxHeight(Double.MAX_VALUE);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("gameBtn");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Button btn = (Button) event.getTarget();
                    int element = Integer.valueOf(btn.getText());
                    makeMove(element);
                }
            });
            gameBtns[i] = btn;
        }
    }

    /**
     * Creates grid for game area.
     */
    private void initGameGrid() {
        Game game = model.getGame();
        // Remove old grid if exists
        if (gameGrid != null) {
            gameArea.getChildren().remove(gameGrid);
        }
        // Create new grid
        gameGrid = new GridPane();
        gameGrid.setMaxHeight(Double.MAX_VALUE);
        gameGrid.setMaxWidth(Double.MAX_VALUE);
        // Add column constraints
        double colWidth = 100.0 / game.getCols();
        for (int c = 0; c < game.getCols(); c++) {
            ColumnConstraints cts = new ColumnConstraints();
            cts.setPercentWidth(colWidth);
            gameGrid.getColumnConstraints().add(cts);
        }
        // Add row constraints
        double rowHeight = 100.0 / game.getRows();
        for (int r = 0; r < game.getRows(); r++) {
            RowConstraints cts = new RowConstraints();
            cts.setPercentHeight(rowHeight);
            gameGrid.getRowConstraints().add(cts);
        }
        // Add buttons to grid
        for (Button btn : gameBtns) {
            gameGrid.add(btn, 0, 0);
        }
        // Add grid to game area
        AnchorPane.setTopAnchor(gameGrid, 0.0);
        AnchorPane.setRightAnchor(gameGrid, 0.0);
        AnchorPane.setBottomAnchor(gameGrid, 0.0);
        AnchorPane.setLeftAnchor(gameGrid, 0.0);
        gameArea.getChildren().add(gameGrid);
    }

    /**
     * Refreshs game area elements.
     */
    private void renderGameArea() {
        Game game = model.getGame();
        for (Button btn : gameBtns) {
            int element = Integer.valueOf(btn.getText());
            int[] pos = game.getPosition(element);
            GridPane.setRowIndex(btn, pos[0]);
            GridPane.setColumnIndex(btn, pos[1]);
        }
        movesCounter.setText(String.valueOf(game.getMovesCount()));
    }

    /**
     * Process game move.
     */
    private void makeMove(int element) {
        Game game = model.getGame();
        if (game.makeMove(element)) {
            renderGameArea();
        }
        if (game.isCompleted()) {
            endGame();
        }
    }

    /**
     * Ends the game.
     */
    private void endGame() {
        // Disable game buttons
        for (Button gameBtn : gameBtns) {
            gameBtn.setDisable(true);
        }
        // Ask for new game
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("game.complete"));
        alert.setHeaderText(null);
        alert.setContentText(resourceBundle.getString("game.restart"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            wizardStage.showAndWait();
        }
    }
}
