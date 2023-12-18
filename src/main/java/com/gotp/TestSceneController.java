package com.gotp;

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

    private Parent root;
    private Stage stage;
    private Scene scene;

    /**
     * size of the board that will be created.
     */
    private int size = 9;

    BoardController boardController = new BoardController();

    @FXML
    void choose11x11(ActionEvent event) {
        size = 11;
        System.out.println("chose 11");
    }

    @FXML
    void choose15x15(ActionEvent event) {
        size = 15;
    }

    @FXML
    void choose19x19(ActionEvent event) {
        size = 19;
    }

    @FXML
    void choose9x9(ActionEvent event) {
        size = 9;
    }

    @FXML
    void startGame(ActionEvent event) throws IOException {
        // App.setRoot("board");
        //boardController.addToGrid(9);

        FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("board.fxml"));
        root = boardLoader.load();
        BoardController boardController = boardLoader.getController();

        boardController.addToGrid(9);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}

