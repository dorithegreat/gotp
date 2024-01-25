package com.gotp.GUIcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardController {

    /**
     * grid that will become the board.
     */
    @FXML
    private GridPane grid;

    /**
     * button for passing the turn.
     */
    @FXML
    private Button passButton;

    /**
     * button for resigning the game.
     */
    @FXML
    private Button resignButton;


    /**
     * generates the board.
     * @param boardSize size of the created board
     */
    @FXML
    public void addToGrid(final int boardSize) throws IOException {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid.add(new BoardPiece(), i, j);
                //! rows that are added beyond the initial ones are scaled differently
                //I'll fix that when I figure out what's causing it
            }
        }
    }

    /**
     * function for passing the turn.
     * * currently always switches to the winning screen. This is obviously just for testing.
     * @param event the event that triggered this method
     */
     @FXML
    void pass(final ActionEvent event) {

    }

    /**
     * function for resigning the game.
     * * currently always switches to the losing screen. Same as above.
     * @param event the event that triggered this method
     */
    @FXML
    void resign(final ActionEvent event) throws IOException {
        //a few declarations that will be necessary for changing the scene
        Parent root;
        Stage stage;
        Scene scene;

        FXMLLoader endLoader = new FXMLLoader(getClass().getResource("end_won.fxml"));
        root = endLoader.load();
        //EndScreenController endController = endLoader.getController();

        //TODO do whatever is necessery with the end screen here

        //code that changes the scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
