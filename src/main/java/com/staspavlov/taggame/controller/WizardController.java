package com.staspavlov.taggame.controller;

import com.staspavlov.taggame.game.Game;
import com.staspavlov.taggame.game.GameImpl;
import com.staspavlov.taggame.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 * Controller for create new game.
 * @author Stanislav Pavlov <mail@staspavlov.com>
 */
public class WizardController {

    /**
     * Model.
     */
    private final Model model;

    /**
     * ChoiceBox for number of rows.
     */
    @FXML
    private ChoiceBox choiceRows;

    /**
     * ChoiceBox for number of columns.
     */
    @FXML
    private ChoiceBox choiceCols;

    /**
     * Creates WizardController instance.
     * @param model Model
     */
    public WizardController(Model model) {
        this.model = model;
    }

    /**
     * Procsses click on OK button.
     * @param event
     */
    @FXML
    public void okAction(ActionEvent event) {
        int rows = Integer.valueOf((String) choiceRows.getValue());
        int cols = Integer.valueOf((String) choiceCols.getValue());
        Game game = new GameImpl(rows, cols);
        game.shuffle();
        model.setGame(game);
        Button btn = (Button) event.getTarget();
        btn.getScene().getWindow().hide();
    }

    /**
     * Procsses click on Cancel button.
     * @param event
     */
    @FXML
    public void cancelAction(ActionEvent event) {
        Button btn = (Button) event.getTarget();
        btn.getScene().getWindow().hide();
    }

}
