package com.staspavlov.taggame.main;

import com.staspavlov.taggame.controller.GameController;
import com.staspavlov.taggame.controller.WizardController;
import com.staspavlov.taggame.game.Game;
import com.staspavlov.taggame.game.GameImpl;
import com.staspavlov.taggame.model.ElementSize;
import com.staspavlov.taggame.model.Model;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Tag game.
 * @author Stanislav Pavlov <mail@staspavlov.com>
 */
public class App extends Application {

    /**
     * Path to defaults file.
     */
    private final static String DEFAULTS = "bundle/Defaults.properties";

    /**
     * Paths to FXML files.
     */
    private final static String GAME_FXML = "/fxml/Game.fxml";
    private final static String WIZARD_FXML = "/fxml/Wizard.fxml";

    /**
     * Model.
     */
    private Model model;

    /**
     * Stage for game area.
     */
    private Stage gameStage;

    /**
     * Controller for game area.
     */
    private GameController gameController;

    /**
     * Stage for new game wizard.
     */
    private Stage wizardStage;

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application.
     * @param stage The root stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        gameStage = stage;
        wizardStage = new Stage();

        initModel();
        initGameStage();
        initWizardStage();

        // Reinit stages on Locale change
        model.getLocaleProperty().addListener(new ChangeListener<Locale>() {
            @Override
            public void changed(ObservableValue<? extends Locale> observable, Locale oldValue, Locale newValue) {
                try {
                    initGameStage();
                    initWizardStage();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        gameStage.show();
    }

    /**
     * Stops the application.
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        if (gameController != null) {
            gameController.cleanUp();
        }
    }

    /**
     * Initializes model.
     * @throws NumberFormatException, IOException
     */
    private void initModel() throws NumberFormatException, IOException {
        // Read defaults
        Properties defaults = new Properties();
        defaults.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULTS));

        // Create game
        int rows = Integer.valueOf(defaults.getProperty("rowsNumber"));
        int cols = Integer.valueOf(defaults.getProperty("colsNumber"));
        Game game = new GameImpl(rows, cols);
        game.shuffle();

        // Create locale
        Locale locale = new Locale(defaults.getProperty("locale"));

        // Game area elements sizes
        ElementSize elementSize = ElementSize.valueOf(defaults.getProperty("elementSize"));

        // Create model
        model = new Model(game, locale, elementSize);
    }

    /**
     * Initializes game stage.
     * @throws IOException
     */
    private void initGameStage() throws IOException {
        if (gameController != null) {
            gameController.cleanUp();
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(GAME_FXML));
        loader.setResources(ResourceBundle.getBundle("bundle.Locale", model.getLocale()));

        gameController = new GameController(model, gameStage, wizardStage);
        loader.setController(gameController);

        gameStage.setTitle(loader.getResources().getString("game.title"));
        gameStage.setScene(new Scene((Parent) loader.load()));
    }

    /**
     * Initializes setup stage.
     * @throws IOException
     */
    private void initWizardStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(WIZARD_FXML));
        loader.setResources(ResourceBundle.getBundle("bundle.Locale", model.getLocale()));

        WizardController ctrl = new WizardController(model);
        loader.setController(ctrl);

        wizardStage.setTitle(loader.getResources().getString("wizard.title"));
        wizardStage.setScene(new Scene((Parent) loader.load()));

        if (wizardStage.getModality() != Modality.WINDOW_MODAL) {
            wizardStage.initModality(Modality.WINDOW_MODAL);
            wizardStage.initOwner(gameStage);
        }
    }

}
