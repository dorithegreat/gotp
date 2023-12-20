package com.gotp.GUIcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class TestSceneController {

    /**
     * toggle group containing buttons, so that only one of them can be chosen at once.
     */
    @FXML
    private ToggleGroup abc;

    /**
     * button to choose 11x11 board size.
     */
    @FXML
    private RadioButton button11;

    /**
     * button to choose 15x15 board size.
     */
    @FXML
    private RadioButton button15;

    /**
     * button to choose 19x19 board size.
     */
    @FXML
    private RadioButton button19;

    /**
     * button to choose 9x9 board size.
     */
    @FXML
    private RadioButton button9;


    /**
     * size of the board that will be created.
     */
    private int size = 9;

    /**
     * controller of the next scene.
     * Is needed so that the size of the board can be set from the current controller
     */
    private BoardController boardController = new BoardController();

    @FXML
    void choose11x11(final ActionEvent event) {
        size = 11;
        System.out.println("chose 11");
    }

    @FXML
    void choose15x15(final ActionEvent event) {
        size = 15;
    }

    @FXML
    void choose19x19(final ActionEvent event) {
        size = 19;
    }

    @FXML
    void choose9x9(final ActionEvent event) {
        size = 9;
    }

    /**
     * Handler for the start button, goes to the board screen and starts the game.
     * @param event
     * @throws IOException //TODO figure out when
     */
    @FXML
    void startGame(final ActionEvent event) throws IOException {
        //a few declarations that will be necessary for changing the scene
        Parent root;
        Stage stage;
        Scene scene;

        FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("board.fxml"));
        root = boardLoader.load();
        BoardController boardController = boardLoader.getController();

        //calls method in the other controller to create a board of certain size
        boardController.addToGrid(size);

        //code that changes the scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}

